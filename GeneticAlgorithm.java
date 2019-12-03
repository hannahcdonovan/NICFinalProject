import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class GeneticAlgorithm {

    /**
     * The population size.
     */
    private int popSize;

    /**
     * The problem.
     */
    private Problem problem;

    /**
     * Number of cities.
     */
    private int numCities;

    /**
     * The selection type - ts, rs, sg.
     */
    private String selectionType;
    
    /**
     * The probability of actually performing the crossover.
     */
    private double crossoverProb;
    
    /**
     * The probability of mutating each value in the individual.
     */
    private double mutationProb;

    /**
     * The number of iterations.
     */
    private int iterations;

    /**
     * The current population.
     */
    private Population currentPopulation;

    /**
     * The best fitness we find after optimize
     */
    private int bestOverallFitness;

    private static Random RANDOM_GENERATOR = new Random();

    public GeneticAlgorithm(int popSize, Problem problem, int numCities, String selectionType, 
                            double crossoverProb, double mutationProb, int iterations,
                            Population currentPopulation) {
        this.popSize = popSize;
        this.problem = problem;
        this.numCities = numCities;
        this.selectionType = selectionType;
        this.crossoverProb = crossoverProb;
        this.mutationProb = mutationProb;
        this.iterations = iterations;
        this.currentPopulation = currentPopulation;
    }

    /**
     * Heuristic Crossover
     */
    public Individual heuristicCrossover(Individual parent1, Individual parent2) {
        Individual offspring = new Individual(this.problem);

        int startingCity = RANDOM_GENERATOR.nextInt(this.numCities);

        List<Integer> tour1 = parent1.getTour();
        List<Integer> tour2 = parent2.getTour();

        // Update the starting cities in both parents tours
        moveStartingCity(tour1, startingCity);
        moveStartingCity(tour2, startingCity);

        // initialize offspring tour
        offspring.initialize(startingCity);
        
        List<Integer> offspringTour = offspring.getTour();

        int i = 2;
        int j = 2;

        while ((i < offspringTour.size()) && (j < offspringTour.size())) {
            if (offspringTour.contains(tour1.get(i)) && offspringTour.contains(tour2.get(j))) {
                i++;
                j++;
            } else if (offspringTour.contains(tour1.get(i))) {
                concatenate(tour2, j, offspringTour);
                j++;
            } else if (offspringTour.contains(tour2.get(j))) {
                concatenate(tour1, i, offspringTour);
                i++;
            } else {
                int lastCity = offspringTour.get(numCities - 1);
                if (this.problem.getDistance(lastCity, tour1.get(i)) < this.problem.getDistance(lastCity, tour2.get(j))) {
                    concatenate(tour1, i, offspringTour);
                    i++;
                } else {
                    concatenate(tour2, j, offspringTour);
                    j++;
                }
            }
        }

        offspring.setTour(offspringTour);

        return offspring;

    }

    private static void moveStartingCity(List<Integer> tour, int startingCity) {
        int startingCityIndex = tour.indexOf(startingCity);
        tour.remove(startingCityIndex);
        tour.add(0, startingCity);
    }

    private static void concatenate(List<Integer> tour, int startingIndex, List<Integer> offspringTour) {
        int cityVal = -1;
        for (int i = startingIndex; i > 0; i--) {
            cityVal = tour.get(i);
            offspringTour.set(i, cityVal);
        }
    }

}