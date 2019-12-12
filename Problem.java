
import java.util.List;
import java.util.ArrayList;

public class Problem {

	private List<City> cityList;
	private int size;
	private double[][] distances;

	/**
	 * Problem constructor. Essentially a package containing all relevant problem information.
	 * @param cityList A list containing all the City objects. Created when we read in the problem file. 
	 */
	public Problem(List<City> cityList) {

		this.cityList = cityList;
		this.size = cityList.size();
		this.distances = new double[this.size][this.size];
		this.calculateDistances();
	}

	/**
	 * Calculates distances and populates the distances matrix based
	 * on Eucledian distance between nodes.
	 */
	public void calculateDistances() {

		for (int i = 0; i < this.cityList.size(); i++) {
			City currentCity = this.cityList.get(i);
			double currentX = currentCity.getXCoord();
			double currentY = currentCity.getYCoord();

			for (int j = i; j < this.cityList.size(); j++) {
				City comparingCity = this.cityList.get(j);
				double comparingX = comparingCity.getXCoord();
				double comparingY = comparingCity.getYCoord();

				double distance = 0.0;
				if (j != i) {
					double xDiffSquared = Math.pow(currentX - comparingX, 2);
					double yDiffSquared = Math.pow(currentY - comparingY, 2);
					double sum = xDiffSquared + yDiffSquared;
					distance = Math.sqrt(sum);
				}

				this.distances[i][j] = distance;
				this.distances[j][i] = distance;
			}
		}
	}

	/****************************** GETTERS AND SETTERS *******************************************************/

	/**
	 * Problem size getter.
	 * 
	 * @return the size of the Problem, which is the number of cities.
	 */
	public int getNumCities() {
		return this.size;
	}

	/**
	 * cityList getter.
	 * 
	 * @return the List containing all of the cities in the problem.
	 */
	public List<City> getCityList() {
		return this.cityList;
	}

	/**
	 * Distance getter.
	 * 
	 * @return a double representing the distance between the two given city numbers
	 *         according to the distance matrix.
	 */
	public double getDistance(int city1, int city2) {
		return this.distances[city1][city2];
	}
}