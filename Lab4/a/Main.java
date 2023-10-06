import java.io.*;
import java.util.*;
import java.util.concurrent.locks.*;

class PhoneBook {
    private String name;
    private String phoneNumber;

    public PhoneBook(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public String getNumber() {
        return phoneNumber;
    }
}

class Database {
    private Map<String, String> data;
    private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    public Database() {
        data = new HashMap<>();
    }

    public String findByName(String name) {
        readLock.lock();
        try {
            return data.get(name);
        } finally {
            readLock.unlock();
        }
    }

    public Set<String> findByPhone(String phone) {
        readLock.lock();
        try {
            Set<String> matchingNames = new HashSet<>();
            for (Map.Entry<String, String> entry : data.entrySet()) {
                if (entry.getValue().equals(phone)) {
                    matchingNames.add(entry.getKey());
                }
            }
            return matchingNames;
        } finally {
            readLock.unlock();
        }
    }

    public void deleteData(String name) {
        writeLock.lock();
        try {
            data.remove(name);
            saveFile();
        } finally {
            writeLock.unlock();
        }
    }

    public void addData(PhoneBook entry) {
        writeLock.lock();
        try {
            data.put(entry.getName(), entry.getNumber());
            saveFile();
        } finally {
            writeLock.unlock();
        }
    }

    private void saveFile() {
        try (FileWriter writer = new FileWriter("Phonebook.txt")) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        Database data = new Database();

        PhoneBook Person1 = new PhoneBook("Остап Ступка", "+380635678");
        PhoneBook Person2 = new PhoneBook("Тарас Шевченко", "+380981234");

        data.addData(Person1);
        data.addData(Person2);

        Thread findByNameThread = new Thread(() -> {
            String phoneNumber = data.findByName(Person1.getName());
            System.out.println(Person1.getName() + ": " + phoneNumber);
        });

        Thread findByPhoneThread = new Thread(() -> {
            Set<String> names = data.findByPhone(Person2.getNumber());
            String name = names.toString();
            System.out.println(Person2.getNumber() + ": " + name);
        });

        Thread addEntryThread = new Thread(() -> {
            data.addData(new PhoneBook("Іван Франко", "+380938273"));
        });

        Thread deleteEntryThread = new Thread(() -> {
            data.deleteData(Person1.getName());
        });

        findByNameThread.start();
        findByPhoneThread.start();
        addEntryThread.start();
        deleteEntryThread.start();
    }
}