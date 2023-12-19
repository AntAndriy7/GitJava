package organizer;

import java.util.Date;

public class Event {
    private String title;
    private Date date;
    private String category;
    private boolean isRecurring;

    public Event(String title, Date date, String category, boolean isRecurring) {
        this.title = title;
        this.date = date;
        this.category = category;
        this.isRecurring = isRecurring;
    }

    public String getTitle() {
        return title;
    }

    public Date getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public boolean isRecurring() {
        return isRecurring;
    }
}
