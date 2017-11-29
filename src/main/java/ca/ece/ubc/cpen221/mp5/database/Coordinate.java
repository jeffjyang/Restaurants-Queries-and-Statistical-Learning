package ca.ece.ubc.cpen221.mp5.database;

public class Coordinate {
    private double latitude;
    private double longitude;

   // double MAXERR = 10e-1;

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

//    @Override
//    public boolean equals(Object obj) {
//	if (obj instanceof Coordinate) {
//	    Coordinate other = (Coordinate) obj;
//	   
//	    if (Math.abs(other.getLat() - latitude) > MAXERR) {
//		return false;
//	    }
//	    if (Math.abs(other.getLong() - longitude) > MAXERR) {
//		return false;
//	    }
//	    
//	    return true;
//	}
//	return false;
//    }


}
