import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.Reader;


public class TestingSuite {



	public String TESTNAME76 = "pr76.tsp";
	public String TESTNAME225 = "tsp225.tsp";
	public String TESTNAME442 = "pcb442.tsp";
	public String TESTNAME657 = "d657.tsp";
	public String TESTNAME1000 = "pr1002.tsp";


	public String DEFAULT_SELECTION = "rs";
	public int DEFAULT_INDIVIDUAL_NUM = 32;
	public String DEFAULT_NEIGHBORHOOD_TOPOLOGY = "vn";
	public double DEFAULT_MUTATION_PROB = 0.01;
	public int DEFAULT_PSO_ITERATIONS = 5;

	public int DEFAULT_ITERATION_NUM = 1000;


	

	public TestingSuite() {

	}

	public List<Double> runTestWithParams(String testName, double mutationProb, int indivividualsNum, String selectionType, String neighborhoodTopType, int psoIterations) {
		List<City> cityList = getCityList(testName);
        Problem prob = new Problem(cityList);
        int numCities = prob.getNumCities();
        GeneticAlgorithm ga = new GeneticAlgorithm(indivividualsNum, prob, numCities, selectionType, 0.8, mutationProb, DEFAULT_ITERATION_NUM, neighborhoodTopType, psoIterations);
        List<Double> fitnessList = ga.optimize();
        return fitnessList;
	}


	// helper method to find the best medians over each run of the algorithm
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


	public static void printList(List<Double> fitnessList) {

		System.out.println("___________________");
		for(int i = 0; i < fitnessList.size(); i++) {
			int iterationNum = (i + 1) * 100;
			System.out.println(iterationNum + " -> " + fitnessList.get(i));
		}
		System.out.println("___________________");
	}


	public void runMutationTests(String testName){

		double[] mutationProbArr = new double[]{0.0, 0.01, 0.05, 0.1, 0.3};
		List<List<Double>> resultsList = new ArrayList<List<Double>>();

		for(int k = 0; k < mutationProbArr.length; k++) {
			List<List<Double>> listOfFitnessLists = new ArrayList<List<Double>>();
			for(int i = 0; i < 10; i++) {
				List<Double> fitnessList = this.runTestWithParams(testName, mutationProbArr[k], DEFAULT_INDIVIDUAL_NUM, DEFAULT_SELECTION, DEFAULT_NEIGHBORHOOD_TOPOLOGY, DEFAULT_PSO_ITERATIONS);
				listOfFitnessLists.add(fitnessList);
			}
			List<Double> processedMedians = processList(listOfFitnessLists);
			resultsList.add(processedMedians);

		}

		for(int i = 0; i < mutationProbArr.length; i++) {
			System.out.println("TEST RESULTS FOR " + testName + "AND MUTATION PROB = " + mutationProbArr[i]);
			printList(resultsList.get(i));
		}

	}

	public void runIndividualNumTests(String testName){
		int[] individualNumArr = new int[]{8, 18, 32, 50, 72};
		List<List<Double>> resultsList = new ArrayList<List<Double>>();

		for(int k = 0; k < individualNumArr.length; k++) {
			List<List<Double>> listOfFitnessLists = new ArrayList<List<Double>>();
			for(int i = 0; i < 10; i++) {
				List<Double> fitnessList = this.runTestWithParams(testName, DEFAULT_MUTATION_PROB, individualNumArr[k], DEFAULT_SELECTION, DEFAULT_NEIGHBORHOOD_TOPOLOGY, DEFAULT_PSO_ITERATIONS);
				listOfFitnessLists.add(fitnessList);
			}
			List<Double> processedMedians = processList(listOfFitnessLists);
			resultsList.add(processedMedians);

		}

		for(int i = 0; i < individualNumArr.length; i++) {
			System.out.println("TEST RESULTS FOR " + testName + "AND INDIVIDUAL NUM = " + individualNumArr[i]);
			printList(resultsList.get(i));
		}
	}

	public void runNeighborhoodTopologyTests(String testName){

		String[] neighborhoodTypeList = new String[]{"vn", "gl", "ri", "ra"};
		List<List<Double>> resultsList = new ArrayList<List<Double>>();

		for(int k = 0; k < neighborhoodTypeList.length; k++) {
			List<List<Double>> listOfFitnessLists = new ArrayList<List<Double>>();
			for(int i = 0; i < 10; i++) {
				List<Double> fitnessList = this.runTestWithParams(testName, DEFAULT_MUTATION_PROB, DEFAULT_INDIVIDUAL_NUM, DEFAULT_SELECTION, neighborhoodTypeList[k], DEFAULT_PSO_ITERATIONS);
				listOfFitnessLists.add(fitnessList);
			}
			List<Double> processedMedians = processList(listOfFitnessLists);
			resultsList.add(processedMedians);

		}

		for(int i = 0; i < neighborhoodTypeList.length; i++) {
			System.out.println("TEST RESULTS FOR " + testName + "AND TOPOLOGY = " + neighborhoodTypeList[i]);
			printList(resultsList.get(i));
		}
	}

