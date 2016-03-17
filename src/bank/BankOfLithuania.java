package bank;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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

public class BankOfLithuania implements BankInterface {
	static Logger log = LoggerFactory.getLogger(BankOfLithuania.class);
	
	@Override
	public String getFileNameByDate(LocalDate selectedDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(fileNameDatePattern); // "yyyy-MM-dd"
		String dateInFile = selectedDate.format(formatter);
		String result = "bankOfLithuania-"+dateInFile+".xml";
		return result;
	}
	
	@Override
	public String getDownloadUrlByDate(LocalDate selectedDate) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); //("yyyy-MM-dd HH:mm");
		String formattedDateTime = selectedDate.format(formatter); //dateTime.format(formatter); // "1986-04-08 12:30"
		String result = "http://webservices.lb.lt/ExchangeRates/ExchangeRates.asmx/getExchangeRatesByDate?Date="+formattedDateTime; //"yyyy-MM-dd" //"http://webservices.lb.lt/ExchangeRates/ExchangeRates.asmx/getExchangeRatesByDate?Date=2010-12-30";
		return result;
	}
	
	// Lithuania: node item, in which node currency and node rate
	@Override
	public Float fisToRate(FileInputStream fis, String inputCurrency) { // public static Float fisToRateLT(FileInputStream fis, String inputCurrency){
		log.debug("[fisToRateLT]");
		Float resultRate = null;
		try {
			Document doc = BankUtil.fisToDocument(fis);
			XPath xPath = XPathFactory.newInstance().newXPath();
			
			//String expression = "//item/currency[.='"+inputCurrency+"']"+"/../rate[.]"; //"/currencies/currency";	    // EG Currency name="AED" rate="3,12312321"     
			String expression = "//item[currency='"+inputCurrency+"']/rate[.]";
			log.debug("[fisToRateLT] expression: " + expression);
			//NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
			Node node = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
			if(node != null){
				//log.debug("node is " + node); // [rate: null]
				String currencyRate = node.getTextContent(); // node.getNodeValue();
				//log.debug("currencyrate is " + currencyRate); // currencyrate is 1.0891
				Float currencyRateFloat = Float.parseFloat(currencyRate);
				log.debug("[fisToRateLT] HAVE NODE AND currencyRateFloat is: " + currencyRateFloat);
				// For Quantity, result rate is rate / quantity for LT!!!
				String expressionQuantity = "//item[currency='"+inputCurrency+"']/quantity[.]";
				Node nodeQuantity = (Node) xPath.compile(expressionQuantity).evaluate(doc, XPathConstants.NODE);
				String quantity = nodeQuantity.getTextContent();
				Float quantityFloat = Float.parseFloat(quantity);
				log.debug("[fisToRateLT] quantityFloat is: " + quantityFloat);

				resultRate = currencyRateFloat / quantityFloat;
				log.debug("[fisToRateLT] rate is: " + resultRate);
			}else{
				log.error("[fisToRateLT] I DO NOT HAVE NODE!!!!!!!!!!");
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
			NodeList nList = doc.getElementsByTagName("currency"); // row
			for (int i = 0; i < nList.getLength(); i++) {
				String nodeValue = nList.item(i).getTextContent();
				returnCurrencies.add(nodeValue);
				//log.debug("fisToCurrencies] nodeValue: " + nodeValue);
			}
			return returnCurrencies;
		}else{
			log.error("[fisToCurrencies] fis null!");
			return null;
		}
	}
}





















//if (node.getNodeType() == Node.ELEMENT_NODE) {
//Element eElement = (Element) node;
//resultRate = eElement.getAttribute("rate");
//log.debug("[fisToRateLT] I HAVE RATE, RATE IS: " + resultRate);
//}else{
//log.debug("[fisToRateLT] NO RATE?");
//}

//XPath xPath = XPathFactory.newInstance().newXPath();
//String expression = "//currency";
//NodeList nList = (NodeList) xPath.compile(expression).evaluate(doc, XPathConstants.NODESET);
