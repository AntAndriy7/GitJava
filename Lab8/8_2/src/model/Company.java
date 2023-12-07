package model;

import java.util.List;

public class Company {
    private int ID;
    private String name;
    private int yearFounded;

    public Company(int ID, String name, int yearFounded) {
        this.ID = ID;
        this.name = name;
        this.yearFounded = yearFounded;
    }
    private List<Employee> employees;

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public int getYearFounded() {
        return yearFounded;
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