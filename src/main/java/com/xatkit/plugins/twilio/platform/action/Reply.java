package com.xatkit.plugins.twilio.platform.action;

import com.twilio.rest.api.v2010.account.Message;
import com.xatkit.core.platform.action.RuntimeAction;
import com.xatkit.execution.StateContext;
import com.xatkit.intent.EventInstance;
import com.xatkit.plugins.twilio.TwilioUtils;
import com.xatkit.plugins.twilio.platform.TwilioPlatform;
import lombok.NonNull;

import static fr.inria.atlanmod.commons.Preconditions.checkArgument;
import static fr.inria.atlanmod.commons.Preconditions.checkNotNull;

/**
 * A {@link RuntimeAction} that assigns the provided {@code username} to the given {@code issue}.
 * <p>
 * <b>Note:</b> this class requires that its containing {@link TwilioPlatform} has been loaded with a valid Twilio
 * credentials in order to authenticate the bot and access the Twilio API.
 */
public class Reply extends RuntimeAction<TwilioPlatform> {


    /**
     * The message to send.
     */
    private String message;

    /**
     * The user's phone number to send a message to.
     */
    private String userNumber;

    /**
     * The twilio phone number from which the message is sent.
     */
    private String twilioNumber;


    /**
     * sets the message and phone numbers to reply a message
     *
     * @param platform the {@link TwilioPlatform} containing this action
     * @param context  the {@link StateContext} associated to this action
     * @param message  the message to post
     */
    public  Reply(@NonNull TwilioPlatform platform, @NonNull StateContext context,@NonNull String message) {
        super(platform, context);
        this.userNumber = getUserNumber(context);
        this.twilioNumber = getTwilioNumber(context);
        this.message = message;
    }
    
    @Override
    protected Object compute() throws Exception {
        Message twilioMessage = Message.creator( 
                new com.twilio.type.PhoneNumber(userNumber), 
                new com.twilio.type.PhoneNumber(twilioNumber),  
                this.message)      
            .create();
        return twilioMessage;
    }

    /**
     * Returns the user number associated to the incoming message.
     * <p>
     * This method searches for the value stored with the {@link TwilioUtils#TWILIO_FROM_NUMBER} key in the
     * platform data of the current {@link EventInstance}.
     *
     * @param context the {@link StateContext} to retrieve the number from
     * @return the number associated to the incoming message
     * @throws NullPointerException     if the provided {@code context} is {@code null}, or if it does not contain the
     *                                  from number information
     * @throws IllegalArgumentException if the retrieved number is not a {@link String}
     * @see StateContext#getEventInstance()
     * @see EventInstance#getPlatformData()
     */
    public static String getUserNumber(@NonNull StateContext context) {
        Object number = context.getEventInstance().getPlatformData().get(TwilioUtils.TWILIO_FROM_NUMBER);
        checkNotNull(number, "Cannot retrieve the Telephone number from the context, expected a non null "
                + TwilioUtils.TWILIO_FROM_NUMBER + " value, found %s", number);
        checkArgument(number instanceof String, "Invalid user number type, expected %s, found %s",
                String.class.getSimpleName(), number.getClass().getSimpleName());
        return (String) number;
    }

    /**
     * Returns the twilio number associated to the incoming message.
     * <p>
     * This method searches for the value stored with the {@link TwilioUtils#TWILIO_FROM_NUMBER} key in the
     * platform data of the current {@link EventInstance}.
     *
     * @param context the {@link StateContext} to retrieve the number from
     * @return the number associated to the incoming message
     * @throws NullPointerException     if the provided {@code context} is {@code null}, or if it does not contain the
     *                                  from number information
     * @throws IllegalArgumentException if the retrieved number is not a {@link String}
     * @see StateContext#getEventInstance()
     * @see EventInstance#getPlatformData()
     */
    public static String getTwilioNumber(@NonNull StateContext context) {
        Object number = context.getEventInstance().getPlatformData().get(TwilioUtils.TWILIO_TO_NUMBER);
        checkNotNull(number, "Cannot retrieve the Telephone number from the context, expected a non null "
                + TwilioUtils.TWILIO_TO_NUMBER + " value, found %s", number);
        checkArgument(number instanceof String, "Invalid Twilio number type, expected %s, found %s",
                String.class.getSimpleName(), number.getClass().getSimpleName());
        return (String) number;
    }
}