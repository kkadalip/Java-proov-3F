package model;

//import java.util.Date;

public class Currency {

	String _shortName;
	//String _fullName;
	Float _rate;
	//Date _date;

	public Currency(String shortName, Float rate) { // String fullName,  //, Date date) {
		_shortName = shortName;
		//_fullName = fullName;
		_rate = rate;
		//_date = date;
	}

	// GETTERS AND SETTERS:
	public String get_shortName() {return _shortName;}
	public void set_shortName(String _shortName) {this._shortName = _shortName;}

//	public String get_fullName() {return _fullName;}
//	public void set_fullName(String _fullName) {this._fullName = _fullName;}

	public Float get_rate() {return _rate;}
	public void set_rate(Float _rate) {this._rate = _rate;}

//	public Date get_date() {return _date;}
//	public void set_date(Date _date) {this._date = _date;}
	
	@Override
	public String toString() {
		//return super.toString();
		return "[Currency] shortname: " + _shortName + " rate: " + _rate; // " fullName: " + _fullName    " date: " + _date;
	}
}
