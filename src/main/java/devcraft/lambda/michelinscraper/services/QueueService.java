package devcraft.lambda.michelinscraper.services;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.AmazonSQSException;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import com.amazonaws.services.sqs.model.SendMessageBatchRequestEntry;
import com.google.gson.Gson;
import devcraft.lambda.michelinscraper.Properties;
import devcraft.lambda.michelinscraper.models.Restaurant;
import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class QueueService {

    private static final String QUEUE_NAME = Properties.getQueueName();
    private final Gson gson;
    private final AmazonSQS sqs;

    public QueueService(AmazonSQS sqs) {
        gson = new Gson();
        this.sqs = sqs;
        createQueue(sqs);
    }

    public void send(List<Restaurant> basicRestaurants) {
        String queueUrl = sqs.getQueueUrl(QUEUE_NAME).getQueueUrl();

        List<List<Restaurant>> batched = ListUtils.partition(basicRestaurants, 10);

        batched.forEach(restaurantList -> {
            SendMessageBatchRequest batchRequest = new SendMessageBatchRequest()
                    .withQueueUrl(queueUrl)
                    .withEntries(createEntries(basicRestaurants));
            sqs.sendMessageBatch(batchRequest);
        });
    }

    private List<SendMessageBatchRequestEntry> createEntries(List<Restaurant> basicRestaurants) {
        return basicRestaurants.stream()
                .map(this::toSendBatchRequestEntry)
                .collect(toList());
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