	public void runSelectionTypeTests(String testName) {

		String[] selectionTypeList = new String[]{"ts", "rs"};
		List<List<Double>> resultsList = new ArrayList<List<Double>>();

		for(int k = 0; k < selectionTypeList.length; k++) {
			List<List<Double>> listOfFitnessLists = new ArrayList<List<Double>>();
			for(int i = 0; i < 10; i++) {
				List<Double> fitnessList = this.runTestWithParams(testName, DEFAULT_MUTATION_PROB, DEFAULT_INDIVIDUAL_NUM, selectionTypeList[k], DEFAULT_NEIGHBORHOOD_TOPOLOGY, DEFAULT_PSO_ITERATIONS);
				listOfFitnessLists.add(fitnessList);
			}
			List<Double> processedMedians = processList(listOfFitnessLists);
			resultsList.add(processedMedians);

		}

		for(int i = 0; i < selectionTypeList.length; i++) {
			System.out.println("TEST RESULTS FOR " + testName + "AND SELECTION TYPE = " + selectionTypeList[i]);
			printList(resultsList.get(i));
		}
	}

	public void runPSOIterationNumTests(String testName){
		int[] psoIterationsList = new int[]{0, 2, 5, 10, 20};
		List<List<Double>> resultsList = new ArrayList<List<Double>>();

		for(int k = 0; k < psoIterationsList.length; k++) {
			List<List<Double>> listOfFitnessLists = new ArrayList<List<Double>>();
			for(int i = 0; i < 10; i++) {
				List<Double> fitnessList = this.runTestWithParams(testName, DEFAULT_MUTATION_PROB, DEFAULT_INDIVIDUAL_NUM, DEFAULT_SELECTION, DEFAULT_NEIGHBORHOOD_TOPOLOGY, psoIterationsList[k]);
				listOfFitnessLists.add(fitnessList);
			}
			List<Double> processedMedians = processList(listOfFitnessLists);
			resultsList.add(processedMedians);

		}

		for(int i = 0; i < psoIterationsList.length; i++) {
			System.out.println("TEST RESULTS FOR " + testName + "AND PSO ITERATIONS = " + psoIterationsList[i]);
			printList(resultsList.get(i));
		}
	}


	public void runTestsWrapper() {

		System.out.println("** BEGIN MUTATION TEST **");
		this.runMutationTests(TESTNAME76);

		System.out.println("** BEGIN SELECTION TYPE TEST **");
		this.runSelectionTypeTests(TESTNAME76);

		System.out.println("** BEGIN INDIVIDUAL NUM TEST **");
		this.runIndividualNumTests(TESTNAME76);

		System.out.println("** BEGIN NEIGHBORHOOD TOPOLOGY TEST **");
		this.runNeighborhoodTopologyTests(TESTNAME76);

		System.out.println("** BEGIN PSO ITERATION NUM TEST **");
		this.runPSOIterationNumTests(TESTNAME76);
	}





	public void runTestsOnDifferentProblems() {

	}



	public void runManyTestsForSpecificParams(String topology, String selectionType, int individualNum, int psoIterations, double mutationProb){
		String[] problemsList = new String[]{TESTNAME76, TESTNAME225, TESTNAME442, TESTNAME657, TESTNAME1000};
		List<List<Double>> resultsList = new ArrayList<List<Double>>();

		for(int k = 0; k < problemsList.length; k++) {
			List<List<Double>> listOfFitnessLists = new ArrayList<List<Double>>();
			for(int i = 0; i < 10; i++) {
				List<Double> fitnessList = this.runTestWithParams(problemsList[k], mutationProb, individualNum, selectionType, topology, psoIterations);
				listOfFitnessLists.add(fitnessList);
			}
			List<Double> processedMedians = processList(listOfFitnessLists);
			resultsList.add(processedMedians);

		}

		System.out.println("ALGORITHM FOR top: " + topology + "\n selection: " + selectionType
								+ "\n individual Num: " + individualNum + "\n PSO iterations: "
								+ psoIterations + "\n mutation prob: " + mutationProb);

		for(int i = 0; i < problemsList.length; i++) {
			System.out.println("TEST RESULTS FOR " + problemsList[i]);
			printList(resultsList.get(i));
		}
	}


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

    public static void main(String args[]) {

    	TestingSuite testSuite = new TestingSuite();

    	//testSuite.runTestsWrapper();

    	testSuite.runManyTestsForSpecificParams("ra", "rs", 72, 20, 0.1);

    }


}