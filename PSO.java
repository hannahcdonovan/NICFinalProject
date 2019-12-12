import java.util.List;

public class PSO {

    private Swarm swarm;

    private String neighborhoodType;

    private Population population;

    private int iterations;

    /**
     * PSO Algorithm Constructor
     * 
     * Embedded within the GA optimize method.
     * 
     * @param population The population (i.e. the parentAndOffspringPopulation in GA) we want to run PSO on.
     * @param neighborhoodType String indicating the neighborhood topology.
     * @param iterations The number of iterations to run PSO for.
     */
    public PSO(Population population, String neighborhoodType, int iterations) {
        this.population = population;
        // Create new swarm based on the population we are performing PSO on
        this.swarm = new Swarm(population);
        this.neighborhoodType = neighborhoodType;
        this.iterations = iterations;
    }

    /**
     * Decide which neighborhood to use and create the neighborhood for the swarm.
     */
    public void makeNeighborhood() {
        if (this.neighborhoodType.equals("vn")) {
            this.swarm.makeVonNeumannNeighborhood();
        } else if (this.neighborhoodType.equals("gl")) {
            this.swarm.makeGlobalNeighborhood();
        } else if (this.neighborhoodType.equals("ra")) {
            this.swarm.makeRandomNeighborhood(5);
        } else if (this.neighborhoodType.equals("ri")) {
            this.swarm.makeRingNeighborhood();
        }
    }

    /**
     * Optimize method to be called on the PSO object.
     * 
     * Steps of algorithm (to be performed for a number of iterations):
     * 1. Make the neighboorhoods for the swarm
     * 2. Update the bests in the neighborhood.
     * 3. Perform heuristic crossover on between each Individual and its neighborhood best.
     * 
     */
    public void optimize() {
        // make neighborhoods for the swarm
        this.makeNeighborhood();

        for (int i = 0; i < this.iterations; i++) {
            // go through the neighborhoods and update the best (beforehand so that it doesnt affect
            // crossover)
            for (Neighborhood neighborhood : this.swarm.getNeighborhoods()) {
                neighborhood.updateBest();
            }

            // perform crossover between each individual's neighborhood best and itself
            for (Individual ind : this.swarm.getIndividuals()) {

                Individual best = ind.getNeighborhood().getNeighborhoodBest();
                Individual offspring = this.population.heuristicCrossover(ind, best);
                ind.setTour(offspring.getTour());
            }
        }
    }

}