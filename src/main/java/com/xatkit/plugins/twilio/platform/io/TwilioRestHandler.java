package com.xatkit.plugins.twilio.platform.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xatkit.core.XatkitException;
import com.xatkit.core.platform.io.IntentRecognitionHelper;
import com.xatkit.core.recognition.IntentRecognitionProviderException;
import com.xatkit.core.server.RestHandler;
import com.xatkit.core.server.RestHandlerException;
import com.xatkit.execution.StateContext;
import com.xatkit.intent.RecognizedIntent;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import java.text.MessageFormat;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.xatkit.plugins.twilio.TwilioUtils;

import static java.util.Objects.isNull;


public class TwilioRestHandler extends RestHandler<JsonElement>{

    private TwilioEventProvider provider;

    private static JsonParser jsonParser = new JsonParser();
    
    GsonBuilder gsonMapBuilder = new GsonBuilder();
    
    public TwilioRestHandler(TwilioEventProvider provider) {
        super();
        this.provider = provider;
    }

    /**
     * {@inheritDoc}
     * <p>
     * this method get the message and the phone numbers in the parsed content from the twilio post request after a user sends a message and
     * creates or obtain the context associated to the user phone number and calls the getRecognizedIntent method 
     * this method is called after the parseContent method
     */
    @Override
    public JsonElement handleParsedContent(List<Header> headers, List<NameValuePair> params, JsonElement content)
            throws RestHandlerException {
                JsonObject contentObject = content.getAsJsonObject();     
                String message = contentObject.get("Body").getAsString();
                String from = contentObject.get("From").getAsString();
                String to = contentObject.get("To").getAsString();
                StateContext context =
                    provider.getRuntimePlatform().getXatkitBot().getOrCreateContext(from);
                RecognizedIntent recognizedIntent;
                    try {
                        recognizedIntent =
                                IntentRecognitionHelper.getRecognizedIntent(message, context,
                                        provider.getRuntimePlatform().getXatkitBot());
                        recognizedIntent.getPlatformData().put(TwilioUtils.TWILIO_FROM_NUMBER,from);
                        recognizedIntent.getPlatformData().put(TwilioUtils.TWILIO_TO_NUMBER,to);
                        provider.sendEventInstance(recognizedIntent, context);
                    } catch (IntentRecognitionProviderException e) {
                        throw new RuntimeException("An internal error occurred when computing"
                                + "the Twilio intent, see attached exception", e);
                    }
        return content;
    }

    /**
     * {@inheritDoc}
     * <p>
     * this method is used to accept the application/x-www-form-urlencoded content type from the twilio request
     */
    @Override
    public boolean acceptContentType(String contentType) {
        System.out.println("content type: "+contentType);
        return contentType.equals("application/x-www-form-urlencoded");
    }

    /**
     * {@inheritDoc}
     * <p>
     * this method parses the content obtained from the request in a x-www-form-urlencoded format and converts it to a JsonElement
     * this method is called before handleParsedContent
     */
    @Override
    protected JsonElement parseContent(Object content) {
        if (isNull(content)) {
            return null;
        }
        if (content instanceof String) {
            System.out.println(content);
            String decodeURL = decode((String)content);
            List<String> uriToList = Stream.of(decodeURL.split("&")).map(elem -> new String(elem))
                .collect(Collectors.toList());
            Map<String, String> uriToListToMap = new HashMap<>();
            for (String individualElement : uriToList) {
                uriToListToMap.put(individualElement.split("=")[0], individualElement.split("=")[1]);
            }
            Gson gsonObject = gsonMapBuilder.create();
            String uriToJSON = gsonObject.toJson(uriToListToMap);
            return jsonParser.parse(uriToJSON);
        }
        throw new XatkitException(MessageFormat.format("Cannot parse the provided content {0}, expected a {1}"
                + " found {4}", content, String.class.getName(), content.getClass().getName()));
    }

    public static String decode(String url) {
        try {
            String decodeURL = java.net.URLDecoder.decode(url, StandardCharsets.UTF_8.name());
            return decodeURL;
        } catch (UnsupportedEncodingException e) {
            return "Issue while decoding" + e.getMessage();
        }
    }
    
}
