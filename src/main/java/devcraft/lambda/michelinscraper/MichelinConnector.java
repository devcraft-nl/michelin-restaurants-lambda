package devcraft.lambda.michelinscraper;

import devcraft.lambda.michelinscraper.models.BasicRestaurant;
import devcraft.lambda.michelinscraper.models.Restaurant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class MichelinConnector {

    private static final String MICHELIN_URL = "https://guide.michelin.com";
    private static final String RESTAURANTS_PATH = "/en/restaurants/page/";

    private final RestaurantsParser restaurantsParser = RestaurantsParser.getInstance();
    private final GeoCoder geoCoder = new GeoCoder();

    private static MichelinConnector instance;

    private MichelinConnector(){}

    public static MichelinConnector getInstance(){
        if(instance == null){
            instance = new MichelinConnector();
        }
        return instance;
    }

    public List<Restaurant> getRestaurants(int page) throws IOException {
        Document doc = Jsoup.connect(MICHELIN_URL + RESTAURANTS_PATH + page).get();
        List<BasicRestaurant> basicRestaurants = restaurantsParser.parseToBasicRestaurantList(doc);
        return basicRestaurants.stream()
                    .map(RestaurantDetailConnection::new)
                    .filter(RestaurantDetailConnection::isDocumentFetched)
                    .map(conn -> restaurantsParser.enrichBasicRestaurant(conn.getBasicRestaurant(), conn.getDocument()))
//                    .map(geoCoder::search)
                    .collect(Collectors.toList());
    }

    private static class RestaurantDetailConnection {

        private final BasicRestaurant basicRestaurant;
        private Document document = null;

        public RestaurantDetailConnection(BasicRestaurant basicRestaurant) {
            this.basicRestaurant = basicRestaurant;
            try {
                document = Jsoup.connect(MICHELIN_URL + basicRestaurant.getLink()).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public boolean isDocumentFetched() {
            return document != null;
        }

        public BasicRestaurant getBasicRestaurant() {
            return basicRestaurant;
        }

        public Document getDocument() {
            return document;
        }
    }
}
