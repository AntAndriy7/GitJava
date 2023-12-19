package socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class ClientSocketTask2 {
    final static String NAME = "localhost";
    final static int PORT = 50055;

    public static void main(String[] args) {
        try (Socket socket = new Socket(NAME, PORT)) {
            DataInputStream inputStream = new DataInputStream(socket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

            Scanner scn = new Scanner(System.in);

            while (true) {
                System.out.println("\n\t\tГоловне меню:");
                System.out.println("1. Нагадування про найближчі події");
                System.out.println("2. Події за діапазоном дат");
                System.out.println("3. Події за категорією");
                System.out.println("4. Всі події відсортовані за датою");
                System.out.println("0. Вийти з програми");

                System.out.print("Виберіть дію: ");
                int choice = scn.nextInt();

                if (choice == 0) {
                    break;
                } else if (choice > 4) {
                    System.out.println("\nПомилка! Спробуйте ще раз");
                } else {
                    outputStream.writeInt(choice);
                    outputStream.flush();

                    String serverResponse = inputStream.readUTF();
                    System.out.println("\nВідповідь від сервера: " + serverResponse);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}