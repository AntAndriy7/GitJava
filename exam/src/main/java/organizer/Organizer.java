package organizer;

import java.util.*;

public class Organizer {
    private List<Event> events;

    public Organizer() {
        events = new ArrayList<>();
    }

    public void addEvent(Event event) {
        events.add(event);
    }

    public List<Event> getByDate(Date startDate, Date endDate) {
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : events) {
            Date eventDate = event.getDate();
            if (eventDate.after(startDate) && eventDate.before(endDate)) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }

    public List<Event> getByCategory(String category) {
        List<Event> filteredEvents = new ArrayList<>();
        for (Event event : events) {
            if (event.getCategory().equals(category)) {
                filteredEvents.add(event);
            }
        }
        return filteredEvents;
    }

    public List<Event> getReminders(Date currentDate, int daysAhead) {
        List<Event> reminders = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.DAY_OF_MONTH, daysAhead);

        for (Event event : events) {
            Date eventDate = event.getDate();
            if (eventDate.before(calendar.getTime())) {
                reminders.add(event);
            }
        }

        return reminders;
    }

    public List<Event> getAllEvents() {
        return events;
    }
}