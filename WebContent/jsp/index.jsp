<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:set var="language" value="${not empty param.language ? param.language : not empty language ? language : pageContext.request.locale}" scope="session" />
<fmt:setLocale value="${language}" />
<fmt:setBundle basename="text" />

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html">

<title>Finest</title>

<meta http-equiv="Content-Security-Policy"
	content="
    default-src 'self';
    script-src 'self' 'unsafe-inline' https://ajax.googleapis.com;
    connect-src 'self';
    font-src 'self';
    img-src 'self' https://ajax.googleapis.com;
    style-src 'self' 'unsafe-inline' https://ajax.googleapis.com;
    media-src 'self';">

<link rel="shortcut icon" href="static/lazydraw.ico" />

<!-- MY OWN CSS AND JS -->
<link rel="stylesheet" type="text/css" href="static/style.css">
<script src="static/default.js"></script>

<!-- jQuery (Uncompressed: //code.jquery.com/jquery-1.12.1.js)-->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<!-- jQuery UI (Uncompressed: //code.jquery.com/ui/1.11.4/jquery-ui.js) -->
<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>

<script>
$(document).on("click","#swapCurrenciesBtn",function(event){
	if($("#inputMoneyAmount").val() && $("#selectOutputCurrency").val() != $("#selectInputCurrency").val()){
		$("#someForm").submit();
	}
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
//Ajaxifying an existing form
$(document).on("submit","#someForm",function(event) {
	console.log("submitting the ajax post form");
	var $form = $(this);
	/*
	$.get("Something", function(responseXml) {                // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response XML...
	    $("#somediv").html($(responseXml).find("data").html()); // Parse XML, find <data> element and append its HTML to HTML DOM element with ID "somediv".
	});
	 */

	 //  + "&par1=1&par2=2&par3=232"
	$.post($form.attr("action"), $form.serialize() +"&selectedD="+$("#datepicker").datepicker().val()+"&lang="+$("#languageSelect").val(), function(responseJson) { // responseText responseJson responseXml
		console.log("responseJson is: " + responseJson);

		//$("#somediv").html($(responseXml).find("data").html());

		/*
		console.log("responseText is: " + responseText);
		$("#outResult").val(responseText); // text
		 */

		// POPULATE SELECT WITH RESULTS
		//var $select = $("#someselect"); // Locate HTML DOM element with ID "someselect".
		//$select.find("option").remove(); // Find all child elements with tag name "option" and remove them (just to prevent duplicate options when button is pressed again).
		//$.each(responseJson, function(key, value) { // Iterate over the JSON object.
		//	$("<option>").val(key).text(value).appendTo($select); // Create HTML <option> element, set its value with currently iterated key and its text content with currently iterated item and finally append it to the <select>.
		//});
		//var $table = $("<table>").appendTo($("#somediv")); // Create HTML <table> element and append it to HTML DOM element with ID "somediv".

		// EMPTY BOTH RESULTS AND ERRORS HOLDERS:
		$("#errorsTableContainer").html("");
		$('#resultsTableContainer').html("");

		// I GOT ERRORS:
		if(typeof responseJson[0] === 'string'){
			//console.log("responseJson[0]: " + responseJson[0] +" is string, therefore probably error messages");
			var $ul = $("<ul>");
			$("<li id='errorsList'>").appendTo($ul).text("<fmt:message key='label.error' />:"); // Errors:
			$.each(responseJson, function(index, item) { // Iterate over the JSON array.
				console.log("2index " + index + " item " + item);
				$("<li>").text(item).appendTo($ul); // error.inputMoneyEmpty // "<fmt:message key='currency.AED' />"
				//$("<li>").text(item).appendTo($ul); // Create HTML <li> element, set its text content with currently iterated item and append it to the <ul>.
			});
			$("#errorsTableContainer").html($ul);
		}else{
			// I GOT RESULTS:
			var $table = $("<table>"); //.appendTo($("#somediv"));
			$("<tr>").appendTo($table).append($("<th>").text("Bank:")).append($("<th>").text("Result:"));

			$.each(responseJson, function(index, result) { // Iterate over the JSON array.
				//console.log("1index " + index + " item " + result);
				$("<tr>").appendTo($table).append(
						$("<td>").text(result._bankName)).append(
								$("<td>").text(result._resultValue));
				//$("<tr>").($table)                     

				//$("<tr>").appendTo($table) // Create HTML <tr> element, set its text content with currently iterated item and append it to the <table>.
				//	.append($("<td>").text("Bank: " + result._bankName))        // Create HTML <td> element, set its text content with id of currently iterated product and append it to the <tr>.
				//    .append($("<td>").text("Result: " + result._resultValue))
			});
			$('#resultsTableContainer').html($table);
		}



		//$.get("Something", function(responseXml) {                // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response XML...
		//    $("#somediv").html($(responseXml).find("data").html()); // Parse XML, find <data> element and append its HTML to HTML DOM element with ID "somediv".
		//});
		//$("#somediv").html($(responseXml).find("data").html());

	});
	event.preventDefault(); // Important! Prevents submitting the form.
});

$(function() {
	$("#datepicker").datepicker({
		dateFormat : "dd.mm.yy",
		//minDate: 
		//maxDate : new Date(2010, 11, 30),
		maxDate : -1, // YESTERDAY
		changeMonth : true,
		changeYear : true,
		
		beforeShow: function(el){
	        // set the current value before showing the widget
	        $(this).data('previous', $(el).val() );
	    },
		onSelect: function(newDateText) {
			// compare the new value to the previous one
	        if( $(this).data('previous') != newDateText ){
	            // do whatever has to be done, e.g. log it to console
	            console.log( 'datePicker changed to: ' + newDateText );
	            $('#datePickerForm').submit();
	        }
			//console.log("Selected date: " + newDateText + "; input's current value: " + this.value); // both same eg 05.03.2016
			// TODO upon change GET ALL DISPLAYED CURRENCIES!, GET?
		}
	});
	var dateYesterday = new Date();
	dateYesterday.setDate(dateYesterday.getDate() - 1); // YESTERDAY
	//$("#datepicker").datepicker('setDate', dateYesterday); // DEFAULT DATE SET TO YESTERDAY //new Date(2010, 11, 30));

	//$("#datepicker").datepicker();
	//$("#datepicker").datepicker("option", "dateFormat", "dd.mm.yy");

	//$( "#datepicker" ).datepicker({ minDate: -20, maxDate: "+1M +10D" });

	//var date = $('#datepicker').datepicker({ dateFormat: 'dd-mm-yy' }).val();
	
});

</script>

</head>
<body>
	<div class="floating-box-container">
		<div id="floating-box-main"  class="floating-box">
			<p class="unselectable" id="td-title"><fmt:message key="label.title" /></p>
			<form action="" method=GET id="datePickerForm">
				<table style="width:100%">
					<tr>
						<td class="tdInfo unselectable">
							<fmt:message key="label.language" />:
						</td>
						<td>
							<select class="hoverShadow dataSelect" id="languageSelect" name="language" onchange="submit()">
				            	<option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
				            	<option value="et" ${language == 'et' ? 'selected' : ''}>Eesti keel</option>
				        	</select>
						</td>
					</tr>
					<tr>
						<td class="tdInfo unselectable"><fmt:message key="label.date" />:</td>
						<td><input required="required" class="hoverShadow dataEntry" type="text" id="datepicker" name="date" value="${selectedD}"></td>
					</tr>
				</table>
			</form>
			<form action="" method="POST" id="someForm">
				<table style="width:100%">
					<tbody>
							<!--
							<tr>
								<td class="tdInfo unselectable"><fmt:message key="label.date" />:</td>
								<td><input required="required" class="hoverShadow dataEntry" type="text"
									id="datepicker" name="date"></td>
							</tr>
							-->
							<tr>
								<td class="tdInfo unselectable"><fmt:message key="label.inputAmount" />:</td>
								<td><input required="required" class="hoverShadow dataEntry" type="number"
									min="0" step="any" name="inputMoneyAmount" id="inputMoneyAmount" placeholder="" /></td>
							</tr>
							<tr>
								<td class="tdInfo unselectable"><fmt:message key="label.inputCurrency" />:</td>
								<!-- <td style="width:3px"></td>  -->
								<td><select id="selectInputCurrency"
									class="hoverShadow dataSelect" name="inputCurrency">
										<c:forEach items="${requestScope['displayedCurrencies']}"
											var="item">
											<option id="input_${item}"
												value="${item}">${item} -
												<fmt:message key="currency.${item}" /></option>
										</c:forEach>
								</select></td>
							</tr>
							<tr>
								<td colspan="2">
									<button class="unselectable" id="swapCurrenciesBtn" type="button" style="font-size:12px; margin:0; padding:0;" onClick="swapCurrencies();">▲ ▼</button>
								</td>
							</tr>
							<tr>
								<!-- <td> <input class="hoverShadow dataEntry" type="text" placeholder=""
								name="outputResult" id="outResult" /> </td> -->
								<td class="tdInfo unselectable"><fmt:message key="label.outputCurrency" />:</td>
								<!-- <td style="width:3px"></td>  -->
								<td><select id="selectOutputCurrency"
									class="hoverShadow dataSelect" name="outputCurrency">
										<!--<option value="empty"></option>-->
										<c:forEach items="${requestScope['displayedCurrencies']}"
											var="item">
											<option id="output_${item}"
												value="${item}">${item} - <fmt:message key="currency.${item}" /></option>
										</c:forEach>
								</select></td>
							</tr>
							<tr>
								<td colspan="2">
									<input class="gradientButton" type="submit" value="<fmt:message key="label.done" />">
								</td>
							</tr>
					</tbody>
				</table>
			</form>
		<div id="errorsTableContainer"></div>
			<div id="resultsTableContainer"></div>
		</div>
	</div>
</body>
</html>