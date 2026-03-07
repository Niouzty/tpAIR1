# tpAIR1 (TP4 Spring Boot)

## Stack et architecture
- Spring Boot 3.5.5 / Java 17
- Web MVC (REST), Spring Data JPA (PostgreSQL en prod, H2 en test)
- Security stateless + JWT (userId, role) ; rôles USER/ADMIN
- Validation Jakarta, MapStruct pour DTO/mapping, AOP (logs), Swagger (springdoc)
- Packages : `api` (controllers/DTO), `service`, `repository`, `model`, `enums`, `logging`, `service/exceptions`

## Endpoints (REST, JSON)
- Auth : `POST /api/auth/register`, `POST /api/auth/login` → token JWT
- Annonces (Bearer) : `GET /api/annonces` (q, status, cat, author, page/size/sort), `GET /api/annonces/{id}`, `POST`, `PUT`, `DELETE` (ARCHIVED requis), `PATCH /publish`, `PATCH /archive`

## Règles métier & sécurité
- Seul l’auteur peut modifier/publier/supprimer ; archive réservée ADMIN
- Annonce PUBLISHED non modifiable ; suppression seulement après ARCHIVE
- Champs validés (title/description/adress/mail), @Version pour concurrence

## Lancement
Profil Postgres (par défaut) : adapter `spring.datasource.*` dans `application.yml` puis  
`./mvnw spring-boot:run`  
Profil test (H2) : `./mvnw -Dspring.profiles.active=test test`

Swagger : `http://localhost:8080/swagger-ui.html`

## Tests
- MockMvc (auth + annonces, 401/403/400, règles métier)
- Mockito (service : tri autorisé, archive admin)
- Lancer : `./mvnw test`

## Script SQL Postgres (optionnel)
```sql
CREATE SCHEMA IF NOT EXISTS "MasterAnnonce";
SET search_path TO "MasterAnnonce", public;

CREATE TABLE IF NOT EXISTS "users" (
    "id" BIGSERIAL PRIMARY KEY,
    "username" VARCHAR(255) NOT NULL UNIQUE,
    "email" VARCHAR(255) NOT NULL UNIQUE,
    "password" VARCHAR(255) NOT NULL,
    "createdAt" TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS "category" (
    "id" BIGSERIAL PRIMARY KEY,
    "label" VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS "annonce" (
    "id" BIGSERIAL PRIMARY KEY,
    "title" VARCHAR(64) NOT NULL,
    "description" VARCHAR(256) NOT NULL,
    "adress" VARCHAR(64) NOT NULL,
    "mail" VARCHAR(64) NOT NULL,
    "date" TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    "status" VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    "version" BIGINT NOT NULL DEFAULT 0,
    "author_id" BIGINT NOT NULL,
    "category_id" BIGINT NOT NULL,
    CONSTRAINT "chk_annonce_status" CHECK ("status" IN ('DRAFT', 'PUBLISHED', 'ARCHIVED')),
    CONSTRAINT "fk_annonce_author" FOREIGN KEY ("author_id") REFERENCES "users"("id"),
    CONSTRAINT "fk_annonce_category" FOREIGN KEY ("category_id") REFERENCES "category"("id")
);

CREATE INDEX IF NOT EXISTS "idx_annonce_author" ON "annonce"("author_id");
CREATE INDEX IF NOT EXISTS "idx_annonce_category" ON "annonce"("category_id");
CREATE INDEX IF NOT EXISTS "idx_annonce_status" ON "annonce"("status");
CREATE INDEX IF NOT EXISTS "idx_annonce_date" ON "annonce"("date" DESC);

INSERT INTO "category"("label") VALUES ('General')
ON CONFLICT ("label") DO NOTHING;

INSERT INTO "users"("username", "email", "password")
VALUES ('admin', 'admin@local.test', 'admin')
ON CONFLICT ("username") DO NOTHING;
```

## CI / Qualité
- Workflow GitHub Actions : `mvn -B clean verify` (Java 17/21), artefact `master-annonce-jar`, job Docker sur main/tags
- JaCoCo activé (`mvn verify`, rapport `target/site/jacoco`)
- Postman collection : `postman/MasterAnnonce.postman_collection.json`

## Problèmes rencontrés / solutions
- Secret JWT trop court → génération auto d’une clé HS256 si <32 bytes
- Noms de paramètres REST : option `-parameters` dans maven-compiler
- Profils DB : Postgres par défaut, H2 pour tests (health DB activé)
