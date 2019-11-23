package Model;



import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Vehicle {

	private String vehicleID;
	private int year;
	private String make;
	private String model;
	private String type;
	private int numSeats;
	private String status;
	private String image;

	private ObservableList<RentalRecord> record = FXCollections.observableArrayList();
	private int countRecord = 0;

	public Vehicle(String vehicleID, int year, String make, String model, String type, int numSeats, String status, String image) {
		this.vehicleID = vehicleID;
		this.year = year;
		this.make = make;
		this.model = model;
		this.type = type;
		this.numSeats = numSeats;
		this.status = status;
		this.image = image;
		record.add(null);
	}

	// Abstract methods getDetails(),rent(), returnVehicle() and
	// completeMaintenance() to be overridden in Car and Van class
	public abstract String getDetails();

	public abstract boolean rent(String customerId, DateTime rentDate, int numOfRentDay);

	public abstract boolean returnVehicle(DateTime returnDate);

	public abstract boolean completeMaintenance(DateTime completionDate);

	public abstract String setDatefromDB(String dateFromDB);

	public abstract DateTime getLastMaintenance();

	// performMaintenance() method performs the same way in subclasses
	public boolean performMaintenance() {
		if (status == "rented") {
			JOptionPane.showMessageDialog(null,"Vehicle is bring rented and maintenance cannot be performed. Returning to main menu...");

			return false;
		} else if (status == "under maintenance") {
			System.out.println();
			JOptionPane.showMessageDialog(null,"Vehicle is already under maintenance. Returning to main menu...");

			return false;
		} else {
			this.status = "under maintenance";
			JOptionPane.showMessageDialog(null,"Vehicle " + vehicleID + " is now under maintenance. Returning to main menu...");
			return true;
		}
	}

	// Getters and Setters for Vehicle class variables
	public String getVehicleID() {
		return vehicleID;
	}

	public void setVehicleID(String vehicleID) {
		this.vehicleID = vehicleID;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNumSeats() {
		return numSeats;
	}

	public void setNumSeats(int numSeats) {
		this.numSeats = numSeats;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public ObservableList<RentalRecord> getRecord() {
		return record;
	}

	public void setRecord(ObservableList<RentalRecord> record) {
		this.record = record;
	}

	public int getCountRecord() {
		return countRecord;
	}

	public void setCountRecord(int count) {
		countRecord = count;
	}

	public void incrementCountRecord() {
		countRecord++;
	}



	public abstract String setDateFromDB(String dateFromDB);

}

