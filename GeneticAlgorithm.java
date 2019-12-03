import java.util.Random;

public class GeneticAlgorithm {

    /**
     * The population size.
     */
    public int popSize;

    /**
     * Number of cities.
     */
    public int numCities;

    /**
     * The selection type - ts, rs, sg.
     */
    public String selectionType;
    
    /**
     * The probability of actually performing the crossover.
     */
    public double crossoverProb;
    
    /**
     * The probability of mutating each value in the individual.
     */
    public double mutationProb;

    /**
     * The number of iterations.
     */
    public int iterations;

    /**
     * The current population.
     */
    public Population currentPopulation;

    /**
     * The best fitness we find after optimize
     */
    public int bestOverallFitness;

    /**
     * Constructor
     */
    public GeneticAlgorithm() {

    }

    /**
     * Heuristic Crossover
     */
    public Individual crossover(Individual alex, Individual billie) {
        //Construct new individual here

        Random rand = new Random();
        int startingCity = rand.nextInt(Indi)

    }

}