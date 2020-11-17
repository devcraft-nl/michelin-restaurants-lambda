package devcraft.lambda.michelinscraper;

public class Properties {

    private static String googleMapsApiKey;
    private static String queueName = "restaurantsQueue";

    static {
        googleMapsApiKey = System.getenv("GOOGLE_MAPS_API_KEY");
    }

    private Properties(){}

    public static String googleMapsApiKey() {
        return googleMapsApiKey;
    }

    public static String getQueueName() {
        return queueName;
    }
}
