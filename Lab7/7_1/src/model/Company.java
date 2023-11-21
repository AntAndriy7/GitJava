package model;

import java.util.List;

public class Company {
    private String ID;
    private String name;
    private int yearFounded;
    private List<Employee> employees;

    public String getID() {
        return ID;
    }
    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getYearFounded() {
        return yearFounded;
    }
    public void setYearFounded(int yearFounded) {
        this.yearFounded = yearFounded;
    }

    public List<Employee> getEmployees() {
        return employees;
    }
    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "\nКомпанія" +
                " | ID: " + ID +
                " | Назва: " + name +
                " | Рік заснування: " + yearFounded +
                " | Співробітники:" + employees;
    }
}