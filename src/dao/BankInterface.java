package dao;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.List;

public interface BankInterface {
	public Float fisToRate(FileInputStream fis, String inputCurrency);
	public List<String> fisToCurrencies(FileInputStream fis);
	// GET BANK URL FROM DATE
	public String getDownloadUrlByDate(LocalDate selectedDate); // (String selectedDate); // TODO Date selectedDate
}
