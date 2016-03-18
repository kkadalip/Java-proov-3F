package dao;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.util.List;

import javax.servlet.ServletContext;

import model.Result;

public interface BankInterface {
	String fileNameDatePattern = "-yyyy-MM-dd";
	public String getDownloadUrlByDate(LocalDate selectedDate);
	public String getFileNameByDate(LocalDate selectedDate);
	public Float fisToRate(FileInputStream fis, String inputCurrency);
	public List<String> fisToCurrencies(FileInputStream fis);
	public List<String> getCurrencies(ServletContext context, LocalDate selectedDate);
	public Result getResult(ServletContext context, LocalDate selectedDate, String inputCurrency, String outputCurrency, Float inputMoneyAmount);
}
