package jms;

import jakarta.jms.*;
import org.apache.activemq.ActiveMQConnectionFactory;

import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        final String brokerURL = "tcp://localhost:61616";
        final String queueNAME = "Queue";

        try {
            ConnectionFactory cFactory = new ActiveMQConnectionFactory(brokerURL);
            Connection connection = cFactory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queueNAME);
            MessageProducer producer = session.createProducer(destination);
            MessageConsumer consumer = session.createConsumer(destination);

            Scanner scn = new Scanner(System.in);

            while (true) {
                System.out.println("\n\t\tГоловне меню:");
                System.out.println("1. Додавання нового підрозділу");
                System.out.println("2. Видалення підрозділу");
                System.out.println("3. Прийом на роботу співробітника в заданий підрозділ");
                System.out.println("4. Звільнення співробітника");
                System.out.println("5. Редагування особистих даних співробітника");
                System.out.println("6. Переведення співробітника з одного підрозділу в інший");
                System.out.println("7. Підрахунок кількості співробітників в підрозділі");
                System.out.println("8. Отримання списку співробітників для заданого підрозділу");
                System.out.println("9. Отримання списку підрозділів");
                System.out.println("0. Вийти з програми");

                System.out.print("Виберіть дію: ");
                int choice = scn.nextInt();

                if (choice == 0) {
                    break;
                }

                ObjectMessage request = session.createObjectMessage();
                request.setIntProperty("choice", choice);
                producer.send(request);

                Message response = consumer.receive();

                if (response instanceof TextMessage) {
                    TextMessage res = (TextMessage) response;
                    String result = res.getText();
                    System.out.println(result);
                }
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}