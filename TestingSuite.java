import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.Reader;

public class TestingSuite {

	/*****************************
	 * TESTING CONSTATNS
	 ***********************************************/

	private static String TESTNAME76 = "pr76.tsp";
	private static String TESTNAME225 = "tsp225.tsp";
	private static String TESTNAME442 = "pcb442.tsp";
	private static String TESTNAME657 = "d657.tsp";
	private static String TESTNAME1000 = "pr1002.tsp";
	private static String TESTNAME2103 = "d2103.tsp";

	private static String DEFAULT_SELECTION = "rs";
	private static int DEFAULT_INDIVIDUAL_NUM = 32;
	private static String DEFAULT_NEIGHBORHOOD_TOPOLOGY = "vn";
	private static double DEFAULT_MUTATION_PROB = 0.01;
	private static int DEFAULT_PSO_ITERATIONS = 5;

	private static int DEFAULT_ITERATION_NUM = 1000;

	/**
	 * Constructor for the TestingSuite object. Takes no parameters.
	 */
	private TestingSuite() {
	}

	/**
	 * Method to run tests on differing problems. Used to run all of our test
	 * suites.
	 * 
	 * @param testName         The name of the problem we are testing
	 * @param mutationProb     The mutation probability.
	 * @param populationSize   The population size.
	 * @param selectionType    The type of selection we'd like to do -- either
	 *                         Tournament or Rank.
	 * @param neighborhoodType String indicating the type of neighborhood -- von
	 *                         Neumann, Ring, Global, or Random.
	 * @param psoIterations    The number of iterations to run the embedded PSO
	 *                         algorithm for.
	 * @return A list of all the best fitnesses at each 100 point in the iteration.
	 */
	public List<Double> runTestWithParams(String testName, double mutationProb, int populationSize,
			String selectionType, String neighborhoodType, int psoIterations) {
		List<City> cityList = getCityList(testName);
		Problem prob = new Problem(cityList);
		int numCities = prob.getNumCities();
		GeneticAlgorithm ga = new GeneticAlgorithm(populationSize, prob, numCities, selectionType, mutationProb,
				DEFAULT_ITERATION_NUM, neighborhoodType, psoIterations);
		List<Double> fitnessList = ga.optimize();
		return fitnessList;
	}

	/**
	 * For each run in the algorithm, we will add the fitnessList produced. For each
	 * list, we then want to calculate the median between all the respective indexes
	 * in the inner lists. This ensures that we are finding the median value
	 * produced at each iteration point over all runs. Helps us observe the overall
	 * behavior of the graph.
	 * 
	 * @param doubleList List containing all the fitnessLists for each run of the
	 *                   algorithm.
	 * @return A list of all the median values at each 100 point in the iteration
	 *         over all algorithm runs.
	 */
	public static List<Double> processList(List<List<Double>> doubleList) {

		List<Double> results = new ArrayList<Double>();
		for (int i = 0; i < doubleList.get(0).size(); i++) {
			List<Double> nums = new ArrayList<Double>();
			for (int j = 0; j < doubleList.size(); j++) {
				nums.add(doubleList.get(j).get(i));
			}
			Collections.sort(nums);
			int medianIndex = nums.size() / 2;
			double median = nums.get(medianIndex);
			results.add(median);
		}
		return results;
	}

	/**
	 * Helper method for printing the fitnessList produced by optimizing the
	 * algorithm.
	 * 
	 * @param fitnessList The list of fitnessess at each 100 iteration. Produced by
	 *                    running optimize.
	 */
	public static void printList(List<Double> fitnessList) {

		System.out.println("___________________");
		for (int i = 0; i < fitnessList.size(); i++) {
			int iterationNum = (i + 1) * 100;
			System.out.println(iterationNum + " -> " + fitnessList.get(i));
		}
		System.out.println("___________________");
	}

	/**
	 * Test we run for varying the mutation probability.
	 * 
	 * @param testName The name of TSP filename problem that we are trying to
	 *                 optimize.
	 */
	public void runMutationTests(String testName) {

		// The mutation probs we want to test
		double[] mutationProbArr = new double[] { 0.0, 0.01, 0.05, 0.1, 0.3 };
		List<List<Double>> resultsList = new ArrayList<List<Double>>();

		for (int k = 0; k < mutationProbArr.length; k++) {
			List<List<Double>> listOfFitnessLists = new ArrayList<List<Double>>();
			for (int i = 0; i < 10; i++) {
				List<Double> fitnessList = this.runTestWithParams(testName, mutationProbArr[k], DEFAULT_INDIVIDUAL_NUM,
						DEFAULT_SELECTION, DEFAULT_NEIGHBORHOOD_TOPOLOGY, DEFAULT_PSO_ITERATIONS);
				listOfFitnessLists.add(fitnessList);
			}
			List<Double> processedMedians = processList(listOfFitnessLists);
			resultsList.add(processedMedians);

		}

		// Print all the results (i.e. medians) for each mutation prob run
		for (int i = 0; i < mutationProbArr.length; i++) {
			System.out.println("TEST RESULTS FOR " + testName + "AND MUTATION PROB = " + mutationProbArr[i]);
			printList(resultsList.get(i));
		}

	}

