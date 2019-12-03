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
        List<City> cityList = getCityList("ts225.tsp");
        System.out.println(cityList);
    }
}