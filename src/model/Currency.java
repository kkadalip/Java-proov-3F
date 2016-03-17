package model;

public class Currency {

	String _shortName;
	Float _rate;

	public Currency(String shortName, Float rate) {
		_shortName = shortName;
		_rate = rate;
	}

	// GETTERS AND SETTERS:
	public String get_shortName() {return _shortName;}
	public void set_shortName(String _shortName) {this._shortName = _shortName;}

	public Float get_rate() {return _rate;}
	public void set_rate(Float _rate) {this._rate = _rate;}

	@Override
	public String toString() {
		return "[Currency] shortname: " + _shortName + " rate: " + _rate;
	}
}
