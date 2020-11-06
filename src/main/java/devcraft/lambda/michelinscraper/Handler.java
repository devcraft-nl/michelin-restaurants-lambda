package devcraft.lambda.michelinscraper;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import devcraft.lambda.michelinscraper.models.Restaurant;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// Handler value: devcraft.lambda.michelinscraper.Handler
public class Handler implements RequestHandler<Map<String, String>, String> {

    private final MichelinConnector michelinConnector = MichelinConnector.getInstance();

    @Override
    public String handleRequest(Map<String, String> event, Context context) {
        LambdaLogger logger = context.getLogger();
        String response = "200 OK";
        try {
            List<Restaurant> basicRestaurants = michelinConnector.getRestaurants(1);

        } catch (IOException e) {
            logger.log("ERROR: could not parse remote site: ");
            response = "500 Error";
        }
        return response;
    }

}