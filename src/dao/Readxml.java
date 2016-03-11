package dao;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.servlet.ServletContext;
import javax.swing.plaf.synth.SynthSeparatorUI;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.tools.ant.util.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParseException;

import controller.Default;

//import ch.qos.logback.core.util.FileUtil;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Currency;
import model.Result;

public class Readxml {
	static Logger log = LoggerFactory.getLogger(Readxml.class); // info trace debug warn error

	public static void main(String argv[]) {

		//doStuff();

		//		List<Currency> swagCurrencies = getCurrencies();
		//		System.out.println("swagcurrencies size: " + swagCurrencies.size());
		//		for(Currency c : swagCurrencies){
		//			if(c != null){
		//				System.out.println("CURRENCY: " + c.toString());
		//			}else{
		//				System.out.println("c IS NULL!!");
		//			}
		//			
		//		}
	}

	// 0) Download all XMLs (DATE!!, don't let user select newer dates!)
	// 1) Get all possible currencies from XMLs
	// 2) Generate a List of Currency SHORTNAME - FULLNAME
	// 3) Display said list
	// 4) User selects input, output
	// 5) Calculate the results from different bank XML-s, create a list of Result-s and display them.

	// genereeri uus fail??
	// eraldi fail, kus on põhivaluutad ja tõlked??


	public static List<Result> calculateResults(String inputMoneyAmount, String inputCurrency, String outputCurreny, String date){
		// calculate using all the banks and their different currencies
		log.debug("[calculateResults]");
		
		
		return null;
	}
	
