<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html">

<title>Finest</title>

<meta http-equiv="Content-Security-Policy"
	content="
    default-src 'self';
    script-src 'self' 'unsafe-inline' https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js;
    connect-src 'self';
    font-src 'self';
    img-src 'self';
    style-src 'self' 'unsafe-inline';
    media-src 'self';">

<link rel="stylesheet" type="text/css" href="static/style.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script src="static/default.js"></script>
<script>
	// string as plain text
	$(document).on("click", "#ajaxbutton", function() {
		console.log("clicked button!");
		$.get("Something", function(responseText) { // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response text...
			$("#somediv").text(responseText); // Locate HTML DOM element with ID "somediv" and set its text content with the response text.
		});
	});
	// Returning List<String> as JSON
	$(document).on("click", "#ajaxbutton2", function() { // When HTML DOM "click" event is invoked on element with ID "somebutton", execute the following function...
		$.get("Something", function(responseJson) { // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response JSON...
			var $ul = $("<ul>").appendTo($("#somediv2")); // Create HTML <ul> element and append it to HTML DOM element with ID "somediv".
			$.each(responseJson, function(index, item) { // Iterate over the JSON array.
				$("<li>").text(item).appendTo($ul); // Create HTML <li> element, set its text content with currently iterated item and append it to the <ul>.
			});
		});
	});
	// Returning Map<String, String> as JSON
	$(document).on("click", "#ajaxbutton3", function() { // When HTML DOM "click" event is invoked on element with ID "somebutton", execute the following function...
		$.get("Something", function(responseJson) { // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response JSON...
			var $select = $("#someselect3"); // Locate HTML DOM element with ID "someselect".
			$select.find("option").remove(); // Find all child elements with tag name "option" and remove them (just to prevent duplicate options when button is pressed again).
			$.each(responseJson, function(key, value) { // Iterate over the JSON object.
				$("<option>").val(key).text(value).appendTo($select); // Create HTML <option> element, set its value with currently iterated key and its text content with currently iterated item and finally append it to the <select>.
			});
		});
	});
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
	$(document).on("click", "#somebutton", function() {             // When HTML DOM "click" event is invoked on element with ID "somebutton", execute the following function...
	    $.get("someservlet", function(responseXml) {                // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response XML...
	        $("#somediv").html($(responseXml).find("data").html()); // Parse XML, find <data> element and append its HTML to HTML DOM element with ID "somediv".
	    });
	});
	 */

	// Ajaxifying an existing form
	$(document).on("submit", "#someform", function(event) {
		console.log("submitting the ajax post form");
		var $form = $(this);

		$.post($form.attr("action"), $form.serialize(), function(responseText) { // responseJson responseXml
			// ...
			//$("#somediv").html($(responseXml).find("data").html());
			console.log("responseText is: " + responseText);
			$("#outResult").val(responseText); // text
		});

		event.preventDefault(); // Important! Prevents submitting the form.
	});
</script>

</head>
<body>
	<div id="floating-box">

		AJAX valuutakalkulaator <br>

		<form action="" method="POST" id="someform">
			Lähtesumma: <input type="text" name="inputMoneyAmount" /> <br>
			Lähtevaluuta: <select name="inputCurrency">
				<c:forEach items="${requestScope['displayedCurrencies']}" var="item">
					<option id="input_${item._shortName}" value="${item._rate}">${item._shortName}
						- ${item._fullName} - ${item._rate} - ${item._date}</option>
				</c:forEach>
			</select> (select box, sisuks valuuta lühend + valuuta täisnimi, täisnime
			järgi tähestikulises järjekorras) <br> Sihtvaluuta: sama mis
			eelmine <select name="outputCurrency">
				<c:forEach items="${requestScope['displayedCurrencies']}" var="item">
					<option id="output_${item._shortName}" value="${item._rate}">${item._shortName}
						- ${item._fullName} - ${item._rate} - ${item._date}</option>
				</c:forEach>
			</select>
			<!-- 
		<select>
			<option value="empty"></option>
			<option value="AED">AED - Araabia Ühendemiraatide dirhem</option>
			<option value="AFN">ARS - Argentina peeso</option>
			<option value="ALL">AUD - Austraalia dollar</option>
		</select> 
		 -->
			<br> Kursi kuupäev: input väli formaadis dd.mm.yyyy koos mõne
			javascript date pickeriga!!!! <br> <input type="submit"
				value="Done">
		</form>
		Tulemus: <input required type="text" name="outputResult"
			id="outResult" /> <br>
		<button id="ajaxbutton">press here</button>
		<div id="somediv">eh</div>
		<br>
		<button id="ajaxbutton2">press here 2</button>
		<div id="somediv2">eh2</div>
		<br>
		<button id="ajaxbutton3">press here 3</button>
		<select id="someselect3"></select>


		<br>
		<br>
		<table style="table-layour:fixed; width:100%">
			<tbody>
				<tr>
					<td>
						<input class="dataEntry" type="text" placeholder="" name="inputMoneyAmount2" />
					</td>
					<!-- <td style="width:3px"></td>  -->
					<td>
						<select class="dataSelect">
							<!--<option value="empty"></option>-->
							<option value="AED">AED - Araabia Ühendemiraatide dirhem</option>
							<option value="AFN">ARS - Argentina peeso</option>
							<option value="ALL">AUD - Austraalia dollar</option>
						</select> 
					</td>
				</tr>
				<tr>
					<td>
						<input class="dataEntry" type="text" placeholder="" name="outputResult2" id="outResult2" />
					</td>
					<!-- <td style="width:3px"></td>  -->
					<td>
						<select class="dataSelect">
							<!--<option value="empty"></option>-->
							<option value="AED">AED - Araabia Ühendemiraatide dirhem</option>
							<option value="AFN">ARS - Argentina peeso</option>
							<option value="ALL">AUD - Austraalia dollar</option>
						</select> 
					</td>
				</tr>
			</tbody>
		</table>

	</div>
</body>
</html>