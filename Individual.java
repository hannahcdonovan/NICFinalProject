import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class Individual implements Comparable<Individual> {
    
    private List<Integer> tour;

    private Problem problem;

    private static Random RANDOM_GENERATOR = new Random();

    private double fitness;

    private Neighborhood neighborhood;

    public Individual(Problem problem) {
        this.problem = problem;
        tour = new ArrayList<Integer>();
        this.neighborhood = new Neighborhood(new ArrayList<Individual>());

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
        randIndividual.evalAndSetFitness();
        return randIndividual;
    }

    public double evalAndSetFitness() {
        double fitnessTotal = 0.0;
        int numCities = this.problem.getNumCities();

        for (int i = 0; i < numCities - 1; i++) {
            fitnessTotal += this.problem.getDistance(tour.get(i), tour.get(i+1));
        }
        fitnessTotal += this.problem.getDistance(tour.get(numCities - 1), tour.get(0));
        this.fitness = fitnessTotal;

        return fitnessTotal;
    }



    /**
     * For some probability at each city in the Individual's tour, the city may be swapped
     * with some other random city. This will add some randomness but will also maintain the 
     * integrity of the tour. 
     */
    public void mutate() {

        int randIndex = RANDOM_GENERATOR.nextInt(this.problem.getNumCities());
        int city1 = this.tour.get(randIndex);

        int randIndex2 = RANDOM_GENERATOR.nextInt(this.problem.getNumCities());
        int city2 = this.tour.get(randIndex2);

        this.tour.set(randIndex, city2);
        this.tour.set(randIndex2, city1);

        this.evalAndSetFitness();
    }


    public double getFitness() {
        return this.fitness;
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
        this.tour = newTour;
        this.evalAndSetFitness();
    }

    public String toString() {
        String representation = "";
       //for (int i = 0; i < this.problem.getNumCities(); i++) {
         //   representation += tour.get(i) + ".";
        //}
        representation += " -> " + this.fitness;
        return representation;
    }

    public int compareTo(Individual otherInd) {
        return (int) Math.round(otherInd.fitness - this.fitness); 
    }

    public Individual copyIndividual() {
        Individual newInd = new Individual(this.problem);
        newInd.setTour(this.tour);
        return newInd;
    }

    public void setNeighborhood(Neighborhood newNeighborhood) {
        this.neighborhood = newNeighborhood;
    }

    public Neighborhood getNeighborhood() {
        return this.neighborhood;
    }

    public Problem getProblem() {
        return this.problem;
    }

}