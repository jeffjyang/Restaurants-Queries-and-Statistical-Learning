package ca.ece.ubc.cpen221.mp5.database;

public interface Review {
    
    /**
     * Gets the user id of the user that wrote the review
     * 
     * @return the user id of the user that wrote the review
     */
    public String getUserId();
    
    /**
     * Gets the date of the review 
     * 
     * @return the date of the review
     */
    public String getDate();
    
    /**
     * Gets the text of the review
     * 
     * @return the text of the review
     */
    public String getText();
    
    /**
     * Updates the text of the review 
     * 
     * @param text
     * 		the new text of the review
     */
    public void updateText(String text);
    
    // TODO getRating() / getStars()?????????
}
