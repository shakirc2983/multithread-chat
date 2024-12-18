import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

public class Client {
    private final Socket clientSocket;
    private final BufferedWriter out;
    private final BufferedReader in;

    public Client(String address, int port) throws IOException {

        try {
            this.clientSocket = new Socket(address,port);
            this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void start() throws IOException {
        Scanner scanner = new Scanner(System.in);
        String message;

        sendMessage("Client has joined the Server");

        while (scanner.hasNextLine()) {
            message = scanner.nextLine();
            if ("exit".equalsIgnoreCase(message)) {
                sendMessage("Client is disconnecting...");
                break;
            }
            sendMessage(message);
        }

        out.close();
    }


    public void stop() throws IOException {
        clientSocket.close();
    }

    private void sendMessage(String message) throws IOException {
        out.write(message);
        out.newLine();
        out.flush();
    }


    public static void main(String args[]) throws IOException {
        Client client = new Client("127.0.0.1",8000);
        client.start();
        client.stop();

    }
}
