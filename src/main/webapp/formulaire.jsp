<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Formulaire</title>
</head>
<body>

<h2>Formulaire de saisie</h2>

<form method="get" action="formulaire-servlet">
    Nom :
    <input type="text" name="nom" required />
    <br><br>
    <input type="submit" value="Envoyer" />
</form>

<%
    String nom = request.getParameter("nom");
    if (nom != null && !nom.isEmpty()) {
%>
    <h3>Bonjour <%= nom %> !</h3>
<%
    }
%>

</body>
</html>