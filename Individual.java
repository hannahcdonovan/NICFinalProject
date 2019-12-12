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

    /**
     * Constructor for Individual object.
     * 
     * Tours within the Individual object are represented as ArrayLists. We used this to help with
     * containment methods and adding elements to the list.
     */
    public Individual(Problem problem) {
        this.problem = problem;
        // Initialize tour and neighborhood
        tour = new ArrayList<Integer>();
        this.neighborhood = new Neighborhood(new ArrayList<Individual>());

    }

    /**
     * Helper method for calcuating the fitness of an Individual (i.e. the total length of the Individual's tour)
     * Simultaneously sets an Individual's fitness.
     * 
     * @return The fitness of the Individual.
     */
    public double evalAndSetFitness() {
        double fitnessTotal = 0.0;
        int numCities = this.problem.getNumCities();

        for (int i = 0; i < numCities - 1; i++) {
            fitnessTotal += this.problem.getDistance(tour.get(i), tour.get(i + 1));
        }
        fitnessTotal += this.problem.getDistance(tour.get(numCities - 1), tour.get(0));
        this.fitness = fitnessTotal;

        return fitnessTotal;
    }

    /**
     * Method for making a random individual. Used when we initialize our first population.
     * 
     * @return A randomly formed individual that maintains the integrity of a tour structure.
     */
    public Individual makeRandomIndividual() {
        Individual randIndividual = new Individual(this.problem);
        int numCities = this.problem.getNumCities();

        // Used to keep track of what cities we have added to the tour - want to keep the integrity
        // of the tour
        Set<Integer> visited = new HashSet<Integer>();

        while (visited.size() < numCities) {
            int nextCity = RANDOM_GENERATOR.nextInt(numCities);

            // To make sure we don't add the same city twice
            // Keep randomly pulling cities until we find something we haven't yet found
            while (visited.contains(nextCity)) {
                nextCity = RANDOM_GENERATOR.nextInt(numCities);
            }
            // Add the city to the Individual's tour ArrayList object
            randIndividual.getTour().add(nextCity);
            visited.add(nextCity);
        }

        // Calculate the fitness of the Individual after we have constructed it and return it
        randIndividual.evalAndSetFitness();
        return randIndividual;
    }

    /**
     * Method to be called upon in the "executeMutation" method in Genetic Algorithms class.
     * 
     * Just switches two random cities in a tour. Once a mutation has occurred, evaluates and
     * sets the fitness of the mutated Individual.
     */
    public void mutate() {

        int randIndex1 = RANDOM_GENERATOR.nextInt(this.problem.getNumCities());
        int city1 = this.tour.get(randIndex1);

        int randIndex2 = RANDOM_GENERATOR.nextInt(this.problem.getNumCities());
        int city2 = this.tour.get(randIndex2);

        this.tour.set(randIndex1, city2);
        this.tour.set(randIndex2, city1);

        this.evalAndSetFitness();
    }

    /*************************** GETTERS AND SETTERS *************************************************/

    /**
     * Gets an Individual's fitness.
     * 
     * @return The Individual's fitness.
     */
    public double getFitness() {
        return this.fitness;
    }

    /**
     * Gets an Individual's tour.
     * 
     * @return The Individual's tour.
     */
    public List<Integer> getTour() {
        return tour;
    }

    /**
     * Used when we perfom heuristic crossover in Genetic Algorithm.
     * 
     * Just set every value in an Individual's tour to one number.
     */
    public void initialize(int startingCity) {
        for (int i = 0; i < this.problem.getNumCities(); i++) {
            tour.add(startingCity);
        }
    }

    /**
     * Set the Individual's tour to a new tour. Simulateously set and evaluate fitness.
     * 
     * @param newTour The tour we want to set the Individual's tour to.
     */
    public void setTour(List<Integer> newTour) {
        this.tour = newTour;
        this.evalAndSetFitness();
    }

    /**
     * toString method. Represents an Individual as its fitness.
     */
    public String toString() {
        String representation = "";
        // for (int i = 0; i < this.problem.getNumCities(); i++) {
        // representation += tour.get(i) + ".";
        // }
        representation += " -> " + this.fitness;
        return representation;
    }

    /**
     * Used for sorting when we sort for Rank Selection.
     *
     * To enable the object to be sorted.
     */
    public int compareTo(Individual otherInd) {
        return (int) Math.round(otherInd.fitness - this.fitness);
    }

    /**
     * Method for copying an Individual to avoid referencing errors.
     * @return
     */
    public Individual copyIndividual() {
        Individual newInd = new Individual(this.problem);
        newInd.setTour(this.tour);
        return newInd;
    }


    /**
     * Assigns a new neighbhorhood to an individual.
     * @param newNeighborhood
     */
    public void setNeighborhood(Neighborhood newNeighborhood) {
        this.neighborhood = newNeighborhood;
    }

    /**
     * Gets an Individual's neighborhood.
     * @return The Individual's neighborhood.
     */
    public Neighborhood getNeighborhood() {
        return this.neighborhood;
    }

    /**
     * Getter for the Individual's problem.
     * 
     * @return The Individual's problem.
     */
    public Problem getProblem() {
        return this.problem;
    }

}