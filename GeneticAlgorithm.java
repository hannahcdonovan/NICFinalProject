import java.util.Random;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.lang.Math;

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

    private String neighborhoodType;


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
                            double crossoverProb, double mutationProb, int iterations, String neighborhoodType) {
        this.popSize = popSize;
        this.problem = problem;
        this.numCities = this.problem.getNumCities();
        this.selectionType = selectionType;
        this.crossoverProb = crossoverProb;
        this.mutationProb = mutationProb;
        this.iterations = iterations;
        this.currentParentPopulation = new Population(this.problem, popSize);
        this.currentParentPopulation.generateRandomPopulation(this.problem);
        this.parentAndOffspringPopulation = new Population(this.problem, popSize * 2);
        this.neighborhoodType = neighborhoodType;
    }

    public Population boltzmannSelection() {
        List<Individual> offspring = this.parentAndOffspringPopulation.getIndividualList();
        Population newPop = new Population(this.problem, offspring.size());
        double totalFitness = 0.0;

        // Calculate total fitness
        for (int i = 0; i < offspring.size(); i++) {
            totalFitness += offspring.get(i).getFitness();
        }

        // evaluate the normalizing denominator
        double denom = Math.exp(totalFitness);

        int j = 0;
        int counter = 0;
        while (counter < this.currentParentPopulation.size()) {
            double probabilityPicker = RANDOM_GENERATOR.nextDouble();
            double numerator = Math.exp(offspring.get(j).getFitness());
            if (probabilityPicker < (numerator / denom)) {
                newPop.addIndividual(offspring.get(j));
                counter++;
            }
            j++;
        }
        return newPop;
    }


    public Population rankSelection() {
        List<Individual> inds = this.parentAndOffspringPopulation.getIndividualList();
        Population newPop = new Population(this.problem, this.popSize);

        Collections.sort(inds);

        System.out.println(this.parentAndOffspringPopulation);

        List<Individual> probScaleList = new ArrayList<Individual>();

        for(int i = 0; i < inds.size(); i++){
            for(int j = 0; j < i + 1; j++) {
                probScaleList.add(inds.get(i));
            }
        }

        List<Individual> newIndsList = new ArrayList<Individual>();

        while(newIndsList.size() < this.popSize) {
            int randIndex = RANDOM_GENERATOR.nextInt(probScaleList.size());
            Individual randIndividual = probScaleList.get(randIndex);
                
            if(!newIndsList.contains(randIndividual)) {
                newIndsList.add(randIndividual);
            }
        }
        newPop.addListIndividuals(newIndsList);
        return newPop;

    }


    public Population tournamentSelection() {
        List<Individual> offspringAndParents = this.parentAndOffspringPopulation.getIndividualList();
        Population newPop = new Population(this.problem, this.popSize);

        Collections.shuffle(offspringAndParents);

        for (int i = 0; i < offspringAndParents.size() - 1; i += 2) {
            if (offspringAndParents.get(i).getFitness() <= offspringAndParents.get(i + 1).getFitness()) {
                newPop.addIndividual(offspringAndParents.get(i));
            } else {
                newPop.addIndividual(offspringAndParents.get(i + 1));
            }
        }
        return newPop;
    }

    /**
     * Performs crossover of parent popultion to create popSize offspring. The parents and the 
     * new offspring will be added to the parentAndOffspringPopulation. 
     */
    public void performParentCrossover() {

        List<Individual> newOffSpring = new ArrayList<Individual>();

        while(newOffSpring.size() < this.popSize){

            double randDub = RANDOM_GENERATOR.nextDouble();
            if(randDub < this.crossoverProb) {
                Individual parent1 = this.currentParentPopulation.getIndividual(RANDOM_GENERATOR.nextInt(popSize));
                Individual parent2 = this.currentParentPopulation.getIndividual(RANDOM_GENERATOR.nextInt(popSize));

                Individual offspring = this.currentParentPopulation.heuristicCrossover(parent1, parent2);
                newOffSpring.add(offspring);
            }
        }

        this.parentAndOffspringPopulation.resetList();
        this.parentAndOffspringPopulation.addListIndividuals(newOffSpring);
        this.parentAndOffspringPopulation.addListIndividuals(this.currentParentPopulation.getIndividualList());

    }


    public void optimize() {
        // Only going to hold the first random pop
        System.out.println(this.currentParentPopulation);
        System.out.println(this.parentAndOffspringPopulation);

        this.performParentCrossover();
        System.out.println("** Parent Population **");
        System.out.println(this.currentParentPopulation);

        System.out.println("** Parent and Offspring Population*");
        System.out.println(this.parentAndOffspringPopulation);

        //PSO here
        PSO pso = new PSO(this.parentAndOffspringPopulation, this.neighborhoodType, 10);
        Population nextPop = pso.optimize();
        System.out.println(this.parentAndOffspringPopulation);


        Population newPop = this.tournamentSelection();
        System.out.println("** TS Selected Population **");
        System.out.println(newPop);


        Population newPop2 = this.rankSelection();
        System.out.println("** RS Selected Population **");
        System.out.println(newPop2);

    }



}