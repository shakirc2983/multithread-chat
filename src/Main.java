import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner_obj = new Scanner(System.in);
        System.out.println("Enter your username: ");
        String username = scanner_obj.nextLine();
        System.out.println("Your username is " + username);
    }
}