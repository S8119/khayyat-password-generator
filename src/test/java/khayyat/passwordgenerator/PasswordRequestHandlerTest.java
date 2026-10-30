package khayyat.passwordgenerator;

import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PasswordRequestHandlerTest
{
    @Test
    void handleRequestReturnsGeneratedPassword()
    {
        // given
        Gson gson = new Gson();
        PasswordGenerator generator = new PasswordGenerator(new Random(8));
        PasswordRequest request = new PasswordRequest(
                12, true, true, true, "!@#".toCharArray());

        APIGatewayV2HTTPEvent event = new APIGatewayV2HTTPEvent();
        APIGatewayV2HTTPEvent.RequestContext requestContext = new APIGatewayV2HTTPEvent.RequestContext();
        APIGatewayV2HTTPEvent.RequestContext.Http http = new APIGatewayV2HTTPEvent.RequestContext.Http();
        http.setMethod("POST");
        requestContext.setHttp(http);
        event.setRequestContext(requestContext);
        event.setBody(gson.toJson(request));

        PasswordRequestHandler handler = new PasswordRequestHandler(gson, generator);

        // when
        APIGatewayV2HTTPResponse apiResponse = handler.handleRequest(event, null);

        // then
        assertEquals(200, apiResponse.getStatusCode());
        PasswordResponse response = gson.fromJson(apiResponse.getBody(), PasswordResponse.class);
        assertNotNull(response.password());
        assertEquals(12, response.password().length());
    }

    @Test
    void handleRequestReturns500WhenBodyIsInvalid()
    {
        // given
        APIGatewayV2HTTPEvent event = new APIGatewayV2HTTPEvent();
        APIGatewayV2HTTPEvent.RequestContext requestContext = new APIGatewayV2HTTPEvent.RequestContext();
        APIGatewayV2HTTPEvent.RequestContext.Http http = new APIGatewayV2HTTPEvent.RequestContext.Http();
        http.setMethod("POST");
        requestContext.setHttp(http);
        event.setRequestContext(requestContext);
        event.setBody("not-json");

        PasswordRequestHandler handler = new PasswordRequestHandler();

        // when
        APIGatewayV2HTTPResponse apiResponse = handler.handleRequest(event, null);

        // then
        assertEquals(500, apiResponse.getStatusCode());
        assertTrue(apiResponse.getBody().contains("Exception")
                || apiResponse.getBody().contains("Json"));
    }

    @Test
    void gsonRoundTripKeepsAllowedSymbols()
    {
        // given
        Gson gson = new Gson();
        PasswordRequest request = new PasswordRequest(
                16, true, true, true, "!@#$%^&".toCharArray());

        // when
        String json = gson.toJson(request);
        PasswordRequest parsed = gson.fromJson(json, PasswordRequest.class);

        // then
        assertEquals("!@#$%^&", new String(parsed.allowedSymbols()));
        assertTrue(json.contains("allowedSymbols"), json);
    }
}
