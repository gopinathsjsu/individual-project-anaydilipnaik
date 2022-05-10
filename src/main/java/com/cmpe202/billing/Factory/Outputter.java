package com.cmpe202.billing.Factory;

import java.io.IOException;

public interface Outputter {
	void output(String output) throws IOException;

	void add(String input) throws IOException;
};