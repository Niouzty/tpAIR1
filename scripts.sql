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
