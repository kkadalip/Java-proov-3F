package dao;

import java.io.FileInputStream;

public interface BankInterface {
	public Float fisToRate(FileInputStream fis, String inputCurrency);
}
