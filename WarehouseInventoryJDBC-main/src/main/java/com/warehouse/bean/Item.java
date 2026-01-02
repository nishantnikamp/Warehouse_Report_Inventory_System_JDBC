package com.warehouse.bean;

public class Item {
	private String itemCode, itemDescription, unitOfMeasure;
	private double unitPrice, onHandQuantity, totalDamagedQuantity;

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String s) {
		itemCode = s;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String s) {
		itemDescription = s;
	}

	public String getUnitOfMeasure() {
		return unitOfMeasure;
	}

	public void setUnitOfMeasure(String s) {
		unitOfMeasure = s;
	}

	public double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(double d) {
		unitPrice = d;
	}

	public double getOnHandQuantity() {
		return onHandQuantity;
	}

	public void setOnHandQuantity(double d) {
		onHandQuantity = d;
	}

	public double getTotalDamagedQuantity() {
		return totalDamagedQuantity;
	}

	public void setTotalDamagedQuantity(double d) {
		totalDamagedQuantity = d;
	}
}