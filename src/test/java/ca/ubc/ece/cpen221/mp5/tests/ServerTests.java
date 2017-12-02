package ca.ubc.ece.cpen221.mp5.tests;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//
//import org.junit.Test;
//
//import ca.ece.ubc.cpen221.mp5.YelpDBServer;

public class ServerTests {

    // gggggg cant seem to kill the server thread :( 
    
//    @SuppressWarnings("deprecation")
//    @Test
//    public void test00() {
//	
//	//boolean stop = false;
//	
//	Thread server = new Thread (new Runnable() {
//	    @Override
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
//	Thread client = new Thread (new Runnable() {
//	    
//	    public void run() {
//		String hostname = "localhost";
//		int port = 3000;
//
//		    System.out.println("ewfewefwefwefwefwefwefw");
//
//		
//		// lol doesnt work
//		try{
//		    Socket socket = new Socket(hostname, port);
//		    PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
//		    BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//
//		   
//
//		    output.println("GETRESTAURANT BJKIoQa5N2T_oDlLVf467Q"); // second one from input file
//		    assertTrue(input.readLine().length() > 30);
//
//		    output.println("GETRESTAURANT not a restaurant");
//		    assertEquals("ERR: INVALID_BUSINESS_ID", input.readLine());
//
//		   
//
//		    System.out.println("Exiting...");
//		    socket.close();
//		    return;
//		}
//		catch(Exception e){
//		    e.printStackTrace();
//		}
//		return;
//	    }
//	    
//	});
//	
//	client.run();
//	
//	while (client.isAlive()) {
//	    // wait
//	}
//	
//	server.stop();
//	
//	assertTrue(true);
//	return;
//	
//
//    }


}



