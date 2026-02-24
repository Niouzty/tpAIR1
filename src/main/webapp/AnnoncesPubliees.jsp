<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="fr.uit.univparis8.tpair.tpair1.model.Annonce" %>
<%@ page import="fr.uit.univparis8.tpair.tpair1.service.AnnonceService" %>
<%@ page import="fr.uit.univparis8.tpair.tpair1.model.AnnonceStatus" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Toutes les Annonces</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .annonce-card {
            margin-bottom: 20px;
            border-left: 5px solid #667eea;
            transition: transform 0.2s;
        }
        .annonce-card:hover {
            transform: translateX(5px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        .navbar-custom {
            background-color: #667eea;
        }
        .navbar-custom a {
            color: white !important;
        }
    </style>
</head>
<body>

<!-- Navbar -->
<nav class="navbar navbar-expand-lg navbar-dark navbar-custom">
    <div class="container">
        <a class="navbar-brand" href="index.jsp">📰 MasterAnnonce</a>
        <a href="index.jsp" class="btn btn-light btn-sm">← Retour</a>
    </div>
</nav>

<!-- Header -->
<div class="bg-light border-bottom py-4">
    <div class="container">
        <h1>📋 Toutes les Annonces Publiées</h1>
        <p class="text-muted">Découvrez tous les annonces disponibles</p>
    </div>
</div>

<!-- Contenu -->
<div class="container py-5">
    <%
        try {
            AnnonceService service = new AnnonceService();
            List<Annonce> allAnnonces = service.listAll();

            // Filtrer les annonces PUBLISHED
            List<Annonce> annonces = new java.util.ArrayList<>();
            if (allAnnonces != null) {
                for (Annonce a : allAnnonces) {
                    if (a != null && a.getStatus() == AnnonceStatus.PUBLISHED) {
                        annonces.add(a);
                    }
                }
            }

            if (annonces == null || annonces.isEmpty()) {
    %>
        <div class="alert alert-info text-center py-5">
            <h3>📭 Aucune annonce publiée pour le moment</h3>
            <p>Revenez plus tard !</p>
            <a href="index.jsp" class="btn btn-primary">← Retour à l'accueil</a>
        </div>
    <%
            } else {
    %>
        <div class="row">
            <%
                for (Annonce ann : annonces) {
                    if (ann != null) {
            %>
            <div class="col-md-6 col-lg-4 mb-4">
                <div class="card annonce-card h-100">
                    <div class="card-body">
                        <h5 class="card-title">
                            <%= ann.getTitle() != null ? ann.getTitle() : "Annonce sans titre" %>
                        </h5>

                        <p class="card-text text-muted">
                            <%= ann.getDescription() != null ? ann.getDescription() : "Aucune description" %>
                        </p>

                        <div class="mb-3">
                            <span class="badge bg-success">✓ Publiée</span>
                            <% if (ann.getCategory() != null) { %>
                                <span class="badge bg-info"><%= ann.getCategory().getLabel() %></span>
                            <% } %>
                        </div>

                        <div class="row small text-muted">
                            <div class="col-6">
                                <strong>📍 Localisation</strong><br>
                                <%= ann.getAdress() != null ? ann.getAdress() : "Non spécifiée" %>
                            </div>
                            <div class="col-6">
                                <strong>📧 Contact</strong><br>
                                <a href="mailto:<%= ann.getMail() %>">
                                    <%= ann.getMail() != null ? ann.getMail() : "Non fourni" %>
                                </a>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <%
                    }
                }
            %>
        </div>

        <div class="text-center mt-5">
            <p class="text-muted">Total : <%= annonces.size() %> annonce(s)</p>
            <a href="index.jsp" class="btn btn-primary">← Retour à l'accueil</a>
        </div>
    <%
            }
        } catch (Exception e) {
    %>
        <div class="alert alert-danger">
            <strong>❌ Erreur :</strong> <%= e.getMessage() %>
        </div>
        <a href="index.jsp" class="btn btn-primary">← Retour à l'accueil</a>
    <%
        }
    %>
</div>


<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
