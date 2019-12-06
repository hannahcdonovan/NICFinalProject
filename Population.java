import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Population {

    private List<Individual> individualList;

    private int popSize;

    private static Random RANDOM_GENERATOR = new Random();

    public Population(int popSize) {
        this.popSize = popSize;
        individualList = new ArrayList<Individual>();
    }

    public void generateRandomPopulation(Problem problem) {
        Individual dummy = new Individual(problem);
        for (int i = 0; i < this.popSize; i++) {
            Individual ind = dummy.makeRandomIndividual();
            individualList.add(ind);
        }
    }

    private static void moveStartingCity(List<Integer> tour, int startingCity) {
        int startingCityIndex = tour.indexOf(startingCity);
        tour.remove(startingCityIndex);
        tour.add(0, startingCity);
    }

    /**
     * Heuristic Crossover
     */
    public Individual heuristicCrossover(Individual parent1, Individual parent2) {
        Problem problem = parent1.getProblem();
        Individual offspring = new Individual(problem);

        int startingCity = RANDOM_GENERATOR.nextInt(problem.getNumCities());

        List<Integer> tour1 = parent1.getTour();
        List<Integer> tour2 = parent2.getTour();

        //rotate lists to make the starting city first for both 
        int tour1RotationAmount = tour1.size() - tour1.indexOf(startingCity);
        int tour2RotationAmount = tour2.size() - tour2.indexOf(startingCity);

        Collections.rotate(tour1, tour1RotationAmount);
        Collections.rotate(tour2, tour2RotationAmount);

        //System.out.println("parent 1 - " + parent1 +  " fitness -> " + parent1.getFitness());
        //System.out.println("parent 2 - " + parent2 +  " fitness -> " + parent2.getFitness());

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
                int lastCity = offspringTour.get(problem.getNumCities() - 1);
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

    public List<Individual> getIndividualList() {
        return individualList;
    }

    public Individual getIndividual(int i) {
        return this.individualList.get(i);
    }

    public void addListIndividuals(List<Individual> inds) {
        for(int i = 0; i < inds.size(); i++) {
            this.addIndividual(inds.get(i));
        }
    }

    public void resetList() {
        this.individualList = new ArrayList<Individual>();
    }

    public void addIndividual(Individual ind) {
        individualList.add(ind);
    }

    public int size() {
        return this.popSize;
    }

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