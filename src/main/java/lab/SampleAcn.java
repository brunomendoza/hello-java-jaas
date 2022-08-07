package lab;

import javax.security.auth.callback.*;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;
import java.io.*;
import java.util.Arrays;

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
                // The LoginContext instantiates a new empty
                // javax.security.auth.Subject object (which represents the
                // user or service being authenticated).
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
                // Display the message according to the specified type
                TextOutputCallback textOutputCallback = (TextOutputCallback) callback;

                switch (textOutputCallback.getMessageType()) {
                    case TextOutputCallback.INFORMATION:
                        System.out.println(textOutputCallback.getMessage());
                        break;
                    case TextOutputCallback.ERROR:
                        System.out.println("ERROR: " + textOutputCallback.getMessage());
                        break;
                    case TextOutputCallback.WARNING:
                        System.out.println("WARNING: " + textOutputCallback.getMessage());
                        break;
                    default:
                        throw new IOException("Unsupported message type: " + textOutputCallback.getMessageType());
                }
            } else if (callback instanceof NameCallback) {
                // Prompt the user for a username
                NameCallback nameCallback = (NameCallback) callback;

                System.err.println(nameCallback.getPrompt());
                System.err.flush();
                nameCallback.setName(new BufferedReader(new InputStreamReader(System.in)).readLine());
            } else if (callback instanceof PasswordCallback) {
                // Prompt the user for sensitive information
                PasswordCallback passwordCallback = (PasswordCallback) callback;

                System.err.println(passwordCallback.getPrompt());
                System.err.flush();
                passwordCallback.setPassword(readPassword(System.in));
            } else {
                throw new UnsupportedCallbackException(callback, "Unrecognized Callback");
            }
        }
    }

    private char[] readPassword(InputStream inputStream) throws IOException {
        char[] lineBuffer;
        char[] buffer;
        int i;

        buffer = lineBuffer = new char[128];

        int room = buffer.length;
        int offset = 0;
        int c;

        loop:
        while(true) {
            switch (c = inputStream.read()) {
                case -1:
                case '\n':
                    break loop;
                case '\r':
                    int c2 = inputStream.read();
                    if ((c2 != '\n')  && (c2 != -1)) {
                        if (!(inputStream instanceof PushbackInputStream)) {
                            inputStream = new PushbackInputStream(inputStream);
                        }

                        ((PushbackInputStream) inputStream).unread(c2);
                    } else {
                        break loop;
                    }
                default:
                    if (--room < 0) {
                        buffer = new char[offset + 128];
                        room = buffer.length -offset;
                        System.arraycopy(lineBuffer, 0, buffer, 0, offset);
                        Arrays.fill(lineBuffer, ' ');
                        lineBuffer = buffer;
                    }

                    buffer[offset++] = (char) c;
                    break;
            }
        }

        if (offset == 0) {
            return null;
        }

        char[] ret = new char[offset];
        System.arraycopy(buffer, 0, ret, 0, offset);
        Arrays.fill(buffer, ' ');

        return ret;
    }
}