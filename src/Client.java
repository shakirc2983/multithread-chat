import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
  private final Socket clientSocket;
  private final BufferedWriter out;
  private final BufferedReader in;

  public Client(String address, int port) throws IOException {
    try {
      this.clientSocket = new Socket(address, port);
      this.out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
      this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void start(Scanner scanner) throws IOException {
    String message;

    sendMessage("Client has joined the Server");

    while (scanner.hasNextLine()) {
      System.out.print("Message: ");
      message = scanner.nextLine();
      if ("exit".equalsIgnoreCase(message)) {
        sendMessage("Client is disconnecting...");
        break;
      }
      sendMessage(message);
    }
    scanner.close();
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

  public static String userInput(Scanner scanner) {
    System.out.println("Enter your username:");
    String username = scanner.nextLine();
    return username;
  }

  public static void main(String args[]) throws IOException {
    Client client = new Client("127.0.0.1", 8000);
    Scanner scanner = new Scanner(System.in);
    String username = userInput(scanner);
    client.start(scanner);
    client.stop();
  }
}
