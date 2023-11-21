package parser;

import model.Department;
import model.Company;
import model.Employee;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

public class DepartmentStAXParser {
    public static List<Department> parse(String path) {
        DepartmentHandler handler = new DepartmentHandler();
        parseXML(path, handler);
        return handler.getDepartments();
    }

    private static void parseXML(String path, DepartmentHandler handler) {
        try {
            XMLInputFactory factory = XMLInputFactory.newInstance();
            XMLStreamReader reader = factory.createXMLStreamReader(new FileInputStream(path));

            while (reader.hasNext()) {
                int event = reader.next();

                switch (event) {
                    case XMLStreamConstants.START_ELEMENT:
                        handler.startElement(reader.getLocalName(), reader.getAttributeValue(null, "ID"));
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        handler.characters(reader.getText().trim());
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        handler.endElement(reader.getLocalName());
                        break;
                }
            }

        } catch (XMLStreamException | java.io.IOException e) {
            e.printStackTrace();
        }
    }

    private static class DepartmentHandler {
        private List<Department> departments;
        private Department department;
        private Company company;
        private Employee employee;
        private String content;

        public List<Department> getDepartments() {
            return departments;
        }

        public void startElement(String elementName, String attribute) {
            if ("Department".equals(elementName)) {
                department = new Department();
            } else if ("company".equals(elementName)) {
                company = new Company();
                company.setID(attribute);
            } else if ("employee".equals(elementName)) {
                employee = new Employee();
                employee.setID(attribute);
            }
        }

        public void characters(String text) {
            content = text;
        }

        public void endElement(String elementName) {
            if ("Department".equals(elementName)) {
                if (departments == null) {
                    departments = new ArrayList<>();
                }
                departments.add(department);
            } else if ("company".equals(elementName)) {
                if (department.getCompanies() == null) {
                    department.setCompanies(new ArrayList<>());
                }
                department.getCompanies().add(company);
            } else if ("employee".equals(elementName)) {
                if (company.getEmployees() == null) {
                    company.setEmployees(new ArrayList<>());
                }
                company.getEmployees().add(employee);
            } else if ("name".equals(elementName)) {
                company.setName(content);
            } else if ("year_founded".equals(elementName)) {
                company.setYearFounded(Integer.parseInt(content));
            } else if ("full_name".equals(elementName)) {
                employee.setFullName(content);
            } else if ("age".equals(elementName)) {
                employee.setAge(Integer.parseInt(content));
            } else if ("salary".equals(elementName)) {
                employee.setSalary(Double.parseDouble(content));
            }
        }
    }
}
