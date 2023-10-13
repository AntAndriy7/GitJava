import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class Main {
    public static void main(String[] args) {
        int numThreads = 3;
        int SIZE = 150;
        int[] elements = new int[SIZE];
        CyclicBarrier barrier = new CyclicBarrier(numThreads);

        Random random = new Random();
        for (int i = 0; i < SIZE; i++) {
            elements[i] = random.nextInt(2);
        }

        System.out.print("Початковий стан масиву: ");
        for (int element : elements) {
            System.out.print(element + " ");
        }
        System.out.println();

        Thread[] threads = new Thread[numThreads];
        int elementsPerThread = elements.length / numThreads;

        for (int i = 0; i < numThreads; i++) {
            int startIndex = i * elementsPerThread;
            int endIndex = (i == numThreads - 1) ? elements.length - 1 : (startIndex + elementsPerThread - 1);
            System.out.println("startIndex: " + startIndex + "; endIndex: " + endIndex);
            threads[i] = new Thread(new Recruits(elements, startIndex, endIndex, barrier));
            threads[i].start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.print("Кінцевий стан масиву: ");
        for (int element : elements) {
            System.out.print(element + " ");
        }
    }
}

class Recruits implements Runnable {
    private final int[] elements;
    private final int start;
    private final int end;
    private final CyclicBarrier barrier;

    public Recruits(int[] elements, int start, int end, CyclicBarrier barrier) {
        this.elements = elements;
        this.start = start;
        this.end = end;
        this.barrier = barrier;
    }

    public boolean checkCondition() {
        boolean isCorrect = true;
        while (isCorrect) {
            isCorrect = false;

            for (int i = 0; i < elements.length; i++) {
                if (i > 0 && elements[i] == 0 && elements[i - 1] == 1) {
                    isCorrect = true;
                } else if (i < elements.length - 1 && elements[i] == 1 && elements[i + 1] == 0) {
                    isCorrect = true;
                }
            }
            return isCorrect;
        }
        return isCorrect;
    }

    @Override
    public void run() {
        boolean isChanged = true;

        while (isChanged) {
            isChanged = checkCondition();

            for (int i = start; i < end; i++) {
                if (i > 0 && elements[i] == 0 && elements[i - 1] == 1) {
                    elements[i] = 1;
                    elements[i - 1] = 0;
                    isChanged = true;
                } else if (i < elements.length - 1 && elements[i] == 1 && elements[i + 1] == 0) {
                    elements[i] = 0;
                    elements[i + 1] = 1;
                    isChanged = true;
                }
            }

            try {
                barrier.await();
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }
}
