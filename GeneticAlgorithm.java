import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

public class GeneticAlgorithm {

    private int popSize;
    private Problem problem;
    private int numCities;
    private String selectionType;
    private double mutationProb;
    private int iterations;
    private int psoIterations;
    private String neighborhoodType;

    // Notice that the current population will be of size n
    private Population currentParentPopulation;

    // This will be equal to size 2n because it contains 2 generations
    // Note: This will be the population we will perform the embedded PSO algorithm
    // on
    private Population parentAndOffspringPopulation;
    private int bestOverallFitness;
    private static Random RANDOM_GENERATOR = new Random();

    /**
     * Constructor for the Genetic Algorithms instance. Includes necessary
     * parameters for the PSO instance because the PSO algorithm is embedded within
     * GA.
     * 
     * Note: The order the methods in this class appear mimic the order in which
     * they are executed in the algorithm
     * 
     * Steps of the algorithm (to be performed over a number of iterations): 
     * 1.Randomly generate a population of individuals of size popSize (call this n)
     * 2. Perform heuristic crossover on individuals in currentPopulation, yielding
     * an offspring pop of size n 
     * 3. Add these individuals to the population  parentAndOffspring pop 
     * 4. Perform PSO on the parentAndOffspring pop, yielding an adjusted pop of size 2n 
     * 5. Do mutation on the PSOified parentAndOffspring pop with probability mutationProb 
     * 6. Do selection on the parentAndOffspring pop, to yield n individuals 
     * 7. Set this population equal to the current population and repeat the process
     * 
     * @param popSize          The population size.
     * @param problem          Instance of the Problem object, containing relevant
     *                         TSP problem info.
     * @param numCities        The number of cities in the given TSP problem.
     * @param selectionType    String indicating the type of selection -- Tournament
     *                         or Rank -- to perform.
     * @param mutationProb     The probability of mutating an individual or not.
     * @param iterations       The number of iterations to run the overall GA
     *                         algorithm for.
     * @param neighborhoodType The type of neighborhood for PSO -- von Neumann,
     *                         Ring, Global, or Random.
     * @param psoIterations    The number of iterations to run the embedded PSO
     *                         algorithm for.
     */
    public GeneticAlgorithm(int popSize, Problem problem, int numCities, String selectionType, double mutationProb,
            int iterations, String neighborhoodType, int psoIterations) {
        this.popSize = popSize;
        this.problem = problem;
        this.numCities = this.problem.getNumCities();
        this.selectionType = selectionType;
        this.mutationProb = mutationProb;
        this.iterations = iterations;

        // Initialize new population instances & randomly generate a population for the
        // current pop
        // Step 1 of the algorithm
        this.currentParentPopulation = new Population(popSize);

        // Step 1 of algorithm
        this.currentParentPopulation.generateRandomPopulation(this.problem);

        // Initialize the offspring population to two times the size of popSize (see
        // notes in constructor for why)
        this.parentAndOffspringPopulation = new Population(popSize * 2);

        this.neighborhoodType = neighborhoodType;
        this.psoIterations = psoIterations;
    }

    /**
     * Step 2 & 3: Perform heuristic crossover on individuals in currentPopulation,
     * yielding an offspring pop of size n & add individuals created during
     * crossover and parents to parentAndOffspring population
     * 
     * During this step, we also take the currentPopulation (i.e. the parents) and
     * the individuals we create during crossover and add them both to the
     * parentAndOffspring population instance.
     */
    public void performParentCrossover() {

        // Offspring list that we are going to add to when we perform crossover amongst
        // parents
        List<Individual> newOffSpring = new ArrayList<Individual>();

        while (newOffSpring.size() < this.popSize) {

            // Randomly choose two parents to cross
            int index1 = RANDOM_GENERATOR.nextInt(popSize);
            int index2 = RANDOM_GENERATOR.nextInt(popSize);

            while(index2 == index1) {
                index2 = RANDOM_GENERATOR.nextInt(popSize);
            }
            Individual parent1 = this.currentParentPopulation.getIndividual(index1);
            Individual parent2 = this.currentParentPopulation.getIndividual(index2);

            // Perform the crossover & add it to the offspring list
            Individual offspring = this.currentParentPopulation.heuristicCrossover(parent1, parent2);
            newOffSpring.add(offspring);
        }

        // Reset the parentAndOffspringPopulation and add both the parents and offspring
        // to the
        // parentAndOffspringPopulation Population instance
        this.parentAndOffspringPopulation.resetList();
        this.parentAndOffspringPopulation.updateIndividualList(newOffSpring);
        this.parentAndOffspringPopulation.updateIndividualList(this.currentParentPopulation.getIndividualList());

    }

    /**
     * Step 5: Perform mutation after the parentAndOffspringPopulation has been
     * PSOified.
     * 
     * Method to actually perform mutatation on the parentAndOffspringPopulation.
     * Iterates through the individual list within the Population object and chooses
     * whether or not to mutate each individual with a mutation probability.
     */
    public void executeMutation() {
        for (Individual ind : this.parentAndOffspringPopulation.getIndividualList()) {

            double randDub = RANDOM_GENERATOR.nextDouble();

            if (randDub < this.mutationProb) {
                ind.mutate();
            }
        }
    }

