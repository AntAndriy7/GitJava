import java.io.*;
import java.util.concurrent.locks.*;
class Garden {
    private int[][] garden;
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public Garden(int rows, int col) {
        garden = new int[rows][col];
    }

    public void waterPlant(int row, int col) {
        lock.writeLock().lock();
        try {
            garden[row][col] = 1;
            System.out.println("Садівник поливає рослину на рядку " + (row + 1) + ", колонці " + (col + 1));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void changePlant(int row, int col) {
        lock.writeLock().lock();
        try {
            int state = garden[row][col];
            if (state == 1) {
                System.out.println("Природа змінила стан рослини на рядку " + (row + 1) + ", колонці " + (col + 1) + " на " + 0);
                garden[row][col] = 0;
            } else {
                System.out.println("Природа змінила стан рослини на рядку " + (row + 1) + ", колонці " + (col + 1) + " на " + 1);
                garden[row][col] = 1;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public int getPlant(int row, int col) {
        lock.readLock().lock();
        try {
            return garden[row][col];
        } finally {
            lock.readLock().unlock();
        }
    }

    public void printGarden() {
        lock.readLock().lock();
        try {
            System.out.println("Стан саду:");
            for (int i = 0; i < garden.length; i++) {
                for (int j = 0; j < garden[i].length; j++) {
                    System.out.print(garden[i][j] + " ");
                }
                System.out.println();
            }
        } finally {
            lock.readLock().unlock();
        }
    }
}

public class Main {
    public static void main(String[] args) {
        final int SIZE = 5;
        Garden garden = new Garden(SIZE, SIZE);

        Thread gardenerThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int row = (int) (Math.random() * SIZE);
                int col = (int) (Math.random() * SIZE);

                if (garden.getPlant(row, col) == 0) {
                    garden.waterPlant(row, col);
                } else {
                    System.out.print("Садівник на рядку " + (row + 1) + ", колонці " + (col + 1));
                    System.out.println(", але в рослини стан 1, тому він пішов перевіряти іншу.");
                }
            }
        });


        Thread natureThread = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                int row = (int) (Math.random() * SIZE);
                int col = (int) (Math.random() * SIZE);
                garden.changePlant(row, col);
            }
        });

        Thread fileWriterThread = new Thread(() -> {
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter("garden.txt"));
                while (true) {
                    String gardenState = getString(garden, SIZE);
                    writer.write(gardenState);
                    writer.newLine();
                    writer.flush();
                    Thread.sleep(3000);
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread monitorThread = new Thread(() -> {
            while (true) {
                garden.printGarden();
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        gardenerThread.start();
        natureThread.start();
        fileWriterThread.start();
        monitorThread.start();
    }

    private static String getString(Garden garden, int SIZE) {
        StringBuilder build = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                build.append(garden.getPlant(i, j)).append(" ");
            }
            build.append("\n");
        }
        return build.toString();
    }
}
