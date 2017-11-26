package ca.ece.ubc.cpen221.mp5.database;

public interface User {

    /**
     * Gets the user id for the user 
     * @return the user id of the user 
     */
    public String getUserId();
    
    /**
     * Gets the name of the user
     * @return the name of the user
     */
    public String getName();
    
    /**
     * Sets the name of the user
     * @param name
     * 		the name of the user 
     */
    public void setName(String name);
    
    
}
