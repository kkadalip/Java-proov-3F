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
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.0/jquery.min.js"></script>
<!-- jQuery UI (Uncompressed: //code.jquery.com/ui/1.11.4/jquery-ui.js) -->
<link rel="stylesheet"
	href="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/themes/smoothness/jquery-ui.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.4/jquery-ui.min.js"></script>

<script>
</script>

</head>
<body>
	<div id="allFloatBoxesContainer">

		<div class="floating-box-container">
			<div class="floating-box unselectable" id="floating-box-top">
				<p unselectable="on"><fmt:message key="label.title" /></p>
			</div>
		</div>


		<div class="floating-box-container">
			<div class="floating-box" id="floating-box-main">

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
			 	<table style="width: 100%">
					<tr>
						<td unselectable="on" class="tdInfo unselectable"><fmt:message key="label.language" />:</td>
						<td>
						    <form action="" method="GET" id="languageForm">
						        <select class="hoverShadow dataSelect" id="languageSelect" name="language" onchange="submit()">
						            <option value="en" ${language == 'en' ? 'selected' : ''}>English</option>
						            <option value="et" ${language == 'et' ? 'selected' : ''}>Eesti keel</option>
						        </select>
						    </form>
					    </td>
					</tr>
			 	</table>
				<form action="" method="POST" id="someForm">
					<table style="width: 100%">
						<tbody>
								<tr>
									<td unselectable="on" class="tdInfo unselectable"><fmt:message key="label.date" />:</td>
									<td><input required="required" class="hoverShadow dataEntry" type="text"
										id="datepicker" name="selectedDate"></td>
								</tr>
								<tr>
									<td unselectable="on" class="tdInfo unselectable"><fmt:message key="label.inputAmount" />:</td>
									<td><input required="required" class="hoverShadow dataEntry" type="number"
										min="0" step="any" name="inputMoneyAmount" placeholder="" /></td>
								</tr>
								<tr>
									<td unselectable="on" class="tdInfo unselectable"><fmt:message key="label.inputCurrency" />:</td>
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
									<td></td>
									<td colspan="2"><button id="swapCurrenciesBtn"
											type="button" style="font-size:12px; margin:0; padding:0;" onClick="swapCurrencies();">▲<br>▼</button></td>
								</tr>
								<tr>
									<!-- <td> <input class="hoverShadow dataEntry" type="text" placeholder=""
									name="outputResult" id="outResult" /> </td> -->
									<td unselectable="on" class="tdInfo unselectable"><fmt:message key="label.outputCurrency" />:</td>
									<!-- <td style="width:3px"></td>  -->
									<td><select id="selectOutputCurrency"
										class="hoverShadow dataSelect" name="outputCurrency">
											<!--<option value="empty"></option>-->
											<c:forEach items="${requestScope['displayedCurrencies']}"
												var="item">
												<option id="output_${item}"
													value="${item}">${item} -
													<fmt:message key="currency.${item}" /></option>
											</c:forEach>
									</select></td>
								</tr>
							
						</tbody>
					</table>
						<!-- <input type="submit" value="Done">  -->
						<br>
						<!-- <button class="gradientButton" type="submit" value="">Done</button>  -->
						<input class="gradientButton" type="submit" value="<fmt:message key="label.done" />"> <br>
					</form>

				<div id="errorsTableContainer">
				
				</div>

				<div id="resultsTableContainer"></div>

			</div>
		</div>
	</div>
</body>
</html>