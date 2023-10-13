import java.util.Random;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;

public class Main {
    private static final int SIZE = 4;

    public static void main(String[] args) {
        CyclicBarrier barrier = new CyclicBarrier(SIZE);
        Inspector checker = new Inspector(SIZE);
        Thread[] threads = new Thread[SIZE];
        String[] letters = {"ABCDABCD", "AABBCCDD", "CDCDCDCD", "CDCABDCD"};

        for (int i = 0; i < SIZE; i++) {
            threads[i] = new Thread(new Swapper(letters[i], barrier, checker, i + 1));
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("\nУсі потоки завершили роботу!");
    }
}

class Inspector {
    private boolean active = true;
    private int threads = 0;
    private final int SIZE;
    private final int[] sumData;
    private final CyclicBarrier barrier;

    public Inspector(int numThreads) {
        SIZE = numThreads;
        sumData = new int[numThreads];
        barrier = new CyclicBarrier(numThreads);
    }

    public boolean isActive() {
        return active;
    }

    public void equalityCheck() {
        for (int i = 0; i < sumData.length; i++) {
            int count = 1;
            for (int j = i + 1; j < sumData.length; j++) {
                if (sumData[i] == sumData[j]) {
                    count++;
                }
                if (count >= 3) {
                    active = false;
                }
            }
        }
    }

    public void getSymCount(int data) {
        sumData[threads] = data;
        threads++;
        if (threads == SIZE) {
            System.out.println();
        }
        try {
            barrier.await();
        } catch (InterruptedException | BrokenBarrierException e) {
            e.printStackTrace();
        }
        threads--;
        if (threads == 0) {
            equalityCheck();
        }
    }
}

class Swapper implements Runnable {
    private boolean active = true;
    private final Random random = new Random();
    private final StringBuilder currentString;
    private final CyclicBarrier barrier;
    private final Inspector check;
    private int symCount;
    private final int threadID;

    public Swapper(String letters, CyclicBarrier barrier, Inspector check, int ID) {
        this.currentString = new StringBuilder(letters);
        this.barrier = barrier;
        this.check = check;
        this.symCount = sumData(letters);
        this.threadID = ID;
    }

    private int sumData(String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == 'A' || str.charAt(i) == 'B') {
                count++;
            }
        }
        return count;
    }

    @Override
    public void run() {
        while (active) {
            int rand = random.nextInt(currentString.length());
            char randChar = currentString.charAt(rand);
            switch (randChar) {
                case 'A': {
                    currentString.setCharAt(rand, 'C');
                    symCount--;
                    break;
                }
                case 'B': {
                    currentString.setCharAt(rand, 'D');
                    symCount--;
                    break;
                }
                case 'C': {
                    currentString.setCharAt(rand, 'A');
                    symCount++;
                    break;
                }
                case 'D': {
                    currentString.setCharAt(rand, 'B');
                    symCount++;
                    break;
                }
            }
            check.getSymCount(symCount);

            System.out.println("Thread #" + this.threadID + " " + currentString + " " + symCount);

            try {
                Thread.sleep(500);
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }

            active = check.isActive();
        }
    }
}