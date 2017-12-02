package ca.ece.ubc.cpen221.mp5;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

//Code taken from Fibonnacci server code example
public class YelpDBServer {
    public int DB_PORT;			// static? 

    private YelpDBWrapper db;

    private ServerSocket serverSocket;

    private final String illegalRequest = "ERR: ILLEGAL_REQUEST";
    
    public YelpDBServer(int port) throws IOException {
	System.out.println("Starting server");
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
	    System.out.println("we blocking?");
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
		socket.getOutputStream()), true);

	try {
	    for (String line = in.readLine(); line != null; line = in.readLine()) {
		System.err.println("request: " + line);
		try {

		    System.err.println("reply: ");
		    //TODO: finish adding queries
		    // get restaurant query	// TODO ghetto af 
		    if (line.contains("GETRESTAURANT")) {
			String[] split = line.split("GETRESTAURANT");
			String json = "";
			for (String token : split) {
			    json += token;
			}
			System.out.println(db.getRestaurant(json.trim()));
			out.println(db.getRestaurant(json.trim()));
		    }
		    // ADDUSER
		    else if (line.contains("ADDUSER")) {
			String[] split = line.split("ADDUSER");
			String json = "";
			for (String token : split) {
			    json += token;
			}
			out.println(db.addUser(json));
		    }

		    // ADDRESTAURANT
		    else if (line.contains("ADDRESTAURANT")) {
			String[] split = line.split("ADDRESTAURANT");
			String json = "";
			for (String token : split) {
			    json += token;
			}
			out.println(db.addRestaurant(json));
		    }

		    // ADDREVIEW 
		    else if (line.contains("ADDREVIEW")) {
			String[] split = line.split("ADDREVIEW");
			String json = "";
			for (String token : split) {
			    json += token;
			}
			out.println(db.addReview(json));
		    }

		    // QUERY
		    else if (line.contains("QUERY")) {
			String[] split = line.split("QUERY");
			String json = "";
			for (String token : split) {
			    json += token;
			}
			out.println(db.getQuery(json));
			//out.flush();
		    }

		    // not a valid input 
		    else {
			out.println(illegalRequest);
		    }


		} catch (Exception e) { //Query exception here
		    e.printStackTrace();
		}
	    }
	} finally {
	    out.close();
	    in.close();
	    System.out.println("client disconnected");
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
