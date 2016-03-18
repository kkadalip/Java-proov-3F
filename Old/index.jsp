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
    script-src 'self' 'unsafe-inline' https://ajax.googleapis.com;
    connect-src 'self';
    font-src 'self';
    img-src 'self' https://ajax.googleapis.com;
    style-src 'self' 'unsafe-inline' https://ajax.googleapis.com;
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

<!-- INLINE JS -->
<script>
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
							<tr>
								<td class="tdInfo unselectable"><fmt:message key="label.inputAmount" />:</td>
								<td><input required="required" class="hoverShadow dataEntry" type="number"
									min="0" step="any" name="inputMoneyAmount" id="inputMoneyAmount" placeholder="" /></td>
							</tr>
							<tr>
								<td class="tdInfo unselectable"><fmt:message key="label.inputCurrency" />:</td>
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