	/**
	 * Test we run for varying populzationSizes.
	 * 
	 * @param testName The name of TSP filename problem that we are trying to
	 *                 optimize.
	 */
	public void runPopulationSizeTests(String testName) {
		// Differing population sizes we are looking at
		int[] populationSizeArr = new int[] { 8, 18, 32, 50, 72 };
		List<List<Double>> resultsList = new ArrayList<List<Double>>();

		for (int k = 0; k < populationSizeArr.length; k++) {
			List<List<Double>> listOfFitnessLists = new ArrayList<List<Double>>();
			for (int i = 0; i < 10; i++) {
				List<Double> fitnessList = this.runTestWithParams(testName, DEFAULT_MUTATION_PROB, populationSizeArr[k],
						DEFAULT_SELECTION, DEFAULT_NEIGHBORHOOD_TOPOLOGY, DEFAULT_PSO_ITERATIONS);
				listOfFitnessLists.add(fitnessList);
			}
			List<Double> processedMedians = processList(listOfFitnessLists);
			resultsList.add(processedMedians);

		}

		for (int i = 0; i < populationSizeArr.length; i++) {
			System.out.println("TEST RESULTS FOR " + testName + "AND INDIVIDUAL NUM = " + populationSizeArr[i]);
			printList(resultsList.get(i));
		}
	}

	/**
	 * Test for varying the neighborhood topology.
	 * 
	 * @param testName The name of TSP filename problem that we are trying to
	 *                 optimize.
	 */
	public void runNeighborhoodTopologyTests(String testName) {
		// Differing neighborhood topologies
		String[] neighborhoodTypeList = new String[] { "vn", "gl", "ri", "ra" };
		List<List<Double>> resultsList = new ArrayList<List<Double>>();

		for (int k = 0; k < neighborhoodTypeList.length; k++) {
			List<List<Double>> listOfFitnessLists = new ArrayList<List<Double>>();
			for (int i = 0; i < 10; i++) {
				List<Double> fitnessList = this.runTestWithParams(testName, DEFAULT_MUTATION_PROB,
						DEFAULT_INDIVIDUAL_NUM, DEFAULT_SELECTION, neighborhoodTypeList[k], DEFAULT_PSO_ITERATIONS);
				listOfFitnessLists.add(fitnessList);
			}
			List<Double> processedMedians = processList(listOfFitnessLists);
			resultsList.add(processedMedians);

		}

		for (int i = 0; i < neighborhoodTypeList.length; i++) {
			System.out.println("TEST RESULTS FOR " + testName + "AND TOPOLOGY = " + neighborhoodTypeList[i]);
			printList(resultsList.get(i));
		}
	}

	/**
	 * Test for varying the selection type.
	 * 
	 * @param testName The name of TSP filename problem that we are trying to
	 *                 optimize.
	 */
	public void runSelectionTypeTests(String testName) {
		// Differing selection types
		String[] selectionTypeList = new String[] { "ts", "rs" };
		List<List<Double>> resultsList = new ArrayList<List<Double>>();

		for (int k = 0; k < selectionTypeList.length; k++) {
			List<List<Double>> listOfFitnessLists = new ArrayList<List<Double>>();
			for (int i = 0; i < 10; i++) {
				List<Double> fitnessList = this.runTestWithParams(testName, DEFAULT_MUTATION_PROB,
						DEFAULT_INDIVIDUAL_NUM, selectionTypeList[k], DEFAULT_NEIGHBORHOOD_TOPOLOGY,
						DEFAULT_PSO_ITERATIONS);
				listOfFitnessLists.add(fitnessList);
			}
			List<Double> processedMedians = processList(listOfFitnessLists);
			resultsList.add(processedMedians);

		}

		for (int i = 0; i < selectionTypeList.length; i++) {
			System.out.println("TEST RESULTS FOR " + testName + "AND SELECTION TYPE = " + selectionTypeList[i]);
			printList(resultsList.get(i));
		}
	}

