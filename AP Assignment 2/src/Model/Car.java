package Model;

import javax.swing.JOptionPane;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class Car extends Vehicle {

	private final int RATE_4 = 78;
	private final int RATE_7 = 113;
	private final int MINDAYSUNTOTHURS = 2;
	private final int MINDAYFRISAT = 3;
	private final int MAXRENTDAY = 14;
	private final double LATEFEE_PERDAY_4 = 78 * 1.25;
	private final double LATEFEE_PERDAY_7 = 113 * 1.25;

	public Car(){
		this(null, 0, null, null, "Car", 0, "available", null);
	}
	public Car(String vehicleID, int year, String make, String model, String type, int numSeats, String status, String image) {
		super(vehicleID, year, make, model, type, numSeats, status, image);
	}

	// Override toString() method for Car object
	public String toString() {

		return getVehicleID() + ":" + getType() + ":" + getYear() + ":" + getMake() + ":" + getModel() + ":" + getNumSeats() + ":"
				+ getStatus() + ":" + getImage();

	}

	public String getDetails() {
		String result;
		String vehicleDetails;
		String record = "";
		StringBuilder sbObject = new StringBuilder();
		if (getRecord().get(0) == null) {
			System.out.println();
			result = "\nVehicle ID: " + getVehicleID() + "\nYear: " + getYear()
					+ "\nMake: " + getMake() + "\nModel: " + getModel() + "\nNumber of seats:" + getNumSeats()
					+ "\nStatus: " + getStatus() + "\nRENTAL RECORD: Empty";

			return result;
		} else {
			System.out.println();
			vehicleDetails = "\nVehicle ID: " + getVehicleID() + "\nYear: "
					+ getYear() + "\nMake: " + getMake() + "\nModel: " + getModel() + "\nNumber of seats: "
					+ getNumSeats() + "\nStatus: " + getStatus();

			record = "\n";
			for (int i = 0; i < getRecord().size(); i++) {

				if (getRecord().get(i) != null) {
					sbObject.append("\n----------------------------------------------" + getRecord().get(i).getDetails()+ "\n********************************* \n");
				}
			}
			record = sbObject.toString();
			return vehicleDetails + record;
		}
	}

	// Perform rent() on a car
	public boolean rent(String customerID, DateTime rentDate, int numOfRentDay) {

		if ((getStatus() .equals("available") || (getStatus() .equals ("available") && (rentDate.getNameOfDay() != "FRI" && rentDate.getNameOfDay() != "SAT")
						&& numOfRentDay >= MINDAYSUNTOTHURS && numOfRentDay <= MAXRENTDAY))) {


			getRecord().add(0, new RentalRecord(customerID, "", rentDate, null, null, 0.0, 0.0));


			getRecord().get(0).setCustomerID(customerID);
			getRecord().get(0).setRecordID(getVehicleID(), customerID, rentDate);
			getRecord().get(0).setEstimatedReturnDate(rentDate, numOfRentDay);
			getRecord().get(0).setRentDate(rentDate);

			if (getNumSeats() == 4) {
				getRecord().get(0).setRentalFee(RATE_4 * numOfRentDay);
			} else {
				getRecord().get(0).setRentalFee(RATE_7 * numOfRentDay);
			}

			setStatus("rented");
			JOptionPane.showMessageDialog(null, "Vehicle " + getVehicleID() + " is now rented by customer: " + customerID);
			return true;

		} else {
			JOptionPane.showMessageDialog(null, "Vehicle " + getVehicleID() + " is unable to be rented. Check availability or length of rent. ");
			return false;
		}
	}

	// Perform returnVehicle() on a car
	public boolean returnVehicle(DateTime returnDate) {

		if (DateTime.diffDays(returnDate, getRecord().get(0).getRentDate()) <= 0 || returnDate==null) {
			JOptionPane.showMessageDialog(null,"Vehicle cannot be returned, check the date. Returning to main menu...");
			return false;
		} else {
			setStatus("available");
			getRecord().get(0).setActualReturnDate(returnDate);

			int daysLate = DateTime.diffDays(returnDate, getRecord().get(0).getEstimatedReturnDate());
			if (daysLate > 0) {
				if (getNumSeats() == 4) {
					getRecord().get(0).setLateFee(daysLate * LATEFEE_PERDAY_4);
				} else if (getNumSeats() == 7) {
					getRecord().get(0).setLateFee(daysLate * LATEFEE_PERDAY_7);
				}
			}
			JOptionPane.showMessageDialog(null,"Vehicle " + getVehicleID() + " is now returned and available for rent.");
			return true;
		}
	}

	// Perform completeMaintenance() on a car
	public boolean completeMaintenance(DateTime completionDate) {
		if (getStatus().equals("under maintenance")) {
			setStatus("available");
			JOptionPane.showMessageDialog(null,
					getVehicleID() + " has all maintenance completed and ready for rent. Returning to main menu...");
			return true;
		} else {
			JOptionPane.showMessageDialog(null,"ERROR: Not able to complete maintenance on this vehicle.");
			return false;
		}
	}

	@Override
	public String setDatefromDB(String dateFromDB) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String setDateFromDB(String dateFromDB) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public DateTime getLastMaintenance() {
		// TODO Auto-generated method stub
		return null;
	}
}
