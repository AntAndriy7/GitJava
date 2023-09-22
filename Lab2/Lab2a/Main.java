import java.util.Random;

class Bee implements Runnable {
    private final int id;
    private final int[] forest;
    private static int forestIndex = 0;

    public Bee(int id, int[] forest) {
        this.id = id;
        this.forest = forest;
    }

    public void run() {
        System.out.println("Запуск зграї бджіл №" + (id + 1));
        try {
            HideAndSeek();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }

    private void HideAndSeek() throws InterruptedException {
        Random random = new Random();
        while (true) {
            int task;
            synchronized (forest) {
                if (forestIndex < forest.length) {
                    task = forestIndex;
                    forestIndex++;
                } else {
                    System.out.println("Зграя бджіл №" + (id + 1) + " завершила пошук і повертається у вулик!");
                    return;
                }
            }

            if (forest[task] == 1) {
                System.out.println("Зграя бджіл №" + (id + 1) + " знайшла Вінні-Пуха на позиції: " + task
                        + "; провела показове покарання та повертається у вулик!");
                return;
            } else {
                System.out.println("Зграя бджіл №" + (id + 1) + " шукає Вінні-Пуха на позиції: " + task);
            }

            Thread.sleep(random.nextInt(500));
        }
    }
}

public class Main {
    public static final int SWARM = 3;
    public static final int SIZE = 30;
    public static final int[] FOREST = new int[SIZE];

    public static void main(String[] args) throws InterruptedException {
        Random random = new Random();
        int winniePosition = random.nextInt(SIZE);
        FOREST[winniePosition] = 1;

        Thread[] threads = new Thread[SWARM];

        for (int i = 0; i < SWARM; i++) {
            Bee bee = new Bee(i, FOREST);
            threads[i] = new Thread(bee);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        System.out.println("Всі зграї у вулику!");
    }
}