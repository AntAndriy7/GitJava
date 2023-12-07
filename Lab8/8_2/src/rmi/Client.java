package rmi;

import java.rmi.Naming;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            String serviceName = "RemoteDepartment";
            RemoteDepartment remoteDepartment = (RemoteDepartment) Naming.lookup("//localhost/" + serviceName);

            Scanner scn = new Scanner(System.in);

            while (true) {
                System.out.println("\n\t\tГоловне меню:");
                System.out.println("1. Додавання нового підрозділу");
                System.out.println("2. Видалення підрозділу");
                System.out.println("3. Прийом на роботу співробітника в заданий підрозділ");
                System.out.println("4. Звільнення співробітника");
                System.out.println("5. Редагування особистих даних співробітника");
                System.out.println("6. Переведення співробітника з одного підрозділу в інший");
                System.out.println("7. Підрахунок кількості співробітників в підрозділі");
                System.out.println("8. Отримання списку співробітників для заданого підрозділу");
                System.out.println("9. Отримання списку підрозділів");
                System.out.println("0. Вийти з програми");

                System.out.print("Виберіть дію: ");
                int choice = scn.nextInt();

                if (choice == 0) {
                    break;
                }

                String serverResponse = remoteDepartment.execute(choice);
                System.out.println("Відповідь від сервера: " + serverResponse);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}