	// ATM USING ONLY FOR DOWNLOAD + DISPLAY:
	public static List<Currency> downloadAllForDate(ServletContext context){//, Date date){
		log.debug("[downloadAllForDate]");
		
		String day = "30";
		String month = "12";
		String year = "2010";

		String timeString = year+"-"+month+"-"+day;

		String bankOfEstonia = "http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1="+timeString+"&lng=est&print=off"; //"http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1=2010-12-30&lng=est&print=off";
		String bankOfLithuania = "http://webservices.lb.lt/ExchangeRates/ExchangeRates.asmx/getExchangeRatesByDate?Date="+timeString; //"http://webservices.lb.lt/ExchangeRates/ExchangeRates.asmx/getExchangeRatesByDate?Date=2010-12-30";

		// DOWNLOAD FOR X      URL(with date) and file name (eg eesti-2010-12-30)
		FileInputStream fisEstonia = getFileForX(context, bankOfEstonia,"bankOfEstonia-"+timeString+".xml");
		//FileInputStream fisLithuania = getFileForX(context, bankOfLithuania,"bankOfLithuania-"+timeString+".xml");
		
		List<Currency> bankOfEstoniaCurrencies = fisToCurrencies(fisEstonia);
		//List<Result> resultsEstonia = 
		
		return bankOfEstoniaCurrencies;
	}
	public static FileInputStream getFileForX (ServletContext context, String downloadURL, String fileName){
		log.debug("[getFileForX]");
		try {
			FileInputStream fis;
			String xmlFilesPath = "/WEB-INF/xml/";
			URL resourceUrl = context.getResource(xmlFilesPath+fileName); //context.getResource("/WEB-INF/xml/eesti.xml");
			if(resourceUrl == null){
				log.debug("[getFileForX] LOCAL XML NOT FOUND, DOWNLOADING");
				URL bankURL = new URL(downloadURL);
				String path = context.getRealPath(xmlFilesPath);
				String newFilePath = path+fileName;
				System.out.println("newFilePath: " + newFilePath); // C:\Users\karlk\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp1\wtpwebapps\Java-Proov-3F\WEB-INF\/WEB-INF/xml/eesti.xml
				File newXmlFile = new File(newFilePath); //("WEB-INF/xml/eesti.xml"); // ./WEB-INF/xml/eesti.xml is the same thing				
				FileUtils.copyURLToFile(bankURL, newXmlFile);
				fis = new FileInputStream(newXmlFile);
				//fis.close();
			}else{
				log.debug("[getFileForX] LOCAL XML FOUND");
				fis = new FileInputStream(new File(resourceUrl.toURI()));
			}
			return fis;
		} catch (MalformedURLException e) {
			log.error("[getFileForX] url malformed!", e);
		} catch (IOException e) {
			log.error("[getFileForX] copyURLToFile failed!", e);
		} catch (URISyntaxException e) {
			log.error("[getFileForX] resourceUrl.toURI failed!", e);
		} 
		return null;
	}
	public static List<Currency> fisToCurrencies(FileInputStream fis){
		log.debug("[fisToCurrencies]");
		try {
			List<Currency> returnCurrencies = new ArrayList<Currency>();

			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(fis);
			doc.getDocumentElement().normalize();

			log.debug("Root element: " + doc.getDocumentElement().getNodeName());

			// FOLLOWING IS SPECIFIC TO CERTAIN XML:
			NodeList nList = doc.getElementsByTagName("Currency"); // row
			log.debug(nList.getLength() + " nodes found");

			log.debug("----------------------------");
			log.debug("nList length: " + nList.getLength());
			String dateString = doc.getElementsByTagName("Date").item(0).getTextContent(); //.getTextContent(); //07.03.16        "January 2, 2010";
			log.debug("dateString: " + dateString);
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy"); // new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH); // DateFormat
			// java.text.ParseException: Unparseable date: ""
			Date date = format.parse(dateString);
			log.debug("DATE IS: " + date);

			// CURRENCY ELEMENTS:
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				//System.out.println("\nCurrent Element :" + nNode.getNodeName()); // Current Element :Currency
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					//System.out.println("I HAVE NODE!");

					String name = eElement.getAttribute("name"); // shortName
					String text = eElement.getAttribute("text"); // fullName
					Float rate;
					NumberFormat nf = new DecimalFormat ("#,#");
					try{
						rate = nf.parse(eElement.getAttribute("rate")).floatValue();
						//rate = Float.parseFloat(eElement.getAttribute("rate"));
					}catch(NumberFormatException e){
						e.printStackTrace();
						rate = null;
					}


					//				String dateString = eElement.getTextContent(); //07.03.16        "January 2, 2010";
					//				System.out.println("dateString: " + dateString);
					//				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy"); // new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH); // DateFormat
					//				// java.text.ParseException: Unparseable date: ""
					//				Date date = format.parse(dateString);
					//				System.out.println("DATE IS: " + date);

					log.debug("name: " + eElement.getAttribute("name") + " text: " + eElement.getAttribute("text") + " rate: " + eElement.getAttribute("rate"));
					Currency addCurrency = new Currency(name, text, rate, date);
					//System.out.println("addcurrency " + addCurrency.toString());
					returnCurrencies.add(addCurrency);
					//returnCurrencies.add(new Currency(name, text, rate, date));
				}
			}
			return returnCurrencies;	
		} catch (Exception e) {
			log.error("[fisToCurrencies] failed!", e);
		}
		return null;
	}
}
















/*
 * private static void doStuff(){
		try {			
			//File xmlFile = new File("/Users/example.xml");
			URL url = new URL("http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1=2010-12-30&lng=est&print=off");
			//URL url = new URL("http://webservices.lb.lt/ExchangeRates/ExchangeRates.asmx/getExchangeRatesByDate?Date=2010-12-30");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			//Document doc = db.parse(xmlFile);
			Document doc = db.parse(url.openStream());
			NodeList nodes = doc.getElementsByTagName("Currency"); // row
			log.debug(nodes.getLength() + " nodes found");

			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			log.debug("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("Currency");

			log.debug("----------------------------");
			log.debug("nList length: " + nList.getLength());
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				//System.out.println("\nCurrent Element :" + nNode.getNodeName()); // Current Element :Currency

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					//log.debug("I HAVE NODE!");
					log.debug("name: " + eElement.getAttribute("name") + " text: " + eElement.getAttribute("text") + " rate: " + eElement.getAttribute("rate"));

					//					System.out.println("Staff id : " + eElement.getAttribute("id"));
					//					System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
					//					System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
					//					System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
					//					System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());

				}
			}

			// RECURSIVELY PRINT NODES:
			//			if (doc.hasChildNodes()) {
			//				System.out.println("\nPRINTING NODES:");
			//				printNote(doc.getChildNodes());
			//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
 */

