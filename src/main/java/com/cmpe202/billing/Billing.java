package com.cmpe202.billing;

import java.io.*;
import java.util.*;
import com.cmpe202.billing.Factory.*;
import com.cmpe202.billing.Strategy.*;

public class Billing {

	private static OutputterFactory factory = new OutputterFactory();
	private static HashMap<String, Integer> itemQuantityCap = new HashMap<String, Integer>();
	static {
		itemQuantityCap = new HashMap<String, Integer>();
		itemQuantityCap.put("essentials", 3);
		itemQuantityCap.put("luxury", 4);
		itemQuantityCap.put("misc", 6);
	}

	private static ArrayList<Cards> cardLoader(String cardsPath) throws Exception {
		try {
			ArrayList<Cards> cards = new ArrayList<Cards>();
			StrategyLoader loader = new StrategyLoader(new LoadCardsStrategy());
			ArrayList<String[]> cardData = loader.load_data(cardsPath);
			for (String[] csv_card : cardData) {
				Cards newCard = new Cards(csv_card[0]);
				cards.add(newCard);
			}
			return cards;
		} catch (FileNotFoundException ex) {
			System.err.println("File Not Found");
			throw ex;
		} catch (Exception ex) {
			System.err.println("Error while loading cards");
			throw ex;
		}
	};

	private static ArrayList<Items> itemsLoader(String itemsPath) throws Exception {
		try {
			ArrayList<Items> itemsList = new ArrayList<Items>();
			StrategyLoader loader = new StrategyLoader(new LoadItemsStrategy());
			ArrayList<String[]> itemsData = loader.load_data(itemsPath);
			for (String[] lineData : itemsData) {
				Items item = new Items(
						lineData[0].trim(),
						lineData[1].trim(),
						Integer.parseInt(lineData[2].trim()),
						Double.parseDouble(lineData[3].trim()));
				itemsList.add(item);
			}
			return itemsList;
		} catch (FileNotFoundException ex) {
			System.err.println("File Not Found");
			throw ex;
		} catch (Exception ex) {
			System.err.println("Error while loading items");
			throw ex;
		}
	};

	private static ArrayList<Orders> orderLoader(String ordersPath) throws Exception {
		try {
			ArrayList<Orders> orderList = new ArrayList<Orders>();
			String cardNo = "";
			StrategyLoader loader = new StrategyLoader(new LoadOrdersStrategy());
			ArrayList<String[]> ordersData = loader.load_data(ordersPath);
			for (String[] line_data : ordersData) {
				if (line_data.length >= 3) {
					cardNo = line_data[2];
				}
				Orders new_order = new Orders(
						line_data[0],
						cardNo,
						Integer.parseInt(line_data[1]));
				orderList.add(new_order);
			}
			return orderList;
		} catch (FileNotFoundException ex) {
			System.err.println("File Not Found");
			throw ex;
		} catch (Exception ex) {
			System.err.println("Error while loading order" + ex.toString());
			throw ex;
		}
	};

	private static boolean isCardExists(ArrayList<Cards> cards, String cardNo) {
		for (Cards card : cards) {
			if (card.getCard().equals(cardNo)) {
				return true;
			}
		}
		return false;
	}

	public static void updateCard(String cardNo) throws IOException {
		Outputter writer = factory.getOutput("FILE", "Cards.csv");
		String input = cardNo + ",\n";
		writer.output(input);
	}

	public static void keepBillDetails(double amount, String errors, ArrayList<String> lineItems)
			throws IOException {
		String outputFile = "output.csv";
		String errorFile = "errors.txt";

		if (errors.length() > 0) {
			Outputter err_bill = factory.getOutput("ERROR_RECEIPT", errorFile);
			String err_msg = "Errors\n";
			err_msg += errors;
			err_bill.output(err_msg);
		} else {
			Receipt receipt = (Receipt) factory.getOutput("RECEIPT", outputFile);
			String heading = "Item,Quantity,Price,TotalPrice,\n";
			receipt.output(
					receipt.addAmountForCSV(heading, amount, lineItems));
		}
	};

	public static void main(String[] args) {
		try {
			ArrayList<Cards> cards = cardLoader("./Cards.csv");
			ArrayList<Items> items = itemsLoader("./Items.csv");
			ArrayList<Orders> orders = orderLoader("./Orders.csv");
			System.out.println("Data load success!");
			double total = 0;
			ArrayList<String> lineItems = new ArrayList<String>();
			String errorMessage = "";
			HashMap<String, Items> itemSet = new HashMap<String, Items>();
			for (Items item : items) {
				itemSet.put(item.getItem().toLowerCase(), item);
			}
			for (Orders order : orders) {
				Items item = itemSet.getOrDefault(order.getItem().toLowerCase(), null);
				if (item == null) {
					errorMessage += "Missing item";
					System.out.println("Missing item");
					continue;
				}
				if (order.getQuantity() > item.getQuantity()) {
					errorMessage += "Insufficient inventory";
					System.out.println("Insufficient inventory");
					continue;
				}
				Integer qtyLimit = itemQuantityCap.getOrDefault(item.getCategory().toLowerCase(), null);
				if (qtyLimit == null) {
					errorMessage += "Invalid category\tItem = " + item.getCategory() + "\n";
					System.out.println("Invalid category\tItem = " + item.getCategory() + "\n");
					continue;
				}
				if (order.getQuantity() > qtyLimit) {
					errorMessage += "Wrong quantity";
					System.out.println("Wrong quantity");
					continue;
				}
				double price = item.getPrice() * order.getQuantity();
				total += price;
				lineItems.add(order.getItem() + "," + (int) order.getQuantity() + "," + (int) price + ",\n");
				System.out.println("Valid order!" + ".\t Total = " + total);
				if (!isCardExists(cards, order.getCard())) {
					cards.add(new Cards(order.getCard()));
					updateCard(order.getCard());
				}
			}
			keepBillDetails(total, errorMessage, lineItems);
			System.out.println("Process complete!");
			System.exit(0);

		} catch (IOException ex) {
			System.err.println("Error handling files" + ex.toString());
			System.exit(0);
		} catch (Exception ex) {
			System.err.println("Exception" + ex.toString());
			System.exit(0);
		}
	};
};