<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
        <%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
    
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>





 <select>
        <c:forEach items="${PayeeList}" var="payee">
   
        <option value="${payee.getName()}">${payee.getName()}</option>
     </c:forEach>
        
      </select>

</body>
</html>