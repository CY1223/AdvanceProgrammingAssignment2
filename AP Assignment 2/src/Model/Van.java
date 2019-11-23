package Model;

import javax.swing.JOptionPane;

public class Van extends Vehicle {

	private final int NUMSEATS = 15;
	private final int RATE = 235;
	private final int LATEFEE = 299;
	private final int MAINTENANCEINTERVAL = 12;
	private DateTime lastMaintenance;
	private String datefromDB;
	public Van(){
		this(null, 0, null, null, null, 15, "available", null,null);
	}
	public Van(String vehicleID, int year, String make, String model, String type, int numseats, String status,
			DateTime lastMaintenance, String image) {
		super(vehicleID, year, make, model, type, numseats, status, image);
		this.lastMaintenance = lastMaintenance;
	}

	// Override toString() method for Van object
	public String toString() {

		return getVehicleID() + ":" + getType() + ":" + getYear() + ":" + getMake() + ":" + getModel() + ":" + getNumSeats() + ":"
				+ getStatus() + ":" + lastMaintenance + ":" + getImage();

	}

	public String getDetails() {
		String result;
		String vehicleDetails;
		String record = "";
		StringBuilder sbObject = new StringBuilder();

		if (getRecord().get(0)==null) {
			System.out.println();
			result = "\n*********************************" + "\nVehicle ID: " + getVehicleID() + "\nYear: " + getYear()
					+ "\nMake: " + getMake() + "\nModel: " + getModel() + "\nNumber of seats: " + getNumSeats()
					+ "\nStatus: " + getStatus() + "\nLast Maintenance Date: " + lastMaintenance
					+ "\nRENTAL RECORD: Empty";

			return result;
		} else {
			System.out.println();
			vehicleDetails = "\n*********************************" + "\nVehicle ID: " + getVehicleID() + "\nYear: "
					+ getYear() + "\nMake: " + getMake() + "\nModel: " + getModel() + "\nNumber of seats: "
					+ getNumSeats() + "\nStatus: " + getStatus() + "\n Last Maintenance Date: " + lastMaintenance;

			record = "\n";
			for (int i = 0; i < getRecord().size(); i++) {

				if (getRecord().get(i) != null) {
					sbObject.append("\n---------------------------------" + getRecord().get(i).getDetails()
							+ "\n********************************* \n");
				}
			}
			record = sbObject.toString();
			return vehicleDetails + record;
		}
	}

	// Perform rent() on a van
	public boolean rent(String customerID, DateTime rentDate, int numOfRentDay) {

		System.out.println(lastMaintenance);
		DateTime nextMaintenanceDate = new DateTime(lastMaintenance, MAINTENANCEINTERVAL);
		DateTime returnDate = new DateTime(rentDate, numOfRentDay);

		if ((getStatus() .equals("available") && numOfRentDay >= 1
				&& DateTime.diffDays(nextMaintenanceDate, returnDate) > 0)) {

			getRecord().add(0, new RentalRecord(customerID, "", rentDate, null, null, 0.0, 0.0));

			getRecord().get(0).setCustomerID(customerID);
			getRecord().get(0).setRecordID(getVehicleID(), customerID, rentDate);
			getRecord().get(0).setEstimatedReturnDate(rentDate, numOfRentDay);
			getRecord().get(0).setRentDate(rentDate);
			getRecord().get(0).setRentalFee(RATE * numOfRentDay);

			setStatus("rented");
			JOptionPane.showMessageDialog(null,"Vehicle " + getVehicleID() + " is now rented by customer: " + customerID);
			return true;
		} else {
			JOptionPane.showMessageDialog(null,"Vehicle " + getVehicleID() + " is unable to be rented.");
			return false;
		}
	}

	// Perform returnVehicle() on a van
	public boolean returnVehicle(DateTime returnDate) {

		if (DateTime.diffDays(returnDate, getRecord().get(0).getRentDate()) <= 0) {
			JOptionPane.showMessageDialog(null,"Vehicle cannot be returned, check the date.");
			return false;
		} else {
			setStatus("available");
			getRecord().get(0).setActualReturnDate(returnDate);

			int daysLate = DateTime.diffDays(returnDate, getRecord().get(0).getEstimatedReturnDate());
			if (daysLate > 0) {
				getRecord().get(0).setLateFee(daysLate * LATEFEE);

			}

			System.out.println(getDetails());
			JOptionPane.showMessageDialog(null,"Vehicle " + getVehicleID() + " is now returned and available for rent.");
			return true;
		}
	}

	public boolean completeMaintenance(DateTime completionDate) {
		if (getStatus() == "under maintenance") {
			lastMaintenance = completionDate;
			setStatus("available");
			JOptionPane.showMessageDialog(null,getVehicleID() + " has all maintenance completed and ready for rent. Returning to main menu...");
			return true;
		} else {
			JOptionPane.showMessageDialog(null,"ERROR: Unable to complete maintenance on this vehicle.");
			return false;
		}
	}

	// Getters and Setters for Van class
	public int getNUMSEATS() {
		return NUMSEATS;
	}

	public int getRATE() {
		return RATE;
	}

	public int getLATEFEE() {
		return LATEFEE;
	}

	public int getMAINTENANCEINTERVAL() {
		return MAINTENANCEINTERVAL;
	}

	public void setLastMaintenance(DateTime lastMaintenance) {
		this.lastMaintenance = lastMaintenance;
	}


	@Override
	public String setDateFromDB(String dateFromDB) {
		return this.datefromDB = dateFromDB;
	}

	@Override
	public String setDatefromDB(String dateFromDB) {
		return this.datefromDB = dateFromDB;
	}
	@Override
	public DateTime getLastMaintenance(){
		return lastMaintenance;

	}

}
