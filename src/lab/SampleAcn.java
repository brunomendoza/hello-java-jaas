package lab;

import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.io.IOException;

/**
 * // https://docs.oracle.com/javase/1.5.0/docs/guide/security/jaas/tutorials/GeneralAcnOnly.html#MyCallbackHandler
 */
class SampleAcn {

    public static void main(String[] args) {
        LoginContext loginContext = null;
        boolean isLogged = false;
        int count = 0;

        try {
            loginContext = new LoginContext("Sample", new MyCallBackHandler());
        } catch (LoginException e) {
            System.err.println("Cannot create login context: " + e.getMessage());
            System.exit(-1);
        } catch (SecurityException e) {
            System.err.println("Cannot create login context: " + e.getMessage());
            System.exit(-1);
        }

        while (!isLogged && count < 3) {
            try {
                // Carry out the authentication process
                // If authentication is successful, the SampleLoginModule populates the Subject with a Principal
                // representing the user.
                loginContext.login();
                isLogged = true;
            } catch (LoginException loginException) {
                count++;
                System.err.println("Authentication failed" + loginException.getMessage());

                try {
                    Thread.currentThread().sleep(3);
                } catch (Exception e) {
                    // ignore
                }
            }
        }

        if (isLogged) {
            System.out.println("Authentication succeeded!");
        } else {
            System.out.println("You extinguished your tries... :-(");
        }
    }

}

/**
 * MyCallBackHandler is used to obtain authentication information.
 * CallbackHandler implementations at com.sun.security.auth.callback
 * The LoginContext forwards the CallbackHandler directly to the underlying LoginModules.
 */
class MyCallBackHandler implements CallbackHandler {
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        for (Callback callback : callbacks) {
            if (callback instanceof TextOutputCallback) {

            } else if (callback instanceof NameCallback) {

            } else if (callback instanceof PasswordCallback) {

            } else {
                throw new UnsupportedCallbackException(callback, "Unrecognized Callback");
            }
        }
    }
}