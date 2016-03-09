<?xml version="1.0" encoding="UTF-8"?>
<%@page contentType="application/xml" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<data>
    <table>
        <c:forEach items="${products}" var="product">
            <tr>
                <td>${product.id}</td>
                <td><c:out value="${product.name}" /></td>
                <td><fmt:formatNumber value="${product.price}" type="currency" currencyCode="USD" /></td>
            </tr>
        </c:forEach>
    </table>
</data>