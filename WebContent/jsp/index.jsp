<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

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

<!-- MY OWN CSS AND JS -->
<link rel="stylesheet" type="text/css" href="static/style.css">
<script src="static/default.js"></script>

<!-- jQuery (Uncompressed: //code.jquery.com/jquery-1.12.1.js)-->
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<!-- jQuery UI (Uncompressed: //code.jquery.com/ui/1.11.4/jquery-ui.js) -->
<link rel="stylesheet"
	href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>

<script>
	// STRING AS PLAIN TEXT
	/*
	$(document).on("click", "#ajaxbutton", function() {
		console.log("clicked button!");
		$.get("Something", function(responseText) { // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response text...
			$("#somediv").text(responseText); // Locate HTML DOM element with ID "somediv" and set its text content with the response text.
		});
	});
	 */
	// Returning List<String> as JSON
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
	// Returning Map<String, String> as JSON
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
	// Returning List<Entity> as JSON
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
	// Returning List<Entity> as XML
	/*
		$(document).on("click", "#submitButton", function() {             // When HTML DOM "click" event is invoked on element with ID "somebutton", execute the following function...
	console.log("Im in here!");
	   $.get("Something", function(responseXml) {                // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response XML...
	       $("#somediv").html($(responseXml).find("data").html()); // Parse XML, find <data> element and append its HTML to HTML DOM element with ID "somediv".
	   });
	});
	 */

	// Ajaxifying an existing form
	$(document).on(
			"submit",
			"#someform",
			function(event) {
				console.log("submitting the ajax post form");
				var $form = $(this);
				/*
				$.get("Something", function(responseXml) {                // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response XML...
				    $("#somediv").html($(responseXml).find("data").html()); // Parse XML, find <data> element and append its HTML to HTML DOM element with ID "somediv".
				});
				 */

				$.post($form.attr("action"), $form.serialize(), function(
						responseJson) { // responseText responseJson responseXml
					// ...
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
					var $table = $("<table>"); //.appendTo($("#somediv"));
					$("<tr>").appendTo($table).append($("<th>").text("Bank:"))
							.append($("<th>").text("Result:"));

					$.each(responseJson, function(index, result) { // Iterate over the JSON array.
						$("<tr>").appendTo($table).append(
								$("<td>").text(result._bankName)).append(
								$("<td>").text(result._resultValue));
						//$("<tr>").($table)                     

						//$("<tr>").appendTo($table) // Create HTML <tr> element, set its text content with currently iterated item and append it to the <table>.
						//	.append($("<td>").text("Bank: " + result._bankName))        // Create HTML <td> element, set its text content with id of currently iterated product and append it to the <tr>.
						//    .append($("<td>").text("Result: " + result._resultValue))
					});
					$('#resultsTableContainer').html($table);
					
					var $ul = $("<ul>");
					$.each(responseJson, function(index, item) { // Iterate over the JSON array.
			            $("<li>").text(item).appendTo($ul);      // Create HTML <li> element, set its text content with currently iterated item and append it to the <ul>.
			        });
					$("#errorsTableContainer").html($ul);

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
			maxDate : new Date(2010, 11, 30),
			changeMonth : true,
			changeYear : true
		});
		$("#datepicker").datepicker('setDate', new Date(2010, 11, 30));

		//$("#datepicker").datepicker();
		//$("#datepicker").datepicker("option", "dateFormat", "dd.mm.yy");

		//$( "#datepicker" ).datepicker({ minDate: -20, maxDate: "+1M +10D" });

		//var date = $('#datepicker').datepicker({ dateFormat: 'dd-mm-yy' }).val();
	});

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
</script>

</head>
<body>
	<div id="allFloatBoxesContainer">

		<div class="floating-box-container">
			<div class="floating-box" id="floating-box-top">
				<p>Currency converter</p>
			</div>
		</div>


		<div class="floating-box-container">
			<div class="floating-box" id="floating-box-main">
				<form action="" method="POST" id="someform">
					<!-- 
			<select>
				<option value="empty"></option>
				<option value="AED">AED - Araabia Ühendemiraatide dirhem</option>
				<option value="AFN">ARS - Argentina peeso</option>
				<option value="ALL">AUD - Austraalia dollar</option>
			</select> 
			 -->

					<!-- 
				<button id="ajaxbutton">press here</button>
				<div id="somediv">eh</div>
				<br>
				<button id="ajaxbutton2">press here 2</button>
				<div id="somediv2">eh2</div>
				<br>
				<button id="ajaxbutton3">press here 3</button>
				<select id="someselect3"></select> <br> <br>
				 -->

					<table style="table-layour: fixed; width: 100%">
						<tbody>
							<tr>
								<td class="tdInfo">Date:</td>
								<td><input class="hoverShadow dataEntry" type="text"
									id="datepicker" name="selectedDate"></td>
							</tr>
							<tr>
								<td class="tdInfo">Amount:</td>
								<td><input class="hoverShadow dataEntry" type="number"
									min="0" step="any" name="inputMoneyAmount" placeholder="" /></td>
							</tr>
							<tr>
								<td class="tdInfo">Input:</td>
								<!-- <td style="width:3px"></td>  -->
								<td><select id="selectInputCurrency"
									class="hoverShadow dataSelect" name="inputCurrency">
										<c:forEach items="${requestScope['displayedCurrencies']}"
											var="item">
											<option id="input_${item._shortName}"
												value="${item._shortName}">${item._shortName}-
												${item._fullName}</option>
										</c:forEach>
								</select></td>
							</tr>
							<tr>
								<td></td>
								<td colspan="2"><button id="swapCurrenciesBtn"
										type="button" onClick="swapCurrencies();">Swap</button></td>
							</tr>
							<tr>
								<!-- <td> <input class="hoverShadow dataEntry" type="text" placeholder=""
								name="outputResult" id="outResult" /> </td> -->
								<td class="tdInfo">Output:</td>
								<!-- <td style="width:3px"></td>  -->
								<td><select id="selectOutputCurrency"
									class="hoverShadow dataSelect" name="outputCurrency">
										<!--<option value="empty"></option>-->
										<c:forEach items="${requestScope['displayedCurrencies']}"
											var="item">
											<option id="input_${item._shortName}"
												value="${item._shortName}">${item._shortName}-
												${item._fullName}</option>
										</c:forEach>
								</select></td>
							</tr>
						</tbody>
					</table>
					<!-- <input type="submit" value="Done">  -->
					<br>
					<!-- <button class="gradientButton" type="submit" value="">Done</button>  -->
					<input class="gradientButton" type="submit" value="Done"> <br>
				</form>

				<div id="errorsTableContainer">errors here</div>

				<div id="resultsTableContainer"></div>

			</div>
		</div>
	</div>
</body>
</html>