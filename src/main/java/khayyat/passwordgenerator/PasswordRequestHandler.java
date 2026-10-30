package khayyat.passwordgenerator;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import com.google.gson.Gson;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * AWS Lambda entry point. Function URLs wrap the HTTP call in
 * {@link APIGatewayV2HTTPEvent}; the JSON body is a {@link PasswordRequest}.
 */
public class PasswordRequestHandler
        implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse>
{
    private final Gson gson;
    private final PasswordGenerator passwordGenerator;

    public PasswordRequestHandler()
    {
        this(new Gson(), new PasswordGenerator());
    }

    public PasswordRequestHandler(Gson gson, PasswordGenerator passwordGenerator)
    {
        this.gson = gson;
        this.passwordGenerator = passwordGenerator;
    }

    @Override
    public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context context)
    {
        try
        {
            String body = event.getBody();
            PasswordRequest request = gson.fromJson(body, PasswordRequest.class);

            PasswordResponse response = passwordGenerator.generate(request);

            String responseJson = gson.toJson(response);
            APIGatewayV2HTTPResponse apiResponse = new APIGatewayV2HTTPResponse();
            apiResponse.setStatusCode(200);
            apiResponse.setBody(responseJson);
            return apiResponse;
        } catch (Exception e)
        {
            e.printStackTrace();
            return toResponseEvent(e);
        }
    }

    private APIGatewayV2HTTPResponse toResponseEvent(Exception e)
    {
        APIGatewayV2HTTPResponse apiResponse = new APIGatewayV2HTTPResponse();
        apiResponse.setStatusCode(500);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        apiResponse.setBody(stringWriter.toString());
        return apiResponse;
    }
}
