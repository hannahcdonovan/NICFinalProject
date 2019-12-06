import java.util.List;

public class PSO {

    private Swarm swarm;

    private String neighborhoodType;

    private Population population;

    private int iterations;

    public PSO(Population population, String neighborhoodType, int iterations) {
        this.population = population;
        this.swarm = new Swarm(population);
        this.neighborhoodType = neighborhoodType;
        this.iterations = iterations;
    }

    public void makeNeighborhood() {
        if (neighborhoodType.equals("vn")) {
            this.swarm.makeVonNeumannNeighborhood();
        } else if (neighborhoodType.equals("gl")) {
            this.swarm.makeGlobalNeighborhood();
        } else if (neighborhoodType.equals("rand")) {
            this.swarm.makeRandomNeighborhood(5);
        } else if (neighborhoodType.equals("global")) {
            this.swarm.makeGlobalNeighborhood();
        }
    }

    public void optimize() {
        this.makeNeighborhood();
        List<Neighborhood> neighborhoodList = this.swarm.getNeighborhoods();
        for (int i = 0; i < this.iterations; i++) {
            System.out.println(this.swarm.getNeighborhoods().size());
            for (Neighborhood neighborhood: this.swarm.getNeighborhoods()) {
                neighborhood.updateBest();
            }
            for (Individual ind : this.swarm.getIndividuals()) {

                System.out.println(ind);
                Individual best = ind.getNeighborhood().getNeighborhoodBest();
                Individual offspring = this.population.heuristicCrossover(ind, best);
                ind.setTour(offspring.getTour());
            }
        }
    }

}