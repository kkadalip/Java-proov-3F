package bank;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dao.BankInterface;
import dao.BankUtil;
import model.Result;

public class BankOfEstonia implements BankInterface {
	static Logger log = LoggerFactory.getLogger(BankOfEstonia.class);
	
	@Override
	public String getFileNameByDate(LocalDate selectedDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fileNameDatePattern); // "yyyy-MM-dd"
		String dateInFile = selectedDate.format(formatter);
		String result = "bankOfEstonia-"+dateInFile+".xml";
		return result;
	}
	
	@Override
	public String getDownloadUrlByDate(LocalDate selectedDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //("yyyy-MM-dd HH:mm");
		String formattedDateTime = selectedDate.format(formatter); //dateTime.format(formatter); // "1986-04-08 12:30"
		String result = "http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1="+formattedDateTime+"&lng=est&print=off"; //"yyyy-MM-dd"
		return result;
	}
	
	// Estonia: node Currency attribute rate
	@Override
	public Float fisToRate(FileInputStream fis, String inputCurrency) {	
		Float resultRate = null;
		try {
			log.debug("[fisToRateEST]");
			Document doc = BankUtil.fisToDocument(fis);
			XPath xPath =  XPathFactory.newInstance().newXPath();
			
			String expression = "//Currency[@name='"+inputCurrency+"']"; //"/currencies/currency";	    // EG Currency name="AED" rate="3,12312321"     
			//NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
			Node node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
			if(node != null){
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) node;
					String resultRateString = eElement.getAttribute("rate");
					resultRate = BankUtil.parseStringNumber(resultRateString, ' ', ',');
					log.debug("[fisToRateEST] HAVE NODE! I HAVE RATE, RATE IS: " + resultRate.toString());
				}else{
					log.debug("[fisToRateEST] NO RATE?");
				}
			}else{
				log.error("[fisToRateEST] NO NODE!!!");
			}
		} catch (Exception e) {
			log.error("[fisToRateEST] failed!", e);
		}
		return resultRate;
	}
	
	public List<String> fisToCurrencies(FileInputStream fis){
		log.debug("[fisToCurrencies]");
		if(fis != null){
			List<String> returnCurrencies = new ArrayList<String>();
			Document doc = BankUtil.fisToDocument(fis);
			NodeList nList = doc.getElementsByTagName("Currency"); // row
			log.debug("[fisToCurrencies] " +nList.getLength() + " nodes found");
			for (int temp = 0; temp < nList.getLength(); temp++) {
				Node nNode = nList.item(temp);
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					String name = eElement.getAttribute("name"); // shortName
					//log.debug("[fisToCurrencies] name: " + eElement.getAttribute("name") + " text: " + eElement.getAttribute("text") + " rate: " + eElement.getAttribute("rate"));
					returnCurrencies.add(name);
				}
			}
			return returnCurrencies;	
		}else{
			log.error("[fisToCurrencies] fis null!");
			return null;
		}
	}

	@Override
	public List<String> getCurrencies(ServletContext context, LocalDate selectedDate) {
		String url = getDownloadUrlByDate(selectedDate);
		String filename = getFileNameByDate(selectedDate);
		FileInputStream fisEstonia = BankUtil.getFisForX(context, url,filename);
		List<String> currencies = fisToCurrencies(fisEstonia);
		return currencies;
	}
	
	@Override
	public Result getResult(ServletContext context, LocalDate selectedDate, String inputCurrency, String outputCurrency, Float inputMoneyAmount) {
		String url = getDownloadUrlByDate(selectedDate);
		String filename = getFileNameByDate(selectedDate);
		Float fisESTinputRate = fisToRate(BankUtil.getFisForX(context, url,filename),inputCurrency);
		Float fisESToutputRate = fisToRate(BankUtil.getFisForX(context, url,filename),outputCurrency);
		if(fisESTinputRate != null && fisESToutputRate != null && inputMoneyAmount != null){
			Float outputAmountEstonia = fisESTinputRate / fisESToutputRate * inputMoneyAmount;
			String output = BankUtil.displayedFloat(outputAmountEstonia);
			log.debug("[calculateResults]  input: " + fisESTinputRate + " / " + fisESToutputRate + " * " +  inputMoneyAmount);
			log.debug("[calculateResults]  Bank of Estonia RESULT: " + output); //outputAmountEstonia.toString());
			return new Result("Bank of Estonia", output); // outputAmountEstonia.toString()
		}else{
			log.error("Bank of Estonia DOES NOT HAVE RESULT!");
			return new Result("Bank of Estonia","-");
		}
	}
}




































