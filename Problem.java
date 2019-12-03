

import java.util.List;
import java.util.ArrayList;


public class Problem {

	private List<City> cityList;
	private int size;
	private double[][] distances;

	
	public Problem(List<City> cityList) {

		this.cityList = cityList;
		this.size = cityList.size();
		this.distances = new double[this.size][this.size];
		this.calculateDistances();
	}

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


	public int getSize() {
		return this.size;
	}

	public List<City> getCityList() {
		return this.cityList;
	}

	public double getDistance(int city1, int city2) {
		return this.distances[city1][city2];
	}
}