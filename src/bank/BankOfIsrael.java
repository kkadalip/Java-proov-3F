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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import dao.BankInterface;
import dao.BankUtil;
import model.Result;

// http://www.boi.org.il/en/Markets/Pages/explainxml.aspx
// TODAY http://www.boi.org.il/currency.xml
// 01.03.2012 http://www.boi.org.il/currency.xml?rdate=20120301
public class BankOfIsrael implements BankInterface {
	static Logger log = LoggerFactory.getLogger(BankOfIsrael.class);
	
	@Override
	public String getFileNameByDate(LocalDate selectedDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fileNameDatePattern); // "yyyy-MM-dd"
		String dateInFile = selectedDate.format(formatter);
		String result = "bankOfIsrael-"+dateInFile+".xml";
		return result;
	}
	
	@Override
	public String getDownloadUrlByDate (LocalDate selectedDate){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd"); //("yyyy-MM-dd HH:mm");
		String formattedDateTime = selectedDate.format(formatter); //dateTime.format(formatter); // "1986-04-08 12:30"
		String result = "http://www.boi.org.il/currency.xml?rdate="+formattedDateTime; //"yyyy-MM-dd"
		return result;
	}
		
	@Override
	public Float fisToRate(FileInputStream fis, String inputCurrency) {
		Float resultRate = null;
		try {
			log.debug("[fisToRate Israel]");
			Document doc = BankUtil.fisToDocument(fis);
			XPath xPath = XPathFactory.newInstance().newXPath();
			String expression = "//CURRENCY[CURRENCYCODE='"+inputCurrency+"']/RATE[.]";
			log.debug("[fisToRate Israel] expression: " + expression);
			Node node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
			if(node != null){
				String currencyRate = node.getTextContent(); // node.getNodeValue();
				resultRate = Float.parseFloat(currencyRate);
				log.debug("[fisToRate Israel] rate is: " + resultRate);
			}else{
				log.error("[fisToRate Israel] I DO NOT HAVE NODE!!!!!!!!!!");
				return null;
			}
		} catch (Exception e) {
			log.error("[fisToRateLT] failed!", e);
		}
		return resultRate;
	}

	@Override
	public List<String> fisToCurrencies(FileInputStream fis) {
		log.debug("[fisToCurrencies]");
		if(fis != null){
			List<String> returnCurrencies = new ArrayList<String>();
			Document doc = BankUtil.fisToDocument(fis);
			NodeList nList = doc.getElementsByTagName("CURRENCYCODE"); // row
			for (int i = 0; i < nList.getLength(); i++) {
				String nodeValue = nList.item(i).getTextContent();
				returnCurrencies.add(nodeValue);
				//log.debug("[fisToCurrencies] nodeValue: " + nodeValue);
			}
			return returnCurrencies;
		}else{
			log.error("[fisToCurrencies] fis null!");
			return null;
		}
	}
	
	
	public List<String> getCurrencies(ServletContext context, LocalDate selectedDate){
		String url = getDownloadUrlByDate(selectedDate);
		String filename = getFileNameByDate(selectedDate);
		FileInputStream fisIsrael = BankUtil.getFisForX(context, url, filename);
		List<String> currencies = fisToCurrencies(fisIsrael);
		return currencies;
	}

	@Override
	public Result getResult(ServletContext context, LocalDate selectedDate, String inputCurrency, String outputCurrency, Float inputMoneyAmount) {
		String translationLabel = "label.bankOfIsrael";
		
		String url = getDownloadUrlByDate(selectedDate);
		String filename = getFileNameByDate(selectedDate);
		Float fisISRinputRate = fisToRate(BankUtil.getFisForX(context, url, filename),inputCurrency);
		Float fisISRoutputRate = fisToRate(BankUtil.getFisForX(context, url, filename),outputCurrency);
		if(fisISRinputRate != null && fisISRoutputRate != null && inputMoneyAmount != null){
			Float outputAmountIsrael = fisISRinputRate / fisISRoutputRate * inputMoneyAmount;
			String output = BankUtil.displayedFloat(outputAmountIsrael);
			log.debug("[calculateResults] Bank of Israel RESULT: " + output);
			return new Result(translationLabel, output);
		}else{
			log.error("[calculateResults] Bank of Israel DOES NOT HAVE RESULT!");
			return new Result(translationLabel,"-");
		}		
	}
}





























//if(fisISRinputRate != null){log.debug("[calculateResults] fisISRinputRate: " + fisISRinputRate.toString());
//}else{log.debug("[calculateResults] fisISRinputRate IS NULL!");}
//if(fisISRoutputRate != null){log.debug("[calculateResults]  fisISRoutputRate: " + fisISRoutputRate.toString());
//}else{log.debug("[calculateResults] fisISRoutputRate IS NULL!");}

//String dateInUrl = BankUtil.datepickerToUrlFormat(selectedDate, "dd.MM.yy","yyyyMMdd");		