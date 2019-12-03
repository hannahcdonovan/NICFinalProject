import java.util.Random;
import java.util.Arrays;

public class GeneticAlgorithm {

    /**
     * The population size.
     */
    public int popSize;

    /**
     * The problem.
     */
    public Problem problem;

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
        Individual charlie = new Individual(this.problem);

        Random rand = new Random();
        int startingCity = rand.nextInt(numCities)

        int[] x1 = alex.getTour();
        int[] x2 = billie.getTour();

        int x1StartPos = 



    }

    private static int[] moveStartingCity(int[] tour, int startingCity) {
        int[] result = int[tour.size()];
        int startingCityIndex = Arrays.asList(tour).indexOf(startingCity);
        

        for()


    }

}