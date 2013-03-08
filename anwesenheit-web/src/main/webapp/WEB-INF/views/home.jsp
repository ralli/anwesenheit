<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>Home</title>
</head>
<body>
  <div class="hero-unit">
    <h1>Anwesenheitsverwaltung</h1>
    <p>Spring Web MVC Demonstration</p>
    <p>
      <a class="btn btn-large" href="<c:url value="/vet"/>">Vets</a>
      <a class="btn btn-large" href="<c:url value="/owner"/>">Owners</a>
    </p>
  </div>  
</body>
</html>
