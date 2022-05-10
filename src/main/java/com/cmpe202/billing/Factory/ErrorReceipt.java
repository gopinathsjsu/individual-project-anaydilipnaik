package com.cmpe202.billing.Factory;

import java.io.FileWriter;
import java.io.IOException;

public class ErrorReceipt implements Outputter {

	private FileWriter writer;

	public void add(String output) throws IOException {
		this.writer.append(output);
	}

	private void close() throws IOException {
		this.writer.flush();
		this.writer.close();
	}

	public void output(String output) throws IOException {
		this.add(output);
		this.close();
	};

	public ErrorReceipt(String filePath) throws IOException {
		this.writer = new FileWriter(filePath);
	};
};