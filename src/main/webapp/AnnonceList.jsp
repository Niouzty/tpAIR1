<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List"%>
<%@ page import="fr.uit.univparis8.tpair.tpair1.model.Annonce"%>

<h2>Liste</h2>
<a href="AnnonceAdd">+ Add</a>

<%
List<Annonce> annonces = (List<Annonce>) request.getAttribute("annonces");
for(Annonce a : annonces){
%>
<div>
<b><%=a.getTitle()%></b> - <%=a.getMail()%>
<a href="AnnonceUpdate?id=<%=a.getId()%>">edit</a>
<a href="AnnonceDelete?id=<%=a.getId()%>">delete</a>
</div>
<% } %>