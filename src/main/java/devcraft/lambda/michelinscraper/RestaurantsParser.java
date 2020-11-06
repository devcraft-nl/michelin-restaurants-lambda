package devcraft.lambda.michelinscraper;

import devcraft.lambda.michelinscraper.models.BasicRestaurant;
import devcraft.lambda.michelinscraper.models.Restaurant;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;
import java.util.stream.Collectors;

public class RestaurantsParser {

    private static RestaurantsParser instance;

    private RestaurantsParser() {
    }

    public static RestaurantsParser getInstance() {
        if (instance == null) {
            instance = new RestaurantsParser();
        }
        return instance;
    }

    public List<BasicRestaurant> parseToBasicRestaurantList(Document doc) {
        Elements cardMenuElements = doc.select("div.card__menu");
        return cardMenuElements.stream()
                .map(this::mapToBasicRestaurant)
                .collect(Collectors.toList());
    }

    private BasicRestaurant mapToBasicRestaurant(Element element) {
        Elements titleElements = element.select("h5").select("a");
        String name = titleElements.html();
        String link = titleElements.attr("href");
        return new BasicRestaurant(name, link);
    }

    public Restaurant enrichBasicRestaurant(BasicRestaurant basicRestaurant, Document doc) {
        Elements headingList = doc.select("ul.restaurant-details__heading--list");
        String addressString = headingList.stream()
                .filter(element -> elementContains(element, "i.fa-map-marker-alt"))
                .map(element -> element.select("li").first().text())
                .findFirst()
                .orElse(StringUtils.EMPTY);
        Elements dflex = doc.select("div.d-flex");
        String telephone = dflex.stream()
                .filter(element -> elementContains(element, "span.fa-phone"))
                .map(element -> element.select("a.link").attr("href"))
                .map(link -> link.replace("tel:", ""))
                .findFirst()
                .orElse(StringUtils.EMPTY);
        String website = dflex.stream()
                .filter(element -> elementContains(element, "span.fa-browser"))
                .map(Element::text)
                .findFirst()
                .orElse(StringUtils.EMPTY);

        return new Restaurant(basicRestaurant.getName(), addressString, telephone, website);
    }

    private boolean elementContains(Element element, String cssSelector){
        return element.select(cssSelector).size() > 0;
    }

}
