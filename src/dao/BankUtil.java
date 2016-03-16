package dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BankUtil {
	static Logger log = LoggerFactory.getLogger(BankUtil.class);
	
	public static FileInputStream getFisForX (ServletContext context, String downloadURL, String fileName){
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

	// CONVERT fileInputStream to Document
	public static Document fisToDocument(FileInputStream fis){
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(fis);
			doc.getDocumentElement().normalize();
			return doc;
		}catch(Exception e){
			log.error("[fisToDocument] failed!", e);
		}
		return null;
	}
	
	// CONVERT date picker date to correct format that suits the needs.
	// INPUT: 16.03.2016 (if dateFormat is "dd.MM.yy")
	// OUTPUT: 2016-03-16 (if outputFormat is "yyyy-MM-dd")
	public static String datepickerToUrlFormat(String selectedDate, String inputDateFormat, String outputDateFormat){
		SimpleDateFormat format = new SimpleDateFormat(inputDateFormat); // eg "dd.MM.yy"
		Date date = null;
		try {
			date = format.parse(selectedDate);
		} catch (java.text.ParseException e) {
			log.error("[datepickerToUrlFormat] could not parse date picker url!", e);
		}
		log.debug("[datepickerToUrlFormat] DATE IS: " + date);
		SimpleDateFormat format2 = new SimpleDateFormat(outputDateFormat); // eg "yyyy-MM-dd" or "dd-MM-yy"
		String dateInUrl = format2.format(date);
		log.debug("[datepickerToUrlFormat] RESULT dateInUrl: " + dateInUrl);
		return dateInUrl;
	}
	
	// CONVERT rate value to Float. Eg Bank of Estonia PARSE FLOAT 16 123,123123123 would use " " as thousands separator and "," as decimal separator
	public static Float parseStringNumber(String number, char thousandsSeparator, char decimalSeparator){
		DecimalFormat df = new DecimalFormat(); // new DecimalFormat("#.#");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator(' ');
		df.setDecimalFormatSymbols(symbols);
		Float resultRate = null;
		try {
			resultRate = df.parse(number).floatValue();
		} catch (ParseException e) {
			log.error("[parseStringNumber] could not parse!",e);
		}
		return resultRate;
	}
	
	public static List<String> fisToCurrencies(FileInputStream fis){
		log.debug("[fisToCurrencies]");
		try {
			List<String> returnCurrencies = new ArrayList<String>();
			Document doc = fisToDocument(fis);
			log.debug("Root element: " + doc.getDocumentElement().getNodeName());

			// FOLLOWING IS SPECIFIC TO CERTAIN XML:
			NodeList nList = doc.getElementsByTagName("Currency"); // row
			log.debug(nList.getLength() + " nodes found");

			// CURRENCY ELEMENTS: // TODO REPLACE WITH XPATH
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String name = eElement.getAttribute("name"); // shortName
					log.debug("name: " + eElement.getAttribute("name") + " text: " + eElement.getAttribute("text") + " rate: " + eElement.getAttribute("rate"));
					returnCurrencies.add(name);
				}
			}
			return returnCurrencies;	
		} catch (Exception e) {
			log.error("[fisToCurrencies] failed!", e);
		}
		return null;
	}

}












////public static List<Currency> fisToCurrencies(FileInputStream fis){
//public static List<String> fisToCurrencies(FileInputStream fis){
//	log.debug("[fisToCurrencies]");
//	try {
////		List<Currency> returnCurrencies = new ArrayList<Currency>();
//		List<String> returnCurrencies = new ArrayList<String>();
//		Document doc = fisToDocument(fis);
//		log.debug("Root element: " + doc.getDocumentElement().getNodeName());
//
//		// FOLLOWING IS SPECIFIC TO CERTAIN XML:
//		NodeList nList = doc.getElementsByTagName("Currency"); // row
//		log.debug(nList.getLength() + " nodes found");
//
//		// CURRENCY ELEMENTS: // TODO REPLACE WITH XPATH
//		for (int temp = 0; temp < nList.getLength(); temp++) {
//			Node nNode = nList.item(temp);
//			//System.out.println("\nCurrent Element :" + nNode.getNodeName()); // Current Element :Currency
//			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//				Element eElement = (Element) nNode;
//				//System.out.println("I HAVE NODE!");
//
//				String name = eElement.getAttribute("name"); // shortName
//				//String text = eElement.getAttribute("text"); // fullName NOT USING ANYMORE
//				// NOT USING RATE ANYMORE
////				Float rate;
////				NumberFormat nf = new DecimalFormat ("#,#");
////				try{
////					rate = nf.parse(eElement.getAttribute("rate")).floatValue();
////					//rate = Float.parseFloat(eElement.getAttribute("rate"));
////				}catch(NumberFormatException e){
////					e.printStackTrace();
////					rate = null;
////				}
//				log.debug("name: " + eElement.getAttribute("name") + " text: " + eElement.getAttribute("text") + " rate: " + eElement.getAttribute("rate"));
////				Currency addCurrency = new Currency(name, rate); //new Currency(name, text, rate);
//				returnCurrencies.add(name);
//			}
//		}
//		return returnCurrencies;	
//	} catch (Exception e) {
//		log.error("[fisToCurrencies] failed!", e);
//	}
//	return null;
//}
