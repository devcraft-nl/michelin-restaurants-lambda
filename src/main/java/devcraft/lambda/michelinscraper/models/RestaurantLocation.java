package devcraft.lambda.michelinscraper.models;

public class RestaurantLocation {

    private final double latitude;
    private final double longitude;

    public RestaurantLocation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
