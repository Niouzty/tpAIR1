package fr.uit.univparis8.tpair.tpair1.config;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * Configuration JAX-RS pour l'API REST.
 * Tous les endpoints seront sous le chemin /api
 */
@ApplicationPath("/api")
public class RestApplication extends Application {
    // Configuration basique - Jersey scannera automatiquement les resources annotées @Path
}
