The LoginContext instantiates a new empty javax.security.auth.Subject object (which represents the user or service being authenticated). The LoginContext constructs the configured LoginModule (in our case SampleLoginModule) and initializes it with this new Subject and MyCallbackHandler.

The LoginContext's login method then calls methods in the SampleLoginModule to perform the login and authentication. The SampleLoginModule will utilize the MyCallbackHandler to obtain the user name and password. Then the SampleLoginModule will check that the name and password are the ones it expects.

If authentication is successful, the SampleLoginModule populates the Subject with a Principal representing the user. The Principal the SampleLoginModule places in the Subject is an instance of SamplePrincipal, which is a sample class implementing the java.security.Principal interface. 

Username: testUser
Password: testPassword

$ ant run