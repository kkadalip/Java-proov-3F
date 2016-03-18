$(function() {
	$("#datepicker").datepicker({
		dateFormat : "dd.mm.yy",
		maxDate : -1, // YESTERDAY
		changeMonth : true,
		changeYear : true,
		
		beforeShow: function(el){ // set the current value before showing the widget
	        $(this).data('previous', $(el).val() );
	    },
		onSelect: function(newDateText) { // compare the new value to the previous one
	        if( $(this).data('previous') != newDateText ){ // do whatever has to be done, e.g. log it to console: console.log('datePicker changed to: ' + newDateText);
	            $('#datePickerForm').submit();
	        }
		}
	});
	var dateYesterday = new Date();
	dateYesterday.setDate(dateYesterday.getDate() - 1); // YESTERDAY
});

function swapCurrencies(){
	var a = "#selectInputCurrency";
	var b = "#selectOutputCurrency";
	var fromVal = $(a +" option:selected").val();
	var toVal = $(b +" option:selected").val();
	$(a).val(toVal);
	$(b).val(fromVal);
}
$(document).on("click","#swapCurrenciesBtn",function(event){
	swapCurrencies();
	if($("#inputMoneyAmount").val() && $("#selectOutputCurrency").val() != $("#selectInputCurrency").val()){
		$("#someForm").submit();
	}
});

$(document).on("change","#languageSelect",function(event){
	$("#datePickerForm").submit();
});


$(document).on("change","#selectOutputCurrency",function(event){
	if($("#inputMoneyAmount").val()){
		$("#someForm").submit();
	}
});
$(document).on("change","#selectInputCurrency",function(event){
	if($("#inputMoneyAmount").val()){
		$("#someForm").submit();
	}
});

//Ajaxifying an existing form (submitting the ajax post form):
$(document).on("submit","#someForm",function(event) {
	//console.log("submit someform");
	var $form = $(this);
	// Adding selectedD (date) and lang (language) as extra params to serialized form!
	$.post($form.attr("action"), $form.serialize() +"&selectedD="+$("#datepicker").datepicker().val()+"&lang="+$("#languageSelect").val(), function(responseJson) { // responseText responseJson responseXml
		//console.log("responseJson is: " + responseJson);
		// EMPTY BOTH RESULTS AND ERRORS HOLDERS:
		$("#errorsTableContainer").html("");
		$("#resultsTableContainer").html("");
		// I GOT ERRORS:
		if(typeof responseJson[0] === 'string'){ // responseJson[0] is string, therefore responseJson has error messages.
			var $ul = $("<ul>");
			$("<li id='errorsList'>").appendTo($ul).text("<fmt:message key='label.error' />:"); // Errors:
			$.each(responseJson, function(index, item) { // Iterate over the JSON array.
				$("<li>").text(item).appendTo($ul);
			});
			$("#errorsTableContainer").html($ul);
		}else{
		// NO ERRORS:
			var $table = $("<table>");
			$("<tr>").appendTo($table).append($("<th>").text("Bank:")).append($("<th>").text("Result:"));
			$.each(responseJson, function(index, result) { // Iterate over the JSON array.
				$("<tr>").appendTo($table).append(
						$("<td>").text(result._bankName)).append(
								$("<td>").text(result._resultValue));
			});
			$('#resultsTableContainer').html($table);
		}
	});
	event.preventDefault(); // Important! Prevents submitting the form.
});























































//$(function() {
//	$("#datepicker").datepicker({
//		dateFormat : "dd.mm.yy",
//		//minDate: 
//		//maxDate : new Date(2010, 11, 30),
//		maxDate : -1, // YESTERDAY
//		changeMonth : true,
//		changeYear : true,
//		
//		beforeShow: function(el){
//	        // set the current value before showing the widget
//	        $(this).data('previous', $(el).val() );
//	    },
//		onSelect: function(newDateText) {
//			// compare the new value to the previous one
//	        if( $(this).data('previous') != newDateText ){
//	            // do whatever has to be done, e.g. log it to console
//	            console.log( 'datePicker changed to: ' + newDateText );
//	            $('#datePickerForm').submit();
//	        }
//			//console.log("Selected date: " + newDateText + "; input's current value: " + this.value); // both same eg 05.03.2016
//			// upon change GET ALL DISPLAYED CURRENCIES!, GET?
//		}
//	});
//	var dateYesterday = new Date();
//	dateYesterday.setDate(dateYesterday.getDate() - 1); // YESTERDAY
//	//$("#datepicker").datepicker('setDate', dateYesterday); // DEFAULT DATE SET TO YESTERDAY //new Date(2010, 11, 30));
//
//	//$("#datepicker").datepicker();
//	//$("#datepicker").datepicker("option", "dateFormat", "dd.mm.yy");
//
//	//$( "#datepicker" ).datepicker({ minDate: -20, maxDate: "+1M +10D" });
//
//	//var date = $('#datepicker').datepicker({ dateFormat: 'dd-mm-yy' }).val();
//});

