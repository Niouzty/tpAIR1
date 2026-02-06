# MasterAnnonce - Application J2EE de Gestion d'Annonces

## Vue d'ensemble

MasterAnnonce est une application web Jakarta EE permettant aux utilisateurs de creer, publier et gerer leurs annonces de maniere securisee.

## Architecture

### Modele des donnees

- User : id, username (unique), email (unique), password, createdAt
- Category : id, label (unique)
- Annonce : id, title, description, adress, mail, date, status (DRAFT/PUBLISHED/ARCHIVED), author_id (FK User), category_id (FK Category)

### Couches

- Web Layer : Servlets (controleurs)
- Service Layer : AnnonceService, AuthService (logique metier)
- DAO Layer : AnnonceRepository, UserRepository, CategoryRepository
- JPA Entities : Annonce, User, Category
- Database : PostgreSQL

### Packages

- fr.uit.univparis8.tpair.tpair1 : Servlets
- fr.uit.univparis8.tpair.tpair1.service : Services
- fr.uit.univparis8.tpair.tpair1.dao : Repositories
- fr.uit.univparis8.tpair.tpair1.model : Entites JPA
- fr.uit.univparis8.tpair.tpair1.filter : Filtres de securite
- fr.uit.univparis8.tpair.tpair1.jpa : Utilitaires JPA

## Fonctionnalites

### Authentification
- Systeme de login/logout
- Sessions utilisateur
- Filtre de securite (AuthFilter)

### Gestion des Annonces
- Liste paginee des annonces de l'utilisateur
- Creation d'annonce
- Modification (proprietaire uniquement)
- Suppression (proprietaire uniquement)
- Publication DRAFT -> PUBLISHED
- Archivage -> ARCHIVED
- Recherche par mot-cle
- Filtrage par categorie et statut
- Validation des donnees

## Configuration technique

Stack
- JDK : Java 11+
- Application Server : Tomcat 10+ (Jakarta EE)
- ORM : Hibernate 6.2.7
- Database : PostgreSQL
- Build : Maven
- Frontend : JSP + Bootstrap 5

Dependances principales
- jakarta.jakartaee-web-api 10.0.0
- hibernate-core 6.2.7.Final
- postgresql 42.6.0
- junit-jupiter 5.10.0

Configuration JPA (persistence.xml)
- URL : jdbc:postgresql://database-etudiants:5432/epembelefuala
- Driver : org.postgresql.Driver
- hibernate.hbm2ddl.auto : update

## Exercices TP2 realises

### Exercice 1 : Setup JPA/Hibernate
- Dependances Maven
- persistence.xml
- JPAUtil

### Exercice 2 : Mapping JPA
- Entites User, Category, Annonce
- Annotations JPA
- Bean Validation
- Relations mappees

### Exercice 3 : Repositories
- AnnonceRepository avec CRUD et search
- searchByAuthor() pour filtrer par utilisateur
- UserRepository et CategoryRepository
- JPQL uniquement

### Exercice 4 : Service et Transactions
- AnnonceService avec logique metier
- Transactions dans le service
- Validation des annonces
- Controle d'acces par utilisateur (ownership)

### Exercice 5 : Web (Servlets et JSP)
- Authentification (LoginServlet, LogoutServlet)
- AuthFilter pour la securite
- CRUD annonces
- Formulaires avec validation
- Gestion des erreurs

### Exercice 6 : Validation et gestion des erreurs
- Validation serveur des formulaires
- Messages d'erreur dans les JSP
- Conservation des valeurs saisies

### Bonus : Tests
- 4 tests unitaires dans AnnonceServiceTest
- Tests de propriete, transition de statut, controle d'acces, validation

## Problemes rencontres et solutions

### Probleme 1 : Isolation des donnees par utilisateur

Probleme : Les utilisateurs voyaient TOUTES les annonces et pouvaient modifier celles des autres.

Solution :
1. Ajouter searchByAuthor() dans AnnonceRepository pour filtrer par author_id
2. Ajouter userId en parametre dans create(), update(), delete(), publish(), archive()
3. Verifier l'ownership avant toute modification
4. Recuperer userId de la session dans les Servlets
5. Passer userId aux appels service

Resultat : Chaque utilisateur ne voit et ne modifie que SES annonces.

### Probleme 2 : Validation avec messages simples

Solution : Ajouter constructeur ValidationException(String message)

### Probleme 3 : Encodage et redirection

Solution :
- Ajouter req.setCharacterEncoding("UTF-8")
- Ajouter resp.setContentType("text/html;charset=UTF-8")
- Utiliser req.getContextPath() dans les redirections
- Ajouter session.setMaxInactiveInterval(30 * 60)

## Deploiement

Prerequisites
- PostgreSQL operationnel
- Tomcat 10+
- Maven

Etapes
1. mvn clean package
2. cp target/tpAIR1-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/
3. Redemarrer Tomcat

Acces
http://localhost:8080/tpAIR1-1.0-SNAPSHOT/

## Statut

BUILD SUCCESS
- 24 fichiers Java compiles
- 4 tests unitaires passants
- WAR genere

Application prete au deploiement.

Version : 2.0 (avec corrections de securite)
Date : 2026-02-23
