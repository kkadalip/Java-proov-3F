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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;

import bank.BankOfEstonia;
import bank.BankOfIsrael;
import bank.BankOfLithuania;
import model.Result;

public class BankUtil {
	static Logger log = LoggerFactory.getLogger(BankUtil.class); // info trace debug warn error
	
	// ATM USING ONLY FOR DISPLAY!!!
//	public static List<Currency> downloadAllForDate(ServletContext context, String selectedDate){ //, Date date){
	public static List<String> downloadAllForDate(ServletContext context, LocalDate selectedDate){ //, Date date){ // TODO list of classes/banks
		log.debug("[downloadAllForDate] selectedDate " + selectedDate);
		
		BankOfEstonia bankOfEstonia = new BankOfEstonia(); // NEW
		String bankOfEstoniaUrl = bankOfEstonia.getDownloadUrlByDate(selectedDate); // NEW
		String bankOfEstoniaFileName = bankOfEstonia.getFileNameByDate(selectedDate);
		
		BankOfLithuania bankOfLithuania = new BankOfLithuania();
		String bankOfLithuaniaUrl = bankOfLithuania.getDownloadUrlByDate(selectedDate);
		String bankOfLithuaniaFileName = bankOfLithuania.getFileNameByDate(selectedDate);
		
		BankOfIsrael bankOfIsrael = new BankOfIsrael(); // NEW
		String bankOfIsraelUrl = bankOfIsrael.getDownloadUrlByDate(selectedDate); // NEW
		String bankOfIsraelFileName = bankOfIsrael.getFileNameByDate(selectedDate);

//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//		String dateInFile = selectedDate.format(formatter);
		
		// DOWNLOAD FOR X      URL(with date) and file name (eg eesti-2010-12-30)
		//fisEstonia = getFisForX(context, bankOfEstonia,"bankOfEstonia-"+timeString+".xml");
		FileInputStream fisEstonia = BankUtil.getFisForX(context, bankOfEstoniaUrl,bankOfEstoniaFileName);
		FileInputStream fisLithuania = BankUtil.getFisForX(context, bankOfLithuaniaUrl,bankOfLithuaniaFileName);
		FileInputStream fisIsrael = BankUtil.getFisForX(context, bankOfIsraelUrl,bankOfIsraelFileName);

		List<String> uniqueCurrencies = new ArrayList<String>();

		BankOfEstonia bankEST = new BankOfEstonia();
		List<String> bankOfEstoniaCurrencies = bankEST.fisToCurrencies(fisEstonia);
		BankOfLithuania bankLT = new BankOfLithuania();
		List<String> bankOfLithuaniaCurrencies = bankLT.fisToCurrencies(fisLithuania);
		BankOfIsrael bankISR = new BankOfIsrael();
		List<String> bankOfIsraelCurrencies = bankISR.fisToCurrencies(fisIsrael);
		
		List<List<String>> listsOfCurrencies = new ArrayList<List<String>>();
		listsOfCurrencies.add(bankOfEstoniaCurrencies);
		listsOfCurrencies.add(bankOfLithuaniaCurrencies);
		listsOfCurrencies.add(bankOfIsraelCurrencies);
		
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

	public List<Result> calculateResults(ServletContext context, Float inputMoneyAmount, String inputCurrency, String outputCurreny, LocalDate selectedDate){ //, List<Class> bankClasses){ //, String date){
		//List<Class> bankClasses = new ArrayList<Class>();
		//public static List<Result> calculateResults(ServletContext context, Float inputMoneyAmount, String inputCurrency, String outputCurreny, String selectedDate){ //, String date){
		log.debug("[calculateResults]");
		
//		String dateInUrl = 	BankUtil.datepickerToUrlFormat(selectedDate, "dd.MM.yy","yyyy-MM-dd");
//		String dateInUrlIsrael = BankUtil.datepickerToUrlFormat(selectedDate, "dd.MM.yy","yyyyMMdd");

		List<Result> resultsList = new ArrayList<Result>();
		
		// TODO add to BankUtil getFileFromDate bank_name date
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String dateInFile = selectedDate.format(formatter);
		
		// ESTONIA START
		log.debug("[calculateResults] GETTING INPUT CURRENCY RATE FOR: " + inputCurrency);
		BankOfEstonia bankEST = new BankOfEstonia(); // NEW
		String bankOfESTurl = bankEST.getDownloadUrlByDate(selectedDate); // NEW
		String bankofESTfileName = "bankOfEstonia-"+dateInFile+".xml";//String bankofEST = "bankOfEstonia-2010-12-30.xml";
		Float fisESTinputRate = bankEST.fisToRate(BankUtil.getFisForX(context, bankOfESTurl,bankofESTfileName),inputCurrency);
		Float fisESToutputRate = bankEST.fisToRate(BankUtil.getFisForX(context, bankOfESTurl,bankofESTfileName),outputCurreny);
		if(fisESTinputRate != null){log.debug("[calculateResults] fisESTinputRate: " + fisESTinputRate.toString());
		}else{log.debug("[calculateResults] fisEstoniaInputRate IS NULL!");}
		if(fisESToutputRate != null){ log.debug("[calculateResults] fisESToutputRate: " + fisESToutputRate.toString()); 
		}else{log.debug("[calculateResults] fisESToutputRate IS NULL!");}
		// ------------------------
		if(fisESTinputRate != null && fisESToutputRate != null){
			if(fisESTinputRate != null && fisESToutputRate != null && inputMoneyAmount != null){
				Float outputAmountEstonia = fisESTinputRate / fisESToutputRate * inputMoneyAmount;
				String output = displayedFloat(outputAmountEstonia);
				log.debug("[calculateResults]  input: " + fisESTinputRate + " / " + fisESToutputRate + " * " +  inputMoneyAmount);
				log.debug("[calculateResults]  Bank of Estonia RESULT: " + output); //outputAmountEstonia.toString());
				resultsList.add(new Result("Bank of Estonia", output)); // outputAmountEstonia.toString()
			}
		}else{
			log.error("Bank of Estonia DOES NOT HAVE RESULT!");
			resultsList.add(new Result("Bank of Estonia","-"));
		}
		// ESTONIA END
		
		// LITHUANIA START
		BankOfLithuania bankLT = new BankOfLithuania();
		String bankOfLTurl = bankLT.getDownloadUrlByDate(selectedDate); // NEW
		String bankOfLTfileName = "bankOfLithuania-"+dateInFile+".xml"; //String bankOfLT = "bankOfLithuania-2010-12-30.xml";
		Float fisLTinputRate = bankLT.fisToRate(BankUtil.getFisForX(context, bankOfLTurl,bankOfLTfileName),inputCurrency);
		Float fisLToutputRate = bankLT.fisToRate(BankUtil.getFisForX(context, bankOfLTurl,bankOfLTfileName),outputCurreny);
		if(fisLTinputRate != null){log.debug("[calculateResults] fisLTinputRate: " + fisLTinputRate.toString());
		}else{log.debug("[calculateResults] fisLTinputRate IS NULL!");}
		if(fisLToutputRate != null){log.debug("[calculateResults] fisLToutputRate: " + fisLToutputRate.toString());
		}else{log.debug("[calculateResults] fisLToutputRate IS NULL!");}
		// ------------------------
		if(fisLTinputRate != null && fisLToutputRate != null && inputMoneyAmount != null){
			Float outputAmountLithuania = fisLTinputRate / fisLToutputRate * inputMoneyAmount;
			log.debug("[calculateResults] Bank of Lithuania RESULT: " + outputAmountLithuania.toString());
			resultsList.add(new Result("Bank of Lithuania", displayedFloat(outputAmountLithuania))); //outputAmountLithuania.toString()));
		}else{
			log.error("[calculateResults] Bank of Lithuania DOES NOT HAVE RESULT!");
			resultsList.add(new Result("Bank of Lithuania","-"));
		}
		// LITHUANIA END
		
		// ISRAEL START
		BankOfIsrael bankISR = new BankOfIsrael();
		String bankOfISRurl = bankISR.getDownloadUrlByDate(selectedDate);
		String bankOfISRfileName = "bankOfIsrael-"+dateInFile+".xml";
		Float fisISRinputRate = bankISR.fisToRate(BankUtil.getFisForX(context, bankOfISRurl,bankOfISRfileName),inputCurrency);
		Float fisISRoutputRate = bankISR.fisToRate(BankUtil.getFisForX(context, bankOfISRurl,bankOfISRfileName),outputCurreny);
		if(fisISRinputRate != null){log.debug("[calculateResults] fisISRinputRate: " + fisISRinputRate.toString());
		}else{log.debug("[calculateResults] fisISRinputRate IS NULL!");}
		if(fisISRoutputRate != null){log.debug("[calculateResults]  fisISRoutputRate: " + fisISRoutputRate.toString());
		}else{log.debug("[calculateResults] fisISRoutputRate IS NULL!");}
		// ------------------------
		if(fisISRinputRate != null && fisISRoutputRate != null && inputMoneyAmount != null){
			Float outputAmountIsrael = fisISRinputRate / fisISRoutputRate * inputMoneyAmount;
			log.debug("[calculateResults] Bank of Israel RESULT: " + outputAmountIsrael.toString());
			resultsList.add(new Result("Bank of Israel", displayedFloat(outputAmountIsrael))); //outputAmountLithuania.toString()));
		}else{
			log.error("[calculateResults] Bank of Israel DOES NOT HAVE RESULT!");
			resultsList.add(new Result("Bank of Israel","-"));
		}
		// ISRAEL END
		
		return resultsList;
	}
	
	public static String displayedFloat(Float regularFloat){
		DecimalFormat df = new DecimalFormat("0.00");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator('.');
		df.setDecimalFormatSymbols(symbols);
		String output = df.format(regularFloat);
		return output;
	}
	
	// INPUT: servlet context (for dynamic file paths), URL for downloading XML, file name for saved file (main part of it)
	// OUTPUT: file gets saved and fileinputstream from saved file
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

	
	// CONVERT rate value to Float. Eg Bank of Estonia PARSE FLOAT 16 123,123123123 would use " " as thousands separator and "," as decimal separator
	public static Float parseStringNumber(String number, char thousandsSeparator, char decimalSeparator){
		DecimalFormat df = new DecimalFormat(); // "#.00" new DecimalFormat("#.#");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(',');
		symbols.setGroupingSeparator(' ');
		df.setDecimalFormatSymbols(symbols);
		Float resultRate = null;
		try {
			resultRate = df.parse(number).floatValue();
			log.debug("[parseStringNumber] resultRate: " + resultRate);
		} catch (ParseException e) {
			log.error("[parseStringNumber] could not parse!",e);
		}
		return resultRate;
	}
}




















//FileInputStream fisEstonia = BankUtil.getFisForX(context, bankOfEstoniaUrl,"bankOfEstonia-"+dateInFile+".xml");
//FileInputStream fisLithuania = BankUtil.getFisForX(context, bankOfLithuaniaUrl,"bankOfLithuania-"+dateInFile+".xml");
//FileInputStream fisIsrael = BankUtil.getFisForX(context, bankOfIsraelUrl,"bankOfIsrael-"+dateInFile+".xml");

//String bankOfISRurl = "http://www.boi.org.il/currency.xml?rdate="+dateInUrlIsrael;

//FileInputStream fisLT = getFisForX(context, bankOfEstonia,"bankOfEstonia-2010-12-30.xml");

// String bankOfLTurl = "http://webservices.lb.lt/ExchangeRates/ExchangeRates.asmx/getExchangeRatesByDate?Date="+dateInUrl;
//String bankOfESTurl = "http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1="+dateInUrl+"&lng=est&print=off"; //String bankOfEstonia = "http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1=2010-12-30&lng=est&print=off";		

//String dateInUrl = 	BankUtil.datepickerToUrlFormat(selectedDate, "dd.MM.yy","yyyy-MM-dd");
//String dateInUrlIsrael = BankUtil.datepickerToUrlFormat(selectedDate, "dd.MM.yy","yyyyMMdd");
//String bankOfEstonia = "http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1="+dateInUrl+"&lng=est&print=off"; //"http://statistika.eestipank.ee/Reports?type=curd&format=xml&date1=2010-12-30&lng=est&print=off";
//String bankOfIsrael = "http://www.boi.org.il/currency.xml?rdate="+dateInUrlIsrael;

// CONVERT date picker date to correct format that suits the needs.
// INPUT: 16.03.2016 (if dateFormat is "dd.MM.yy")
// OUTPUT: 2016-03-16 (if outputFormat is "yyyy-MM-dd")
//public static String datepickerToUrlFormat(String selectedDate, String inputDateFormat, String outputDateFormat){
//	SimpleDateFormat format = new SimpleDateFormat(inputDateFormat); // eg "dd.MM.yy"
//	Date date = null;
//	try {
//		date = format.parse(selectedDate);
//	} catch (java.text.ParseException e) {
//		log.error("[datepickerToUrlFormat] could not parse date picker url!", e);
//	}
//	log.debug("[datepickerToUrlFormat] DATE IS: " + date);
//	SimpleDateFormat format2 = new SimpleDateFormat(outputDateFormat); // eg "yyyy-MM-dd" or "dd-MM-yy"
//	String dateInUrl = format2.format(date);
//	log.debug("[datepickerToUrlFormat] RESULT dateInUrl: " + dateInUrl);
//	return dateInUrl;
//}



//if(fisESTinputRate != null && fisESToutputRate != null){
//	DecimalFormat df = new DecimalFormat(); // new DecimalFormat("#.#");
//	DecimalFormatSymbols symbols = new DecimalFormatSymbols();
//	symbols.setDecimalSeparator(',');
//	symbols.setGroupingSeparator(' ');
//	//symbols.setGroupingSeparator(' ');
//	df.setDecimalFormatSymbols(symbols);
//	//df.parse(p);
//	//float f = df.parse(str).floatValue();
	
