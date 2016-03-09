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
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<script src="static/default.js"></script>
<script>
	$(document).on("click", "#ajaxbutton", function() {
		console.log("clicked button!");
		$.get("Something", function(responseText) { // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response text...
			$("#somediv").text(responseText); // Locate HTML DOM element with ID "somediv" and set its text content with the response text.
		});
	});
	
	$(document).on("click", "#ajaxbutton2", function() {  // When HTML DOM "click" event is invoked on element with ID "somebutton", execute the following function...
	    $.get("Something", function(responseJson) {    // Execute Ajax GET request on URL of "someservlet" and execute the following function with Ajax response JSON...
	        var $ul = $("<ul>").appendTo($("#somediv2")); // Create HTML <ul> element and append it to HTML DOM element with ID "somediv".
	        $.each(responseJson, function(index, item) { // Iterate over the JSON array.
	            $("<li>").text(item).appendTo($ul);      // Create HTML <li> element, set its text content with currently iterated item and append it to the <ul>.
	        });
	    });
	});
</script>

</head>
<body>
	AJAX valuutakalkulaator
	<br>

	<form action="" method="POST">
		Lähtesumma: <input type="text" name="inputMoneyAmount" /> <br>
		Lähtevaluuta: <select name="inputCurrency">
			<c:forEach items="${requestScope['displayedCurrencies']}" var="item">
				<option id="input_${item._shortName}" value="${item._rate}">${item._shortName}
					- ${item._fullName} - ${item._rate} - ${item._date}</option>
			</c:forEach>
		</select> (select box, sisuks valuuta lühend + valuuta täisnimi, täisnime järgi
		tähestikulises järjekorras) <br> Sihtvaluuta: sama mis eelmine <select
			name="outputCurrency">
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
	Tulemus:
	<input required type="text" name="inputSum" />
	
	<br>

	<button id="ajaxbutton">press here</button>
	<div id="somediv">eh</div>
	
	<button id="ajaxbutton2">press here 2</button>
	<div id="somediv2">eh2</div>
</body>
</html>