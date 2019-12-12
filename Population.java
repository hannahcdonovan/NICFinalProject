import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Population {

    private List<Individual> individualList;

    private int popSize;

    private static Random RANDOM_GENERATOR = new Random();

    /**
     * Constructor for the Population object.
     */
    public Population(int popSize) {
        this.popSize = popSize;
        individualList = new ArrayList<Individual>();
    }

    /**
     * Used for Step 1 of the overarching algorithm.
     * Fills the individualList instance with randomly constructed individuals.
     */
    public void generateRandomPopulation(Problem problem) {
        Individual dummy = new Individual(problem);
        for (int i = 0; i < this.popSize; i++) {
            Individual ind = dummy.makeRandomIndividual();
            individualList.add(ind);
        }
    }

    /**
     * Heuristic Crossover
     * 
     * Semi-greedy method that is explained in more detail in our report!
     * 
     * @param parent1 The first parent that we would like to cross.
     * @param parent2 The second parent that we would like to cross.
     * @return The crossed over Individual.
     */
    public Individual heuristicCrossover(Individual parent1, Individual parent2) {
        Problem problem = parent1.getProblem();
        Individual offspring = new Individual(problem);

        int startingCity = RANDOM_GENERATOR.nextInt(problem.getNumCities());

        List<Integer> tour1 = parent1.getTour();
        List<Integer> tour2 = parent2.getTour();

        Individual copy1 = parent1.copyIndividual();
        Individual copy2 = parent2.copyIndividual();

        //rotate lists to make the starting city first for both 
        int tour1RotationAmount = tour1.size() - tour1.indexOf(startingCity);
        int tour2RotationAmount = tour2.size() - tour2.indexOf(startingCity);

        Collections.rotate(tour1, tour1RotationAmount);
        Collections.rotate(tour2, tour2RotationAmount);

        // initialize offspring tour
        offspring.initialize(startingCity);
        
        List<Integer> offspringTour = offspring.getTour();

        int i = 1;
        int j = 1;
        int offspringCounter = 1;

        while ((i < offspringTour.size()) && (j < offspringTour.size())) {
            // Case 1: parent1 and parent2 city are already in the new tour
            if (offspringTour.contains(tour1.get(i)) && offspringTour.contains(tour2.get(j))) {
                i++;
                j++;
            // Case 2: If parent1 city in the new tour, add parent 2 city
            } else if (offspringTour.contains(tour1.get(i))) {
                offspringTour.set(offspringCounter, tour2.get(j));
                j++;
                offspringCounter++;
            // Case 3: If parent2  city in the new tour, add parent1 city
            } else if (offspringTour.contains(tour2.get(j))) {
                offspringTour.set(offspringCounter, tour1.get(i));
                i++;
                offspringCounter++;
            // Case 4: Neither of the cities are in the new tour yet -- add the city that is the least
            // distance from the city that was just added
            } else {
                int lastCity = offspringTour.get(offspringCounter - 1);
                if (problem.getDistance(lastCity, tour1.get(i)) < problem.getDistance(lastCity, tour2.get(j))) {
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

    /*************************************** GETTERS AND SETTERS **********************************************/

    /**
     * Getter to get the Individuals in the Population.
     * @return individualList
     */
    public List<Individual> getIndividualList() {
        return individualList;
    }

    /**
     * Gets an Individual in the individualList based on a given index.
     * 
     * @param i Index at which we want to get an individual.
     * @return An Individual from the individualList based on given index.
     */
    public Individual getIndividual(int i) {
        return this.individualList.get(i);
    }

    /**
     * Adds an Individual to the individualList.
     */
    public void addIndividual(Individual ind) {
        individualList.add(ind);
    }

    /**
     * Update the entire individualList object to contain all elements in new individualList.
     * @param inds List of individuals that we want to update the Population's individualList instance to.
     */
    public void updateIndividualList(List<Individual> inds) {
        for(int i = 0; i < inds.size(); i++) {
            this.addIndividual(inds.get(i));
        }
    }

    /**
     * Clear the individualList instance to be a new, empty ArrayList.
     */
    public void resetList() {
        this.individualList = new ArrayList<Individual>();
    }

    /**
     * Gets the population size.
     * @return The size of the population.
     */
    public int size() {
        return this.popSize;
    }

    /**
     * toString method for the Population object.
     * 
     * @return String representation of Population object. Either the individuals in the individualList or
     *         a string indicating that the population is empty.
     */
    public String toString() {

        if(this.individualList.size() == 0) {
            return "POPULATION is EMPTY";
        }

        String result = "POPULATION: " + "\n";

        for(int i = 0; i < this.individualList.size(); i++) {
            result += this.individualList.get(i).toString() + "\n";
        }
        result += "______________________";
        return result;
    }
}