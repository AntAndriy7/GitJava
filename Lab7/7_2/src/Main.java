import database.DatabaseConnection;
import database.DatabaseOperations;
import department.Department;
import model.Company;
import model.Employee;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Department department = new Department();

        DatabaseConnection.getConnection();

        Scanner scn = new Scanner(System.in);

        while (true) {
            List<Company> companies = DatabaseOperations.getCompanies();

            System.out.println("\n\t\tГоловне меню:");
            System.out.println("1. Додати нові дані");
            System.out.println("2. Вивести дані з бази данних");
            System.out.println("3. Вивести всіх робітників, що належать одній компанії");
            System.out.println("4. Оновити дані");
            System.out.println("5. Видалити дані");
            System.out.println("0. Вийти з програми");

            System.out.print("Виберіть дію: ");
            int choice = scn.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\n1) Додати дані компанії");
                    System.out.println("2) Додати дані співробітника");
                    System.out.print("Виберіть дію: ");
                    int choice2 = scn.nextInt();
                    switch (choice2) {
                        case 1:
                            System.out.print("\nВведіть назву компанії: ");
                            String name = scn.next();
                            System.out.print("Введіть рік створення компанії: ");
                            int year = scn.nextInt();
                            System.out.print("Введіть ID компанії: ");
                            int id = scn.nextInt();

                            Company com = new Company(id, name, year);
                            department.addCom(com);
                            break;
                        case 2:
                            System.out.print("\nВведіть ім'я та прізвище співробітника: ");
                            String fullName = scn.next();
                            System.out.print("Введіть вік співробітника: ");
                            int age = scn.nextInt();
                            System.out.print("Введіть заробітну плату співробітника: ");
                            double salary = scn.nextDouble();
                            System.out.print("Введіть ID компанії в якій працює співробітник: ");
                            int idcom = scn.nextInt();
                            System.out.print("Введіть ID співробітника: ");
                            int idemp = scn.nextInt();

                            if (!companies.isEmpty()) {
                                Company firstCompany = companies.get(idcom - 1);
                                Employee newEmployee = new Employee(idemp, fullName, age, salary);
                                List<Employee> newEmp = new ArrayList<>();
                                newEmp.add(newEmployee);
                                firstCompany.setEmployees(newEmp);
                                department.addEmp(newEmployee, firstCompany.getID());
                            }

                            break;
                        default:
                            System.out.println("Невірний вибір опції. Будь ласка, спробуйте ще раз.");
                            break;
                    }
                    break;
                case 2:
                    department.displayDepartment();
                    break;
                case 3:
                    System.out.print("Введіть ID компанії: ");
                    int idThat = scn.nextInt();
                    department.displayOne(idThat);
                    break;
                case 4:
                    department.displayDepartment();
                    System.out.println("\n1) Оновити дані компанії");
                    System.out.println("2) Оновити дані співробітника");
                    System.out.print("Виберіть дію: ");
                    int choice3 = scn.nextInt();
                    switch (choice3) {
                        case 1:
                            System.out.print("\nВведіть ID компанії, дані якогої потрібно оновити: ");
                            int idEdtC = scn.nextInt();
                            System.out.print("Введіть назву: ");
                            String neweName = scn.next();
                            System.out.print("Введіть рік зіснування: ");
                            int newYear = scn.nextInt();
                            department.edtCom(idEdtC, neweName, newYear);
                            break;
                        case 2:
                            System.out.print("\nВведіть ID співробітника, дані якого потрібно оновити: ");
                            int idEdtE = scn.nextInt();
                            System.out.print("Введіть ім'я та прізвище: ");
                            String newName = scn.next();
                            System.out.print("Введіть вік: ");
                            int newAge = scn.nextInt();
                            System.out.print("Введіть заробітну плату: ");
                            double newSalary = scn.nextDouble();
                            department.edtEmp(idEdtE, newName, newAge, newSalary);
                            break;
                        default:
                            System.out.println("Невірний вибір опції. Будь ласка, спробуйте ще раз.");
                            break;
                    }
                    break;
                case 5:
                    department.displayDepartment();
                    int idDel;
                    System.out.println("\n1) Видалити дані компанію");
                    System.out.println("2) Видалити дані співробітника");
                    System.out.print("Виберіть дію: ");
                    int choice4 = scn.nextInt();
                    switch (choice4) {
                        case 1:
                            System.out.print("Введіть ID: ");
                            idDel = scn.nextInt();
                            department.delEmp(idDel, true);
                            break;
                        case 2:
                            System.out.print("Введіть ID: ");
                            idDel = scn.nextInt();
                            department.delEmp(idDel, false);
                            break;
                        default:
                            System.out.println("Невірний вибір опції. Будь ласка, спробуйте ще раз.");
                            break;
                    }
                    break;
                case 0:
                    System.out.println("Подарунок готовий! Вихід з програми.");
                    DatabaseConnection.closeConnection();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Невірний вибір опції. Будь ласка, спробуйте ще раз.");
            }
        }
    }
}



