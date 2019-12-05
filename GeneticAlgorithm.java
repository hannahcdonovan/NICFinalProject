import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;
import java.util.Collections;

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
    private Population currentParentPopulation;


    /**
    * The population of offspring and parents used for PSO 
    */
    private Population parentAndOffspringPopulation;

            //DIFFERENCE BETWEEN CURRENTPOPULATION AND PSOPOPULATION
    // have currentPopulation which is size N
    // have psoPopulation which will be size 2N (N parents + N offspring)
    // do PSO on the psoPopulation
    // then SELECT on the psoPopulation 
    // the resulting population should then be size N -> use this as the new "current population"
    // repeat the process 



            //OVERALL WHAT WE WANT TO DO 
    //mate the individuals in the currentPopulation
    //add parents and these offspring to psoPopulation
    //run PSO on psoPopulation
    //do mutation on the psoPopulation
    //select from the psoPopulation to get N individuals
    //this becomes the new currentPopulation
    //repeat the process

    /**
     * The best fitness we find after optimize
     */
    private int bestOverallFitness;

    private static Random RANDOM_GENERATOR = new Random();

    public GeneticAlgorithm(int popSize, Problem problem, int numCities, String selectionType, 
                            double crossoverProb, double mutationProb, int iterations,
                            Population currentParentPopulation, Population parentAndOffspringPopulation) {
        this.popSize = popSize;
        this.problem = problem;
        this.numCities = numCities;
        this.selectionType = selectionType;
        this.crossoverProb = crossoverProb;
        this.mutationProb = mutationProb;
        this.iterations = iterations;
        this.currentParentPopulation = currentParentPopulation;
        this.parentAndOffspringPopulation = parentAndOffspringPopulation;
    }

    public Population boltzmannSelection() {
        List<Individual> offspring = this.parentAndOffspringPopulation.getIndividualList();
        Population newPop = new Population(offspring.size());
        double totalFitness = 0.0;

        // Calculate total fitness
        for (int i = 0; i < offspring.size(); i++) {
            totalFitness += offspring.get(i).getFitness();
        }

        // evaluate the normalizing denominator
        double denom = Math.exp(totalFitness);

        int j = 0;
        while (newPop.size() < this.parentAndOffspringPopulation.size()) {
            double probabilityPicker = RANDOM_GENERATOR.nextDouble();
            double numerator = Math.exp(offspring.get(j).getFitness());
            if (probabilityPicker < (numerator / denom)) {
                newPop.addIndividual(offspring.get(j));
            }
            j++;
        }
        return newPop;
    }


    public Population tournamentSelection() {
        List<Individual> offspring = this.parentAndOffspringPopulation.getIndividualList();
        Population newPop = new Population(offspring.size());
        int j = 0;

        while (j < 2) {
            for (int i = 0; i < offspring.size() - 1; i++) {
                if (offspring.get(i).getFitness() <= offspring.get(i + 1).getFitness()) {
                    newPop.addIndividual(offspring.get(i));
                } else {
                    newPop.addIndividual(offspring.get(i + 1));
                }
            }
            j++;
        }
        return newPop;
        
    }

    /**
     * Heuristic Crossover
     */
    public Individual heuristicCrossover(Individual parent1, Individual parent2) {
        Individual offspring = new Individual(this.problem);

        int startingCity = RANDOM_GENERATOR.nextInt(this.numCities);

        List<Integer> tour1 = parent1.getTour();
        List<Integer> tour2 = parent2.getTour();

        //rotate lists to make the starting city first for both 
        int tour1RotationAmount = tour1.size() - tour1.indexOf(startingCity);
        int tour2RotationAmount = tour2.size() - tour2.indexOf(startingCity);

        Collections.rotate(tour1, tour1RotationAmount);
        Collections.rotate(tour2, tour2RotationAmount);

        System.out.println("parent 1 - " + parent1 +  " fitness -> " + parent1.getFitness());
        System.out.println("parent 2 - " + parent2 +  " fitness -> " + parent2.getFitness());

        // Update the starting cities in both parents tours
        moveStartingCity(tour1, startingCity);
        moveStartingCity(tour2, startingCity);

        // initialize offspring tour
        offspring.initialize(startingCity);
        
        List<Integer> offspringTour = offspring.getTour();

        int i = 1;
        int j = 1;
        int offspringCounter = 1;

        while ((i < offspringTour.size()) && (j < offspringTour.size())) {
            if (offspringTour.contains(tour1.get(i)) && offspringTour.contains(tour2.get(j))) {
                i++;
                j++;
            } else if (offspringTour.contains(tour1.get(i))) {
                offspringTour.set(offspringCounter, tour2.get(j));
                j++;
                offspringCounter++;
            } else if (offspringTour.contains(tour2.get(j))) {
                offspringTour.set(offspringCounter, tour1.get(i));
                i++;
                offspringCounter++;
            } else {
                int lastCity = offspringTour.get(numCities - 1);
                if (this.problem.getDistance(lastCity, tour1.get(i)) < this.problem.getDistance(lastCity, tour2.get(j))) {
                    offspringTour.set(offspringCounter, tour1.get(i));
                    i++;
                    offspringCounter++;
                } else {
                    offspringTour.set(offspringCounter, tour2.get(j));
                    j++;
                    offspringCounter++;
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

}