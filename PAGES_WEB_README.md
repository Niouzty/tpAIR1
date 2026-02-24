# 🌐 Pages Web - MasterAnnonce

## 📄 Pages Créées

### 1. **index.jsp** - Page d'Accueil
- **URL** : `http://localhost:8080/tpAIR1/`
- **Description** : Page d'accueil avec navigation principale
- **Contenu** :
  - Bouton "Parcourir les annonces"
  - Liens de connexion/inscription
  - Si connecté : lien pour ajouter une annonce

### 2. **AnnoncesPubliees.jsp** - Liste des Annonces Publiées
- **URL** : `http://localhost:8080/tpAIR1/AnnoncesPubliees.jsp`
- **Description** : Affiche toutes les annonces avec le statut "PUBLISHED"
- **Contenu** :
  - Navbar avec navigation
  - Grille d'annonces en cartes Bootstrap
  - Pour chaque annonce :
    - Titre
    - Description
    - Catégorie
    - Localisation
    - Email de contact
    - Date de création
  - Message si aucune annonce publiée

---

## 🎨 Design

- **Couleurs** : Dégradé purple (#667eea → #764ba2)
- **Framework** : Bootstrap 5.3.2
- **Responsive** : Mobile-friendly
- **Animations** : Hover effects sur les cartes

---

## 🔗 Navigation

```
index.jsp (Accueil)
├── AnnoncesPubliees.jsp (Annonces publiques)
├── Login (Connexion)
├── Register (Inscription)
├── AnnonceAdd (+ Nouvelle annonce - si connecté)
├── AnnonceList (Mes annonces - si connecté)
└── Logout (Déconnexion - si connecté)
```

---

## 💾 Données Affichées

Les annonces affichées sur `AnnoncesPubliees.jsp` viennent de la base de données :
- **Filtre** : Uniquement les annonces avec `status = PUBLISHED`
- **Source** : `AnnonceService.listAll()`
- **Tri** : Par date décroissante (plus récentes d'abord)

---

## ✅ À faire pour tester

1. **Compiler et déployer** :
   ```bash
   mvn clean package -DskipTests
   cp target/tpAIR1-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/
   ```

2. **Redémarrer Tomcat** :
   ```bash
   $CATALINA_HOME/bin/shutdown.sh && sleep 2 && $CATALINA_HOME/bin/startup.sh
   ```

3. **Accéder à l'application** :
   - Accueil : http://localhost:8080/tpAIR1/
   - Annonces : http://localhost:8080/tpAIR1/AnnoncesPubliees.jsp

---

## 📝 Notes

- Les pages JSP utilisent `AnnonceService` pour récupérer les données
- Les annonces doivent avoir le statut "PUBLISHED" pour apparaître
- Pour voir les annonces en action, créez-en une et publiez-la depuis `/AnnonceAdd`
