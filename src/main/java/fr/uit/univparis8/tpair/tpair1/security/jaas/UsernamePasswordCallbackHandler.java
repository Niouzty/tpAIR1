package fr.uit.univparis8.tpair.tpair1.security.jaas;

import javax.security.auth.callback.*;
import java.io.IOException;

public class UsernamePasswordCallbackHandler implements CallbackHandler {

    private final String username;
    private final String password;

    public UsernamePasswordCallbackHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof NameCallback) {
                ((NameCallback) callback).setName(username);
            } else if (callback instanceof PasswordCallback) {
                ((PasswordCallback) callback).setPassword(password != null ? password.toCharArray() : new char[0]);
            } else {
                throw new UnsupportedCallbackException(callback);
            }
        }
    }
}
