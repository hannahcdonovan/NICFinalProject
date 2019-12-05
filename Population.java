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
        Individual dummy = new Individual(problem);
        for (int i = 0; i < this.popSize; i++) {
            Individual ind = dummy.makeRandomIndividual();
            individualList.add(ind);
        }
    }

    public List<Individual> getIndividualList() {
        return individualList;
    }

    public void addIndividual(Individual ind) {
        individualList.add(ind);
    }

    public int size() {
        return this.popSize;
    }
}