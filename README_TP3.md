# TP3 - Backend API REST Sécurisé

## 📋 Vue d'ensemble

Ce projet transforme l'application **MasterAnnonce** en une **API REST professionnelle**, sécurisée, testée et industrialisable, en utilisant **Jakarta EE**, **JAX-RS** et **JPA/Hibernate**, **sans Spring**.

---

## 🏗️ Architecture

### Structure en couches

```
src/main/java/fr/uit/univparis8/tpair/tpair1/
├── config/              # Configuration JAX-RS
│   └── RestApplication.java
├── resource/            # Ressources JAX-RS (endpoints REST)
│   ├── HelloWorldResource.java
│   ├── AnnonceResource.java
│   └── AuthResource.java
├── dto/                 # Data Transfer Objects
│   ├── AnnonceDTO.java
│   ├── ErrorResponse.java
│   ├── PagedResponse.java
│   ├── LoginRequest.java
│   └── LoginResponse.java
├── service/             # Logique métier
│   └── AnnonceService.java (existant)
├── security/            # Sécurité & Authentification
│   ├── UserPrincipal.java
│   ├── RolePrincipal.java
│   ├── TokenManager.java
│   └── SecurityFilter.java
├── exception/           # Gestion centralisée des exceptions
│   ├── ValidationExceptionMapper.java
│   └── GeneralExceptionMapper.java
├── model/              # Entités JPA (existantes)
│   ├── Annonce.java
│   ├── User.java
│   ├── Category.java
│   └── AnnonceStatus.java
└── dao/                # Data Access Objects (existants)
```

### Points d'entrée de l'API

**Tous les endpoints sont préfixés par `/api`**

#### 1. Endpoints publics (sans authentification)

```
GET  /api/helloWorld              # Test simple
GET  /api/helloWorld/with-query   # Test avec QueryParams (?nom=X&prenom=Y)
GET  /api/helloWorld/with-path/{nom}/{prenom}  # Test avec PathParams
POST /api/auth/login              # Login et génération de token
```

#### 2. Endpoints protégés (authentification requise)

```
GET    /api/annonces              # Liste paginée des annonces
GET    /api/annonces/{id}         # Détail d'une annonce
POST   /api/annonces              # Créer une annonce
PUT    /api/annonces/{id}         # Mettre à jour une annonce
DELETE /api/annonces/{id}         # Supprimer une annonce
POST   /api/auth/logout           # Logout (revoke token)
```

---

## 🔐 Sécurité & Authentification

### Fonctionnement

1. **Login** : Utilisateur envoie `POST /api/auth/login` avec credentials
   ```json
   {
     "username": "admin",
     "password": "admin123"
   }
   ```

2. **Token Generation** : Le serveur génère un token UUID et le retourne
   ```json
   {
     "token": "550e8400-e29b-41d4-a716-446655440000",
     "userId": 1,
     "username": "admin",
     "message": "Authentification réussie"
   }
   ```

3. **Protected Requests** : Chaque requête protégée doit inclure le token dans le header
   ```
   Authorization: Bearer 550e8400-e29b-41d4-a716-446655440000
   ```

4. **Token Validation** : Le `SecurityFilter` valide le token avant d'autoriser l'accès

### Composants de sécurité

- **TokenManager** : Gère les tokens en mémoire (stockage, validation, expiration)
- **SecurityFilter** : JAX-RS Filter qui intercepte les requêtes et valide les tokens
- **UserPrincipal/RolePrincipal** : Représentations de l'identité utilisateur

---

## ✅ Validation & Gestion des erreurs

### Bean Validation

Les DTOs utilisent les annotations de validation :
```java
@NotBlank(message = "Le titre est obligatoire")
@Positive(message = "Le prix doit être positif")
```

### Gestion centralisée des erreurs

Tous les codes HTTP standards sont implémentés :

| Code | Cas d'usage |
|------|------------|
| **200** | Succès (GET, PUT) |
| **201** | Créé avec succès (POST) |
| **204** | Suppression réussie (DELETE) |
| **400** | Erreur client (données invalides) |
| **401** | Non authentifié (token absent/invalide) |
| **403** | Non autorisé (pas de permission) |
| **404** | Ressource inexistante |
| **500** | Erreur serveur |

### Format de réponse d'erreur normalisé

```json
{
  "status": 400,
  "message": "Validation échouée",
  "timestamp": "2026-02-23T14:00:00",
  "errors": [
    {
      "field": "titre",
      "message": "Le titre est obligatoire"
    }
  ]
}
```

