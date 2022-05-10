package com.cmpe202.billing;

public class Cards {
	private String card;

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	};

	public Cards(String cardNo) {
		this.setCard(cardNo);
	}

	@Override
	public String toString() {
		return this.getCard();
	};
};