////Ajaxifying an existing form:
//$(document).on("submit","#someForm",function(event) {
//	console.log("submitting the ajax post form");
//	var $form = $(this);
//	/*
//	$.get("Something", function(responseXml) {                // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response XML...
//	    $("#somediv").html($(responseXml).find("data").html()); // Parse XML, find <data> element and append its HTML to HTML DOM element with ID "somediv".
//	});
//	 */
//
//	 //  + "&par1=1&par2=2&par3=232"
//	$.post($form.attr("action"), $form.serialize() +"&selectedD="+$("#datepicker").datepicker().val()+"&lang="+$("#languageSelect").val(), function(responseJson) { // responseText responseJson responseXml
//		console.log("responseJson is: " + responseJson);
//
//		//$("#somediv").html($(responseXml).find("data").html());
//
//		/*
//		console.log("responseText is: " + responseText);
//		$("#outResult").val(responseText); // text
//		 */
//
//		// POPULATE SELECT WITH RESULTS
//		//var $select = $("#someselect"); // Locate HTML DOM element with ID "someselect".
//		//$select.find("option").remove(); // Find all child elements with tag name "option" and remove them (just to prevent duplicate options when button is pressed again).
//		//$.each(responseJson, function(key, value) { // Iterate over the JSON object.
//		//	$("<option>").val(key).text(value).appendTo($select); // Create HTML <option> element, set its value with currently iterated key and its text content with currently iterated item and finally append it to the <select>.
//		//});
//		//var $table = $("<table>").appendTo($("#somediv")); // Create HTML <table> element and append it to HTML DOM element with ID "somediv".
//
//		// EMPTY BOTH RESULTS AND ERRORS HOLDERS:
//		$("#errorsTableContainer").html("");
//		$('#resultsTableContainer').html("");
//
//		// I GOT ERRORS:
//		if(typeof responseJson[0] === 'string'){
//			//console.log("responseJson[0]: " + responseJson[0] +" is string, therefore probably error messages");
//			var $ul = $("<ul>");
//			$("<li id='errorsList'>").appendTo($ul).text("<fmt:message key='label.error' />:"); // Errors:
//			$.each(responseJson, function(index, item) { // Iterate over the JSON array.
//				console.log("2index " + index + " item " + item);
//				$("<li>").text(item).appendTo($ul); // error.inputMoneyEmpty // "<fmt:message key='currency.AED' />"
//				//$("<li>").text(item).appendTo($ul); // Create HTML <li> element, set its text content with currently iterated item and append it to the <ul>.
//			});
//			$("#errorsTableContainer").html($ul);
//		}else{
//			// I GOT RESULTS:
//			var $table = $("<table>"); //.appendTo($("#somediv"));
//			$("<tr>").appendTo($table).append($("<th>").text("Bank:")).append($("<th>").text("Result:"));
//
//			$.each(responseJson, function(index, result) { // Iterate over the JSON array.
//				//console.log("1index " + index + " item " + result);
//				$("<tr>").appendTo($table).append(
//						$("<td>").text(result._bankName)).append(
//								$("<td>").text(result._resultValue));
//				//$("<tr>").($table)                     
//
//				//$("<tr>").appendTo($table) // Create HTML <tr> element, set its text content with currently iterated item and append it to the <table>.
//				//	.append($("<td>").text("Bank: " + result._bankName))        // Create HTML <td> element, set its text content with id of currently iterated product and append it to the <tr>.
//				//    .append($("<td>").text("Result: " + result._resultValue))
//			});
//			$('#resultsTableContainer').html($table);
//		}
//		//$.get("Something", function(responseXml) {                // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response XML...
//		//    $("#somediv").html($(responseXml).find("data").html()); // Parse XML, find <data> element and append its HTML to HTML DOM element with ID "somediv".
//		//});
//		//$("#somediv").html($(responseXml).find("data").html());
//
//	});
//	event.preventDefault(); // Important! Prevents submitting the form.
//});

//console.log("Default.js loaded");

//$(document).ready(function() {
//console.log("DOCUMENT READY!!!");	
//});


/*
 * function selectOption(sectorID){
	$("#option_"+sectorID).selected = true;
}
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
*/


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