---

## 🛠️ Technologies utilisées

| Technologie | Version | Rôle |
|------------|---------|------|
| Jakarta EE | 10.0.0 | Framework Java EE moderne |
| JAX-RS (Jersey) | 3.1.2 | Implémentation REST |
| Hibernate | 6.2.7 | ORM & JPA |
| PostgreSQL | 42.6.0 | Base de données |
| Bean Validation | 3.0.2 | Validation des données |
| Hibernate Validator | 8.0.1 | Implémentation BV |
| H2 Database | 2.2.220 | DB en mémoire pour tests |
| JUnit 5 | 5.10.0 | Tests unitaires |
| Mockito | 5.2.0 | Mocks pour tests |
| AssertJ | 3.24.1 | Assertions fluides |

---

## 🚀 Démarrage de l'application

### Prérequis
- JDK 11+
- Maven 3.6+
- Tomcat 10+ (ou application server compatible Jakarta EE 10)
- PostgreSQL (ou base de données compatible)

### Compilation
```bash
mvn clean package
```

### Déploiement sur Tomcat
```bash
cp target/tpAIR1-1.0-SNAPSHOT.war $CATALINA_HOME/webapps/
```

### Accès à l'API
```
http://localhost:8080/tpAIR1/api/helloWorld
```

---

## 📝 Exemples d'utilisation

### 1. Test endpoint simple
```bash
curl -X GET "http://localhost:8080/tpAIR1/api/helloWorld"
```

Réponse :
```json
{
  "message": "Hello World!"
}
```

### 2. Authentification
```bash
curl -X POST "http://localhost:8080/tpAIR1/api/auth/login" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

Réponse :
```json
{
  "token": "550e8400-e29b-41d4-a716-446655440000",
  "userId": 1,
  "username": "admin",
  "message": "Authentification réussie"
}
```

### 3. Lister les annonces (protégé)
```bash
curl -X GET "http://localhost:8080/tpAIR1/api/annonces" \
  -H "Authorization: Bearer 550e8400-e29b-41d4-a716-446655440000"
```

### 4. Créer une annonce (protégé)
```bash
curl -X POST "http://localhost:8080/tpAIR1/api/annonces" \
  -H "Authorization: Bearer 550e8400-e29b-41d4-a716-446655440000" \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Appartement 3 pièces",
    "description": "Bel appartement à Paris",
    "categorie": "Immobilier",
    "prix": 1200.50
  }'
```

---

## ⚙️ Configuration JAX-RS

La configuration se fait via la classe `RestApplication` :

```java
@ApplicationPath("/api")
public class RestApplication extends Application {
    // Configuration basique - Jersey scannera automatiquement 
    // les resources annotées @Path
}
```

**Avantages de cette approche** :
- ✅ Auto-scanning des ressources
- ✅ Support automatique des annotations
- ✅ Configuration minimale
- ✅ Facile à tester et maintenir

---

## 🎯 Implémentation par Partie

### Partie I - Exposition REST ✅
- [x] Configuration JAX-RS avec point d'entrée `/api`
- [x] HelloWorldResource pour test basique
- [x] Endpoints REST Annonce (CRUD complet)
- [x] DTOs avec Builder pattern
- [x] Pagination des résultats

### Partie II - Validation & Erreurs ✅
- [x] Bean Validation sur les DTOs
- [x] Gestion centralisée via ExceptionMappers
- [x] Codes HTTP corrects
- [x] Réponses JSON normalisées

### Partie III - Sécurité ✅
- [x] Authentification stateless (tokens)
- [x] Endpoint `/api/auth/login`
- [x] Filtre de sécurité JAX-RS
- [x] Validation des tokens
- [x] Gestion des erreurs 401/403

### Partie IV - Tests & Qualité (À faire)
- [ ] Tests Repository avec H2 in-memory
- [ ] Tests unitaires
- [ ] Tests d'intégration REST
- [ ] Tests de charge
- [ ] Documentation OpenAPI

---

## 🐛 Problèmes rencontrés & Solutions

### 1. Erreur 404 à la racine
**Problème** : `/api/` retournait 404 car le `web.xml` était vide.

**Solution** : Ajout de la configuration `welcome-file-list` dans le `web.xml` :
```xml
<welcome-file-list>
  <welcome-file>index.jsp</welcome-file>
