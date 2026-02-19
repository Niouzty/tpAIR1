# MasterAnnonce - TP3

Backend/API Jakarta EE + interface JSP pour la gestion d'annonces.

## 1) Prérequis
- Java 21 (ou 17+)
- Maven 3.9+
- PostgreSQL 14+
- Tomcat `10.1.52`

## 2) Base de donnees
Le projet utilise le schema PostgreSQL `MasterAnnonce`.

### Script SQL
Le script a lancer est a la racine du projet:
- `scripts.sql`

Execution:
```bash
psql -h 127.0.0.1 -p 5432 -U postgres -d postgres -f scripts.sql
```

Ce script cree:
- schema `MasterAnnonce`
- tables `users`, `category`, `annonce`
- contraintes/index
- donnees minimales (`General`, utilisateur `admin`)

## 3) Configuration JPA actuelle
Fichier:
- `src/main/resources/META-INF/persistence.xml`

Configuration attendue (locale):
- URL: `jdbc:postgresql://127.0.0.1:5432/postgres`
- User: `postgres`
- Password: `Exauce1964`
- Schema: `MasterAnnonce`

## 4) Modifications faites par rapport au TP3
### REST (JAX-RS)
- Point d'entree API `/api`
- Ressource annonces avec CRUD
- DTO + validation + mapping service
- Gestion d'erreurs JSON centralisee (400/401/404/409/500)

### Securite
- Authentification token (login REST)
- Filtre de securite sur endpoints proteges
- Regles metier: auteur uniquement, transitions de statut
- Integration JAAS (login/token)

### Robustesse/qualite
- Logging structure
- Tests unitaires + integration
- OpenAPI: `openapi/openapi.yaml`
- Script charge simple: `load-tests/k6-smoke.js`

### Ajustements web (interface JSP)
- Redirection apres login/inscription vers la page d'accueil
- Page publique des annonces publiees (`/AnnoncePublic`)
- Liste personnelle (`/AnnonceList`) reservee a l'utilisateur connecte

## 5) Page d'accueil (`index.jsp`)
Fichier:
- `src/main/webapp/index.jsp`

Comportement:
- Toujours accessible via: `http://localhost:8080/tpAIR1/index.jsp`
- Bouton public: consultation des annonces publiees (`/AnnoncePublic`)
- Si utilisateur connecte:
  - `Ajouter une annonce`
  - `Mes annonces`
  - `Deconnexion`
- Si utilisateur non connecte:
  - `Connexion`
  - `Inscription`

Objectif de cette page:
- Servir de point d'entree unique
- Separer clairement navigation publique et navigation authentifiee

## 6) Lancement
```bash
./mvnw clean package
```

Deployer `target/tpAIR1-1.0-SNAPSHOT.war` sur Tomcat 10.1.52 puis ouvrir:
- `http://localhost:8080/tpAIR1/index.jsp`

## 7) URLs principales
- Accueil: `/tpAIR1/index.jsp`
- Login: `/tpAIR1/Login`
- Register: `/tpAIR1/Register`
- Mes annonces: `/tpAIR1/AnnonceList`
- Annonces publiees (public): `/tpAIR1/AnnoncePublic`
- API base: `/tpAIR1/api`
