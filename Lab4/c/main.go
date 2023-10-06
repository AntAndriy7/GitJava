package main

import (
	"fmt"
	"sync"
)

type Route struct {
	From string
	To   string
}

type Graph struct {
	mutex  sync.RWMutex
	cities map[string]bool
	routes map[Route]int
}

func newGraph() *Graph {
	return &Graph{
		cities: make(map[string]bool),
		routes: make(map[Route]int),
	}
}

func (g *Graph) addCity(city string) {
	g.mutex.Lock()
	defer g.mutex.Unlock()
	g.cities[city] = true
	if g.cities[city] == true {
		fmt.Printf("%s успішно додано\n", city)
	}
}

func (g *Graph) removeCity(city string) {
	g.mutex.Lock()
	defer g.mutex.Unlock()
	delete(g.cities, city)

	for route := range g.routes {
		if route.From == city || route.To == city {
			delete(g.routes, route)
		}
	}

	if g.cities[city] == false {
		fmt.Printf("%s і всі маршрути з цим містом успішно видалено\n", city)
	}
}

func (g *Graph) addRoute(from, to string, price int) {
	g.mutex.Lock()
	defer g.mutex.Unlock()

	if _, existsFrom := g.cities[from]; !existsFrom {
		fmt.Printf("%s не існує\n", from)
		return
	}
	if _, existsTo := g.cities[to]; !existsTo {
		fmt.Printf("%s не існує\n", to)
		return
	}

	g.routes[Route{From: from, To: to}] = price
	fmt.Printf("Маршрут створено між %s і %s, ціна - %d\n", from, to, price)

}

func (g *Graph) removeRoute(from, to string) {
	g.mutex.Lock()
	defer g.mutex.Unlock()
	delete(g.routes, Route{From: from, To: to})
	fmt.Printf("Маршрут видалено між %s і %s\n", from, to)
}

func (g *Graph) updatePrice(from, to string, newPrice int) {
	g.mutex.Lock()
	defer g.mutex.Unlock()
	if _, ok := g.routes[Route{From: from, To: to}]; ok {
		delete(g.routes, Route{From: from, To: to})
		g.routes[Route{From: from, To: to}] = newPrice
		fmt.Printf("Нова ціна між %s і %s становить - %d\n", from, to, newPrice)
	} else {
		fmt.Printf("Маршрут від %s до %s не знайдено\n", from, to)
	}
}

func (g *Graph) betweenCities(X, Y string) int {
	if _, existsFrom := g.cities[X]; !existsFrom {
		fmt.Printf("\n%s не існує\n", X)
	} else if _, existsTo := g.cities[Y]; !existsTo {
		fmt.Printf("\n%s не існує \n", Y)
	} else {
		price := g.findPrice(X, Y)
		if price > 0 {
			fmt.Printf("\nЦіна між %s і %s $%d\n", X, Y, price)
		} else {
			fmt.Printf("\nНемає маршруту між %s і %s\n", X, Y)
		}
	}
	return 0
}

func (g *Graph) findPrice(X, Y string) int {
	g.mutex.RLock()
	defer g.mutex.RUnlock()
	visited := make(map[string]bool)
	return g.DFS(X, Y, visited, 0)
}

func (g *Graph) DFS(current, target string, visited map[string]bool, currentPrice int) int {
	if current == target {
		return currentPrice
	}
	visited[current] = true
	for route, price := range g.routes {
		if route.From == current && !visited[route.To] {
			if price := g.DFS(route.To, target, visited, currentPrice+price); price > 0 {
				return price
			}
		}
	}
	return 0
}

func main() {
	graph := newGraph()
	fmt.Println()

	City1 := "Kyiv"
	City2 := "Zhytomyr"
	City3 := "Lutsk"
	City4 := "Lviv"
	City5 := "Ivano-Frankivsk"

	// Додавання міст і маршрутів
	graph.addCity(City1)
	graph.addCity(City2)
	graph.addCity(City3)
	graph.addCity(City4)
	graph.addCity(City5)
	fmt.Println()
	graph.addRoute(City1, City2, 300)
	graph.addRoute(City2, City3, 400)
	graph.addRoute(City3, City4, 700)
	graph.addRoute(City2, City5, 500)
	graph.addRoute(City3, City5, 450)

	// Оновлення ціни маршруту
	fmt.Println()
	graph.updatePrice(City1, City2, 350)

	// Видалення маршрута
	fmt.Println()
	graph.removeRoute(City3, City5)

	// Видалення міста та пов'язаних маршрутів
	fmt.Println()
	graph.removeCity(City5)

	// Виведення графу
	fmt.Println("\n\tМіста:", graph.cities)
	fmt.Println("\nМаршрути:")
	for route, price := range graph.routes {
		fmt.Printf("Від %s до %s - $%d\n", route.From, route.To, price)
	}

	CityX := City1
	CityY := City4

	// Знаходження ціни між двома містами
	graph.betweenCities(CityX, CityY)
}
