package model;

public class Employee {
    private int ID;
    private String fullName;
    private int age;
    private double salary;

    public Employee(int ID, String fullName, int age, double salary) {
        this.ID = ID;
        this.fullName = fullName;
        this.age = age;
        this.salary = salary;
    }
    public int getID() {
        return ID;
    }

    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }

    public double getSalary() {
        return salary;
    }
    
    @Override
    public String toString() {
        return "\n\tID: " + ID +
                " | Ім'я: " + fullName +
                " | Вік: " + age +
                " | Заробітнa плата: " + salary;
    }
}