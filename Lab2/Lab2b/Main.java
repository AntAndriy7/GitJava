import java.util.concurrent.SynchronousQueue;
import java.util.Random;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        MilitaryWarehouse task = new MilitaryWarehouse ();

        Thread ivanovThread = new Thread(new Ivanov(task));
        Thread petrovThread = new Thread(new Petrov(task));
        Thread nechipThread = new Thread(new Nechiporchuk(task));

        ivanovThread.start();
        petrovThread.start();
        nechipThread.start();
    }

    static class MilitaryWarehouse  {
        private final SynchronousQueue<Integer> fromIvanovToPetrov = new SynchronousQueue<>();
        private final SynchronousQueue<Integer> fromPetrovToNechip = new SynchronousQueue<>();

        public void addItem(int item) throws InterruptedException {
            fromIvanovToPetrov.put(item);
        }

        public int takeItem() throws InterruptedException {
            return fromIvanovToPetrov.take();
        }

        public void loadItem(int item) throws InterruptedException {
            fromPetrovToNechip.put(item);
        }

        public int calculateItem() throws InterruptedException {
            return fromPetrovToNechip.take();
        }
    }

    private static final int numOfGoods = 10;
    private static final Random random = new Random();

    static class Ivanov implements Runnable {
        private final MilitaryWarehouse warehouse;

        public Ivanov(MilitaryWarehouse warehouse) {
            this.warehouse = warehouse;
        }

        public void run() {
            try {
                for (int i = 1; i <= numOfGoods; i++) {
                    System.out.println("Іванов виніс майно зі складу: " + i);
                    warehouse.addItem(i);
                    Thread.sleep(random.nextInt(100));
                }
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    static class Petrov implements Runnable {
        private final MilitaryWarehouse warehouse;

        public Petrov(MilitaryWarehouse warehouse) {
            this.warehouse = warehouse;
        }

        public void run() {
            try {
                for (int i = 1; i <= numOfGoods; i++) {
                    int item = warehouse.takeItem();
                    System.out.println("Петров завантажив майно: " + item);
                    warehouse.loadItem(item);
                    Thread.sleep(random.nextInt(100));
                }
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }

    static class Nechiporchuk implements Runnable {

        private int total = 0;
        private final MilitaryWarehouse warehouse;

        public Nechiporchuk(MilitaryWarehouse warehouse) {
            this.warehouse = warehouse;
        }

        public void run() {
            try {
                for (int i = 1; i <= numOfGoods; i++) {
                    int item = warehouse.calculateItem();
                    System.out.println("Нечипорчук підрахував вартість майна: " + total + " + " + item + " = " + (total+item));
                    total += item;
                    Thread.sleep(random.nextInt(100));
                }
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }
    }
}