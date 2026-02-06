<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.Map" %>
<%@ page import="fr.uit.univparis8.tpair.tpair1.model.Annonce" %>

<%
    Map<String, String> errors = (Map<String, String>) request.getAttribute("errors");
    Annonce form = (Annonce) request.getAttribute("form");
    if (form == null) form = new Annonce();

    String vTitle = form.getTitle() != null ? form.getTitle() : "";
    String vDesc = form.getDescription() != null ? form.getDescription() : "";
    String vAdress = form.getAdress() != null ? form.getAdress() : "";
    String vMail = form.getMail() != null ? form.getMail() : "";

    java.util.function.Function<String,String> err = (k) ->
            (errors != null && errors.get(k) != null) ? errors.get(k) : null;
%>

<!DOCTYPE html>
<html>
<head>
    <title>Modifier</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="card shadow p-4">
        <h3 class="mb-3">Modifier une annonce</h3>

        <form method="post" action="AnnonceUpdate">
            <input type="hidden" name="id" value="<%= form.getId() %>"/>

            <div class="mb-3">
                <label class="form-label">Titre</label>
                <input name="title" class="form-control" value="<%= vTitle %>">
                <% if (err.apply("title") != null) { %>
                    <div class="text-danger small"><%= err.apply("title") %></div>
                <% } %>
            </div>

            <div class="mb-3">
                <label class="form-label">Description</label>
                <textarea name="description" class="form-control" rows="3"><%= vDesc %></textarea>
                <% if (err.apply("description") != null) { %>
                    <div class="text-danger small"><%= err.apply("description") %></div>
                <% } %>
            </div>

            <div class="mb-3">
                <label class="form-label">Adresse</label>
                <input name="adress" class="form-control" value="<%= vAdress %>">
                <% if (err.apply("adress") != null) { %>
                    <div class="text-danger small"><%= err.apply("adress") %></div>
                <% } %>
            </div>

            <div class="mb-3">
                <label class="form-label">Email</label>
                <input name="mail" class="form-control" value="<%= vMail %>">
                <% if (err.apply("mail") != null) { %>
                    <div class="text-danger small"><%= err.apply("mail") %></div>
                <% } %>
            </div>

            <button class="btn btn-primary">Enregistrer</button>
            <a href="AnnonceList" class="btn btn-secondary">Retour</a>
        </form>
    </div>
</div>

</body>
</html>