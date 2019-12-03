import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Individual {
    
    private List<Integer> tour;

    private Problem problem;

    private static Random RANDOM_GENERATOR = new Random();

    private double fitness;

    public Individual(Problem problem) {
        this.problem = problem;
        tour = new ArrayList<Integer>();

    }

    public Individual makeRandomIndividual() {
        Individual randIndividual = new Individual(this.problem);
        int numCities = this.problem.getNumCities();
        Set<Integer> visited = new HashSet<Integer>();

        while (visited.size() < numCities) {
            int nextCity = RANDOM_GENERATOR.nextInt(numCities);

            while (visited.contains(nextCity)) {
                nextCity = RANDOM_GENERATOR.nextInt(numCities);
            }
            randIndividual.getTour().add(nextCity);
            visited.add(nextCity);
        }
        return randIndividual;
    }

    public double evalFitness() {
        double fitnessTotal = 0.0;
        int numCities = this.problem.getNumCities();

        for (int i = 0; i < numCities - 1; i++) {
            fitnessTotal += this.problem.getDistance(tour.get(i), tour.get(i+1));
        }
        fitnessTotal += this.problem.getDistance(tour.get(numCities - 1), tour.get(0));

        return fitnessTotal;
    }

    public void setFitness() {
        double calculatedFitness = this.evalFitness();
        this.fitness = calculatedFitness;
    }

    public List<Integer> getTour() {
        return tour;
    }

    /**
     * Used when we do crossover
     */
    public void initialize(int startingCity) {
        for (int i = 0; i < this.problem.getNumCities(); i++) {
            tour.add(startingCity);
        }
    }

    public void setTour(List<Integer> newTour) {
        tour = newTour;
    }

    public String toString() {
        String representation = "";
        for (int i = 0; i < this.problem.getNumCities(); i++) {
            representation += tour.get(i) + ".";
        }
        return representation;
    }

}