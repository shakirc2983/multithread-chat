import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.List;

public class Server {
  private final ServerSocket serverSocket;
  private List<PrintWriter> printWriters = new CopyOnWriteArrayList<>();

  public Server(int port) throws IOException {
    try {
      this.serverSocket = new ServerSocket(port);
      System.out.println("Server has started");
      while (true) {
        Socket clientSocket = serverSocket.accept();
        ClientHandler handler = new ClientHandler(this, clientSocket);
        new Thread(handler).start();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public void stop() throws IOException {
    this.serverSocket.close();
  }

  public static void main(String args[]) throws IOException {
    Server server = new Server(8000);
    server.stop();
  }

  private static class ClientHandler implements Runnable {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private Server server;

    public ClientHandler(Server server, Socket clientSocket) {
      this.server = server;
      this.clientSocket = clientSocket;

    }

    public void sendMessageToClients(String fullMessage) {
      for (PrintWriter writer : this.server.printWriters) {
        PrintWriter sender = out;
        if (writer != sender) {
          writer.println(fullMessage);
        }
      }
    }

    @Override
    public void run() {
      try {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        server.printWriters.add(out);
        String message;
        String username = in.readLine();

        sendMessageToClients("Welcome to the Server, " + username);

        while ((message = in.readLine()) != null) {
          String fullMessage = username + ": " + message;
          System.out.println(fullMessage);
          sendMessageToClients(fullMessage);
        }

      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          if (clientSocket != null && !clientSocket.isClosed()) {
            server.printWriters.remove(out);
            out.println("Client has left the server");
            out.close();
            in.close();
            clientSocket.close();
          }
        } catch (IOException e) {
          System.err.println("Error when closing client socket" + e.getMessage());
        }
      }
    }
  }
}
