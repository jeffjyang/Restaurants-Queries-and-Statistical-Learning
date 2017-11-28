package ca.ece.ubc.cpen221.mp5;

import java.io.*;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by harryyao on 2017-11-28.
 */
public class YelpDBClient {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private static final String[] request_Array = new String[] {"GETRESTAURANT", "ADDUSER",
            "ADDRESTAURANT", "ADDREVIEW"};
    private static final Set<String> requests = new HashSet<>(request_Array);

    public YelpDBClient(String hostname, int port) throws IOException {
        socket = new Socket(hostname, port);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
    }

    public void sendRequest(String request) {
        out.print(request + "\n");
        out.flush();
    }

    public static void main(String[] args) {
        /*if (args)

        if (requests.contains())
*/
    }

}
