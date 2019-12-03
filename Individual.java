import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class Individual {
    
    private int[] tour;

    private Problem problem;

    private static Random RANDOM_GENERATOR = new Random();

    private double fitness;

    public Individual(Problem problem) {
        this.problem = problem;

        tour = new int[this.problem.getNumCities()];
    }

    public void makeRandomIndividual() {
        int numCities = this.problem.getNumCities();

        int startingCityId = RANDOM_GENERATOR.nextInt(numCities);

        Set<Integer> visited = new HashSet<Integer>();
        visited.add(startingCityId);

        while (visited.size() < numCities) {
            for (int i = 0; i < tour.length; i++) {
                int nextCity = RANDOM_GENERATOR.nextInt(numCities);
                if (!visited.contains(nextCity)) {
                    tour[i] = nextCity;
                    visited.add(nextCity);
                }
            }
        }
    }

    public int[] getTour() {
        return tour;
    }

    public String toString() {
        String representation = "";
        for (int i = 0; i < tour.length; i++) {
            representation += tour[i] + ".";
        }
        return representation;
    }

}