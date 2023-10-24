package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

type ParkingLot struct {
	totalSpaces    int
	occupiedSpaces int
	maxWaitTime    time.Duration
	mutex          sync.Mutex
}

func NewParkingLot(totalSpaces int, maxWaitTime time.Duration) *ParkingLot {
	return &ParkingLot{
		totalSpaces: totalSpaces,
		maxWaitTime: maxWaitTime,
	}
}

func (pl *ParkingLot) parkCar(carID int, wg *sync.WaitGroup) bool {
	startTime := time.Now()

	pl.mutex.Lock()
	defer pl.mutex.Unlock()

	for pl.occupiedSpaces >= pl.totalSpaces {
		if time.Since(startTime) >= pl.maxWaitTime {
			wg.Done()
			return false
		}
		pl.mutex.Unlock()
		time.Sleep(time.Second)
		pl.mutex.Lock()
	}

	pl.occupiedSpaces++
	fmt.Printf("Car %d parked successfully.\n", carID)
	return true
}

func (pl *ParkingLot) leaveCar() {
	pl.mutex.Lock()
	defer pl.mutex.Unlock()

	pl.occupiedSpaces--
}

func main() {
	numCars := 5
	totalSpaces := 3
	maxWaitTime := 10 * time.Second

	parkingLot := NewParkingLot(totalSpaces, maxWaitTime)
	var wg sync.WaitGroup

	for i := 1; i <= numCars; i++ {
		wg.Add(1)
		go func(carID int) {
			fmt.Printf("Car %d arrived at the parking lot.\n", carID)
			if parkingLot.parkCar(carID, &wg) {
				defer parkingLot.leaveCar()
				time.Sleep(time.Duration(rand.Intn(5)+5) * time.Second)
				fmt.Printf("Car %d left the parking lot.\n", carID)
			} else {
				fmt.Printf("Car %d could not find a parking space.\n", carID)
			}
			wg.Done()
		}(i)
	}

	wg.Wait()
}
