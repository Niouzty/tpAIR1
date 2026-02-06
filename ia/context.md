# context.md — Contexte Projet (J2EE / Jakarta EE)

## Objectif
Application web J2EE (Jakarta EE) basée sur l’architecture MVC :
- **Servlets** = contrôleurs
- **JSP** = vues (UI)
- **Services / DAO** = logique métier + accès aux données
- **JPA (Hibernate/EclipseLink)** = persistance (entités + repositories/DAO)

## Périmètre fonctionnel (générique)
L’application gère des ressources métier (ex: utilisateurs, annonces, produits, etc.) :
- CRUD : création / lecture / mise à jour / suppression
- Authentification (optionnel selon projet)
- Gestion d’erreurs propre (pages erreurs / messages)

## Stack technique
- Java 17+ (ou version demandée par ton cours)
- Tomcat 10+ (Jakarta EE : packages `jakarta.*`) ou Tomcat 9 (Java EE : `javax.*`)
- JSP + JSTL
- JPA + Provider (Hibernate / EclipseLink)
- Base de données : MySQL / PostgreSQL / SQLite (selon projet)
- Maven ou Gradle
- Bootstrap (optionnel) pour UI

## Architecture recommandée
Exemple de packages :

- `fr.xxx.app.web`
    - Servlets (contrôleurs), filtres, listeners
- `fr.xxx.app.service`
    - Services (logique métier)
- `fr.xxx.app.dao`
    - DAO / Repositories (accès DB)
- `fr.xxx.app.model`
    - Entités JPA + DTO éventuels
- `fr.xxx.app.util`
    - Helpers (validation, mapping, etc.)

## Standards MVC (règles d’or)
- Les **JSP n’accèdent jamais à la DB** et ne contiennent pas de logique métier.
- Les **Servlets** :
    - récupèrent les paramètres
    - valident
    - appellent service/DAO
    - posent des attributs dans `request`
    - font `forward` vers une JSP ou `redirect`
- Les **DAO** font uniquement la persistance.
- Les **Services** contiennent les règles métier.

## Gestion des routes
- Chaque action passe par une URL claire (ex: `/annonce/list`, `/annonce/create`, `/auth/login`…)
- POST pour création/modification, GET pour lecture
- Redirection après POST (pattern PRG : Post/Redirect/Get)

## Conventions JSP
- Utiliser JSTL + EL (`${...}`) au lieu de scriptlets `<% %>` (si possible)
- Si scriptlets imposés par le cours : les limiter (pas de logique complexe)

## Sécurité de base
- Validation des entrées côté serveur
- Protection contre injections SQL via JPA (paramétrage) et jamais de concat SQL brute
- Encodage HTML des sorties si affichage de contenu utilisateur (évite XSS)