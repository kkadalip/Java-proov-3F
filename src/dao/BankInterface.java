package dao;

import java.io.FileInputStream;
import java.util.List;

public interface BankInterface {
	public Float fisToRate(FileInputStream fis, String inputCurrency);
	public List<String> fisToCurrencies(FileInputStream fis);
}
