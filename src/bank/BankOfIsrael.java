package bank;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
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

// http://www.boi.org.il/en/Markets/Pages/explainxml.aspx
// TODAY http://www.boi.org.il/currency.xml
// 01.03.2012 http://www.boi.org.il/currency.xml?rdate=20120301
public class BankOfIsrael implements BankInterface {
	static Logger log = LoggerFactory.getLogger(BankOfIsrael.class);
	
	@Override
	public String getDownloadUrlByDate (String selectedDate){ // TODO Date selectedDate
		String dateInUrl = BankUtil.datepickerToUrlFormat(selectedDate, "dd.MM.yy","yyyyMMdd");
		String result = "http://www.boi.org.il/currency.xml?rdate="+dateInUrl;
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
		List<String> returnCurrencies = new ArrayList<String>();
		Document doc = BankUtil.fisToDocument(fis); // TODO instead of fis get doc?
		NodeList nList = doc.getElementsByTagName("CURRENCYCODE"); // row
		for (int i = 0; i < nList.getLength(); i++) {
			String nodeValue = nList.item(i).getTextContent();
			returnCurrencies.add(nodeValue);
			log.debug("fisToCurrencies] nodeValue: " + nodeValue);
		}
		return returnCurrencies;
	}

}
