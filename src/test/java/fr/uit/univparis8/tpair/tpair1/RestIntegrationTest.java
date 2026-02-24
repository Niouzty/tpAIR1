package fr.uit.univparis8.tpair.tpair1;

import fr.uit.univparis8.tpair.tpair1.dto.LoginRequest;
import fr.uit.univparis8.tpair.tpair1.dto.AnnonceDTO;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

/**
 * Tests d'intégration REST pour l'API
 * Vérifie le flow complet : login -> token -> appel endpoints protégés
 * 
 * À exécuter avec : mvn verify (tests d'intégration)
 */
public class RestIntegrationTest {

    private static String baseURI = "http://localhost:8080/tpAIR1";
    private static String token;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = baseURI;
    }

    /**
     * Test 1 : GET /api/helloWorld - endpoint public sans authentification
     */
    @Test
    public void testHelloWorldPublic() {
        when()
            .get("/api/helloWorld")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Hello World!"));
    }

    /**
     * Test 2 : GET /api/params avec QueryParams
     */
    @Test
    public void testParamsWithQueryParams() {
        when()
            .get("/api/params?nom=Dupont&prenom=Jean")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Bonjour Jean Dupont"));
    }

    /**
     * Test 3 : GET /api/params/{nom}/{prenom} avec PathParams
     */
    @Test
    public void testParamsWithPathParams() {
        when()
            .get("/api/params/Dupont/Jean")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Bonjour Jean Dupont"));
    }

    /**
     * Test 4 : POST /api/login - authentification réussie
     */
    @Test
    public void testLoginSuccess() {
        LoginRequest loginReq = new LoginRequest();
        loginReq.username = "admin";
        loginReq.password = "admin";

        String response = 
        given()
            .contentType(ContentType.JSON)
            .body(loginReq)
        .when()
            .post("/api/login")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("username", equalTo("admin"))
            .body("token", notNullValue())
            .extract()
            .path("token");
        
        // Sauvegarder le token pour les tests suivants
        token = response;
    }

    /**
     * Test 5 : POST /api/login - authentification échouée
     */
    @Test
    public void testLoginFailure() {
        LoginRequest loginReq = new LoginRequest();
        loginReq.username = "unknown";
        loginReq.password = "wrongpassword";

        given()
            .contentType(ContentType.JSON)
            .body(loginReq)
        .when()
            .post("/api/login")
        .then()
            .statusCode(401)
            .contentType(ContentType.JSON)
            .body("code", equalTo(401))
            .body("message", containsString("Identifiants invalides"));
    }

    /**
     * Test 6 : GET /api/annonces sans token → 401 Unauthorized
     */
    @Test
    public void testAnnoncesWithoutToken() {
        when()
            .get("/api/annonces")
        .then()
            .statusCode(401)
            .contentType(ContentType.JSON)
            .body("code", equalTo(401))
            .body("message", containsString("Token absent"));
    }

    /**
     * Test 7 : GET /api/annonces avec token valide → 200 OK
     * Remarque : ce test suppose qu'un admin avec user 1 existe en base
     */
    @Test
    public void testAnnoncesWithValidToken() {
        // D'abord faire un login pour obtenir un token
        LoginRequest loginReq = new LoginRequest();
        loginReq.username = "admin";
        loginReq.password = "admin";

        String token = 
        given()
            .contentType(ContentType.JSON)
            .body(loginReq)
        .when()
            .post("/api/login")
        .then()
            .statusCode(200)
            .extract()
            .path("token");

        // Maintenant utiliser le token pour accéder à /api/annonces
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/api/annonces")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("content", notNullValue());
    }

    /**
     * Test 8 : GET /api/annonces avec token invalide → 401 Unauthorized
     */
    @Test
    public void testAnnoncesWithInvalidToken() {
        given()
            .header("Authorization", "Bearer invalid_token_xyz")
        .when()
            .get("/api/annonces")
        .then()
            .statusCode(401)
            .contentType(ContentType.JSON)
            .body("code", equalTo(401))
            .body("message", containsString("Token invalide"));
    }

    /**
     * Test 9 : GET /api/annonces avec format Authorization invalide → 400/401
     */
    @Test
    public void testAnnoncesWithInvalidAuthFormat() {
        given()
            .header("Authorization", "InvalidFormat token")
        .when()
            .get("/api/annonces")
        .then()
            .statusCode(401)
            .contentType(ContentType.JSON);
    }
}
