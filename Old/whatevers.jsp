<?xml version="1.0" encoding="UTF-8"?>
<%@page contentType="application/xml" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<data>
	<p>lalalala whatevers jsp</p>
    <table>
        <c:forEach items="${whatevers}" var="whatever">
            <tr>
                <td>whatever: ${whatever}</td>
            </tr>
        </c:forEach>
    </table>
    <p>lala end</p>
</data>