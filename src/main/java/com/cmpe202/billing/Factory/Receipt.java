package com.cmpe202.billing.Factory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Receipt implements Outputter {

	private FileWriter writer;

	public Receipt(String filePath) throws IOException {
		this.writer = new FileWriter(filePath);
	};

	public String addAmountForCSV(String heading, double amount, ArrayList<String> line_items) {
		String receipt = heading;
		for (int i = 0; i < line_items.size(); i++) {
			if (i == 0) {
				receipt += line_items.get(0).replace("\n", amount + ",\n");
				continue;
			}
			receipt += line_items.get(i);
		}
		return receipt;
	};

	public void add(String output) throws IOException {
		this.writer.append(output);
	}

	public void close() throws IOException {
		this.writer.flush();
		this.writer.close();
	}

	public void output(String output) throws IOException {
		this.add(output);
		this.close();
	};

};