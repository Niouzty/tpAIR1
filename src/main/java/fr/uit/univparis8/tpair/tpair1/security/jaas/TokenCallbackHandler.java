package fr.uit.univparis8.tpair.tpair1.security.jaas;

import javax.security.auth.callback.*;
import java.io.IOException;

public class TokenCallbackHandler implements CallbackHandler {

    private final String token;

    public TokenCallbackHandler(String token) {
        this.token = token;
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof NameCallback) {
                ((NameCallback) callback).setName(token);
            } else {
                throw new UnsupportedCallbackException(callback);
            }
        }
    }
}
