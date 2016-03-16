package dao;

import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class BankUtil {
	static Logger log = LoggerFactory.getLogger(BankUtil.class);
	
	// Convert date picker date to correct format that suits the needs.
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
	
	// eg Bank of Estonia PARSE FLOAT 16 123,123123123 would use " " as thousands separator and "," as decimal separator
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
	
	// Convert fileInputStream to Document
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
}
