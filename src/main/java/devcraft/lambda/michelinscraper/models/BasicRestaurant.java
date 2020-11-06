package devcraft.lambda.michelinscraper.models;

public class BasicRestaurant {

    private final String name;
    private final String link;

    public BasicRestaurant(String name, String link) {
        this.name = name;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }
}
