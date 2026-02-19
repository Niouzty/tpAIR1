package fr.uit.univparis8.tpair.tpair1.security.jaas;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;


public final class JaasConfigSupport {

    private static volatile boolean initialized;

    private JaasConfigSupport() {
    }

    public static void ensureConfigured() {
        if (initialized) {
            return;
        }
        synchronized (JaasConfigSupport.class) {
            if (initialized) {
                return;
            }
            if (System.getProperty("java.security.auth.login.config") == null) {
                URL resource = JaasConfigSupport.class.getClassLoader().getResource("jaas.conf");
                if (resource != null && "file".equals(resource.getProtocol())) {
                    try {
                        File file = new File(resource.toURI());
                        System.setProperty("java.security.auth.login.config", file.getAbsolutePath());
                    } catch (URISyntaxException ignored) {
                    }
                }
            }
            initialized = true;
        }
    }
}
