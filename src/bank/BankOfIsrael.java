package bank;

import java.io.FileInputStream;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dao.BankInterface;


// http://www.boi.org.il/en/Markets/Pages/explainxml.aspx
// TODAY http://www.boi.org.il/currency.xml
// 01.03.2012 http://www.boi.org.il/currency.xml?rdate=20120301
public class BankOfIsrael implements BankInterface {
	static Logger log = LoggerFactory.getLogger(BankOfIsrael.class);
	
	@Override
	public Float fisToRate(FileInputStream fis, String inputCurrency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> fisToCurrencies(FileInputStream fis) {
		// TODO Auto-generated method stub
		return null;
	}

}
