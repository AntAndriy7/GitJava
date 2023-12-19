package rmi;

import java.rmi.Naming;
import java.util.Scanner;

public class ClientRmiTask2 {
    final static String NAME = "RemoteDepartment";

    public static void main(String[] args) {
        try {
            TMI remote = (TMI) Naming.lookup("//localhost/" + NAME);
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
                    String serverResponse = remote.execute(choice);
                    System.out.println("\nВідповідь від сервера: " + serverResponse);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}