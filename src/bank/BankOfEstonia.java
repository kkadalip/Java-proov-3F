package bank;

import java.io.FileInputStream;
//import java.text.DecimalFormat;
//import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

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

public class BankOfEstonia implements BankInterface {
	static Logger log = LoggerFactory.getLogger(BankOfEstonia.class);
	
	// Estonia: node Currency attribute rate
	@Override
	//public static String fisToRateEST(FileInputStream fis, String inputCurrency){
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
		try {
			List<String> returnCurrencies = new ArrayList<String>();
			Document doc = BankUtil.fisToDocument(fis);
			log.debug("Root element: " + doc.getDocumentElement().getNodeName());

			// FOLLOWING IS SPECIFIC TO CERTAIN XML:
			NodeList nList = doc.getElementsByTagName("Currency"); // row
			log.debug(nList.getLength() + " nodes found");

			// TODO
//			XPath xPath = XPathFactory.newInstance().newXPath();
//			String expression = "//item[currency='"+inputCurrency+"']/rate[.]";
//			NodeList currencyNodes = (Node) xPath.compile(expression).evaluate(doc, XPathConstants.NODE);
//			
			// CURRENCY ELEMENTS: // TODO REPLACE WITH XPATH
			// THIS ONLY TAKES FROM ATTRIBUTE SO ONLY WORKS FOR EST
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