//if(fisESTinputRate != null){log.debug("[calculateResults] fisESTinputRate: " + fisESTinputRate.toString());
//}else{log.debug("[calculateResults] fisEstoniaInputRate IS NULL!");}
//if(fisESToutputRate != null){ log.debug("[calculateResults] fisESToutputRate: " + fisESToutputRate.toString()); 
//}else{log.debug("[calculateResults] fisESToutputRate IS NULL!");}

//public static String fisToRateEST(FileInputStream fis, String inputCurrency){

//import java.text.DecimalFormat;
//import java.text.DecimalFormatSymbols;

//public List<String> fisToCurrencies(FileInputStream fis){
//	log.debug("[fisToCurrencies]");
//	if(fis != null){
//		List<String> returnCurrencies = new ArrayList<String>();
//		Document doc = BankUtil.fisToDocument(fis);
//		log.debug("Root element: " + doc.getDocumentElement().getNodeName());
//
//		// FOLLOWING IS SPECIFIC TO CERTAIN XML:
//		NodeList nList = doc.getElementsByTagName("Currency"); // row
//		log.debug(nList.getLength() + " nodes found");
//
////		XPath xPath = XPathFactory.newInstance().newXPath();
////		String expression = "//item[currency='"+inputCurrency+"']/rate[.]";
////		NodeList currencyNodes = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
////		
//		// CURRENCY ELEMENTS: // TODO REPLACE WITH XPATH
//		// THIS ONLY TAKES FROM ATTRIBUTE SO ONLY WORKS FOR EST
//		for (int temp = 0; temp < nList.getLength(); temp++) {
//			Node nNode = nList.item(temp);
//			if (nNode.getNodeType() == Node.ELEMENT_NODE) {
//				Element eElement = (Element) nNode;
//				String name = eElement.getAttribute("name"); // shortName
//				log.debug("name: " + eElement.getAttribute("name") + " text: " + eElement.getAttribute("text") + " rate: " + eElement.getAttribute("rate"));
//				returnCurrencies.add(name);
//			}
//		}
//		return returnCurrencies;	
//	}else{
//		log.error("[fisToCurrencies] fis null!");
//		return null;
//	}
//}


//getDownloadUrlByDate		
//int year = selectedDate.getYear();
//int month = selectedDate.getMonth().getValue();
//int day = selectedDate.getDayOfMonth();
//LocalDateTime dateTime = //LocalDateTime.of(1986, Month.APRIL, 8, 12, 30);
//String dateInUrl = BankUtil.datepickerToUrlFormat(selectedDate, "dd.MM.yy","yyyy-MM-dd");
//String result = "http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1="+dateInUrl+"&lng=est&print=off"; //"http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1=2010-12-30&lng=est&print=off";
//return result;


//// PARSE EST FLOAT 16 123,123123123
//DecimalFormat df = new DecimalFormat(); // new DecimalFormat("#.#");
//DecimalFormatSymbols symbols = new DecimalFormatSymbols();
//symbols.setDecimalSeparator(',');
//symbols.setGroupingSeparator(' ');
////symbols.setGroupingSeparator(' ');
//df.setDecimalFormatSymbols(symbols);
////df.parse(p);
////float f = df.parse(str).floatValue();
//
//resultRate = df.parse(resultRateString).floatValue();
////resultRate = Float.parseFloat(resultRateString);