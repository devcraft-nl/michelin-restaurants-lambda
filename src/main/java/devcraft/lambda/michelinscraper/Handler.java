package devcraft.lambda.michelinscraper;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import devcraft.lambda.michelinscraper.models.Restaurant;
import devcraft.lambda.michelinscraper.services.MichelinConnector;
import devcraft.lambda.michelinscraper.services.QueueService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

// Handler value: devcraft.lambda.michelinscraper.Handler
public class Handler implements RequestHandler<Map<String, String>, String> {

    private final MichelinConnector michelinConnector = MichelinConnector.getInstance();
    private final QueueService queueService = new QueueService(AmazonSQSClientBuilder
            .defaultClient());

    @Override
    public String handleRequest(Map<String, String> event, Context context) {
        List<Restaurant> basicRestaurants = michelinConnector.getRestaurants(1);
        queueService.send(basicRestaurants);
        return "Function execution finished";
    }

}