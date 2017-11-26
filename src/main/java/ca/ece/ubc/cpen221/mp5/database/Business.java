package ca.ece.ubc.cpen221.mp5.database;

public interface Business {

    
    /**
     * Gets the name of the business
     * 
     * @return the name of the business
     */
    public String getName();
    
    /**
     * Sets the name of the business
     * 
     * @param name
     * 		a string representing the name of the business
     */
    public void setName(String name);
    
    /**
     * Gets the state that the business is in
     * 
     * @return the state that the business is in
     */
    public String getState();
    
    /**
     * Gets the city that the business is in
     * 
     * @return the city that the business is in
     */
    public String getCity();
    
    /**
     * Gets the full street address of the business
     * 
     * @return the full street address of the business
     */
    public String getFullAddress();
    
    /**
     * Gets the latitude of the business
     * 
     * @return the latitude of the business
     */
    public double getLatitude(); 

    /**
     * Gets the longitude of the business 
     * 
     * @return the longitude of the business
     */
    public double getLongitude();
    
    
}
