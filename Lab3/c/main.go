package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

var (
	coms = []string{"tobacco", "paper", "matches"}
	done = make(chan struct{})
)

func main() {
	rand.Seed(time.Now().UnixNano())

	var wg sync.WaitGroup

	mediatorSemaphore := make(chan struct{}, 1)
	smokerSemaphores := make([]chan struct{}, 3)

	mediatorSemaphore <- struct{}{}

	wg.Add(1)
	go Mediator(mediatorSemaphore, smokerSemaphores, &wg)

	for i := 0; i < 3; i++ {
		smokerSemaphores[i] = make(chan struct{})
		wg.Add(1)
		go Smoker(i, coms[i], smokerSemaphores, mediatorSemaphore, &wg)
	}

	wg.Wait()
}

func Mediator(mediatorSemaphore chan struct{}, smokerSemaphores []chan struct{}, wg *sync.WaitGroup) {
	for {
		<-mediatorSemaphore
		com1 := rand.Intn(3)
		com2 := rand.Intn(3)

		for com1 == com2 {
			com2 = rand.Intn(3)
		}

		if com1 > com2 {
			com1, com2 = com2, com1
		}

		time.Sleep(time.Second / 4)
		fmt.Printf("\nПосередник приніс %s і %s\n", coms[com1], coms[com2])

		select {
		case <-done:
			wg.Done()
			return
		default:
		}

		smokerSemaphores[3-com1-com2] <- struct{}{}
	}
}

func Smoker(id int, com string, smokerSemaphores []chan struct{}, mediatorSemaphore chan struct{}, wg *sync.WaitGroup) {
	for {
		select {
		case <-smokerSemaphores[id]:
			time.Sleep(time.Second / 4)
			fmt.Printf("Курець№%d з %s забирає компоненти і курить\n", id+1, com)
			time.Sleep(time.Second / 2)
			fmt.Printf("Курець закінчив курити\n")
			mediatorSemaphore <- struct{}{}

		case <-done:
			wg.Done()
			return
		}
	}
}
