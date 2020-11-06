package devcraft.lambda.michelinscraper;

import com.google.maps.*;
import com.google.maps.errors.ApiException;
import com.google.maps.errors.OverDailyLimitException;
import com.google.maps.model.ComponentFilter;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.PlacesSearchResponse;
import devcraft.lambda.michelinscraper.models.Restaurant;
import devcraft.lambda.michelinscraper.models.RestaurantLocation;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GeoCoder {

    /** The LOG. */
    private static final Logger LOG = LoggerFactory.getLogger(GeoCoder.class);

    private final GeoApiContext context;

    public GeoCoder() {
        context = new GeoApiContext.Builder()
                .queryRateLimit(50)
                .maxRetries(3)
                .retryTimeout(1, TimeUnit.SECONDS)
                .apiKey(Properties.googleMapsApiKey())
                .build();
    }

    public Restaurant search(Restaurant restaurant){
        GeocodingApiRequest request = GeocodingApi.newRequest(context)
                .address(restaurant.getAddressString());
        try {
            GeocodingResult[] results = request.await();
            if (ArrayUtils.isNotEmpty(results)) {
                RestaurantLocation location = new RestaurantLocation(results[0].geometry.location.lat, results[0].geometry.location.lng);
                restaurant.setLocation(location);
            }

        } catch (ApiException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
        return restaurant;
    }

    static class GeocodingCallback implements PendingResult.Callback<GeocodingResult[]> {

        private final Restaurant restaurant;
        private RestaurantLocation location;

        public GeocodingCallback(Restaurant restaurant){
            this.restaurant = restaurant;
        }

        @Override
        public void onResult(GeocodingResult[] results)
        {
            if (ArrayUtils.isNotEmpty(results)) {
                location = new RestaurantLocation(results[0].geometry.location.lat, results[0].geometry.location.lng);
                restaurant.setLocation(location);
            }
        }

        @Override
        public void onFailure(Throwable e)
        {
            if (e instanceof OverDailyLimitException) {
              LOG.error("Daily Places API request limit exceeded for: {}", restaurant.getAddressString());
            }
        }
    }

}
