package simulation.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Christopher on 21/04/2016.
 */
public class FileLoader {

	public static String loadAsString (String path) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String buffer;
			while ((buffer = reader.readLine()) != null)
				result.append(buffer).append('\n');
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
}
