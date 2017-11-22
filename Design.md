Database Design
===

## Datatypes:

####Restaurant

#####Representation:
- All attributes in JSON
Rep invariant:
- is not null
- latitude, longitude will not change and are valid locations within the city  
- stars is between 0 and 5 inclusive
- price is an integer between 1 and 4 inclusive
- business_id will not change  
- review_count is greater or equal to 0
- schools will not change
Abstraction function:
- Represents a restaurants in the Yelp dataset
#####Methods:
- observers for all fields  
- observer for getting the average rating
- modifiers for name, address, categories

####Review

#####Representation:
- All attributes in JSON
Rep invariant:
- business_id, user_id, review_id will not change
- stars is an integer between 0 and 5 inclusive
- votes is an non negative integer
- text is a String between 0 and UPPERLIMIT characters
Abstraction function:
- represents a user review for an restaurants
#####Methods:
- observers for all fields
- modifiers for text, stars, votes  
- constructor


####User

#####Representation:
- All attributes in JSON
Rep invariant:
- average_starts is the sum of all review stars divided by number of reviews
- review_count and votes are non-negative integers
- user_id will not change
Abstraction function:
- Represents a user
#####Methods:
- observers for all fields
- modifiers for name, votes

####Categories
#####Representation:
- All attributes in JSON
Representation:
- is not null
- String representing a restaurant category
Abstraction function:
- represents a restaurants category
#####Methods:
- observers for all fields  

####Database
#####Representation:
- Set of all Restaurant, Review, and User
Rep invariant:
- Sets are composed of Restaurant, Review, and User objects
- Sets are not null
Abstraction function:
- Represents the Yelp database  
#####Methods:
- observers for all fields
- add new user, restaurant and rating
- get restaurant by rating, price, neighbourhood hood etc.
- get user by average rating, most helpful etc.
- get most and least helpful review,
