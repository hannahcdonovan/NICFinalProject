import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.Reader;

public class Main {

    /**
	 * Helper method to read in the file and problem information.
	 * 
	 * @param filename The name of the file we want to read in.
	 * @return A list of City objects (i.e. our problem information)
	 */
	public static List<City> getCityList(String filename) {
		String data = null;
		int dimension = 0;
		String edgeWeightType = "";
		List<City> cities = new ArrayList<City>();

		try {
			File initialFile = new File(filename);
			initialFile.createNewFile();

			Reader reader = new FileReader(initialFile);

			BufferedReader buffReader = new BufferedReader(reader);

			boolean first = true;
			int counter = 1;

			while ((data = buffReader.readLine()) != null) {
				City city;
				if (first) {
					if (data.contains("EDGE_WEIGHT_TYPE")) {
						edgeWeightType = data.split(" ")[1];
					} else if (data.contains("DIMENSION")) {
						dimension = Integer.parseInt(data.replace(" ", "").split(":")[1]);
					} else if (data.contains("NODE_COORD_SECTION")) {
						first = false;
					}
				} else {
					if (counter <= dimension) {
						String[] temp = data.trim().replace("  ", " ").replace("  ", " ").split(" ");
						double xCoord = Double.parseDouble(temp[1].trim());
						double yCoord = Double.parseDouble(temp[2].trim());
						city = new City(counter, xCoord, yCoord);
						cities.add(city);
						counter++;
					}
				}
			}
			buffReader.close();
		} catch (IOException e) {
			System.out.println("IO Exception");
		}
		return cities;
    }
    
    public static void main(String[] args) {
        List<City> cityList = getCityList("ulysses22.tsp");
        // System.out.println(cityList);
        Problem prob = new Problem(cityList);
        int numCities = prob.getNumCities();
        // Individual ind = new Individual(prob);
        // Individual randInd = ind.makeRandomIndividual();
        // System.out.print(randInd);
        Population pop = new Population(5);
        Population pop2 = new Population(10);
        pop.generateRandomPopulation(prob);
        // System.out.println(pop.getIndividualList());

        Individual parent1 = pop.getIndividualList().get(0);
        System.out.println("Parent 1: " + parent1);
        Individual parent2 = pop.getIndividualList().get(1);
        System.out.println("Parent 2: " + parent2);


        GeneticAlgorithm ga = new GeneticAlgorithm(5, prob, numCities, "selection", 0.1, 0.1, 10, pop, pop2);

        Individual offspring = ga.heuristicCrossover(parent1, parent2);
        System.out.println("Offspring: " + offspring + " fitness -> " + offspring.getFitness());

        offspring.mutate(0.01);
        System.out.println("Offspring: " + offspring + " fitness -> " + offspring.getFitness());

    }
}