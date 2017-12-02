package ca.ubc.ece.cpen221.mp5.tests;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.Test;

import ca.ece.ubc.cpen221.mp5.YelpDBServer;

public class ServerTests {

    @Test
    public void test00() {
	
//	Thread server = new Thread (new Runnable() {
//	    public void run() {
//		try {
//			YelpDBServer.main(new String[] {"3000"});
//		} catch (Exception e) {
//		    
//		}
//	    }
//	    
//	    
//	});
//	server.run();
//
//	String hostname = "localhost";
//	int port = 3000;
//
//	
//	
//	// lol doesnt work
//	try{
//	    Socket socket = new Socket(hostname, port);
//	    System.out.println("socket");
//
//	    PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
//	    System.out.println("pw");
//
//	    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//	    System.out.println("09090");
//
//	    BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
//
//	    String fromServer;
//	    String fromUser;
//
//	    System.out.println("tetestes");
//	    
//	    fromUser = stdIn.readLine();
//	    if (fromUser != null) {
//		System.out.println("Client: " + fromUser);
//		output.println(fromUser);
//	    }
//
//	    output.println("GETRESTAURANT BJKIoQa5N2T_oDlLVf467Q"); // second one from input file
//	    assertEquals("{\"open\": true, \"url\": \"http://www.yelp.com/biz/jasmine-thai-berkeley\", "
//		    + "\"longitude\": -122.2602981, \"neighborhoods\": [\"UC Campus Area\"], \"business_id\": "
//		    + "\"BJKIoQa5N2T_oDlLVf467Q\", \"name\": \"Jasmine Thai\", \"categories\": [\"Thai\", \"Restaurants\"], "
//		    + "\"state\": \"CA\", \"type\": \"business\", \"stars\": 3.0, \"city\": \"Berkeley\", \"full_address\": "
//		    + "\"1805 Euclid Ave\\nUC Campus Area\\nBerkeley, CA 94709\", \"review_count\": 52, \"photo_url\": "
//		    + "\"http://s3-media2.ak.yelpcdn.com/bphoto/ZwTUUb-6jkuzMDBBsUV6Eg/ms.jpg\", \"schools\": "
//		    + "[\"University of California at Berkeley\"], \"latitude\": 37.8759615, \"price\": 2}"
//		    ,input.readLine().trim());
//
//	    output.println("GETRESTAURANT not a restaurant");
//	    assertEquals("ERR: INVALID_BUSINESS_ID", input.readLine());
//
//	    //		
//	    //		fromServer = input.readLine();
//	    //		System.out.println("Read server");
//	    //		if(fromServer != null)
//	    //		    System.out.println("Server: " + fromServer);
//
//
//
//	    System.out.println("Exiting...");
//	    socket.close();
//	}
//	catch(Exception e){
//	    e.printStackTrace();
//	}

    }


}



