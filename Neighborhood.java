import java.util.List;
import java.util.ArrayList;

public class Neighborhood {

    /**
     * A list of particles representing a neighborhood.
     */
    private List<Individual> neighbors;

    /**
     * Value representing the neighborhood best.
     */
    private Individual neighborhoodBest;

    /**
     * Constructor for the Neighborhood object. Essentially a wrapper for a list of Particles that
     * represent a neighborhood.
     * @param neighborList List used to instantiate Neighborhood object.
     */
    public Neighborhood(List<Individual> neighborList) {
        this.neighbors = neighborList;
    }

    /**
     * Sets the neighborhood best to a copy of the particle's (because the particle is changing over time)
     * position and calling evaluate on particle because .evaluate sets the current score of the particle. 
     * @param currBest A particle representing the iteration's new best.
     */
    public void setNeighborhoodBest(Individual newBest) {
        this.neighborhoodBest = newBest.copyIndividual();
    }

    /**
     * Updates the neighborhood best for each particle's neighborhood on each iteration. 
     */
    public void updateBest() {

        Individual currBestIndividual = neighbors.get(0).copyIndividual();
        double currBestFitness = currBestIndividual.getFitness();
        for (int i = 1; i < neighbors.size(); i++) {
            Individual comparingIndividual = neighbors.get(i);
            double fitness = comparingIndividual.getFitness();

            if (fitness < currBestFitness) {
                currBestIndividual = comparingIndividual.copyIndividual();
                currBestFitness = fitness;
            }
        }
        if(this.neighborhoodBest == null || currBestFitness < this.neighborhoodBest.getFitness()) {
        	this.setNeighborhoodBest(currBestIndividual);
        }
    }

    /**
     * Getter that returns the nbest for a given Neighborhood object.
     * @return Particle, which is the best particle in the neighborhood.
     */
    public Individual getNeighborhoodBest() {
        return this.neighborhoodBest;
    }

}