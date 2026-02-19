package fr.uit.univparis8.tpair.tpair1;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class RestIntegrationTest {

    private static String baseURI = "http://localhost:8080/tpAIR1";
    private static String token;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = baseURI;
        try {
            Response r = when().get("/api/params?nom=Smoke&prenom=Test");
            Assumptions.assumeTrue(r.statusCode() < 500, "Serveur REST local indisponible ou en erreur (>=500)");
        } catch (Exception e) {
            Assumptions.assumeTrue(false, "Serveur REST local indisponible: " + e.getMessage());
        }
    }

    
    @Test
    public void testParamsWithQueryParams() {
        when()
            .get("/api/params?nom=Dupont&prenom=Jean")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Bonjour Jean Dupont"));
    }

    
    @Test
    public void testParamsWithPathParams() {
        when()
            .get("/api/params/Dupont/Jean")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("message", equalTo("Bonjour Jean Dupont"));
    }

    
    @Test
    public void testLoginSuccess() {
        String loginReq = "{\"username\":\"admin\",\"password\":\"admin\"}";

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
        token = response;
    }

    
    @Test
    public void testLoginFailure() {
        String loginReq = "{\"username\":\"unknown\",\"password\":\"wrongpassword\"}";

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

    
    @Test
    public void testAnnoncesWithValidToken() {
        String loginReq = "{\"username\":\"admin\",\"password\":\"admin\"}";

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
        given()
            .header("Authorization", "Bearer " + token)
        .when()
            .get("/api/annonces")
        .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("content", notNullValue());
    }

    
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
