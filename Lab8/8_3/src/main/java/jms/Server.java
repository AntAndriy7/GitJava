package jms;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import database.DatabaseConnection;
import database.DatabaseOperations;
import department.Department;
import model.Company;
import model.Employee;

import java.util.ArrayList;
import java.util.List;

public class Server {
    public static void main(String[] args) {
        final String brokerURL = "tcp://localhost:61616";
        final String queueNAME = "Queue";

        try {
            ConnectionFactory cFactory = new ActiveMQConnectionFactory(brokerURL);
            Connection connection = cFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queueNAME);
            MessageConsumer consumer = session.createConsumer(destination);
            MessageProducer producer = session.createProducer(destination);

            while (true) {
                Message message = consumer.receive();

                if (message instanceof ObjectMessage) {
                    ObjectMessage objectMessage = (ObjectMessage) message;
                    int choice = objectMessage.getIntProperty("choice");

                    Department department = new Department();
                    DatabaseConnection.getConnection();
                    List<Company> companies = DatabaseOperations.getCompanies();

                    switch (choice) {
                        case 1:
                            department.addCom();
                            //result = "Новий підрозділ успішно додано!";
                            break;
                        case 2:
                            department.delDep(3, true);
                            //result = "Підрозділ успішно видалено!";
                            break;
                        case 3:
                            if (!companies.isEmpty()) {
                                Company firstCompany = companies.get(2);
                                Employee newEmployee = new Employee(7, "Jerry", 20, 24000);
                                List<Employee> newEmp = new ArrayList<>();
                                newEmp.add(newEmployee);
                                firstCompany.setEmployees(newEmp);
                                department.addEmp(newEmployee, firstCompany.getID());
                            }
                            //result = "";
                            break;
                        case 4:
                            department.delDep(7, false);
                            //result = "";
                            break;
                        case 5:
                            department.edtEmp(7, "Perry", 22, 35000);
                            //result = "";
                            break;
                        case 6:
                            department.swapOne(2, 7);
                            //result = "";
                            break;
                    }

                    String result = department.displayDepartment();

                    if (choice == 7) {
                        result = department.getOne(1);
                    } else if (choice == 8) {
                        result = department.displayOne(1);
                    }

                    TextMessage response = session.createTextMessage(result);
                    producer.send(response);
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}