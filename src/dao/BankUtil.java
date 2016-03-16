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
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import model.Result;

public class BankUtil {
	static Logger log = LoggerFactory.getLogger(BankUtil.class); // info trace debug warn error
	
	// ATM USING ONLY FOR DISPLAY!!!
//	public static List<Currency> downloadAllForDate(ServletContext context, String selectedDate){ //, Date date){
	public static List<String> downloadAllForDate(ServletContext context, String selectedDate){ //, Date date){ // TODO list of classes/banks
		//log.debug("[downloadAllForDate]");
		log.debug("[downloadAllForDate] selectedDate " + selectedDate);
		
//		String day = "30";
//		String month = "12";
//		String year = "2010";
//		String timeString = year+"-"+month+"-"+day;
		
//		String dateInUrl = datepickerToUrlFormat(selectedDate);
		String dateInUrl = 	BankUtil.datepickerToUrlFormat(selectedDate, "dd.MM.yy","yyyy-MM-dd");

		String bankOfEstonia = "http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1="+dateInUrl+"&lng=est&print=off"; //"http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1=2010-12-30&lng=est&print=off";
		String bankOfLithuania = "http://webservices.lb.lt/ExchangeRates/ExchangeRates.asmx/getExchangeRatesByDate?Date="+dateInUrl; //"http://webservices.lb.lt/ExchangeRates/ExchangeRates.asmx/getExchangeRatesByDate?Date=2010-12-30";

		// DOWNLOAD FOR X      URL(with date) and file name (eg eesti-2010-12-30)
		FileInputStream fisEstonia = BankUtil.getFisForX(context, bankOfEstonia,"bankOfEstonia-"+dateInUrl+".xml");
		//fisEstonia = getFisForX(context, bankOfEstonia,"bankOfEstonia-"+timeString+".xml");
		FileInputStream fisLithuania = BankUtil.getFisForX(context, bankOfLithuania,"bankOfLithuania-"+dateInUrl+".xml");

		// TODO
//		List<Currency> bankOfEstoniaCurrencies = fisToCurrencies(fisEstonia);
//		List<Currency> bankOfLithuaniaCurrencies = fisToCurrencies(fisLithuania);
		
		List<String> uniqueCurrencies = new ArrayList<String>();

		BankOfEstonia bankEST = new BankOfEstonia();
		List<String> bankOfEstoniaCurrencies = bankEST.fisToCurrencies(fisEstonia);
		BankOfLithuania bankLT = new BankOfLithuania();
		List<String> bankOfLithuaniaCurrencies = bankLT.fisToCurrencies(fisLithuania);
		
		List<List<String>> listsOfCurrencies = new ArrayList<List<String>>();
		listsOfCurrencies.add(bankOfEstoniaCurrencies);
		listsOfCurrencies.add(bankOfLithuaniaCurrencies);
		
		for(List<String> currencyList : listsOfCurrencies){
			if(currencyList != null && !currencyList.isEmpty()){
				for(String currency : currencyList){
					if(!uniqueCurrencies.contains(currency)){
						uniqueCurrencies.add(currency);
					}
				}
			}else{
				log.error("[downloadAllForDate] currencyList null or empty!!!");
			}

		}
		return uniqueCurrencies;
	}

