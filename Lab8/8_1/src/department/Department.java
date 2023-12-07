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

    public void addCom() {
        Company com = new Company(3, "Apple", 1985);
        coms.add(com);
        DatabaseOperations.insertCom(com, cTable);
    }

    public void addEmp(Employee emp, int idcom) {
        DatabaseOperations.insertEmp(emp, eTable, idcom);
    }

    public void delDep(int idemp, boolean choice) {
        if (choice) {
            DatabaseOperations.deleteData(idemp, cTable, choice);
        } else {
            DatabaseOperations.deleteData(idemp, eTable, choice);
        }
    }

    public void edtEmp(int idemp, String newName, int newAge, double newSalary) {
        DatabaseOperations.editEData(idemp, eTable, newName, newAge, newSalary);
    }


    public String displayDepartment() {
        return DatabaseOperations.displayData(cTable, eTable);
    }

    public String displayOne(int idcom) {
        return DatabaseOperations.displayOnly(cTable, eTable, idcom);
    }

    public String getOne(int idcom) {
        return "\nКількість працівників в компанії: " + DatabaseOperations.getOnly(eTable, idcom);
    }

    public void swapOne(int idcom, int idemp) {
        DatabaseOperations.swapCom(eTable, idcom, idemp);
    }
}
