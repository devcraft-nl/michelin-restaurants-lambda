package devcraft.lambda.michelinscraper;

import org.apache.commons.lang3.StringUtils;

public class Properties {

    private static String googleMapsApiKey;

    static {
        googleMapsApiKey = System.getenv("GOOGLE_MAPS_API_KEY");
    }

    public Properties(){

    }

    public static String googleMapsApiKey() {
        return googleMapsApiKey;
    }
}
