<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MasterAnnonce - Accueil</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            color: white;
        }
        .hero-section {
            text-align: center;
            padding: 60px 20px;
        }
        .hero-section h1 {
            font-size: 48px;
            font-weight: bold;
            margin-bottom: 20px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.3);
        }
        .hero-section p {
            font-size: 20px;
            margin-bottom: 40px;
            opacity: 0.95;
        }
        .btn-large {
            padding: 12px 40px;
            font-size: 18px;
            margin: 10px;
        }
        .card-feature {
            background: rgba(255,255,255,0.1);
            border: 1px solid rgba(255,255,255,0.3);
            color: white;
            margin-top: 40px;
        }
        .card-feature h5 {
            margin-top: 20px;
        }
    </style>
</head>
<body>

<div class="hero-section">
    <h1>📰 MasterAnnonce</h1>
    <p>La plateforme pour publier et découvrir les meilleures annonces</p>

    <div class="mb-4">
        <a href="AnnoncesPubliees.jsp" class="btn btn-light btn-large" style="font-weight: bold; padding: 15px 50px; font-size: 20px;">
            🔍 PARCOURIR LES ANNONCES
        </a>

        <%
            Long userId = (Long) session.getAttribute("userId");
            if (userId != null) {
        %>
            <br><br>
            <a href="AnnonceAdd" class="btn btn-warning btn-large">➕ Ajouter une annonce</a>
            <a href="AnnonceList" class="btn btn-info btn-large">📋 Mes annonces</a>
            <a href="Logout" class="btn btn-outline-light btn-large">🚪 Déconnexion</a>
        <%
            } else {
        %>
            <br><br>
            <a href="Login" class="btn btn-success btn-large">🔐 Connexion</a>
            <a href="Register" class="btn btn-outline-light btn-large">✍️ Inscription</a>
        <%
            }
        %>
    </div>

    <div class="row mt-5">
        <div class="col-md-3">
            <div class="card-feature">
                <h5>Rapide</h5>
                <p>Publiez vos annonces en quelques secondes</p>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card-feature">
                <h5>🔒 Sécurisé</h5>
                <p>Vos données sont protégées</p>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card-feature">
                <h5>git branch Responsive</h5>
                <p>Utilisable sur tous les appareils</p>
            </div>
        </div>
        <div class="col-md-3">
            <div class="card-feature">
                <h5>Support</h5>
                <p>Aide disponible 24/7</p>
            </div>
        </div>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>