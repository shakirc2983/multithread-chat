import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
  private final ServerSocket serverSocket;

  public Server(int port) throws IOException {
    try {
      this.serverSocket = new ServerSocket(port);
      System.out.println("Server has started");
      while (true) {
        Socket clientSocket = serverSocket.accept();
        new Thread(new ClientHandler(clientSocket)).start();
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

    public ClientHandler(Socket clientSocket) {
      this.clientSocket = clientSocket;

    }

    @Override
    public void run() {
      try {
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String message;
        String username = in.readLine();

        out.println("Welcome to the Server, " + username);
        out.flush();

        while ((message = in.readLine()) != null) {
          System.out.println(username + ": " + message);
        }

      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        try {
          if (clientSocket != null && !clientSocket.isClosed()) {
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
