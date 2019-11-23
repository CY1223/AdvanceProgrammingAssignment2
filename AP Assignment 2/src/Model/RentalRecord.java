package Model;



import java.text.DecimalFormat;

public class RentalRecord {

	private String customerID;
	private String recordID;
	private DateTime rentDate;
	private DateTime estimatedReturnDate;
	private DateTime actualReturnDate;
	private double rentalFee;
	private double lateFee;
	private String dateFromDB;
	private String estimatedReturnDateDB;

	public RentalRecord(String customerID, String recordID, DateTime rentDate, DateTime returnDate,
			DateTime actualReturnDate, double rentalFee, double lateFee) {

		this.customerID = customerID;
		this.recordID = recordID;
		this.rentDate = rentDate;
		this.estimatedReturnDate = returnDate;
		this.actualReturnDate = actualReturnDate;
		this.rentalFee = rentalFee;
		this.lateFee = lateFee;
	}

	public RentalRecord() {
		// TODO Auto-generated constructor stub
	}

	// Overriding toString method
	public String toString() {
		if (actualReturnDate == null) {
			return recordID + ":" + rentDate + ":" + estimatedReturnDate + ":" + "none:" + 0 + ":" + 0;
//			"none:none:none"
		} else {
			return recordID + ":" + rentDate + ":" + estimatedReturnDate + ":" + actualReturnDate + ":" + rentalFee
					+ ":" + lateFee;
		}
	}

	// getDetails() method returns String displaying vehicle details
	public String getDetails() {
		String result;
		DecimalFormat df = new DecimalFormat("#0.00");


		if (actualReturnDate == null) {
			result = "\nRecord ID: " + recordID + "\nRent Date: " + rentDate.getFormattedDate()
					+ "\nEstimated Return Date: " + estimatedReturnDate.getFormattedDate();
		} else {
			result = "\nRecord ID: " + recordID + "\nRent Date: " + rentDate.getFormattedDate()
					+ "\nEstimated Return Date: " + estimatedReturnDate.getFormattedDate() + "\nActual Return Date: "
					+ actualReturnDate.getFormattedDate() + "\nRental Fee: " + df.format(rentalFee) + "\nLate Fee: " + df.format(lateFee);
		}
		return result;
	}

	// Getters and Setters for RentalRecord class variables
	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getRecordID() {
		return recordID;
	}

	public void setRecordID(String vehicleID, String customerID, DateTime rentDate) {
		this.recordID = vehicleID + "_" + customerID + "_" + rentDate.getEightDigitDate();
	}

	public DateTime getRentDate() {
		return rentDate;
	}

	public void setRentDate(DateTime rentDate) {
		this.rentDate = rentDate;
	}

	public DateTime getReturnDate() {
		return estimatedReturnDate;
	}

	public DateTime getEstimatedReturnDate() {
		return estimatedReturnDate;
	}

	public void setEstimatedReturnDate(DateTime startDate, int setClockForwardInDays) {
		DateTime setClock = new DateTime(startDate, setClockForwardInDays);
		this.estimatedReturnDate = setClock;
	}

	public void setEstimatedReturnDateDB(DateTime convertedestimatedReturnDate) {
		this.estimatedReturnDate = convertedestimatedReturnDate;
	}

	public DateTime getActualReturnDate() {
		return actualReturnDate;
	}

	public void setActualReturnDate(DateTime actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}

	public double getRentalFee() {
		return rentalFee;
	}

	public void setRentalFee(double rentalFee) {
		this.rentalFee = rentalFee;
	}

	public double getLateFee() {
		return lateFee;
	}

	public void setLateFee(double lateFee) {
		this.lateFee = lateFee;
	}

	public String setDateFromDB(String dateFromDB) {
		return this.dateFromDB = dateFromDB;

	}

}
