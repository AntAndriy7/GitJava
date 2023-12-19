package socket;

import organizer.Event;
import organizer.Organizer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ServerSocketTask2 {
    final static int PORT = 50055;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Сервер запущено. Очікування з'єднань...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Клієнт підключено.");

                Thread clientThread = new Thread(new ClientHandler(clientSocket));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler implements Runnable {
        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                    DataInputStream inputStream = new DataInputStream(clientSocket.getInputStream());
                    DataOutputStream outputStream = new DataOutputStream(clientSocket.getOutputStream())
            ) {

                Organizer organizer = new Organizer();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date eventDate1 = dateFormat.parse("2023-12-20 10:00:00");
                Date eventDate2 = dateFormat.parse("2023-12-25 14:30:00");
                Date eventDate3 = dateFormat.parse("2024-01-05 18:00:00");

                Event event1 = new Event("Зустріч з другом", eventDate1, "Особисте", false);
                Event event2 = new Event("Подарунки на Різдво", eventDate2, "Свята", false);
                Event event3 = new Event("Засідання з проектом", eventDate3, "Робота", false);

                organizer.addEvent(event1);
                organizer.addEvent(event2);
                organizer.addEvent(event3);

                while (true) {
                    try {
                        int choice = inputStream.readInt();

                        String result = "";

                        switch (choice) {
                            case 1:
                                Date currentDate = new Date();
                                int daysAhead = 7;
                                List<Event> reminders = organizer.getReminders(currentDate, daysAhead);

                                result = "Нагадування про найближчі події:\n";

                                for (Event reminder : reminders) {
                                    result += "Подія: " + reminder.getTitle() + "\n";
                                    result += "Дата: " + dateFormat.format(reminder.getDate()) + "\n";
                                    result += "Категорія: " + reminder.getCategory() + "\n\n";
                                }
                                break;
                            case 2:
                                Date startDate = dateFormat.parse("2023-12-20 00:00:00");
                                Date endDate = dateFormat.parse("2023-12-26 23:59:59");
                                List<Event> eventsInRange = organizer.getByDate(startDate, endDate);

                                result = "Події у вказаному діапазоні дат:\n";

                                for (Event event : eventsInRange) {
                                    result += "Подія: " + event.getTitle() + "\n";
                                    result += "Дата: " + dateFormat.format(event.getDate()) + "\n";
                                    result += "Категорія: " + event.getCategory() + "\n\n";
                                }
                                break;
                            case 3:
                                String category = "Робота";
                                List<Event> eventsByCategory = organizer.getByCategory(category);

                                result = "Події у вказаній категорії (" + category + "):\n";

                                for (Event event : eventsByCategory) {
                                    result += "Подія: " + event.getTitle() + "\n";
                                    result += "Дата: " + dateFormat.format(event.getDate()) + "\n";
                                    result += "Категорія: " + event.getCategory() + "\n\n";
                                }
                                break;
                            case 4:
                                List<Event> allEvents = organizer.getAllEvents();
                                Collections.sort(allEvents, new Comparator<Event>() {
                                    @Override
                                    public int compare(Event event1, Event event2) {
                                        return event1.getDate().compareTo(event2.getDate());
                                    }
                                });

                                result = "Всі події, відсортовані за датою:\n";

                                for (Event event : allEvents) {
                                    result += "Подія: " + event.getTitle() + "\n";
                                    result += "Дата: " + dateFormat.format(event.getDate()) + "\n";
                                    result += "Категорія: " + event.getCategory() + "\n\n";
                                }
                                break;
                        }

                        outputStream.writeUTF(result);
                        outputStream.flush();
                    } catch (EOFException e) {
                        break;
                    } catch (IOException e) {
                        e.printStackTrace();
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    clientSocket.close();
                    System.out.println("Клієнт відключився.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