    /**
     * Rank Selection - One of the possible selection types.
     * 
     * Step 6: After the parentAndOffspringPopulation has been PSOified and mutated,
     * then select to bring the population size back to size popSize (rather than
     * 2*popSize).
     *
     * 
     * @return A population object representing the next generation that will go on
     *         and breed at the start of the next iteration of the algorithm.
     */
    public Population rankSelection() {
        List<Individual> parentsAndOffsprings = this.parentAndOffspringPopulation.getIndividualList();
        Population nextGeneration = new Population(this.popSize);

        Collections.sort(parentsAndOffsprings);

        // Scaled list -- i.e. one containing a number of individuals proportion to its
        // fitness
        // Used to give higher priority to more fit individuals
        List<Individual> probScaleList = new ArrayList<Individual>();

        for (int i = 0; i < parentsAndOffsprings.size(); i++) {
            for (int j = 0; j < i + 1; j++) {
                probScaleList.add(parentsAndOffsprings.get(i));
            }
        }

        // Next generation of individuals
        List<Individual> nextGenIndividuals = new ArrayList<Individual>();

        while (nextGenIndividuals.size() < this.popSize) {
            // Since we are using the scaled list, we can just randomly pick and index
            int randIndex = RANDOM_GENERATOR.nextInt(probScaleList.size());
            Individual randIndividual = probScaleList.get(randIndex);
            // Make sure not adding individuals more than once
            if (!nextGenIndividuals.contains(randIndividual)) {
                nextGenIndividuals.add(randIndividual);
            }
        }
        nextGeneration.updateIndividualList(nextGenIndividuals);
        return nextGeneration;

    }

    /**
     * Tournament Selection - One of the possible selection types.
     * 
     * Step 6: After the parentAndOffspringPopulation has been PSOified and mutated,
     * then select to bring the population size back to size popSize (rather than
     * 2*popSize).
     * 
     * @return A population object representing the next generation that will go on
     *         and breed at the start of the next iteration of the algorithm.
     */
    public Population tournamentSelection() {
        List<Individual> offspringAndParents = this.parentAndOffspringPopulation.getIndividualList();
        Population nextGeneration = new Population(this.popSize);

        Collections.shuffle(offspringAndParents);

        // Iterating every 2 makes sure that we dont accidentally select the same
        // individual twice
        for (int i = 0; i < offspringAndParents.size() - 1; i += 2) {
            // Select the individual with a lower fitness
            if (offspringAndParents.get(i).getFitness() <= offspringAndParents.get(i + 1).getFitness()) {
                nextGeneration.addIndividual(offspringAndParents.get(i));
            } else {
                nextGeneration.addIndividual(offspringAndParents.get(i + 1));
            }
        }
        return nextGeneration;
    }

    /**
     * Helper method for performing selection (Step 6). Determines which type of
     * selection to perform -- Tournament or Rank.
     * 
     * @return Population object, which is the next generation of individuals to be
     *         used at the beginning of the next iteration of the algorithm.
     */
    public Population doSelection() {

        if (this.selectionType.equals("ts")) {
            return this.tournamentSelection();
        } else {
            return this.rankSelection();
        }
    }

    /**
     * Used to calculate the best individual in a given population. Used as a helper
     * method when we optimize GA.
     * 
     * @param pop The population we are pulling the best Individual from.
     * @return The best Individual instance in the population object.
     */
    public Individual findBestInPop(Population pop) {

        Individual bestSoFar = new Individual(this.problem);
        double bestScoreSoFar = Double.POSITIVE_INFINITY;

        for (Individual ind : pop.getIndividualList()) {
            if (ind.getFitness() < bestScoreSoFar) {
                bestScoreSoFar = ind.getFitness();
                bestSoFar = ind.copyIndividual();
            }
        }
        return bestSoFar;
    }

    /**
     * Pulls all the steps together for the total algorithm. Has the PSO algorithm
     * embedded within it.
     * 
     * @return A list of all the best individuals the algorithm finds at each 100
     *         iteration snapshot. Used for testing purposes.
     */
    public List<Double> optimize() {
        // List we will add the best fitnesses to
        List<Double> fitnessResultsList = new ArrayList<Double>();

        // Dummy individual data used to make comparisons
        Individual bestIndividualSoFar = new Individual(this.problem);
        double bestFitnessSoFar = Double.POSITIVE_INFINITY;

        for (int i = 0; i < this.iterations; i++) {
            // Crossover the original population and update parentAndOffspringPopulation
            this.performParentCrossover();

            // Embedded PSO algorithm
            PSO pso = new PSO(this.parentAndOffspringPopulation, this.neighborhoodType, this.psoIterations);
            pso.optimize();
            this.executeMutation();

            Population nextGen = this.doSelection();

            // Determine if the iteration best is better than the overall best
            Individual bestInPop = this.findBestInPop(nextGen);
            if (bestInPop.getFitness() < bestFitnessSoFar) {
                bestFitnessSoFar = bestInPop.getFitness();
                bestIndividualSoFar = bestInPop.copyIndividual();
            }

            // Set the selected population equal to the current pop
            this.currentParentPopulation = nextGen;

            System.out.println(i + " BEST -> " + bestIndividualSoFar);

            // For testing purposes
            if (i % 100 == 0) {
                fitnessResultsList.add(bestFitnessSoFar);
            }

        }
        return fitnessResultsList;
    }
}