/*
private static void printNote(NodeList nodeList) {
	for (int count = 0; count < nodeList.getLength(); count++) {
		Node tempNode = nodeList.item(count);
		// make sure it's element node.
		if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
			// get node name and value
			log.debug("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
			log.debug("Node Value =" + tempNode.getTextContent());
			if (tempNode.hasAttributes()) {
				// get attributes names and values
				NamedNodeMap nodeMap = tempNode.getAttributes();
				for (int i = 0; i < nodeMap.getLength(); i++) {
					Node node = nodeMap.item(i);
					log.debug("attr name : " + node.getNodeName());
					log.debug("attr value : " + node.getNodeValue());
				}
			}
			if (tempNode.hasChildNodes()) {
				// loop again if has child nodes
				printNote(tempNode.getChildNodes());
			}
			log.debug("Node Name =" + tempNode.getNodeName() + " [CLOSE]");
		}
	}
}
 */

//public static List<Currency> getCurrencies(ServletContext context){
//	List<Currency> returnCurrencies = new ArrayList<Currency>();
//	try {
//		String bankOfEstonia = "http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1=2010-12-30&lng=est&print=off";
//		String bankOfLithuania = "http://webservices.lb.lt/ExchangeRates/ExchangeRates.asmx/getExchangeRatesByDate?Date=2010-12-30";
//		
//		URL bankOfEstoniaURL = new URL(bankOfEstonia);
//		
//		//ServletContext context = getContext();
//		//URL resourceUrl = context.getResource("/WEB-INF/test/foo.txt");
//		
//		// of for just input stream
//		//InputStream xmlFile = context.getResourceAsStream("/WEB-INF/xml/eesti.xml"); //("/WEB-INF/test/foo.txt"); InputStream resourceContent =
//		URL resourceUrl = context.getResource("/WEB-INF/xml/eesti.xml");
//		FileInputStream fis;
//		//File resourceFile;
//		//InputStream xmlFileIS = null;
//		if(resourceUrl == null){ // xmlFile
//			log.debug("NO XML FILE FOUND!!!, DOWNLOADING FROM INTERNETZ");
//
//			//String realp = context.getRealPath("/WEB-INF/xml/"); // C:\Users\karlk\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp1\wtpwebapps\Java-Proov-3F\WEB-INF\xml\
//			//URL fileURL = context.getResource("/WEB-INF/xml/");
//			//String contextPath = context.getContextPath(); // /Java-Proov-3F
//			//System.out.println("context path is " + contextPath); // /Java-Proov-3F
//			//System.out.println("FILE URL IS " + fileURL);
//			//String fileURL = contextPath + "/WEB-INF/xml/eesti.xml";
//			//String fileURL = context.getRealPath("/WEB-INF/xml/eesti.xml"); // C:\Users\karlk\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp1\wtpwebapps\Java-Proov-3F\WEB-INF\xml\eesti.xml
//			
//			String path = context.getRealPath("/WEB-INF/xml/");
//			String newFilePath = path+"eesti.xml";
//			System.out.println("newFilePath: " + newFilePath); // C:\Users\karlk\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp1\wtpwebapps\Java-Proov-3F\WEB-INF\/WEB-INF/xml/eesti.xml
//			File newXmlFile = new File(newFilePath); //("WEB-INF/xml/eesti.xml"); // ./WEB-INF/xml/eesti.xml is the same thing				
//
//			FileUtils.copyURLToFile(bankOfEstoniaURL, newXmlFile);
//			//System.out.println("newXmlFile stuff: " + newXmlFile.getAbsolutePath()); // C:\Users\karlk\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp1\wtpwebapps\Java-Proov-3F\WEB-INF\WEB-INF\xml\eesti.xml // C:\Java-Proov-3F\WEB-INF\xml\eesti.xml // C:\WEB-INF\xml\eesti.xml  // with ./WEB-IF it is C:\eclipse\.\WEB-INF\xml\eesti.xml
////			public static void copyURLToFile(URL source,
////                    File destination,
////                    int connectionTimeout,
////                    int readTimeout)
////             throws IOException
//			//xmlFile = newXmlFile;
//			
//			//resourceFile = newXmlFile;
//			//System.out.println("xmlfile is " + xmlFile.toString());
//			fis = new FileInputStream(newXmlFile);
//		}else{
//			log.debug("resourceUrl (XML FILE) FOUND!");
//			fis = new FileInputStream(new File(resourceUrl.toURI()));
//		}
//		//FileInputStream inputStream = new FileInputStream(new File(getClass().getResource(url).toURI()));
//		
//		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		DocumentBuilder db = dbf.newDocumentBuilder();
//		Document doc = db.parse(fis);
//		//Document doc = db.parse(xmlFile);
//		//Document doc = db.parse(xmlFile);
//		//Document doc = db.parse(url.openStream()); // TO STREAM FROM INTERNET
//		
//		//File xmlFile = new File("/Users/example.xml");
//		//File xmlFile = context.getResource("/WEB-INF/xml/eesti.xml");
//		
//		//FileUtils.copyURLToFile(URL, File);
//		//FileUtils.copyURLToFile(bankOfEstonia, File);
//		
//		//optional, but recommended! read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
//		doc.getDocumentElement().normalize();
//
//		log.debug("Root element: " + doc.getDocumentElement().getNodeName());
//
//		NodeList nList = doc.getElementsByTagName("Currency"); // row
//		log.debug(nList.getLength() + " nodes found");
//		
//		log.debug("----------------------------");
//		log.debug("nList length: " + nList.getLength());
//		String dateString = doc.getElementsByTagName("Date").item(0).getTextContent(); //.getTextContent(); //07.03.16        "January 2, 2010";
//		log.debug("dateString: " + dateString);
//		SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy"); // new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH); // DateFormat
//		// java.text.ParseException: Unparseable date: ""
//		Date date = format.parse(dateString);
//		log.debug("DATE IS: " + date);
//		
//		// CURRENCY ELEMENTS:
//		for (int temp = 0; temp < nList.getLength(); temp++) {
//			Node nNode = nList.item(temp);
//			//System.out.println("\nCurrent Element :" + nNode.getNodeName()); // Current Element :Currency
//			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//				Element eElement = (Element) nNode;
//				//System.out.println("I HAVE NODE!");
//				
//				String name = eElement.getAttribute("name"); // shortName
//				String text = eElement.getAttribute("text"); // fullName
//				Float rate;
//				NumberFormat nf = new DecimalFormat ("#,#");
//				try{
//					rate = nf.parse(eElement.getAttribute("rate")).floatValue();
//					//rate = Float.parseFloat(eElement.getAttribute("rate"));
//				}catch(NumberFormatException e){
//					e.printStackTrace();
//					rate = null;
//				}
//				
//				
////				String dateString = eElement.getTextContent(); //07.03.16        "January 2, 2010";
////				System.out.println("dateString: " + dateString);
////				SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy"); // new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH); // DateFormat
////				// java.text.ParseException: Unparseable date: ""
////				Date date = format.parse(dateString);
////				System.out.println("DATE IS: " + date);
//						
//				log.debug("name: " + eElement.getAttribute("name") + " text: " + eElement.getAttribute("text") + " rate: " + eElement.getAttribute("rate"));
//				Currency addCurrency = new Currency(name, text, rate, date);
//				//System.out.println("addcurrency " + addCurrency.toString());
//				returnCurrencies.add(addCurrency);
//				//returnCurrencies.add(new Currency(name, text, rate, date));
//			}
//		}
//	} catch (Exception e) {
//		e.printStackTrace();
//	}
//	
//	return returnCurrencies;
//}
