package com.cmpe202.billing.Strategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class LoadOrdersStrategy implements Strategy {

	public ArrayList<String[]> load_data(String path_to_file) throws FileNotFoundException {
		File input = new File(path_to_file);
		Scanner sc = new Scanner(input);
		ArrayList<String[]> output = new ArrayList<String[]>();
		sc.nextLine();
		while (sc.hasNextLine()) {
			String line = sc.nextLine().replace("\"", "");
			String[] line_data = line.split(",");
			output.add(line_data);
		}
		return output;
	};
};