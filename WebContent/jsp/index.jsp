<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

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
</head>
<body>
	AJAX valuutakalkulaator
	<br>

	<form action="" method="POST">
		Lähtesumma: <input required type="text" name="inputSum" /> <br>
		Lähtevaluuta: <select>
			<option value="empty"></option>
			<option value="AED">AED - Araabia Ühendemiraatide dirhem</option>
			<option value="AFN">ARS - Argentina peeso</option>
			<option value="ALL">AUD - Austraalia dollar</option>
		</select> (select box, sisuks valuuta lühend + valuuta täisnimi, täisnime järgi
		tähestikulises järjekorras) <br> Sihtvaluuta: sama mis eelmine <select>
			<option value="empty"></option>
			<option value="AED">AED - Araabia Ühendemiraatide dirhem</option>
			<option value="AFN">ARS - Argentina peeso</option>
			<option value="ALL">AUD - Austraalia dollar</option>
		</select> <br> Kursi kuupäev: input väli formaadis dd.mm.yyyy koos mõne
		javascript date pickeriga!!!! <br> <input type="submit" value="Done">
	</form>
</body>
</html>