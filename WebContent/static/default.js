function swapCurrencies(){
	var a = "#selectInputCurrency";
	var b = "#selectOutputCurrency";
	var fromVal = $(a +" option:selected").val();
	var toVal = $(b +" option:selected").val();
	$(a).val(toVal);
	$(b).val(fromVal);
}

//STRING AS PLAIN TEXT
/*
$(document).on("click", "#ajaxbutton", function() {
	console.log("clicked button!");
	$.get("Something", function(responseText) { // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response text...
		$("#somediv").text(responseText); // Locate HTML DOM element with ID "somediv" and set its text content with the response text.
	});
});
 */
//Returning List<String> as JSON
/*
$(document).on("click", "#ajaxbutton2", function() { // When HTML DOM "click" event is invoked on element with ID "somebutton", execute the following function...
	$.get("Something", function(responseJson) { // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response JSON...
		var $ul = $("<ul>").appendTo($("#somediv2")); // Create HTML <ul> element and append it to HTML DOM element with ID "somediv".
		$.each(responseJson, function(index, item) { // Iterate over the JSON array.
			$("<li>").text(item).appendTo($ul); // Create HTML <li> element, set its text content with currently iterated item and append it to the <ul>.
		});
	});
});
 */
//Returning Map<String, String> as JSON
/*
$(document).on("click", "#ajaxbutton3", function() { // When HTML DOM "click" event is invoked on element with ID "somebutton", execute the following function...
	$.get("Something", function(responseJson) { // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response JSON...
		var $select = $("#someselect3"); // Locate HTML DOM element with ID "someselect".
		$select.find("option").remove(); // Find all child elements with tag name "option" and remove them (just to prevent duplicate options when button is pressed again).
		$.each(responseJson, function(key, value) { // Iterate over the JSON object.
			$("<option>").val(key).text(value).appendTo($select); // Create HTML <option> element, set its value with currently iterated key and its text content with currently iterated item and finally append it to the <select>.
		});
	});
});
 */
//Returning List<Entity> as JSON
/*
$(document).on("click", "#somebutton", function() { // When HTML DOM "click" event is invoked on element with ID "somebutton", execute the following function...
	$.get("someservlet", function(responseJson) { // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response JSON...
		var $table = $("<table>").appendTo($("#somediv")); // Create HTML <table> element and append it to HTML DOM element with ID "somediv".
		$.each(responseJson, function(index, product) { // Iterate over the JSON array.
			$("<tr>").appendTo($table) // Create HTML <tr> element, set its text content with currently iterated item and append it to the <table>.
			.append($("<td>").text(product.id)) // Create HTML <td> element, set its text content with id of currently iterated product and append it to the <tr>.
			.append($("<td>").text(product.name)) // Create HTML <td> element, set its text content with name of currently iterated product and append it to the <tr>.
			.append($("<td>").text(product.price)); // Create HTML <td> element, set its text content with price of currently iterated product and append it to the <tr>.
		});
	});
});
 */
//Returning List<Entity> as XML
/*
	$(document).on("click", "#submitButton", function() {             // When HTML DOM "click" event is invoked on element with ID "somebutton", execute the following function...
console.log("Im in here!");
   $.get("Something", function(responseXml) {                // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response XML...
       $("#somediv").html($(responseXml).find("data").html()); // Parse XML, find <data> element and append its HTML to HTML DOM element with ID "somediv".
   });
});
 */

function selectOption(sectorID) {
	var optionElement = document.getElementById("option_" + sectorID);
	//var optionElement = $("#option_"+sectorID); // JQUERY WAY
	if (optionElement) {
		console.log("WOO I have option element option_" + sectorID);
		optionElement.selected = true;
		//optionElement.attr('selected','selected'); // JQUERY WAY
	} else {
		console.log("BOO I DO NOT have option_" + sectorID);
	}
}





























//LANGUAGE AJAX:
/*
 $("#languageSelect").on('change', function(){
	 console.log("languageSelect on change");
	 $("#languageForm").submit();
 });
 */
/*
 $(document).on("change","#languageSelect",function(event) {
	 console.log("languageSelect on change");
	 $("#languageForm").submit();
 });
 $(document).on("submit","#languageForm",function(event) {
	 console.log("language form ajax submit");
	 var $form = $(this);
		$.post($form.attr("action"), $form.serialize(), function(responseJson) { // responseText responseJson responseXml
			console.log("responseJson is: " + responseJson);
		});
	 event.preventDefault();
 });
 */

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