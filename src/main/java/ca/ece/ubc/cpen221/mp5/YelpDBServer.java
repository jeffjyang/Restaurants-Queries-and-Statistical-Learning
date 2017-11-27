package ca.ece.ubc.cpen221.mp5;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Code taken from Fibonnaci server code example
public class YelpDBServer {
    public static int DB_PORT;

    private ServerSocket serverSocket;

    public YelpDBServer(int port) throws IOException {
        this.DB_PORT = port;
        serverSocket = new ServerSocket(port);
    }

    //For initializing server
    public static void main(String[] args) {
        YelpDBServer server;

        int port = Integer.parseInt(args[0]);
        try {
           server = new YelpDBServer(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
/*
    public void serve() throws IOException {
        while (true) {
            Socket socket = serverSocket.accept();
            try {
                handle(socket);
            } catch (IOException ioe) {
                ioe.printStackTrace(); // but don't terminate serve()
            } finally {
                socket.close();
            }
        }
    }
*/
    private void handle(Socket socket) {

    }



}
