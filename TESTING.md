# 🧪 Tests & Qualité Logicielle - Partie IV

## Résumé de la Partie IV

Cette partie couvre les tests complets du projet (unitaires, intégration), la configuration pour les exécuter séparément, le logging structuré, et la documentation API.

---

## 🎯 Exercice 8 : Tests Repository avec H2 In-Memory

### Configuration H2

Les tests utilisent H2 en mode in-memory pour isoler les données de test :

```xml
<!-- pom.xml -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <version>2.2.220</version>
    <scope>test</scope>
</dependency>
```

### Exécuter les tests unitaires seuls

```bash
# Tests unitaires uniquement (Repository, Service)
mvn test -P unit-tests

# Ou sans profil (par défaut)
mvn test
```

### Écrire un test Repository

Exemple :

```java
@Test
public void testPaginationAnnonces() {
    List<Annonce> page1 = annonceRepository.search(em, null, null, null, 0, 10);
    List<Annonce> page2 = annonceRepository.search(em, null, null, null, 1, 10);
    
    assertNotNull(page1);
    assertTrue(page1.size() <= 10);
    assertFalse(page1.equals(page2));
}
```

---

## 🎯 Exercice 9 : Tests API REST d'Intégration

### Configuration RestAssured

RestAssured permet de tester les endpoints REST facilement :

```xml
<!-- pom.xml -->
<dependency>
    <groupId>io.rest-assured</groupId>
    <artifactId>rest-assured</artifactId>
    <version>5.3.2</version>
    <scope>test</scope>
</dependency>
```

### Exécuter les tests d'intégration

```bash
# Tests d'intégration seuls
mvn verify -P integration-tests

# Tous les tests (unitaires + intégration)
mvn verify -P all-tests
```

### Exemple : Tester un endpoint

```java
@Test
public void testHelloWorldEndpoint() {
    RestAssured.baseURI = "http://localhost:8080";
    
    given()
        .when()
        .get("/tpAIR1/api/helloWorld")
        .then()
        .statusCode(200)
        .contentType(ContentType.JSON)
        .body("message", equalTo("Hello World!"));
}
```

### Tests des erreurs

```java
@Test
public void testAnnonceNotFound() {
    RestAssured.baseURI = "http://localhost:8080";
    
    String token = loginAndGetToken();
    
    given()
        .header("Authorization", "Bearer " + token)
        .when()
        .get("/tpAIR1/api/annonces/999999")
        .then()
        .statusCode(404)
        .body("status", equalTo(404));
}
```

---

## 🎯 Exercice 10 : Industrialisation

### Profils Maven

Trois profils permettent de lancer les tests séparément :

```bash
# Tests unitaires seuls (JUnit + Repository)
mvn test

# Tests d'intégration seuls (REST + endpoints)
mvn verify -P integration-tests

# Tous les tests
mvn verify -P all-tests
```

### Intérêt de séparer les tests

| Aspect | Tests Unitaires | Tests Intégration |
|--------|-----------------|-------------------|
| **Durée** | Rapides (< 30s) | Lents (1-5 min) |
| **Isolation** | Complète (mocks) | Partielle (BD réelle) |
| **Fiabilité** | Très stable | Peut être flaky |
| **Couverture code** | Complète | Partielle |
| **Feedback** | Immédiat | Délai |
| **Lancer souvent** | OUI | NON |
| **CI/CD** | À chaque commit | Avant merge |

**Recommandation** :
- Développement : `mvn test` souvent
- Before commit : `mvn verify` une fois
- CI/CD pipeline : `mvn test` rapide, puis `mvn verify` si OK

### Logging Structuré

Le logging est configuré avec SLF4J + Logback :

```xml
<!-- logback.xml -->
<appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
        <pattern>
            %d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n
        </pattern>
    </encoder>
</appender>
```

Utilisation :

```java
private static final Logger logger = LoggerFactory.getLogger(AnnonceResource.class);

public Response listerAnnonces() {
    logger.info("Récupération liste annonces");
    try {
        // ...
        logger.debug("Annonces trouvées: {}", annonces.size());
    } catch (Exception e) {
        logger.error("Erreur lors de la récupération", e);
    }
}
```

### Documentation OpenAPI/Swagger

```bash
# La documentation est accessible à :
http://localhost:8080/tpAIR1/api/openapi.json
http://localhost:8080/tpAIR1/swagger-ui.html
```

Utilisation :

```java
@GET
@Path("/annonces")
@Operation(summary = "Lister les annonces")
@APIResponse(responseCode = "200", description = "Liste paginée des annonces")
public Response listerAnnonces() {
    // ...
}
```

### Tests de charge simples

Script bash pour tester la performance :

```bash
#!/bin/bash

# 100 requêtes concurrentes
ab -n 100 -c 10 http://localhost:8080/tpAIR1/api/helloWorld

# Ou avec Apache JMeter
jmeter -n -t testplan.jmx -l results.jtl -j jmeter.log
```

---

## 📝 README avec Explications

Voir : `README_TP3.md` pour :
- Architecture complète
- Endpoints disponibles
- Exemples curl
- Problèmes rencontrés & solutions
- Explications sur les tests

---

## Commandes Résumées

```bash
# Compiler
mvn clean compile

# Tests unitaires seuls
mvn test

# Tests intégration seuls
mvn verify -P integration-tests

# Tous les tests
mvn verify -P all-tests

# Packager
mvn clean package -DskipTests
```

---

**Fin de la Partie IV**

Prochaine: Bonus Exercice 5 - JAAS Integration (voir JAAS_GUIDE.md)
