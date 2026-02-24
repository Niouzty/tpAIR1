# Dépendances Maven Ajoutées pour TP3

## 📦 Dépendances JAX-RS (REST)

```xml
<!-- Jersey Server - Core REST implementation -->
<dependency>
    <groupId>org.glassfish.jersey.core</groupId>
    <artifactId>jersey-server</artifactId>
    <version>3.1.2</version>
</dependency>

<!-- Jersey Servlet Container - Deployment on web servers -->
<dependency>
    <groupId>org.glassfish.jersey.containers</groupId>
    <artifactId>jersey-container-servlet</artifactId>
    <version>3.1.2</version>
</dependency>

<!-- Jersey JSON Binding - Automatic JSON serialization/deserialization -->
<dependency>
    <groupId>org.glassfish.jersey.media</groupId>
    <artifactId>jersey-media-json-binding</artifactId>
    <version>3.1.2</version>
</dependency>
```

### Rôle
- **jersey-server** : Core REST functionality (`@Path`, `@GET`, `@POST`, etc.)
- **jersey-container-servlet** : Allows Jersey to run in Tomcat/Servlet containers
- **jersey-media-json-binding** : Automatic JSON marshalling/unmarshalling using JSONB

---

## ✅ Dépendances Validation (Bean Validation)

```xml
<!-- Jakarta Validation API -->
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
    <version>3.0.2</version>
</dependency>

<!-- Hibernate Validator - Implementation of Bean Validation -->
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>8.0.1.Final</version>
</dependency>
```

### Rôle
- **jakarta.validation-api** : Standard Java validation annotations (`@NotBlank`, `@Email`, etc.)
- **hibernate-validator** : Implementation that validates the annotations

---

## 🗄️ Dépendances Tests (H2 Database)

```xml
<!-- H2 Database - In-memory database for testing -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.2.220</version>
    <scope>test</scope>
</dependency>
```

### Rôle
- Provides an in-memory database for integration tests
- No external database needed for tests
- Fast and lightweight

---

## 📊 Résumé des Dépendances

| Dépendance | Version | Type | Rôle |
|-----------|---------|------|------|
| jersey-server | 3.1.2 | Runtime | Core REST |
| jersey-container-servlet | 3.1.2 | Runtime | Servlet integration |
| jersey-media-json-binding | 3.1.2 | Runtime | JSON handling |
| jakarta.validation-api | 3.0.2 | Runtime | Validation API |
| hibernate-validator | 8.0.1 | Runtime | Validation impl |
| h2 | 2.2.220 | Test | In-memory DB |

---

## 🔗 Dépendances Transitivesdéjà présentes

Ces dépendances étaient déjà dans le projet :

- **jakarta.jakartaee-web-api:10.0.0** : Contient les APIs Jakarta EE (Servlets, JSP, etc.)
- **hibernate-core:6.2.7** : ORM pour la persistance JPA
- **postgresql:42.6.0** : Driver PostgreSQL
- **junit-jupiter:5.10.0** : Framework de tests
- **mockito:5.2.0** : Mocking library
- **assertj-core:3.24.1** : Assertion library

---

## 🚀 Pourquoi ces dépendances ?

### Jersey pour JAX-RS
- ✅ Implémentation standard de JAX-RS
- ✅ Support automatique des annotations REST
- ✅ Intégration facile avec Tomcat
- ✅ Bonne documentation

### Bean Validation
- ✅ Standard Java pour la validation
- ✅ Annotations déclaratives
- ✅ Support dans les ExceptionMappers
- ✅ Fonctionne bien avec Jackson/JSONB

### H2 pour les tests
- ✅ Base de données en mémoire (pas d'installation)
- ✅ Compatible avec JPA/Hibernate
- ✅ Rapide et léger
- ✅ Parfait pour les tests d'intégration

---

## 📝 Gestion des versions

Toutes les versions ont été choisies pour être :
- ✅ Compatibles avec Jakarta EE 10
- ✅ Compatibles avec Java 11+
- ✅ Stables et éprouvées en production
- ✅ À jour au moment du TP (février 2026)

---

## ⚙️ Configuration Maven

Dans le `pom.xml`, les configurations clés :

```xml
<maven.compiler.source>11</maven.compiler.source>
<maven.compiler.target>11</maven.compiler.target>
<project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
```

Assure :
- Compilation pour Java 11
- Encodage UTF-8 pour les sources

---

## 🔄 Transitivité des dépendances

Jersey apporte automatiquement :
- jackson-databind (JSON)
- jaxb-runtime (XML binding)
- jaxb-core (XML processing)
- etc.

Ces dépendances sont gérées automatiquement par Maven.

---

**Nombre total de fichiers JAR dans le WAR** : 38 dépendances
**Taille totale du WAR** : 20 MB
