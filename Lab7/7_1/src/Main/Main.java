package Main;

import model.Department;
import model.Company;
import model.Employee;
import parser.DepartmentStAXParser;
import parser.DepartmentStAXWriter;
import validator.DepartmentValidator;

import java.util.List;
import java.util.ArrayList;

public class Main {
    private static final String XML = "resources/department.xml";

    public static void main(String[] args) {
        if (DepartmentValidator.validate(XML)) {
            System.out.println("XML is valid.");
        } else {
            System.out.println("XML is not valid.");
            return;
        }

        List<Department> departments = new ArrayList<>();

        try {
            departments.addAll(DepartmentStAXParser.parse(XML));

            for (Department department : departments) {
                System.out.println(department);
            }

            List<Company> companies = departments.get(0).getCompanies();
            Company firstX = companies.get(0);
            firstX.setYearFounded(2005);

            Company secondX = companies.get(0);
            secondX.getEmployees().get(1).setSalary(37000);
            secondX.getEmployees().get(1).setFullName("Sophia Smith");

            System.out.println("\nЗміна року заснування команії 1");
            System.out.println("Зміна зп і імені дургому працівнику першої компанії");
            for (Department department : departments) {
                System.out.println(department);
            }

            Company thirdX = companies.get(2);
            thirdX.getEmployees().remove(0);

            System.out.println("\nЗвільнення з роботи працівника команії 3");
            for (Department department : departments) {
                System.out.println(department);
            }

            Company fourthX = companies.get(2);
            Employee newEmployee = new Employee();
            String newEmployeeID = "Emp004";

            if (isEmployeeIDUnique(fourthX, newEmployeeID)) {
                newEmployee.setID(newEmployeeID);
                newEmployee.setFullName("Isabella Brown");
                newEmployee.setAge(18);
                newEmployee.setSalary(100000);

                fourthX.getEmployees().add(newEmployee);
                System.out.println("\nВзяли на роботу працівника в команію 3");
                for (Department department : departments) {
                    System.out.println(department);
                }
            } else {
                System.err.println("Employee ID is not unique within the company.");
            }

            Department fifthX = departments.get(0);
            fifthX.getCompanies().remove(2);

            System.out.println("\nКоманія 3 зникла");
            for (Department department : departments) {
                System.out.println(department);
            }


            Company newCompany = new Company();
            String newCompanyID = "Com003";

            if (isCompanyIDUnique(fifthX, newCompanyID)) {
                newCompany.setID(newCompanyID);
                newCompany.setName("Nike");
                newCompany.setYearFounded(1970);
                List<Employee> emps = new ArrayList<>();
                emps.add(newEmployee);
                newCompany.setEmployees(emps);
                companies.add(newCompany);

                System.out.println("\nСтворили нову компанію і взяли на роботу працівника");
            } else {
                System.err.println("Employee ID is not unique within the company.");
            }

            DepartmentStAXWriter.writeToFile(departments, XML);

            for (Department department : departments) {
                System.out.println(department);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isEmployeeIDUnique(Company company, String employeeID) {
        List<Employee> employees = company.getEmployees();
        if (employees != null) {
            for (Employee employee : employees) {
                if (employee.getID().equals(employeeID)) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean isCompanyIDUnique(Department department, String companyID) {
        List<Company> companies = department.getCompanies();
        if (companies != null) {
            for (Company company : companies) {
                if (company.getID().equals(companyID)) {
                    return false;
                }
            }
        }
        return true;
    }
}