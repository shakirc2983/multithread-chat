import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private final ServerSocket serverSocket;
    private final Socket clientSocket;
    private final PrintWriter out;
    private final BufferedReader in;

    public Server(int port) throws IOException {
        try {
            this.serverSocket = new ServerSocket(port);
            this.clientSocket = serverSocket.accept();
            this.out = new PrintWriter(clientSocket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() throws IOException {
        System.out.println("Server socket connected with client socket");

        String message;
        System.out.println(message = in.readLine());
        while ((message = in.readLine()) != null) {
            System.out.println("Client: " + message);
        }

        in.close();
    }

    public void stop() throws IOException {
        this.serverSocket.close();
    }

    public static void main(String args[]) throws IOException {
        Server server = new Server(8000);
        server.start();
        server.stop();
    }
}
