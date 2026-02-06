<%@ page import="java.util.List"%>
<%@ page import="fr.uit.univparis8.tpair.tpair1.model.Annonce"%>

<!DOCTYPE html>
<html>
<head>
  <title>Liste</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>

<body class="bg-light">
<div class="container mt-5">

  <div class="d-flex justify-content-between align-items-center mb-3">
    <h2 class="m-0">Annonces</h2>
    <div>
      <a href="Logout" class="btn btn-outline-dark btn-sm">Déconnexion</a>
      <a href="AnnonceAdd" class="btn btn-primary btn-sm">Ajouter</a>
    </div>
  </div>

  <%
    String q = (String) request.getAttribute("q");
    String status = (String) request.getAttribute("status");
    Integer pageNum = (Integer) request.getAttribute("page");
    Integer size = (Integer) request.getAttribute("size");

    if (q == null) q = "";
    if (status == null) status = "ALL";
    if (size == null) size = 10;
  %>

  <form class="row g-2 mb-3" method="get" action="AnnonceList">
    <div class="col-md-6">
      <input class="form-control" name="q" placeholder="Rechercher..." value="<%= q %>">
    </div>
    <div class="col-md-3">
      <select class="form-select" name="status">
        <option value="ALL" <%= "ALL".equals(status) ? "selected" : "" %>>Tous</option>
        <option value="DRAFT" <%= "DRAFT".equals(status) ? "selected" : "" %>>DRAFT</option>
        <option value="PUBLISHED" <%= "PUBLISHED".equals(status) ? "selected" : "" %>>PUBLISHED</option>
        <option value="ARCHIVED" <%= "ARCHIVED".equals(status) ? "selected" : "" %>>ARCHIVED</option>
      </select>
    </div>
    <div class="col-md-2">
      <select class="form-select" name="size">
        <option value="5" <%= (size==5) ? "selected" : "" %>>5</option>
        <option value="10" <%= (size==10) ? "selected" : "" %>>10</option>
        <option value="20" <%= (size==20) ? "selected" : "" %>>20</option>
      </select>
    </div>
    <div class="col-md-1 d-grid">
      <button class="btn btn-secondary">OK</button>
    </div>
    <input type="hidden" name="page" value="0"/>
  </form>

  <table class="table table-bordered table-striped shadow">
    <thead>
    <tr>
      <th>Titre</th>
      <th>Email</th>
      <th>Status</th>
      <th style="width: 280px;">Actions</th>
    </tr>
    </thead>
    <tbody>
    <%
      List<Annonce> annonces = (List<Annonce>) request.getAttribute("annonces");
      if (annonces != null) {
        for(Annonce a : annonces){
    %>
      <tr>
        <td><%= a.getTitle() %></td>
        <td><%= a.getMail() %></td>
        <td><%= a.getStatus() %></td>
        <td>
          <a href="AnnonceUpdate?id=<%= a.getId() %>" class="btn btn-warning btn-sm">Edit</a>
          <a href="AnnonceDelete?id=<%= a.getId() %>" class="btn btn-danger btn-sm"
             onclick="return confirm('Supprimer ?');">Delete</a>
          <a href="AnnoncePublish?id=<%= a.getId() %>" class="btn btn-success btn-sm">Publish</a>
          <a href="AnnonceArchive?id=<%= a.getId() %>" class="btn btn-secondary btn-sm">Archive</a>
        </td>
      </tr>
    <% } } %>
    </tbody>
  </table>

  <div class="d-flex justify-content-between">
    <a class="btn btn-outline-primary btn-sm <%= (pageNum <= 0) ? "disabled" : "" %>"
       href="AnnonceList?q=<%= java.net.URLEncoder.encode(q, "UTF-8") %>&status=<%= status %>&size=<%= size %>&page=<%= Math.max(0, pageNum-1) %>">
      ← Précédent
    </a>

    <a class="btn btn-outline-primary btn-sm"
       href="AnnonceList?q=<%= java.net.URLEncoder.encode(q, "UTF-8") %>&status=<%= status %>&size=<%= size %>&page=<%= pageNum+1 %>">
      Suivant →
    </a>
  </div>

</div>
</body>
</html>