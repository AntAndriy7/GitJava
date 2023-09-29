import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Semaphore;

public class Main {
    public static int id = 1;

    public static void main(String[] args) {
        Barber barber = new Barber();
        Thread barberThread = new Thread(barber);
        barberThread.start();

        while (id <= 5) {
            Customer customer = new Customer(id, barber);
            Thread customerThread = new Thread(customer);
            customerThread.start();
            id++;
        }
    }
}

class Barber implements Runnable {
    private final Queue<Customer> queue;
    private final Semaphore semaphore;
    private boolean isActive;

    public Barber() {
        this.queue = new ConcurrentLinkedDeque<>();
        this.semaphore = new Semaphore(0);
        this.isActive = false;
    }

    public void addCustomer(Customer customer) {
        queue.add(customer);
        semaphore.release();
    }

    public boolean hasCustomers() {
        return isActive;
    }

    public void run() {
        while (true) {
            try {
                semaphore.acquire();
                Customer customer = queue.poll();

                if (customer != null) {

                    if (!isActive) {
                        isActive = true;
                    }

                    Thread.sleep(500);
                    System.out.println("Відвідувач " + customer.getId() + " сів у крісло стригтися.");

                    Thread.sleep(1500);
                    System.out.println("Відвідувач " + customer.getId() + " вийшов з перукарні.");

                    if (queue.isEmpty()) {
                        System.out.println("Перукар пішов спати у кріслі.");
                        isActive = false;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class Customer implements Runnable {
    private final int id;
    private final Barber barber;

    public Customer(int id, Barber barber) {
        this.id = id;
        this.barber = barber;
    }

    public int getId() {
        return id;
    }

    public void run() {
        try {
            Thread.sleep(new Random().nextInt(5000) + 1000);
            if (barber.hasCustomers()) {
                System.out.println("Відвідувач " + id + " зайшов у чергу.");
                barber.addCustomer(this);
            } else {
                System.out.println("Відвідувач " + id + " розбудив перукаря.");
                barber.addCustomer(this);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}