</welcome-file-list>
```

### 2. Dépendances JAX-RS manquantes
**Problème** : Les annotations `@Path`, `@GET`, etc., n'étaient pas disponibles.

**Solution** : Ajout des dépendances Maven :
- `jersey-server`
- `jersey-container-servlet`
- `jersey-media-json-binding`

### 3. Validation des DTOs
**Problème** : Les violations de validation n'étaient pas capturées.

**Solution** : Implémentation d'un `ValidationExceptionMapper` pour intercepter et normaliser les erreurs.

### 4. Gestion de l'authentification
**Problème** : Comment passer le token d'une requête à l'autre sans session HTTP ?

**Solution** : 
- TokenManager en mémoire pour stocker les tokens
- Header `Authorization: Bearer <token>` pour chaque requête
- SecurityFilter pour valider avant d'exécuter les ressources

### 5. Intégration avec AnnonceService existante
**Problème** : Le service existant avait des méthodes différentes (`create`, `findById`, etc.) au lieu de ce qui était attendu.

**Solution** : Adaptation de AnnonceResource pour utiliser les vraies méthodes du service.

---

## 📦 Structure du WAR

```
tpAIR1-1.0-SNAPSHOT.war
├── WEB-INF/
│   ├── web.xml
│   ├── classes/
│   │   ├── fr/uit/univparis8/tpair/tpair1/ (toutes les classes compilées)
│   │   └── META-INF/
│   │       ├── persistence.xml
│   │       └── beans.xml
│   └── lib/ (toutes les dépendances JAR)
└── [JSP files] (pour la compatibilité arrière)
```

---

## 🔄 Flux d'une requête authentifiée

```
1. Client envoie :
   GET /api/annonces
   Authorization: Bearer <token>

2. Tomcat/Jersey route vers AnnonceResource.listerAnnonces()

3. SecurityFilter intercepte :
   ├─ Extrait le token du header
   ├─ Valide via TokenManager.validateToken()
   ├─ Attache userId/username au contexte
   └─ Si invalide → HTTP 401

4. AnnonceResource exécute :
   ├─ Appelle AnnonceService.listAll()
   ├─ Convertit Annonce → AnnonceDTO
   ├─ Paginne les résultats
   └─ Retourne PagedResponse

5. Client reçoit :
   {
     "content": [...],
     "pageNumber": 0,
     "pageSize": 10,
     "totalElements": 42,
     "totalPages": 5,
     "hasNextPage": true
   }
```

---

## 📚 Documentation API (À faire)

Pour générer la documentation OpenAPI :
```bash
mvn clean package -P openapi
```

Voir : `http://localhost:8080/tpAIR1/api/openapi.json`

---

## 🧪 Tests (À faire)

Exécuter les tests unitaires :
```bash
mvn test
```

Exécuter les tests d'intégration seuls :
```bash
mvn verify -DskipUnitTests
```

---

## 📝 Notes importantes

### État actuel
- ✅ Partie I : Exposition REST - **COMPLÈTE**
- ✅ Partie II : Validation & Erreurs - **COMPLÈTE**
- ✅ Partie III : Sécurité - **COMPLÈTE**
- ⏳ Partie IV : Tests & Qualité - **À FAIRE**

### Limitations connues
1. **TokenManager en mémoire** : Les tokens sont perdus au redémarrage. Pour la production, utiliser JWT ou Redis.
2. **Authentification simplifiée** : Accepte tous les logins. À remplacer par une vraie vérification en base.
3. **Pas de session** : La sécurité repose entièrement sur le token. Idéal pour les APIs.
4. **Pas de HTTPS** : À ajouter en production.

---

## 🎓 Concepts clés apprenant

1. **JAX-RS** : Comment Jersey implémente la spécification REST
2. **Architecture REST** : Ressources, verbes HTTP, stateless
3. **Authentification stateless** : Tokens vs sessions
4. **Gestion des erreurs** : ExceptionMappers centralisés
5. **DTOs** : Séparation entre le modèle et l'API
6. **Sécurité** : Filtres, validation, codes HTTP

---

## 🔗 Ressources utiles

- [Jakarta EE Documentation](https://jakarta.ee/)
- [JAX-RS Specification](https://projects.eclipse.org/projects/ee4j.jaxrs)
- [Jersey Official Documentation](https://eclipse-ee4j.github.io/jersey/)
- [Hibernate Documentation](https://hibernate.org/)

---

**Auteur** : Étudiant TP AIR #3  
**Date** : Février 2026  
**Durée du TP** : Environ 12 heures
