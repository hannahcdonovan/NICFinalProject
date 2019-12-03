import java.util.Random;
import java.util.Set;
import java.util.HashSet;

public class Individual {
    
    private int[] individualList;

    private Problem problem;

    private static Random RANDOM_GENERATOR = new Random();

    private double fitness;

    public Individual(Problem problem) {
        this.problem = problem;

        individualList = new int[this.problem.getNumCities()];
    }

    public void makeRandomIndividual() {
        double[][] distanceMatrix = this.problem.getDistanceMatrix();
        int numCities = this.problem.getNumCities();

        int startingCityId = RANDOM_GENERATOR.nextInt(numCities);

        Set<Integer> visited = new HashSet<Integer>();
        visited.add(startingCityId);

        while (visited.size() < numCities) {
            for (int i = 0; i < individualList.length; i++) {
                int nextCity = RANDOM_GENERATOR.nextInt(numCities);
                if (!visited.contains(nextCity)) {
                    individualList[i] = nextCity;
                    visited.add(nextCity);
                } 
            }
        }
    }

    public String toString() {
        String representation = "";
        for (int i = 0; i < individualList.length; i++) {
            representation += individualList[i] + ".";
        }
    }

}