	public List<Result> calculateResults(ServletContext context, Float inputMoneyAmount, String inputCurrency, String outputCurreny, String selectedDate){ //, List<Class> bankClasses){ //, String date){
		//List<Class> bankClasses = new ArrayList<Class>();
//	public static List<Result> calculateResults(ServletContext context, Float inputMoneyAmount, String inputCurrency, String outputCurreny, String selectedDate){ //, String date){
		log.debug("[calculateResults]");
		
		String dateInUrl = 	BankUtil.datepickerToUrlFormat(selectedDate, "dd.MM.yy","yyyy-MM-dd");
//		String dateInUrl = datepickerToUrlFormat(selectedDate);

//		FileInputStream fisEST = getFisForX(context, bankOfEstonia,"bankOfEstonia-2010-12-30.xml");
//		String fisESTinputRate = fisToRateEST(fisEST,inputCurrency);
		//String fisEstoniaOutputRate = fisToRate(fisEstonia,outputCurreny); // java.io.IOException: Stream Closed
		
		log.debug("[calculateResults] GETTING INPUT CURRENCY RATE FOR: " + inputCurrency);
		String bankOfESTurl = "http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1="+dateInUrl+"&lng=est&print=off"; //String bankOfEstonia = "http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1=2010-12-30&lng=est&print=off";		
		String bankofESTfileName = "bankOfEstonia-"+dateInUrl+".xml";//String bankofEST = "bankOfEstonia-2010-12-30.xml";
		BankOfEstonia bankEST = new BankOfEstonia();
		Float fisESTinputRate = bankEST.fisToRate(BankUtil.getFisForX(context, bankOfESTurl,bankofESTfileName),inputCurrency);
		Float fisESToutputRate = bankEST.fisToRate(BankUtil.getFisForX(context, bankOfESTurl,bankofESTfileName),outputCurreny);
//		String fisESTinputRate = fisToRateEST(getFisForX(context, bankOfESTurl,bankofESTfileName),inputCurrency);
//		String fisESToutputRate = fisToRateEST(getFisForX(context, bankOfESTurl,bankofESTfileName),outputCurreny);
		
		if(fisESTinputRate != null){
			log.debug("[calculateResults] going to parse fisEstoniaInputRate: " + fisESTinputRate.toString());
		}else{
			log.debug("[calculateResults] fisEstoniaInputRate IS NULL!");
		}
		if(fisESToutputRate != null){
			log.debug("[calculateResults]  ...and outputrate fisESToutputRate: " + fisESToutputRate.toString());
		}else{
			log.debug("[calculateResults] fisESToutputRate IS NULL!");
		}
		
		
		//FileInputStream fisLT = getFisForX(context, bankOfEstonia,"bankOfEstonia-2010-12-30.xml");
		String bankOfLTurl = "http://webservices.lb.lt/ExchangeRates/ExchangeRates.asmx/getExchangeRatesByDate?Date="+dateInUrl;
		String bankOfLTfileName = "bankOfLithuania-"+dateInUrl+".xml"; //String bankOfLT = "bankOfLithuania-2010-12-30.xml";
		BankOfLithuania bankLT = new BankOfLithuania();
		Float fisLTinputRate = bankLT.fisToRate(BankUtil.getFisForX(context, bankOfLTurl,bankOfLTfileName),inputCurrency);
		Float fisLToutputRate = bankLT.fisToRate(BankUtil.getFisForX(context, bankOfLTurl,bankOfLTfileName),outputCurreny);
//		Float fisLTinputRate = fisToRateLT(getFisForX(context, bankOfLTurl,bankOfLTfileName),inputCurrency);
//		Float fisLToutputRate = fisToRateLT(getFisForX(context, bankOfLTurl,bankOfLTfileName),outputCurreny);
		
		if(fisLTinputRate != null){
			log.debug("[calculateResults] going to parse fisLTinputRate: " + fisLTinputRate.toString());
		}else{
			log.debug("[calculateResults] fisLTinputRate IS NULL!");
		}
		if(fisLToutputRate != null){
			log.debug("[calculateResults]  ...and outputrate fisLToutputRate: " + fisLToutputRate.toString());
		}else{
			log.debug("[calculateResults] fisLToutputRate IS NULL!");
		}
		
		List<Result> resultsList = new ArrayList<Result>();
		
		log.debug("[calculateResults] PARSING if not null obv");
		if(fisESTinputRate != null && fisESToutputRate != null){
//			DecimalFormat df = new DecimalFormat(); // new DecimalFormat("#.#");
//			DecimalFormatSymbols symbols = new DecimalFormatSymbols();
//			symbols.setDecimalSeparator(',');
//			symbols.setGroupingSeparator(' ');
//			//symbols.setGroupingSeparator(' ');
//			df.setDecimalFormatSymbols(symbols);
//			//df.parse(p);
//			//float f = df.parse(str).floatValue();
			
			//Float inputCurrencyFloatEST = df.parse(fisESTinputRate).floatValue(); // Float.parseFloat(fisEstoniaInputRate);
			//Float outputCurrencyFloatEST = df.parse(fisESToutputRate).floatValue(); // Float.parseFloat(fisEstoniaOutputRate);
//				if(inputCurrencyFloatEST != null && outputCurrencyFloatEST != null && inputMoneyAmount != null){
//				Float outputAmountEstonia = inputCurrencyFloatEST / outputCurrencyFloatEST * inputMoneyAmount;
			if(fisESTinputRate != null && fisESToutputRate != null && inputMoneyAmount != null){
				Float outputAmountEstonia = fisESTinputRate / fisESToutputRate * inputMoneyAmount;
				log.debug("[calculateResults]  input: " + fisESTinputRate + " / " + fisESToutputRate + " * " +  inputMoneyAmount);
				log.debug("[calculateResults]  Bank of Estonia RESULT: " + outputAmountEstonia.toString());
				resultsList.add(new Result("Bank of Estonia", outputAmountEstonia.toString()));
			}
		}else{
			log.error("Bank of Estonia DOES NOT HAVE RESULT!");
			resultsList.add(new Result("Bank of Estonia","-"));
		}
		
		if(fisLTinputRate != null && fisLToutputRate != null && inputMoneyAmount != null){
			Float outputAmountLithuania = fisLTinputRate / fisLToutputRate * inputMoneyAmount;
			log.debug("Bank of Lithuania RESULT: " + outputAmountLithuania.toString());
			resultsList.add(new Result("Bank of Lithuania", outputAmountLithuania.toString()));
		}else{
			log.error("Bank of Lithuania DOES NOT HAVE RESULT!");
			resultsList.add(new Result("Bank of Lithuania","-"));
		}
		// FAILINIMEDEST LIST (model Bank nt), SIIN KÄIN NAD LÄBI JA PÄRIN VÄLJA ÕIGE ASJA + ARVUTAN?
		return resultsList;
	}
	
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
