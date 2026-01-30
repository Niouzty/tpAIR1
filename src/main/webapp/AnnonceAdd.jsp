<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<h2>Ajouter</h2>

<p style="color:red;"><%= request.getAttribute("error") == null ? "" : request.getAttribute("error") %></p>

<form method="post" action="AnnonceAdd">
Title: <input name="title"><br>
Description:<br>
<textarea name="description"></textarea><br>
Adress: <input name="adress"><br>
Mail: <input name="mail"><br>
<button>Save</button>
</form>

<a href="AnnonceList">Liste</a>
</html>
