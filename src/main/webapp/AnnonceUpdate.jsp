<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="fr.uit.univparis8.tpair.tpair1.model.Annonce"%>
<%
Annonce a = (Annonce) request.getAttribute("annonce");
%>
<h2>Update</h2>

<form method="post" action="AnnonceUpdate">
<input type="hidden" name="id" value="<%=a.getId()%>">

Title: <input name="title" value="<%=a.getTitle()%>"><br>
Description:<br>
<textarea name="description"><%=a.getDescription()%></textarea><br>
Adress: <input name="adress" value="<%=a.getAdress()%>"><br>
Mail: <input name="mail" value="<%=a.getMail()%>"><br>

<button>Update</button>
</form>