package devcraft.lambda.michelinscraper.services;

import devcraft.lambda.michelinscraper.models.BasicRestaurant;
import devcraft.lambda.michelinscraper.models.Restaurant;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

class RestaurantsParserTest {

    private final RestaurantsParser restaurantsParser = RestaurantsParser.getInstance();

    @Test
    void testMichelinSearch() throws IOException, URISyntaxException {
        String html = htmlToString("/michelin-search.html");
        Document doc = Jsoup.parse(html);

        List<BasicRestaurant> restaurantsList = restaurantsParser.parseToBasicRestaurantList(doc);
        assertThat(restaurantsList).hasSize(20)
                .extracting(BasicRestaurant::getName, BasicRestaurant::getLink)
                .contains(
                        tuple("Canon", "/en/california/us-sacramento/restaurant/canon"),
                        tuple("Trailblazer Tavern", "/en/california/san-francisco/restaurant/trailblazer-tavern"),
                        tuple("La Calenda", "/en/california/yountville/restaurant/la-calenda"),
                        tuple("La Super-Rica Taqueria", "/en/california/santa-barbara/restaurant/la-super-rica-taqueria"),
                        tuple("Stockhome", "/en/california/petaluma/restaurant/stockhome"),
                        tuple("J. Zhou", "/en/california/tustin/restaurant/j-zhou"),
                        tuple("Shin Sushi", "/en/california/encino/restaurant/shin-sushi"),
                        tuple("Maccheroni Republic", "/en/california/us-los-angeles/restaurant/maccheroni-republic"),
                        tuple("Dama", "/en/california/us-los-angeles/restaurant/dama"),
                        tuple("Rocio's Mexican Kitchen", "/en/california/bell-gardens/restaurant/rocio-s-mexican-kitchen"),
                        tuple("Mariscos Jalisco", "/en/california/pomona/restaurant/mariscos-jalisco"),
                        tuple("Longo Seafood", "/en/california/rosemead/restaurant/longo-seafood"),
                        tuple("Hayato", "/en/california/us-los-angeles/restaurant/hayato"),
                        tuple("FOB Kitchen", "/en/california/oakland/restaurant/fob-kitchen"),
                        tuple("Five Happiness", "/en/california/san-francisco/restaurant/five-happiness"),
                        tuple("Ramenwell", "/en/california/san-francisco/restaurant/ramenwell"),
                        tuple("Pig in a Pickle", "/en/california/corte-madera/restaurant/pig-in-a-pickle"),
                        tuple("Harborview", "/en/california/san-francisco/restaurant/harborview"),
                        tuple("Sushi Enya", "/en/california/us-los-angeles/restaurant/sushi-enya"),
                        tuple("Okiboru Ramen", "/en/california/us-los-angeles/restaurant/okiboru-ramen")
                );
    }

    @Test
    void testRestaurantDetailParsing() throws IOException, URISyntaxException {
        String html = htmlToString("/restaurant-detail.html");
        Document doc = Jsoup.parse(html);
        BasicRestaurant basicRestaurant = new BasicRestaurant("De nieuwe winkel","/en/gelderland/nijmegen/restaurant/de-nieuwe-winkel");

        Restaurant restaurant = restaurantsParser.enrichBasicRestaurant(basicRestaurant, doc);
        assertThat(restaurant)
                .extracting(Restaurant::getName, Restaurant::getAddressString, Restaurant::getTelephone, Restaurant::getWebsite)
                .contains("De nieuwe winkel",
                        "Gebroeders Van Limburgplein, Nijmegen, 6511 BW, Netherlands",
                        "+31 24 322 5093",
                        "www.denieuwewinkel.com");
    }

    private String htmlToString(String file) throws URISyntaxException, IOException {
        URL url = RestaurantsParserTest.class.getResource(file);
        Path resPath = Paths.get(url.toURI());
        return Files.readString(resPath);
    }

}