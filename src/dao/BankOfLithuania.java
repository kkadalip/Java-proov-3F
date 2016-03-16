package dao;

import java.io.FileInputStream;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class BankOfLithuania implements BankInterface {
	static Logger log = LoggerFactory.getLogger(BankOfLithuania.class);
	
	// Lithuania: node item, in which node currency and node rate
	@Override
	public Float fisToRate(FileInputStream fis, String inputCurrency) { // public static Float fisToRateLT(FileInputStream fis, String inputCurrency){
		//String currencyRate = null;
		Float resultRate = null;
		try {
			log.debug("[fisToRateLT]");
			//Float returnValue;
			
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

			//					if (node.getNodeType() == Node.ELEMENT_NODE) {
			//						Element eElement = (Element) node;
			//						resultRate = eElement.getAttribute("rate");
			//						log.debug("[fisToRateLT] I HAVE RATE, RATE IS: " + resultRate);
			//					}else{
			//						log.debug("[fisToRateLT] NO RATE?");
			//					}
		} catch (Exception e) {
			log.error("[fisToRateLT] failed!", e);
		}
		return resultRate;
	}
}
