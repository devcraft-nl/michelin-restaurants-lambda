package devcraft.lambda.michelinscraper;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import devcraft.lambda.michelinscraper.models.Restaurant;
import devcraft.lambda.michelinscraper.services.MichelinConnector;
import devcraft.lambda.michelinscraper.services.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

// Handler value: devcraft.lambda.michelinscraper.Handler
public class Handler implements RequestHandler<Map<String, String>, String> {

    private static final Logger logger = LoggerFactory.getLogger(Handler.class);
    private final MichelinConnector michelinConnector = MichelinConnector.getInstance();
    private final QueueService queueService = new QueueService();

    @Override
    public String handleRequest(Map<String, String> event, Context context) {
        String response = "200 OK";
        try {
            List<Restaurant> basicRestaurants = michelinConnector.getRestaurants(1);
            queueService.send(basicRestaurants);
        } catch (IOException e) {
            logger.error("Error occured during lambda execution: ", e);
            response = "500 Error";
        }
        return response;
    }

}