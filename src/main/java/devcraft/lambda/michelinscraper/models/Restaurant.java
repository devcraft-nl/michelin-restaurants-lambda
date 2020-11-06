package devcraft.lambda.michelinscraper.models;

public class Restaurant {

    private final String name;
    private final String addressString;
    private final String telephone;
    private final String website;

    private double latitude;
    private double longitude;

    public Restaurant(String name, String addressString, String telephone, String website){
        this.name = name;
        this.addressString = addressString;
        this.telephone = telephone;
        this.website = website;
    }

    public String getName() {
        return name;
    }

    public String getAddressString() {
        return addressString;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getWebsite() {
        return website;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLocation(RestaurantLocation restaurantLocation) {
        this.latitude = restaurantLocation.getLatitude();
        this.longitude = restaurantLocation.getLongitude();
    }

}
