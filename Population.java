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