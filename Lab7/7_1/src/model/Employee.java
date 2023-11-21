package model;

import java.util.List;

public class Employee {
    private String ID;
    private String fullName;
    private int age;
    private double salary;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "\n\tID: " + ID +
                " | Ім'я: " + fullName +
                " | Вік: " + age +
                " | Заробітнa плата: " + salary;
    }
}