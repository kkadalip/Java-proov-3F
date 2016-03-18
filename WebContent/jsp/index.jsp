<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

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

<meta http-equiv="Content-Security-Policy" content="
	default-src 'self';
    script-src 'unsafe-inline' 'self' https://ajax.googleapis.com;
    connect-src 'self';
    font-src 'self';
    img-src 'self' https://ajax.googleapis.com;
    style-src 'self' https://ajax.googleapis.com;
    media-src 'self';">
    
<title>Finest</title>

<link rel="shortcut icon" href="static/lazydraw.ico" />

<!-- jQuery (Uncompressed: //code.jquery.com/jquery-1.12.1.js)-->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<!-- jQuery UI (Uncompressed: //code.jquery.com/ui/1.11.4/jquery-ui.js) -->
<link rel="stylesheet" href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css">
<script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>

<!-- MY OWN CSS AND JS -->
<link rel="stylesheet" type="text/css" href="static/style.css">
<script type="text/javascript" src="static/default.js"></script>

<!-- UNSAFE INLINE, using because of JSTL fmt:message -->
<script>
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
</script>

</head>
<body>
	<div class="floating-box-container">
		<div id="floating-box-main"  class="floating-box">
			<p class="unselectable" id="td-title"><fmt:message key="label.title" /></p>
			<form action="" method=GET id="datePickerForm">
				<table id="languageAndDateTable">
					<tr>
						<td class="tdInfo unselectable">
							<fmt:message key="label.language" />:
						</td>
						<td>
							<select class="hoverShadow dataSelect" id="languageSelect" name="language">
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
				<table id="mainTable">
					<tbody>
							<tr>
								<td class="tdInfo unselectable"><fmt:message key="label.inputAmount" />:</td>
								<td><input required="required" class="hoverShadow dataEntry" type="number"
									min="0" step="any" name="inputMoneyAmount" id="inputMoneyAmount" placeholder="" /></td>
							</tr>
							<tr>
								<td class="tdInfo unselectable"><fmt:message key="label.inputCurrency" />:</td>
								<td><select id="selectInputCurrency"
									class="hoverShadow dataSelect" name="inputCurrency">
										<!-- 
										<c:forEach items="${requestScope['displayedCurrencies']}"
											var="item">
											<option id="input_${item}"
												value="${item}">${item} -
												<fmt:message key="currency.${item}" /></option>
										</c:forEach>
										 -->
										<c:forEach items="${requestScope['displayedCurrencies']}"
											var="item">
											<option id="input_${item._shortName}"
												value="${item._shortName}">${item._shortName} - ${item._fullName}</option>)
										</c:forEach>
								</select></td>
							</tr>
							<tr>
								<td colspan="2">
									<button class="unselectable" id="swapCurrenciesBtn" type="button">▲ ▼</button>
								</td>
							</tr>
							<tr>
								<!-- <td> <input class="hoverShadow dataEntry" type="text" placeholder=""
								name="outputResult" id="outResult" /> </td> -->
								<td class="tdInfo unselectable"><fmt:message key="label.outputCurrency" />:</td>
								<td><select id="selectOutputCurrency"
									class="hoverShadow dataSelect" name="outputCurrency">
										<!--<option value="empty"></option>-->
										<!-- 
										<c:forEach items="${requestScope['displayedCurrencies']}"
											var="item">
											<option id="output_${item}"
												value="${item}">${item} - <fmt:message key="currency.${item}" /></option>
										</c:forEach>
										-->
										<c:forEach items="${requestScope['displayedCurrencies']}"
											var="item">
											<option id="output_${item._shortName}"
												value="${item._shortName}">${item._shortName} - ${item._fullName}</option>)
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