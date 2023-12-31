package model;

import java.util.List;

public class Department {
    private List<Company> companies;

    public List<Company> getCompanies() {
        return companies;
    }
    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    @Override
    public String toString() {
        return "\nDepartment {" + companies + '}';
    }
}