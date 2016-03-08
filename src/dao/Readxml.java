package dao;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;

import org.apache.commons.io.FileUtils;
//import org.apache.tools.ant.util.FileUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.ParseException;

//import ch.qos.logback.core.util.FileUtil;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Currency;

public class Readxml {

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
	
	public static List<Currency> getCurrencies(ServletContext context){
		List<Currency> returnCurrencies = new ArrayList<Currency>();
		try {
			String bankOfEstonia = "http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1=2010-12-30&lng=est&print=off";
			String bankOfLithuania = "http://webservices.lb.lt/ExchangeRates/ExchangeRates.asmx/getExchangeRatesByDate?Date=2010-12-30";
			
			URL bankOfEstoniaURL = new URL(bankOfEstonia);
			
			//ServletContext context = getContext();
			//URL resourceUrl = context.getResource("/WEB-INF/test/foo.txt");
			
			// of for just input stream
			//InputStream xmlFile = context.getResourceAsStream("/WEB-INF/xml/eesti.xml"); //("/WEB-INF/test/foo.txt"); InputStream resourceContent =
			URL resourceUrl = context.getResource("/WEB-INF/xml/eesti.xml");
			FileInputStream fis;
			//File resourceFile;
			//InputStream xmlFileIS = null;
			if(resourceUrl == null){ // xmlFile
				System.out.println("NO XML FILE FOUND!!!, DOWNLOADING FROM INTERNETZ");
				//String realp = context.getRealPath("/WEB-INF/xml/"); // C:\Users\karlk\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp1\wtpwebapps\Java-Proov-3F\WEB-INF\xml\
				//URL fileURL = context.getResource("/WEB-INF/xml/");
				//String contextPath = context.getContextPath(); // /Java-Proov-3F
				//System.out.println("context path is " + contextPath); // /Java-Proov-3F
				//System.out.println("FILE URL IS " + fileURL);
				//String fileURL = contextPath + "/WEB-INF/xml/eesti.xml";
				//String fileURL = context.getRealPath("/WEB-INF/xml/eesti.xml"); // C:\Users\karlk\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp1\wtpwebapps\Java-Proov-3F\WEB-INF\xml\eesti.xml
				
				String path = context.getRealPath("/WEB-INF/xml/");
				String newFilePath = path+"eesti.xml";
				System.out.println("newFilePath: " + newFilePath); // C:\Users\karlk\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp1\wtpwebapps\Java-Proov-3F\WEB-INF\/WEB-INF/xml/eesti.xml
				File newXmlFile = new File(newFilePath); //("WEB-INF/xml/eesti.xml"); // ./WEB-INF/xml/eesti.xml is the same thing				

				FileUtils.copyURLToFile(bankOfEstoniaURL, newXmlFile);
				//System.out.println("newXmlFile stuff: " + newXmlFile.getAbsolutePath()); // C:\Users\karlk\workspace\.metadata\.plugins\org.eclipse.wst.server.core\tmp1\wtpwebapps\Java-Proov-3F\WEB-INF\WEB-INF\xml\eesti.xml // C:\Java-Proov-3F\WEB-INF\xml\eesti.xml // C:\WEB-INF\xml\eesti.xml  // with ./WEB-IF it is C:\eclipse\.\WEB-INF\xml\eesti.xml
//				public static void copyURLToFile(URL source,
//                        File destination,
//                        int connectionTimeout,
//                        int readTimeout)
//                 throws IOException
				//xmlFile = newXmlFile;
				
				//resourceFile = newXmlFile;
				//System.out.println("xmlfile is " + xmlFile.toString());
				fis = new FileInputStream(newXmlFile);
			}else{
				System.out.println("resourceUrl (XML FILE) FOUND!");
				fis = new FileInputStream(new File(resourceUrl.toURI()));
			}
			//FileInputStream inputStream = new FileInputStream(new File(getClass().getResource(url).toURI()));
			
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(fis);
			//Document doc = db.parse(xmlFile);
			//Document doc = db.parse(xmlFile);
			//Document doc = db.parse(url.openStream()); // TO STREAM FROM INTERNET
			
			//File xmlFile = new File("/Users/example.xml");
			//File xmlFile = context.getResource("/WEB-INF/xml/eesti.xml");
			
			//FileUtils.copyURLToFile(URL, File);
			//FileUtils.copyURLToFile(bankOfEstonia, File);
			
			//optional, but recommended! read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("Currency"); // row
			System.out.println(nList.getLength() + " nodes found");
			
			System.out.println("----------------------------");
			System.out.println("nList length: " + nList.getLength());
			String dateString = doc.getElementsByTagName("Date").item(0).getTextContent(); //.getTextContent(); //07.03.16        "January 2, 2010";
			System.out.println("dateString: " + dateString);
			SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy"); // new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH); // DateFormat
			// java.text.ParseException: Unparseable date: ""
			Date date = format.parse(dateString);
			System.out.println("DATE IS: " + date);
			
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
					
					
//					String dateString = eElement.getTextContent(); //07.03.16        "January 2, 2010";
//					System.out.println("dateString: " + dateString);
//					SimpleDateFormat format = new SimpleDateFormat("dd.MM.yy"); // new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH); // DateFormat
//					// java.text.ParseException: Unparseable date: ""
//					Date date = format.parse(dateString);
//					System.out.println("DATE IS: " + date);
							
					System.out.println("name: " + eElement.getAttribute("name") + " text: " + eElement.getAttribute("text") + " rate: " + eElement.getAttribute("rate"));
					Currency addCurrency = new Currency(name, text, rate, date);
					//System.out.println("addcurrency " + addCurrency.toString());
					returnCurrencies.add(addCurrency);
					//returnCurrencies.add(new Currency(name, text, rate, date));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return returnCurrencies;
	}

	private static void doStuff(){
		try {			
			//File xmlFile = new File("/Users/example.xml");
			URL url = new URL("http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1=2010-12-30&lng=est&print=off");
			//URL url = new URL("http://webservices.lb.lt/ExchangeRates/ExchangeRates.asmx/getExchangeRatesByDate?Date=2010-12-30");
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			//Document doc = db.parse(xmlFile);
			Document doc = db.parse(url.openStream());
			NodeList nodes = doc.getElementsByTagName("Currency"); // row
			System.out.println(nodes.getLength() + " nodes found");

			//optional, but recommended
			//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("Currency");

			System.out.println("----------------------------");
			System.out.println("nList length: " + nList.getLength());
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				//System.out.println("\nCurrent Element :" + nNode.getNodeName()); // Current Element :Currency

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					//System.out.println("I HAVE NODE!");
					System.out.println("name: " + eElement.getAttribute("name") + " text: " + eElement.getAttribute("text") + " rate: " + eElement.getAttribute("rate"));

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

	private static void printNote(NodeList nodeList) {
		for (int count = 0; count < nodeList.getLength(); count++) {
			Node tempNode = nodeList.item(count);
			// make sure it's element node.
			if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
				// get node name and value
				System.out.println("\nNode Name =" + tempNode.getNodeName() + " [OPEN]");
				System.out.println("Node Value =" + tempNode.getTextContent());
				if (tempNode.hasAttributes()) {
					// get attributes names and values
					NamedNodeMap nodeMap = tempNode.getAttributes();
					for (int i = 0; i < nodeMap.getLength(); i++) {
						Node node = nodeMap.item(i);
						System.out.println("attr name : " + node.getNodeName());
						System.out.println("attr value : " + node.getNodeValue());
					}
				}
				if (tempNode.hasChildNodes()) {
					// loop again if has child nodes
					printNote(tempNode.getChildNodes());
				}
				System.out.println("Node Name =" + tempNode.getNodeName() + " [CLOSE]");
			}
		}
	}


}