	//Float inputCurrencyFloatEST = df.parse(fisESTinputRate).floatValue(); // Float.parseFloat(fisEstoniaInputRate);
	//Float outputCurrencyFloatEST = df.parse(fisESToutputRate).floatValue(); // Float.parseFloat(fisEstoniaOutputRate);
//		if(inputCurrencyFloatEST != null && outputCurrencyFloatEST != null && inputMoneyAmount != null){
//		Float outputAmountEstonia = inputCurrencyFloatEST / outputCurrencyFloatEST * inputMoneyAmount;

//FileInputStream fisEST = getFisForX(context, bankOfEstonia,"bankOfEstonia-2010-12-30.xml");
//String fisESTinputRate = fisToRateEST(fisEST,inputCurrency);
//String fisEstoniaOutputRate = fisToRate(fisEstonia,outputCurreny); // java.io.IOException: Stream Closed

//String dateInUrl = datepickerToUrlFormat(selectedDate);

//String day = "30";
//String month = "12";
//String year = "2010";
//String timeString = year+"-"+month+"-"+day;

//String dateInUrl = datepickerToUrlFormat(selectedDate);

//List<Currency> bankOfEstoniaCurrencies = fisToCurrencies(fisEstonia);
//List<Currency> bankOfLithuaniaCurrencies = fisToCurrencies(fisLithuania);

//String fisESTinputRate = fisToRateEST(getFisForX(context, bankOfESTurl,bankofESTfileName),inputCurrency);
//String fisESToutputRate = fisToRateEST(getFisForX(context, bankOfESTurl,bankofESTfileName),outputCurreny);

//Float fisLTinputRate = fisToRateLT(getFisForX(context, bankOfLTurl,bankOfLTfileName),inputCurrency);
//Float fisLToutputRate = fisToRateLT(getFisForX(context, bankOfLTurl,bankOfLTfileName),outputCurreny);