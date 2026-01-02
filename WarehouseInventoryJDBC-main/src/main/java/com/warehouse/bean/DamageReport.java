package com.warehouse.bean;

import java.util.Date;

public class DamageReport {
	private int damageID;
	private String itemCode, reportedBy, reason, status;
	private Date reportDate;
	private double damagedQuantity;

	public int getDamageID() {
		return damageID;
	}

	public void setDamageID(int i) {
		damageID = i;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String s) {
		itemCode = s;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date d) {
		reportDate = d;
	}

	public String getReportedBy() {
		return reportedBy;
	}

	public void setReportedBy(String s) {
		reportedBy = s;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String s) {
		reason = s;
	}

	public double getDamagedQuantity() {
		return damagedQuantity;
	}

	public void setDamagedQuantity(double d) {
		damagedQuantity = d;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String s) {
		status = s;
	}
}