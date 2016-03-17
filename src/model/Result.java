package model;

public class Result {

	String _bankName;
	String _resultValue;
	
	public Result(String bankName, String resultValue) { // Float?
		_bankName = bankName;
		_resultValue = resultValue;
	}

	// GETTERS AND SETTERS
	public String get_bankName() {return _bankName;}
	public void set_bankName(String _bankName) {this._bankName = _bankName;}

	public String get_resultValue() {return _resultValue;}
	public void set_resultValue(String _resultValue) {this._resultValue = _resultValue;}
	
	@Override
	public String toString() {
		return "[Result] bankName: " + _bankName + " resultValue: " + _resultValue;
	}
}
