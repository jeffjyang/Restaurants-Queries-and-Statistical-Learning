package ca.ece.ubc.cpen221.mp5.database;

public class Coordinate {
	private double latitude;
	private double longitude;
	
	public Coordinate (double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public Coordinate (YelpRestaurant restaurant) {
		this.latitude = restaurant.getLatitude();
		this.longitude = restaurant.getLongitude();
	}
	
	public double getLat() {
		return this.latitude;
	}
	
	public double getLong() {
		return this.longitude;
	}
}