	/**
	 * Test for varying the number of PSO iterations.
	 * 
	 * @param testName The name of TSP filename problem that we are trying to
	 *                 optimize.
	 */
	public void runPSOIterationNumTests(String testName) {
		// pso iteration nums we are testing
		int[] psoIterationsList = new int[] { 0, 2, 5, 10, 20 };
		List<List<Double>> resultsList = new ArrayList<List<Double>>();

		for (int k = 0; k < psoIterationsList.length; k++) {
			List<List<Double>> listOfFitnessLists = new ArrayList<List<Double>>();
			for (int i = 0; i < 10; i++) {
				List<Double> fitnessList = this.runTestWithParams(testName, DEFAULT_MUTATION_PROB,
						DEFAULT_INDIVIDUAL_NUM, DEFAULT_SELECTION, DEFAULT_NEIGHBORHOOD_TOPOLOGY, psoIterationsList[k]);
				listOfFitnessLists.add(fitnessList);
			}
			List<Double> processedMedians = processList(listOfFitnessLists);
			resultsList.add(processedMedians);

		}

		for (int i = 0; i < psoIterationsList.length; i++) {
			System.out.println("TEST RESULTS FOR " + testName + "AND PSO ITERATIONS = " + psoIterationsList[i]);
			printList(resultsList.get(i));
		}
	}

	/**
	 * Runs all the tests on a problem.
	 * 
	 * @param testName The name of the test we woul like to run each test on.
	 */
	public void runTestsWrapper(String testName) {

		System.out.println("** BEGIN MUTATION TEST **");
		this.runMutationTests(testName);

		System.out.println("** BEGIN SELECTION TYPE TEST **");
		this.runSelectionTypeTests(testName);

		System.out.println("** BEGIN POPULATION SIZE TEST **");
		this.runPopulationSizeTests(testName);

		System.out.println("** BEGIN NEIGHBORHOOD TOPOLOGY TEST **");
		this.runNeighborhoodTopologyTests(testName);

		System.out.println("** BEGIN PSO ITERATION NUM TEST **");
		this.runPSOIterationNumTests(testName);
	}

	/**
	 * Helper method for running many tests on differing problem sizes with specific
	 * parameters.
	 * 
	 * @param topology       String indicating the type of neighborhood topology --
	 *                       von Neumann, Global, Ring, Random.
	 * @param selectionType  String indicating the selectionType -- Random or
	 *                       Tournament.
	 * @param populationSize The size of the population.
	 * @param psoIterations  The number of iterations to run PSO for.
	 * @param mutationProb   The probability of having an individual perform
	 *                       mutation or not.
	 */
	public void runManyTestsForSpecificParams(String topology, String selectionType, int populationSize,
			int psoIterations, double mutationProb) {
		String[] problemsList = new String[] {TESTNAME76, TESTNAME225, TESTNAME442, TESTNAME657};
		List<List<Double>> resultsList = new ArrayList<List<Double>>();

		for (int k = 0; k < problemsList.length; k++) {
			List<List<Double>> listOfFitnessLists = new ArrayList<List<Double>>();
			System.out.println("START TEST FOR " + problemsList[k]);
			for (int i = 0; i < 10; i++) {
				List<Double> fitnessList = this.runTestWithParams(problemsList[k], mutationProb, populationSize,
						selectionType, topology, psoIterations);
				listOfFitnessLists.add(fitnessList);
			}
			List<Double> processedMedians = processList(listOfFitnessLists);
			resultsList.add(processedMedians);
			System.out.println("TEST RESULTS FOR " + problemsList[k]);
			printList(processedMedians);

		}

		System.out.println("ALGORITHM FOR top: " + topology + "\n selection: " + selectionType + "\n population size: "
				+ populationSize + "\n PSO iterations: " + psoIterations + "\n mutation prob: " + mutationProb);

		for (int i = 0; i < problemsList.length; i++) {
			System.out.println("TEST RESULTS FOR " + problemsList[i]);
			printList(resultsList.get(i));
		}
	}

	/**
	 * Method for getting a list of cities (which will be passed into an Problem).
	 * Used to create the relevant problem information.
	 * 
	 * @param filename TSP file we are reading in.
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

	/**
	 * Main method for running differing tests.
	 * 
	 * @param args Command line arguments.
	 */
	public static void main(String args[]) {

		TestingSuite testSuite = new TestingSuite();

		testSuite.runTestsWrapper();

		testSuite.runManyTestsForSpecificParams("ra", "rs", 18, 5, 0.1);

	}

}