package socket;

import database.DatabaseConnection;
import database.DatabaseOperations;
import department.Department;
import model.Company;
import model.Employee;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        final int port = 50055;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Сервер запущено. Очікування з'єднань...");

            Socket clientSocket = serverSocket.accept();
            System.out.println("Клієнт підключено.");

            DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream());

            while (true) {
                try {
                    int choice = inputStream.readInt();

                    Department department = new Department();

                    DatabaseConnection.getConnection();

                    List<Company> companies = DatabaseOperations.getCompanies();

                    switch (choice) {
                        case 1:
                            department.addCom();
                            //result = "Новий підрозділ успішно додано!";
                            break;
                        case 2:
                            department.delDep(3, true);
                            //result = "Підрозділ успішно видалено!";
                            break;
                        case 3:
                            if (!companies.isEmpty()) {
                                Company firstCompany = companies.get(2);
                                Employee newEmployee = new Employee(7, "Jerry", 20, 24000);
                                List<Employee> newEmp = new ArrayList<>();
                                newEmp.add(newEmployee);
                                firstCompany.setEmployees(newEmp);
                                department.addEmp(newEmployee, firstCompany.getID());
                            }
                            //result = "";
                            break;
                        case 4:
                            department.delDep(7, false);
                            //result = "";
                            break;
                        case 5:
                            department.edtEmp(7, "Perry", 22, 35000);
                            //result = "";
                            break;
                        case 6:
                            department.swapOne(2, 7);
                            //result = "";
                            break;
                    }

                    String result = department.displayDepartment();

                    if (choice == 7) {
                        result = department.getOne(1);
                    } else if (choice == 8) {
                        result = department.displayOne(1);
                    }
                    outputStream.writeUTF(result);
                    outputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
