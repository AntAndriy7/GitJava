package main

import "fmt"
import "math/rand"
import "time"

var rng = rand.New(rand.NewSource(time.Now().UnixNano()))

func numberRounds(x int) int {
	result := 0

	for x != 1 {
		x /= 2
		result++
	}

	return result
}

func generateMonkEnergyQi(size int) []int {
	energyQi := make([]int, size)
	fmt.Print("[")

	for i := 0; i < size-1; i++ {
		energyQiValue := (rng.Intn(91) + 10) * 10
		energyQi[i] = energyQiValue
		fmt.Print(energyQiValue, ", ")
	}

	energyQiValue := (rng.Intn(91) + 10) * 10
	energyQi[size-1] = energyQiValue
	fmt.Print(energyQiValue, "]")
	fmt.Println()

	return energyQi
}

func fight(energyQi []int, monk1, monk2 int, assign chan int) {
	monk1EnergyQi := energyQi[monk1]
	monk2EnergyQi := energyQi[monk2]

	if monk1EnergyQi > monk2EnergyQi {
		assign <- monk1EnergyQi
		fmt.Printf("Monk[%d] vs Monk[%d] - winner is Monk[%d]\n", monk1EnergyQi, monk2EnergyQi, monk1EnergyQi)
	} else {
		assign <- monk2EnergyQi
		fmt.Printf("Monk[%d] vs Monk[%d] - winner is Monk[%d]\n", monk1EnergyQi, monk2EnergyQi, monk2EnergyQi)
	}
}

func runTournament(size int) {
	energyQi := generateMonkEnergyQi(size)
	rounds := numberRounds(size)

	fights := size / 2

	for i := 1; i <= rounds; i++ {
		nextRoundEnergy := make(chan int, fights)

		if i == rounds {
			go fight(energyQi, 0, 1, nextRoundEnergy)
			winner := <-nextRoundEnergy
			fmt.Printf("\nWinner of the tournament is Monk[%d]\n", winner)
			break
		}

		for j := 0; j < fights; j++ {
			go fight(energyQi, j*2, j*2+1, nextRoundEnergy)
		}

		energyQi = make([]int, fights)
		for j := 0; j < fights; j++ {
			energyQi[j] = <-nextRoundEnergy
		}

		fights /= 2
	}
}

func main() {
	const tournamentSize = 8
	runTournament(tournamentSize)
}