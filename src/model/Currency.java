package model;

public class Currency implements Comparable<Currency> {

	String _shortName;
	String _fullName;
//	Float _rate;
	
	public Currency(String shortName, String translation){
		_shortName = shortName;
		_fullName = translation;
	}

//	public Currency(String shortName, Float rate) {
//		_shortName = shortName;
//		_rate = rate;
//	}

	// GETTERS AND SETTERS:
	public String get_shortName() {return _shortName;}
	public void set_shortName(String _shortName) {this._shortName = _shortName;}

	public String get_fullName() {return _fullName;}
	public void set_fullName(String _fullName) {this._fullName = _fullName;}
	
//	public Float get_rate() {return _rate;}
//	public void set_rate(Float _rate) {this._rate = _rate;}


	@Override
	public String toString() {
		return "[Currency] shortName: " + _shortName + " fullName: " + _fullName; //" rate: " + _rate;
	}
	
	@Override
	public int compareTo(Currency c) {
		//System.out.println("[Currency][compareTo] this name: " + this._fullName + " comparing to " + c.get_fullName());
		return this._fullName.compareTo( c.get_fullName() );
	}
	
}
