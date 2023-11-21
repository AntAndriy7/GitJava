package department;

import model.Company;
import model.Employee;

import database.DatabaseOperations;

import java.util.ArrayList;
import java.util.List;

public class Department {
    private final List<Company> coms;
    private final String cTable = "company";
    private final String eTable = "employee";

    public Department() {
        coms = new ArrayList<>();
    }

    public void addCom(Company com) {
        coms.add(com);
        DatabaseOperations.insertCom(com, cTable);
    }

    public void addEmp(Employee emp, int idcom) {
        DatabaseOperations.insertEmp(emp, eTable, idcom);
    }

    public void delEmp(int idemp, boolean choice) {
        if (choice) {
            DatabaseOperations.deleteData(idemp, cTable, choice);
        } else {
            DatabaseOperations.deleteData(idemp, eTable, choice);
        }
    }

    public void edtCom(int idemp, String newName, int newYear) {
        DatabaseOperations.editCData(idemp, cTable, newName, newYear);
    }

    public void edtEmp(int idemp, String newName, int newAge, double newSalary) {
        DatabaseOperations.editEData(idemp, eTable, newName, newAge, newSalary);
    }


    public void displayDepartment() {
        DatabaseOperations.displayData(cTable, eTable);
    }

    public void displayOne(int idcom) {
        DatabaseOperations.displayOnly(cTable, eTable, idcom);
    }
}
