import java.util.Random;

public class Main {
    public static void main(String[] args) {
        int numBees = 5;
        int honeyPotCapacity = 13;

        HoneyPot honeyPot = new HoneyPot(honeyPotCapacity);
        HoneyPotSemaphore semaphore = new HoneyPotSemaphore(1);

        Bear bear = new Bear(honeyPot, semaphore);
        bear.start();

        for (int i = 1; i <= numBees; i++) {
            Bee bee = new Bee(i, honeyPot, semaphore, bear);
            bee.start();
        }
    }
}

class HoneyPotSemaphore {
    private int permits;

    public HoneyPotSemaphore(int permits) {
        this.permits = permits;
    }

    public synchronized void acquire() throws InterruptedException {
        while (permits == 0) {
            wait();
        }
        permits--;
    }

    public synchronized void release() {
        permits++;
        notify();
    }
}

class HoneyPot {
    private final int capacity;
    private int honeyCount;

    public HoneyPot(int capacity) {
        this.capacity = capacity;
        this.honeyCount = 0;
    }

    public boolean isFull() {
        return honeyCount == capacity;
    }

    public void addHoney() {
        honeyCount++;
    }

    public int getHoneyCount() {
        return honeyCount;
    }

    public void empty() {
        honeyCount = 0;
    }
}

class Bear extends Thread {
    private final HoneyPot honeyPot;
    private final HoneyPotSemaphore semaphore;

    public Bear(HoneyPot honeyPot, HoneyPotSemaphore semaphore) {
        this.honeyPot = honeyPot;
        this.semaphore = semaphore;
    }

    public void run() {
        try {
            while (true) {
                semaphore.acquire();

                while (!honeyPot.isFull()) {
                    semaphore.release();
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                    }
                    semaphore.acquire();
                }

                System.out.println("Ведмідь прокинувся і з'їв весь мед.");
                honeyPot.empty();
                semaphore.release();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Bee extends Thread {
    private final int id;
    private final HoneyPot honeyPot;
    private final HoneyPotSemaphore semaphore;
    private final Bear bear;

    public Bee(int id, HoneyPot honeyPot, HoneyPotSemaphore semaphore, Bear bear) {
        this.id = id;
        this.honeyPot = honeyPot;
        this.semaphore = semaphore;
        this.bear = bear;
    }

    public void run() {
        try {
            while (true) {
                semaphore.acquire();
                if (!honeyPot.isFull()) {
                    Thread.sleep(new Random().nextInt(300));
                    honeyPot.addHoney();
                    System.out.println("Бджола " + id + " додала порцію меду. В горщику " + honeyPot.getHoneyCount() + " порцій меду.");
                    if (honeyPot.isFull()) {
                        System.out.println("Бджола " + id + " сповістила, що горщик повний і будить ведмедя.");
                        bear.interrupt();
                    }
                }
                semaphore.release();
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}