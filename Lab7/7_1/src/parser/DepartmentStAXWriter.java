package parser;

import model.Department;
import model.Company;
import model.Employee;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileOutputStream;
import java.util.List;

public class DepartmentStAXWriter {
    public static void writeToFile(List<Department> departments, String filePath) {
        XMLOutputFactory factory = XMLOutputFactory.newInstance();
        try {
            XMLStreamWriter writer = factory.createXMLStreamWriter(new FileOutputStream(filePath), "UTF-8");

            writer.writeStartDocument("UTF-8", "1.0");
            writer.writeStartElement("Department");
            writer.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
            writer.writeAttribute("xsi:noNamespaceSchemaLocation", "department.xsd");

            for (Department department : departments) {
                writeDepartment(writer, department);
            }

            writer.writeEndElement();
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeDepartment(XMLStreamWriter writer, Department department) throws XMLStreamException {
        List<Company> companies = department.getCompanies();
        if (companies != null) {
            for (Company company : companies) {
                writeCompany(writer, company);
            }
        }
    }

    private static void writeCompany(XMLStreamWriter writer, Company company) throws XMLStreamException {
        writer.writeStartElement("company");
        writer.writeAttribute("ID", company.getID());

        writer.writeStartElement("name");
        writer.writeCharacters(company.getName());
        writer.writeEndElement();

        writer.writeStartElement("year_founded");
        writer.writeCharacters(String.valueOf(company.getYearFounded()));
        writer.writeEndElement();

        writer.writeStartElement("employees");
        List<Employee> employees = company.getEmployees();
        if (employees != null) {
            for (Employee employee : employees) {
                writeEmployee(writer, employee);
            }
        }
        writer.writeEndElement();
        writer.writeEndElement();
    }

    private static void writeEmployee(XMLStreamWriter writer, Employee employee) throws XMLStreamException {
        writer.writeStartElement("employee");
        writer.writeAttribute("ID", employee.getID());

        writer.writeStartElement("full_name");
        writer.writeCharacters(employee.getFullName());
        writer.writeEndElement();

        writer.writeStartElement("age");
        writer.writeCharacters(String.valueOf(employee.getAge()));
        writer.writeEndElement();

        writer.writeStartElement("salary");
        writer.writeCharacters(String.valueOf(employee.getSalary()));
        writer.writeEndElement();
        writer.writeEndElement();
    }
}
