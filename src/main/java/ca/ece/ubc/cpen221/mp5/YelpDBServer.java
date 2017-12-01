package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.database.YelpDatabase;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

//Code taken from Fibonnacci server code example
public class YelpDBServer {
    public int DB_PORT;			// static? 
  
    YelpDBWrapper db;
    
    private ServerSocket serverSocket;

    public YelpDBServer(int port) throws IOException {
	final int DB_PORT = port;
	this.DB_PORT = DB_PORT;
	serverSocket = new ServerSocket(DB_PORT);

	this.db = YelpDBWrapper.getInstance();

    }

    /**
     * Run the server, listening for connections and handling them.
     * 
     * @throws IOException
     *             if the main server socket is broken
     */


    public void serve() throws IOException {
	while (true) {
	    // block until a client connects
	    final Socket socket = serverSocket.accept();			// TODO 
	    // create a new thread to handle that client
	    Thread handler = new Thread(new Runnable() {
		public void run() {
		    try {
			try {
			    handle(socket);
			} finally {
			    socket.close();
			}
		    } catch (IOException ioe) {
			// this exception wouldn't terminate serve(),
			// since we're now on a different thread, but
			// we still need to handle it
			ioe.printStackTrace();
		    }
		}
	    });
	    // start the thread
	    handler.start();
	}
    }

    //    public void serve() throws IOException {
    //	while (true) {
    //	    Socket socket = serverSocket.accept();
    //	    try {
    //		handle(socket);
    //	    } finally {
    //		socket.close();
    //	    }
    //	}
    //    }


    /**
     * Handle one client connection. Returns when client disconnects.
     * 
     * @param socket
     *            socket where client is connected
     * @throws IOException
     *             if connection encounters an error
     */
    private void handle(Socket socket) throws IOException {
	System.err.println("client connected");

	BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	PrintWriter out = new PrintWriter(new OutputStreamWriter(
		socket.getOutputStream()));

	try {
	    for (String line = in.readLine(); line != null; line = in.readLine()) {
		System.err.println("request: " + line);
		try {

		    
		    
		    System.err.println("reply: ");
		    //TODO: finish adding queries
		    // get restaurant query	// TODO ghetto af 
		    if (line.contains("GETRESTAURANT")) {
			String[] split = line.split("GETRESTAURANT");
			for (String token : split) {
			    if (!"".equals(token)) {
				out.print(db.getRestaurant(token));
			    }
			}
		    }
		    // ADDUSER
		    else if (line.contains("ADDUSER")) {
			String[] split = line.split("ADDUSER");
			String json = split.toString().trim();
			out.print(db.addUser(json));
		    }
		    
		    // ADDRESTAURANT
		    else if (line.contains("ADDRESTAURANT")) {
			String[] split = line.split("ADDRESTAURANT");
			String json = split.toString().trim();
			out.print(db.addRestaurant(json));
		    }
		   
		    // ADDREVIEW 
		    else if (line.contains("ADDREVIEW")) {
			String[] split = line.split("ADDREVIEW");
			String json = split.toString().trim();
			out.print(db.addReview(json));
		    }
		    else if (line.contains("QUERY")) {
			String[] split = line.split("QUERY");
			String json = split.toString().trim();
			out.print(db.getQuery(json));
		    }





		} catch (Exception e) { //Query exception here
		    e.printStackTrace();
		}
		out.flush();
	    }
	} finally {
	    out.close();
	    in.close();
	}

    }

    //For initializing server
    public static void main(String[] args) {
	YelpDBServer server;

	// initialize the port from command line
	int port = 3000;
	if (args.length > 0) {
	    port = Integer.parseInt(args[0]);
	}  
	try {
	    server = new YelpDBServer(port);
	    server.serve();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }







}
