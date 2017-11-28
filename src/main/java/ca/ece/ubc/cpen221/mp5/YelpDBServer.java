package ca.ece.ubc.cpen221.mp5;

import ca.ece.ubc.cpen221.mp5.database.YelpDatabase;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

//Code taken from Fibonnacci server code example
public class YelpDBServer {
    public int DB_PORT;
    private String restaurantJSON = "data/restaurants.JSON";
    private String reviewJSON = "data/reviews.json";
    private String userJSON = "data/users.json";
    private ServerSocket serverSocket;
    YelpDatabase database;

    public YelpDBServer(int port) throws IOException {
        final int DB_PORT = port;
        this.DB_PORT = DB_PORT;
        serverSocket = new ServerSocket(DB_PORT);

        this.database = new YelpDatabase(restaurantJSON, reviewJSON, userJSON);


    }

    //For initializing server
    public static void main(String[] args) {
        YelpDBServer server;

        int port = Integer.parseInt(args[0]);
        try {
           server = new YelpDBServer(port);
           server.serve();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void serve() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            try {
                handle(socket);
            } finally {
                socket.close();
            }
        }
    }
    private void handle(Socket socket) throws IOException {
        System.err.println("client connected");

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(
                socket.getOutputStream()));

        try {
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                System.err.println("request: " + line);
                try {
                    //TODO: Add queries here

                    System.err.println("reply: ");

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
}
