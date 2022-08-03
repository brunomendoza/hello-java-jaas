package lab;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.io.IOException;

class SampleAcn {
    
    // https://docs.oracle.com/javase/1.5.0/docs/guide/security/jaas/tutorials/GeneralAcnOnly.html#MyCallbackHandler

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
                loginContext.login();
                isLogged = true;
            } catch (LoginException loginException) {
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

class MyCallBackHandler implements CallbackHandler {
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

    }
}