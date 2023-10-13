package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

var random = rand.New(rand.NewSource(time.Now().UnixNano()))

type Arr struct {
	array1, array2, array3 []int
	sync.WaitGroup
}

func createArr(size int) *Arr {
	return &Arr{
		array1: genArr(size),
		array2: genArr(size),
		array3: genArr(size),
	}
}

func genArr(size int) []int {
	array := make([]int, size)
	for i := 0; i < size; i++ {
		array[i] = random.Intn(10)
	}
	return array
}

func simulatorArr(array *Arr, size int) {
	iteration := 1
	for {
		array.Add(3)

		go changeEl(array, &array.array1, size)
		go changeEl(array, &array.array2, size)
		go changeEl(array, &array.array3, size)

		array.Wait()

		if checkEqual(array.array1, array.array2, array.array3) {
			fmt.Printf("Iteration %d: Arrays sum are equal [%d, %d, %d]\n", iteration, calculateSum(array.array1), calculateSum(array.array2), calculateSum(array.array3))
			fmt.Println("Array 1:", array.array1)
			fmt.Println("Array 2:", array.array2)
			fmt.Println("Array 3:", array.array3)
			break
		} else {
			fmt.Printf("Sum[%d, %d, %d], Iteration %d:\n", calculateSum(array.array1), calculateSum(array.array2), calculateSum(array.array3), iteration)
			fmt.Println("Array 1:", array.array1)
			fmt.Println("Array 2:", array.array2)
			fmt.Println("Array 3:", array.array3)
		}
		iteration++
	}
}

func changeEl(arr *Arr, array *[]int, size int) {
	defer arr.Done()
	elemToChange := random.Intn(size)
	rand := random.Intn(2)

	if rand == 0 {
		if (*array)[elemToChange] < size*2 {
			(*array)[elemToChange]++
		}
	}
	if rand == 1 {
		if (*array)[elemToChange] > 0 {
			(*array)[elemToChange]--
		}
	}
}

func checkEqual(array1, array2, array3 []int) bool {
	return compareSums(array1, array2) && compareSums(array1, array3) && compareSums(array2, array3)
}

func compareSums(array1, array2 []int) bool {
	sum1 := calculateSum(array1)
	sum2 := calculateSum(array2)
	return sum1 == sum2
}

func calculateSum(array []int) int {
	sum := 0
	for _, val := range array {
		sum += val
	}
	return sum
}

func main() {
	const (
		SIZE = 5
	)

	arr := createArr(SIZE)
	simulatorArr(arr, SIZE)
}
