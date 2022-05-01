package com.xatkit.plugins.twilio.platform.io;

import com.xatkit.core.platform.io.WebhookEventProvider;
import com.xatkit.execution.StateContext;
import com.xatkit.intent.EventInstance;
import com.xatkit.plugins.twilio.platform.TwilioPlatform;
import org.apache.commons.configuration2.Configuration;


public class TwilioEventProvider extends WebhookEventProvider<TwilioPlatform,TwilioRestHandler>{

    private final static String ENDPOINT_URI = "/twilio";

    /**
     * Constructs a {@link TwilioEventProvider} and binds it to the provided {@code twilioPlatform}.
     *
     * @param twilioPlatform the {@link TwilioPlatform} managing this provider
     */
    public TwilioEventProvider(TwilioPlatform twilioPlatform) {
        super(twilioPlatform);
    }

    @Override
	public String getEndpointURI() {
		return ENDPOINT_URI;
	}
    
    @Override
    public void start(Configuration configuration) {
        super.start(configuration); 
    }

    @Override
	protected TwilioRestHandler createRestHandler() {
		return new TwilioRestHandler(this);
	}

    @Override
    public void sendEventInstance(EventInstance eventInstance, StateContext context) {
        super.sendEventInstance(eventInstance, context);
    }

    @Override
    public void run() {
        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {

            }
        }
    }

}
