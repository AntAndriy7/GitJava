package rmi;

import database.DatabaseConnection;
import database.DatabaseOperations;
import department.Department;
import model.Company;
import model.Employee;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        try {
            final int port = 1099;
            String serviceName = "RemoteDepartment";

            LocateRegistry.createRegistry(port);
            RemoteDepartment remote = new RDI();
            Naming.rebind("//localhost:" + port + "/" + serviceName, remote);

            System.out.println("Сервер запущено.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

interface RemoteDepartment extends java.rmi.Remote {
    String execute(int choice) throws RemoteException;
}

class RDI extends UnicastRemoteObject implements RemoteDepartment {
    public RDI() throws RemoteException {
        super();
    }

    @Override
    public String execute(int choice) throws RemoteException {

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

        return result;
    }
}
