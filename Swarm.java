import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Swarm {


    /*
     * List containing all of the Particles in the Swarm
     */
    private Population parentAndOffspringPopulation;


    /*
     * The number of particles in the Swarm
     */
    private int numIndividuals; 


    /*
     * List containing all of the Neighborhoods that comprise the Swarm
     */
    private List<Neighborhood> neighborhoods;


    private List<Individual> individualList;


    private final double NEIGHBORHOOD_CHANGE_PROB = 0.2;

    final int NEIGHBORHOOD_SIZE = 5;



    /* 
     *   Constructor which creates a Swarm with numParticles number of particles. 
     *
     *   @param numParticles - the number of Particles in the Swarm
     */
    public Swarm(Population parentAndOffspringPopulation) {
        this.numIndividuals = parentAndOffspringPopulation.size();
        this.parentAndOffspringPopulation = parentAndOffspringPopulation;
        this.individualList = this.parentAndOffspringPopulation.getIndividualList();
        neighborhoods = new ArrayList<Neighborhood>();
    }


    /*
     * Creates neighborhoods that conform to a ring topology. This means that the particles 
     * are imagined to be in a ring and each particle's neighborhood is comprised of the 
     * particle to its left, the particle to its right, and itself. This method will create a 
     * neighborhood for each particle in the Swarm and add it to neighborhoods
     */    
    public void makeRingNeighborhood() {

        if (numIndividuals > 2) {
            for (int i = 0; i < this.numIndividuals; i++) {

                int beforeIndex = (i - 1) % this.individualList.size();
                int afterIndex = (i + 1) % this.individualList.size();

                if (beforeIndex == -1) {
                    beforeIndex = this.individualList.size() - 1;
                }

                Individual before = this.individualList.get(beforeIndex);
                Individual after = this.individualList.get(afterIndex);

                List<Individual> neighborhoodList = new ArrayList<Individual>();
                neighborhoodList.add(before);
                neighborhoodList.add(after);
                neighborhoodList.add(this.individualList.get(i));

                Neighborhood newNeighborhood = new Neighborhood(neighborhoodList);
                this.individualList.get(i).setNeighborhood(newNeighborhood);
                neighborhoods.add(newNeighborhood);
            }

        } else {
            System.out.println("size smaller than two");
        }
    }


    /*
     * Creates neighborhoods for the Swarm which conform to the Von Neumann topology. This means that
     * the particles can be imagined as arranged in a grid. Each particle's neighborhood is comprised
     * of the particle to its left, the particle to its right, the particle above it, the particle below
     * it, and itself. This grid is imagined as wrapped around, where the particle "above" a particle in the 
     * top row is the particle in the same column in the bottom row. Similarly, the particle to the "right" of 
     * a particle in the rightmost column is the particle on the leftmost column of this same row. A
     * neighborhood is created for each particle and added to neighborhoods. 
     */
    public void makeVonNeumannNeighborhood() {

        int numRows = 0;
        int numColumns = 0;

        double sizeRoot = Math.sqrt(this.numIndividuals);
        int roundedRoot = (int) Math.round(sizeRoot);

        numRows = this.numIndividuals / roundedRoot;
        numColumns = roundedRoot;

        Individual[][] neumannArray = new Individual[numRows][numColumns];

        int counter = 0;

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                if (counter < this.numIndividuals) {
                    neumannArray[i][j] = this.individualList.get(counter);
                    counter++;
                }
            }
        }


        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                List<Individual> neighborhoodList = new ArrayList<Individual>();
                int aboveIndex = (i - 1) % numRows;
                int belowIndex = (i + 1) % numRows;
                int leftIndex = (j - 1) % numColumns;
                int rightIndex = (j + 1) % numColumns;

                if (aboveIndex == -1) {
                    aboveIndex = numRows - 1;
                }
                if (leftIndex == -1) {
                    leftIndex = numColumns - 1;
                }

                Individual above = neumannArray[aboveIndex][j];
                Individual below = neumannArray[belowIndex][j];
                Individual left = neumannArray[i][leftIndex];
                Individual right = neumannArray[i][rightIndex];

                neighborhoodList.add(above);
                neighborhoodList.add(below);
                neighborhoodList.add(left);
                neighborhoodList.add(right);
                neighborhoodList.add(neumannArray[i][j]);

                Neighborhood newNeighborhood = new Neighborhood(neighborhoodList);
                neumannArray[i][j].setNeighborhood(newNeighborhood);
                neighborhoods.add(newNeighborhood);
            }
        }
    }


    /*
     * Creates a Neighborhood for each Particle according to global topology. This means that each
     * particle's neighborhood is every Particle in the Swarm, including itself Therefore, each 
     * Particle has the same Neighborhood. 
     */
    public void makeGlobalNeighborhood() {

        Neighborhood globalNeighborhood = new Neighborhood(this.individualList);
        for (int i = 0; i < this.individualList.size(); i++) {
            this.individualList.get(i).setNeighborhood(globalNeighborhood);
        }

        neighborhoods.add(globalNeighborhood);
    }


    /*
     * This creates a Neighborhood for each Particle in the Swarm according to a random topology. This 
     * means that each Particle has a Neighborhood comprised of k random other unique Particles in the 
     * Swarm. A Neighborhood is created for each Particle and added to neighborhoods
     * 
     */
    public void makeRandomNeighborhood(int k) {
        Random rand = new Random();
        Set<Integer> numSet = new HashSet<Integer>();

        for (int i = 0; i < this.individualList.size(); i++) {
            List<Individual> neighborhoodList = new ArrayList<Individual>();
            neighborhoodList.add(this.individualList.get(i));
            numSet.clear();

            for (int j = 0; j < k; j++) {
                int randNum = rand.nextInt(this.individualList.size());

                while (numSet.contains(randNum)) {
                    randNum = rand.nextInt(this.individualList.size());
                }

                numSet.add(randNum);
                neighborhoodList.add(this.individualList.get(randNum));
            }

            Neighborhood newNeighborhood = new Neighborhood(neighborhoodList);
            this.individualList.get(i).setNeighborhood(newNeighborhood);
            neighborhoods.add(newNeighborhood);
        }
    }


    public void randomizeNeighborhoods(int k) {
        Random gen = new Random();
        for(int i = 0; i < this.individualList.size(); i++) {
            double probabiltyPicker = gen.nextDouble();
            if(probabiltyPicker < k) {
                Neighborhood newNeighborhood = this.pickRandomNeighborhood(this.individualList.get(i), NEIGHBORHOOD_SIZE);
                Neighborhood oldNeighborhood = this.individualList.get(i).getNeighborhood();
                this.neighborhoods.remove(oldNeighborhood);
                this.individualList.get(i).setNeighborhood(newNeighborhood);
                neighborhoods.add(newNeighborhood);
            }
        }
    }

    public Neighborhood pickRandomNeighborhood(Individual p, int k) {
        Random rand = new Random();
        List<Individual> neighborhoodList = new ArrayList<Individual>();
        Set<Integer> numSet = new HashSet<Integer>();

            neighborhoodList.add(p);
            numSet.clear();

            for (int j = 0; j < k; j++) {
                int randNum = rand.nextInt(this.individualList.size());

                while (numSet.contains(randNum)) {
                    randNum = rand.nextInt(this.individualList.size());
                }

                numSet.add(randNum);
                neighborhoodList.add(this.individualList.get(randNum));
            }

        Neighborhood newNeighborhood = new Neighborhood(neighborhoodList);
        return newNeighborhood;
    }


    /*
     * This returns the List of Neighborhoods that comprise the Swarm
     */
    public List<Neighborhood> getNeighborhoods() {
        return this.neighborhoods;
    }


    /*
     * This return the List of Particles that comprise the Swarm
     */
    public List<Individual> getIndividuals() {
        return this.individualList;
    }

    /*
     * This updates the best of each Neighborhood in neighborhoods
     */
    public void updateNeighborhoodBestList() {
        for (int i = 0; i < neighborhoods.size(); i++) {
            neighborhoods.get(i).updateBest();
        }
    }

    /*
     * Prints the String representation of each Particle in the Swarm
     */
    public String toString() {
        String swarm = "";
        for (int i = 0; i < individualList.size(); i++) {
            swarm += individualList.get(i).toString() + "\n";
        }
        return swarm;
    }
}