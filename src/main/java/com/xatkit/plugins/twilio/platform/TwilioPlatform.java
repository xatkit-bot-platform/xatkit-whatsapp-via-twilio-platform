package com.xatkit.plugins.twilio.platform;

import com.twilio.Twilio;
import com.twilio.exception.AuthenticationException;
import com.xatkit.core.XatkitBot;
import com.xatkit.core.platform.RuntimePlatform;
import com.xatkit.core.platform.action.RuntimeActionResult;
import com.xatkit.execution.StateContext;
import com.xatkit.plugins.twilio.platform.action.Reply;
import com.xatkit.plugins.twilio.platform.action.ReplyMedia;
import com.xatkit.plugins.twilio.platform.io.TwilioEventProvider;
import org.apache.commons.configuration2.Configuration;
import fr.inria.atlanmod.commons.log.Log;
import lombok.NonNull;

import static fr.inria.atlanmod.commons.Preconditions.checkArgument;
import static java.util.Objects.nonNull;


/**
 * A {@link RuntimePlatform} class that connects and interacts with the Twilio API.
 */
public class TwilioPlatform extends RuntimePlatform {

    /**
     * The {@link Configuration} key to store the Twilio username id.
     * <p>
     * The {@link TwilioPlatform} can handle {@link Configuration} with username id/auth token.
     *
     * @see #TWILIO_AUTH_TOKEN
     */
    public static String TWILIO_ACCOUNT_SID ="xatkit.twilio.username";

    /**
     * The {@link Configuration} key to store the Twilio auth token.
     * <p>
     * The {@link TwilioPlatform} can handle {@link Configuration} with with username/auth token. 
     *
     * @see #TWILIO_ACCOUNT_SID
     */
    public static String TWILIO_AUTH_TOKEN="xatkit.twilio.auth.token";

    /**
     * Initializes and returns a new {@link TwilioEventProvider}.
     *
     * @return the {@link TwilioEventProvider}
     */
    public TwilioEventProvider getTwilioEventProvider() {
        return new TwilioEventProvider(this);
    }

    /**
     * {@inheritDoc}
     * <p>
     * This method tries to initialize the {@link Twilio} client used to access the Twilio API by looking for
     * the ACCOUNT_SID/AUTH_TOKEN in the provided {@code configuration}.
     *
     * @throws IllegalArgumentException if the provided {@code configuration} does not
     *                                  contain a valid {@code TWILIO_ACCOUNT_SID} or {@code TWILIO_AUTH_TOKEN}
     * @throws AuthenticationException  if the provided credentials are not valid or if a network error occurred when
     *                                  accessing the Twilio API.
     */
    @Override
    public void start(XatkitBot xatkitBot, Configuration configuration) {
        super.start(xatkitBot, configuration);
        String username = configuration.getString(TWILIO_ACCOUNT_SID);
        if (nonNull(username)) {
            String authToken = configuration.getString(TWILIO_AUTH_TOKEN);
            checkArgument(nonNull(authToken) && !authToken.isEmpty(), "Cannot construct a client from the " +
                            "provided username id and auth token, please ensure that the Xatkit configuration contains a " +
                            "valid username id and auth token");
            checkTwilioClient(username,authToken);
        } else {
                Log.warn("No credentials set in the configuration, the client will not be able to call methods on " +
                        "the remote Twilio API. If you want to use the Twilio API you must provide a " +
                        "username id/auth token in the Xatkit configuration");
        }
    }
   
    /**
     * Checks that provided {@code twilioClient} is initialized and has valid credentials.
     * <p>
     * Credentials are checked by trying to retrieve the <i>self user login</i> from the {@link Twilio} client. If
     * the credentials are valid the {@link Twilio} client is initialized, otherwise it throws an
     * {@link AuthenticationException} when receiving the API result.
     *
     * @param username the username to check
     * @param password the auth token  to check
     * @throws AuthenticationException if the provided {@link Twilio} client credentials are invalid.
     */
    private void checkTwilioClient(String username, String password) throws AuthenticationException {
        Twilio.init(username, password); 
    }

    /**
     * Send the provided {@code message} to the phone number of the user sending a message to twilio's phone number.
     * <p>
     * The user phone and twilio phone is extracted from the provided {@code context}.
     *
     * @param context the current {@link StateContext}
     * @param message the message to post
     */
    public void reply(@NonNull StateContext context, @NonNull String message) {
        Reply action = new Reply(this, context, message);
        RuntimeActionResult result = action.call();
    }

    /**
     * Send the provided {@code message} and {@code media}  to the phone number of the user sending a message to twilio's phone number.
     * <p>
     * The user phone and twilio phone is extracted from the provided {@code context}.
     *
     * @param context the current {@link StateContext}
     * @param message the message to post
     */
    public void replyMedia(@NonNull StateContext context, @NonNull String message,@NonNull String media) {
        ReplyMedia action = new ReplyMedia(this, context, message,media);
        RuntimeActionResult result = action.call();
    }

    /**
     * Returns the {@link StateContext} associated to the provided  {@code FromNumber}.
     * @param FromNumber {@code FromNumber} representing the phone number to create a session for
     * @return the {@link StateContext} associated to the phone number
     */
    public StateContext createSessionFromNumber(String FromNumber) {
        return this.xatkitBot.getOrCreateContext(FromNumber);
    }

}
