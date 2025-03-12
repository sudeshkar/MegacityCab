package com.megacitycab.model;

public class TrafficPlan {
	private double baseRate;
	private double perKmRate;
	private CabCategory category;
	private double waitingChargePerHour;
	private double peakHourMultiplier;
	private String category_type;
	public double getBaseRate() {
		return baseRate;
	}
	public TrafficPlan(double baseRate, double perKmRate, CabCategory category,
            double waitingChargePerHour, double peakHourMultiplier) {
		this.baseRate = baseRate;
		this.perKmRate = perKmRate;
		this.category = category;
		this.waitingChargePerHour = waitingChargePerHour;
		this.peakHourMultiplier = peakHourMultiplier;
	}
	public void setBaseRate(double baseRate) {
		this.baseRate = baseRate;
	}
	public double getPerKmRate() {
		return perKmRate;
	}
	public void setPerKmRate(double perKmRate) {
		this.perKmRate = perKmRate;
	}
	public CabCategory getCategory() {
		return category;
	}
	public void setCategory(CabCategory category) {
		this.category = category;
	}
	public double getWaitingChargePerHour() {
		return waitingChargePerHour;
	}
	public void setWaitingChargePerHour(double waitingChargePerHour) {
		this.waitingChargePerHour = waitingChargePerHour;
	}
	public double getPeakHourMultiplier() {
		return peakHourMultiplier;
	}
	public void setPeakHourMultiplier(double peakHourMultiplier) {
		this.peakHourMultiplier = peakHourMultiplier;
	}
	public String getCategory_type() {
		return category_type;
	}
	public void setCategory_type(String category_type) {
		this.category_type = category_type;
	}



}
