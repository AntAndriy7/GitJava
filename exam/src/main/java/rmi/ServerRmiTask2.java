package rmi;

import organizer.Event;
import organizer.Organizer;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ServerRmiTask2 {
    final static int PORT = 1099;
    final static String NAME = "RemoteDepartment";

    public static void main(String[] args) {
        try {
            LocateRegistry.createRegistry(PORT);
            TMI remote = new TaskManager();
            Naming.rebind("//localhost:" + PORT + "/" + NAME, remote);

            System.out.println("Сервер запущено.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

interface TMI extends java.rmi.Remote {
    String execute(int choice) throws RemoteException, ParseException;
}

class TaskManager extends UnicastRemoteObject implements TMI {
    public TaskManager() throws RemoteException {
        super();
    }

    @Override
    public String execute(int choice) throws RemoteException, ParseException {

        String result = "";

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

        return result;
    }
}