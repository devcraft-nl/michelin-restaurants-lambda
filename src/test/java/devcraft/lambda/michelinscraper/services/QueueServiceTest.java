package devcraft.lambda.michelinscraper.services;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.GetQueueUrlResult;
import com.amazonaws.services.sqs.model.SendMessageBatchRequest;
import devcraft.lambda.michelinscraper.models.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class QueueServiceTest {

    QueueService queueService;

    @Captor
    ArgumentCaptor<SendMessageBatchRequest> requestCaptor;

    @Mock
    AmazonSQS amazonSQS;
    @Mock
    GetQueueUrlResult queueUrlResult;

    @Test
    void queueBatching_max10() {
        //given
        Mockito.lenient().when(amazonSQS.getQueueUrl(anyString()))
                .thenReturn(queueUrlResult);
        when(queueUrlResult.getQueueUrl()).thenReturn("dummyQueue");

        //when
        queueService = new QueueService(amazonSQS);
        queueService.send(createRequestList());
        //then
        verify(amazonSQS, times(2)).sendMessageBatch(requestCaptor.capture());
        assertThat(requestCaptor.getAllValues())
                .hasSize(2);
    }

    List<Restaurant> createRequestList() {
        List<Restaurant> restaurants = new ArrayList<Restaurant>();
        for (int i = 0; i < 20; i++) {
            restaurants.add(new Restaurant("name", "address", "012345", "http://website.com"));
        }
        return restaurants;
    }
}