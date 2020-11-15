package devcraft.lambda.michelinscraper.services;

import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.google.gson.Gson;
import devcraft.lambda.michelinscraper.Properties;
import devcraft.lambda.michelinscraper.models.Restaurant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

public class QueueService {

    private static final Logger logger = LoggerFactory.getLogger(MichelinConnector.class);
    private static final String QUEUE_NAME = Properties.getQueueName();
    private final Gson gson;
    private final AmazonSQS sqs;

    public QueueService() {
        gson = new Gson();
        sqs = AmazonSQSClientBuilder
                .defaultClient();
//                .setEndpointConfiguration(EnvironmentVariableCredentialsProvider);
        createQueue(sqs);
    }

    public void send(List<Restaurant> basicRestaurants) {
        String queueUrl = sqs.getQueueUrl(QUEUE_NAME).getQueueUrl();

        SendMessageBatchRequest send_batch_request = new SendMessageBatchRequest()
                .withQueueUrl(queueUrl)
                .withEntries(createEntries(basicRestaurants));
        sqs.sendMessageBatch(send_batch_request);
    }

    private List<SendMessageBatchRequestEntry> createEntries(List<Restaurant> basicRestaurants) {
        return basicRestaurants.stream()
                .map(this::toSendBatchRequestEntry)
                .collect(Collectors.toList());
    }

    private SendMessageBatchRequestEntry toSendBatchRequestEntry(Restaurant restaurant) {
        return new SendMessageBatchRequestEntry(restaurant.getName(), gson.toJson(restaurant));
    }

    private void createQueue(AmazonSQS sqs) {
        try {
            sqs.createQueue(QUEUE_NAME);
        } catch (AmazonSQSException e) {
            if (!e.getErrorCode().equals("QueueAlreadyExists")) {
                throw e;
            }
        }
    }
}
