import java.util.List;
import java.util.ArrayList;

public class Population {

    private List<Individual> individualList;

    private int popSize;

    public Population(int popSize) {
        this.popSize = popSize;
        individualList = new ArrayList<Individual>();
    }

    public void generateRandomPopulation(Problem problem) {
        Individual individual = new Individual(problem);
        for (int i = 0; i < this.popSize; i++) {
            individualList.add(individual.makeRandomIndividual());
        }
    }

    public List<Individual> getIndividualList() {
        return individualList;
    }
}