package com.xatkit.plugins.twilio;

import com.xatkit.plugins.chat.ChatUtils;
import com.xatkit.plugins.twilio.platform.TwilioPlatform;
import com.xatkit.plugins.twilio.platform.io.TwilioEventProvider;
import org.apache.commons.configuration2.Configuration;

/**
 * An utility interface that holds Twilio-related helpers.
 */
public interface TwilioUtils extends ChatUtils {
    
    /**
     * The {@link Configuration} key to store the user's phone number from an incoming message.
     *
     * @see TwilioEventProvider#TwilioEventProvider(TwilioPlatform)
     */
    String TWILIO_FROM_NUMBER = "xatkit.twilio.fromnumber";

    /**
     * The {@link Configuration} key to store the twilio acount phone number.
     *
     * @see TwilioEventProvider#TwilioEventProvider(TwilioPlatform)
     */
    String TWILIO_TO_NUMBER = "xatkit.twilio.tonumber";

}
