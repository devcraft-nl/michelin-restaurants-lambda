package devcraft.lambda.michelinscraper.services;

import devcraft.lambda.michelinscraper.models.Restaurant;
import devcraft.lambda.michelinscraper.services.MichelinConnector;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MichelinConnectorIT {

    private final MichelinConnector michelinConnector = MichelinConnector.getInstance();

    @Test
    void testMichelinSearch() throws IOException {
        List<Restaurant> restaurants = michelinConnector.getRestaurants(1);
        assertThat(restaurants)
                .hasSize(20)
                .extracting(Restaurant::getLatitude, Restaurant::getLongitude)
                .isNotNull();
    }
}