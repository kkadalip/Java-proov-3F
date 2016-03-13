function swapCurrencies(){
	var a = "#selectInputCurrency";
	var b = "#selectOutputCurrency";
    var fromVal = $(a +" option:selected").val();
    var toVal = $(b +" option:selected").val();
  	$(a).val(toVal);
    $(b).val(fromVal);
}

































/*
function switchCurrencies(){
var a = "#selectInputCurrency";
var b = "#selectOutputCurrency";

//Get the values
var fromVal = $(a +" option:selected").val();
console.log("Fromval: " + fromVal);
//var fromText = $(a +" option:selected").text();
var toVal = $(b +" option:selected").val();
console.log("Toval: " + toVal);
//var toText = $(b +" option:selected").text();

	//Set the values
//$(a +" option:selected").val(toVal);
	$(a).val(toVal);
//$(a +" option:selected").text(toText);
//$(b +" option:selected").val(fromVal);
$(b).val(fromVal);
//$(b +" option:selected").text(fromText);

//var a_selected_index = a.options[a.selectedIndex].value;  //e.options[e.selectedIndex].value; e.options[e.selectedIndex].text;
//var b_selected_index = b.options[b.selectedIndex].value;
//a.options[a.selectedIndex].value = b_selected_index;
//b.options[b.selectedIndex].value = a_selected_index;
}
*/