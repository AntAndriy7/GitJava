package database;

import model.Company;
import model.Employee;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOperations {
    private final static String cCOL1 = "idcompany";
    private final static String cCOL2 = "name";
    private final static String cCOL3 = "yearFounded";
    private final static String eCOL1 = "idcom";
    private final static String eCOL2 = "idemployee";
    private final static String eCOL3 = "fullName";
    private final static String eCOL4 = "age";
    private final static String eCOL5 = "salary";

    private static String getCString(int ID, String name, int year) {
        return String.format("\nКомпанія | ID: %d | Назва: %s | Рік заснування: %d | Співробітники:", ID, name, year);
    }

    private static String getEString(int ID, String name, int age, double salary) {
        return String.format("\n\tID: %d | Ім'я: %s | Вік: %d | Заробітнa плата: %.2f", ID, name, age, salary);
    }

    public static void insertCom(Company com, String table) {
        String insert = "INSERT INTO " + table + "(" + cCOL1 + "," + cCOL2 + "," + cCOL3 + ") VALUES (?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insert)) {

            preparedStatement.setInt(1, com.getID());
            preparedStatement.setString(2, com.getName());
            preparedStatement.setInt(3, com.getYearFounded());

            preparedStatement.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void swapCom(String table, int idcom, int idemp) {
        String editQuery = "UPDATE " + table + " SET " + eCOL1 + " = ? WHERE " + eCOL2 + " = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(editQuery)) {

            preparedStatement.setInt(1, idcom);
            preparedStatement.setInt(2, idemp);

            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void insertEmp(Employee emp, String table, int idcom) {
        String query = "INSERT INTO " + table + "(" + eCOL1 + "," + eCOL2 + "," + eCOL3 + "," + eCOL4 + "," + eCOL5 + ") VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, idcom);
            statement.setInt(2, emp.getID());
            statement.setString(3, emp.getFullName());
            statement.setInt(4, emp.getAge());
            statement.setDouble(5, emp.getSalary());

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Company> getCompanies() {
        List<Company> companies = new ArrayList<>();
        String query = "SELECT * FROM company";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int companyId = resultSet.getInt(cCOL1);
                String name = resultSet.getString(cCOL2);
                int yearFounded = resultSet.getInt(cCOL3);

                Company company = new Company(companyId, name, yearFounded);
                List<Employee> employees = getByComID(companyId);
                company.setEmployees(employees);
                companies.add(company);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    public static List<Employee> getByComID(int IDcom) {
        List<Employee> employees = new ArrayList<>();
        String query = "SELECT * FROM employee WHERE idcom = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, IDcom);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    int employeeId = resultSet.getInt(eCOL2);
                    String fullName = resultSet.getString(eCOL3);
                    int age = resultSet.getInt(eCOL4);
                    double salary = resultSet.getDouble(eCOL5);

                    employees.add(new Employee(employeeId, fullName, age, salary));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public static String displayData(String ctable, String etable) {
        StringBuilder result = new StringBuilder();
        String display = "SELECT * FROM " + ctable;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(display);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                int ID = resultSet.getInt(cCOL1);
                String name = resultSet.getString(cCOL2);
                int year = resultSet.getInt(cCOL3);

                result.append(getCString(ID, name, year));
                result.append(displayEData(etable, ID));
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return result.toString();
    }

    public static String displayEData(String table, int IDcom) {
        StringBuilder result = new StringBuilder();
        String display = "SELECT * FROM " + table + " WHERE " + eCOL1 + " = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(display)) {

            preparedStatement.setInt(1, IDcom);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int ID = resultSet.getInt(eCOL2);
                    String fullName = resultSet.getString(eCOL3);
                    int age = resultSet.getInt(eCOL4);
                    double salary = resultSet.getDouble(eCOL5);

                    result.append(getEString(ID, fullName, age, salary));
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return result.toString();
    }

    public static String displayOnly(String ctable, String etable, int IDcom) {
        StringBuilder result = new StringBuilder();
        String display = "SELECT * FROM " + ctable + " WHERE " + cCOL1 + " = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(display)) {

            preparedStatement.setInt(1, IDcom);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int ID = resultSet.getInt(cCOL1);
                    String name = resultSet.getString(cCOL2);
                    int year = resultSet.getInt(cCOL3);

                    result.append(getCString(ID, name, year));
                    result.append(displayEData(etable, ID));
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return result.toString();
    }

    public static void editCData(int ID, String table, String newName, int newYear) {
        String editQuery = "UPDATE " + table + " SET " + cCOL2 + " = ?, " + cCOL3 + " = ? WHERE " + cCOL1 + " = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(editQuery)) {

            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, newYear);
            preparedStatement.setInt(3, ID);

            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void editEData(int ID, String table, String newName, int newAge, double newSalary) {
        String editQuery = "UPDATE " + table + " SET " + eCOL3 + " = ?, " + eCOL4 + " = ?, " + eCOL5 + " = ? WHERE " + eCOL2 + " = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(editQuery)) {

            preparedStatement.setString(1, newName);
            preparedStatement.setInt(2, newAge);
            preparedStatement.setDouble(3, newSalary);
            preparedStatement.setInt(4, ID);

            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static int getOnly(String table, int IDcom) {
        int result = 0;
        String display = "SELECT * FROM " + table + " WHERE " + eCOL1 + " = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(display)) {

            preparedStatement.setInt(1, IDcom);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    result++;
                }
            }

        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return result;
    }

    public static void deleteData(int ID, String table, boolean COL) {
        String deleteQuery;
        if (COL) {
            deleteQuery = "DELETE FROM " + "employee" + " WHERE " + eCOL1 + " = ?";

            try (Connection connection = DatabaseConnection.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

                preparedStatement.setInt(1, ID);
                preparedStatement.executeUpdate();

            } catch (SQLException exception) {
                exception.printStackTrace();
            }

            deleteQuery = "DELETE FROM " + table + " WHERE " + cCOL1 + " = ?";
        } else{
            deleteQuery = "DELETE FROM " + table + " WHERE " + eCOL2 + " = ?";
        }

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {

            preparedStatement.setInt(1, ID);
            preparedStatement.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}