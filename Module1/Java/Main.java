import java.util.Random;
class ParkingLot {
    private int totalSpaces;
    private int occupiedSpaces;
    private int maxWaitTime;

    public ParkingLot(int totalSpaces, int maxWaitTime) {
        this.totalSpaces = totalSpaces;
        this.maxWaitTime = maxWaitTime;
    }

    public synchronized boolean parkCar() throws InterruptedException {
        long startTime = System.currentTimeMillis();

        while (occupiedSpaces >= totalSpaces) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - startTime >= maxWaitTime) {
                return false;
            }
            wait(maxWaitTime - (currentTime - startTime));
        }

        occupiedSpaces++;
        return true;
    }

    public synchronized void leaveCar() {
        occupiedSpaces--;
        notifyAll();
    }
}

public class Main {
    public static void main(String[] args) {
        int numCars = 5;
        int totalSpaces = 3;
        int maxWaitTime = 10000;

        ParkingLot parkingLot = new ParkingLot(totalSpaces, maxWaitTime);

        for (int i = 1; i <= numCars; i++) {
            int carID = i;
            Thread carThread = new Thread(() -> {
                System.out.println("Car " + carID + " arrived at the parking lot.");
                try {
                    if (parkingLot.parkCar()) {
                        System.out.println("Car " + carID + " parked successfully.");
                        Thread.sleep((new Random().nextInt(5) + 5)*1000); // Симуляція перебування на парковці
                        System.out.println("Car " + carID + " left the parking lot.");
                        parkingLot.leaveCar();
                    } else {
                        System.out.println("Car " + carID + " could not find a parking space.");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            carThread.start();
        }
    }
}