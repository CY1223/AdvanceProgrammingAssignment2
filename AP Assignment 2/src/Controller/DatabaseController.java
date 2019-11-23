package Controller;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import Model.Car;
import Model.DateTime;
import Model.RentalRecord;
import Model.Van;
import Model.Vehicle;
import javafx.collections.ObservableList;

import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class DatabaseController {

	private final static String DB_NAME = "DATABASE";
	private final static String V_TABLE = "VEHICLES";
	private final static String R_TABLE = "RENTAL_RECORDS";

	// Prepared Statements
	private final String vanStatement = "INSERT INTO " + V_TABLE
			+ "(vehicleID, type, year, make, model, numSeats, status, lastMaintenance, image)"
			+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final String carStatement = "INSERT INTO " + V_TABLE
			+ "(vehicleID, type, year, make, model, numSeats, status, image)" + "VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	private final String rentalStatement = "INSERT INTO " + R_TABLE
			+ "(recordID, rentDate, estimatedReturnDate, actualReturnDate, rentalFee, lateFee, FKVehicleID)"
			+ "VALUES(?, ?, ?, ?, ?, ?, ?)";

	// Method for getting connection to database
	public static Connection getConnection(String dbName) throws SQLException, ClassNotFoundException {
		// Registering the HSQLDB JDBC driver
		Class.forName("org.hsqldb.jdbc.JDBCDriver");
		Connection con = DriverManager.getConnection("jdbc:hsqldb:file:database/" + dbName, "SA", "");
		return con;
	}

	// Method for checking if DB exists
	public void dbExists() {
		File file = new File("database\\\\BISDB.log");
		if (file.exists() == false) {
			createDB();
		}
	}

	// Method for creating tables in DB
	public static void createDB() {
		try (Connection con = getConnection(DB_NAME); Statement statement = con.createStatement()) {
			// Vehicles table
			statement.executeUpdate("CREATE TABLE " + V_TABLE + "(" + "vehicleID VARCHAR(10) NOT NULL, "
					+ "type VARCHAR (20) NOT NULL, " + "year INT NOT NULL, " + "make VARCHAR(40) NOT NULL, "
					+ "model VARCHAR(30) NOT NULL, " + "numSeats INT NOT NULL, " + "status VARCHAR (20) NOT NULL, "
					+ "image VARCHAR (20) NOT NULL, " + "lastMaintenance VARCHAR (20), " + "PRIMARY KEY (vehicleID))");

			// Rental Records table
			statement.executeUpdate(
					"CREATE TABLE " + R_TABLE + "(" + "recordID VARCHAR(30) NOT NULL, " + "rentDate VARCHAR (20), "
							+ "estimatedReturnDate VARCHAR (20) NOT NULL, " + "actualReturnDate VARCHAR (20), "
							+ "rentalFee DOUBLE, " + "lateFee DOUBLE, " + "FKVehicleID VARCHAR(15) NOT NULL, "
							+ "FOREIGN KEY (FKVehicleID) REFERENCES VEHICLES(vehicleID), " + "PRIMARY KEY (recordID))");

			System.out.println("Database successfully created. ");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// Method to clear all rows from table
	public void clearDB() {
		try (Connection con = getConnection(DB_NAME); Statement statement = con.createStatement()) {
			statement.executeUpdate("DELETE FROM " + R_TABLE);
			statement.executeUpdate("DELETE FROM " + V_TABLE);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	// Method for updating DB with all Arrays
	// Empties all rows first before calling for updates.
	public void updateDBVehicle(ObservableList<Vehicle> vehicles) {
		try (Connection con = getConnection(DB_NAME); Statement statement = con.createStatement()) {

			// Vehicles
			for (Vehicle vehicle : vehicles) {
				if (vehicle instanceof Car) {

					String vehicleSplit[] = vehicle.toString().split(":");
					PreparedStatement cStmt = con.prepareStatement(carStatement);
					for (int i = 0; i < vehicleSplit.length; i++) {
						cStmt.setString(i + 1, vehicleSplit[i]);
					}
					cStmt.executeUpdate();

				} else {
					String vehicleSplit[] = vehicle.toString().split(":");
					PreparedStatement vStmt = con.prepareStatement(vanStatement);
					for (int i = 0; i < vehicleSplit.length; i++) {
						vStmt.setString(i + 1, vehicleSplit[i]);
					}
					vStmt.executeUpdate();

				}
				for (RentalRecord records : vehicle.getRecord()) {

					if (records != null) {
						String recordSplit[] = records.toString().split(":");
						PreparedStatement rStmt = con.prepareStatement(rentalStatement);
						for (int i = 0; i < recordSplit.length; i++) {
							rStmt.setString(i + 1, recordSplit[i]);
							System.out.println("TEST: INSERTED " + recordSplit[i]);
						}
						// Vehicle Foreign Key
						rStmt.setString(7, vehicle.getVehicleID());
						rStmt.executeUpdate();
					} else {
						System.out.println("DID NOT INSERT ANYTHING");
						// break;
					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	public void updateDBAll(ObservableList<Vehicle> vArray) {
		clearDB();
		updateDBVehicle(vArray);
	}

	// Method to draw information from the database and insert objects into the
	// appropriate arrays
	public void updateArray(ObservableList<Vehicle> vArray) {
		try (Connection con = getConnection(DB_NAME); Statement stmt = con.createStatement()) {
			String vehicleQuery;
			String recordQuery;

			// Vehicle Array
			vehicleQuery = "SELECT* FROM " + V_TABLE;
			try (ResultSet resultSet = stmt.executeQuery(vehicleQuery)) {

				while (resultSet.next()) {
					if (resultSet.getString("type").equals("Car")) {

						Vehicle v = new Car(resultSet.getString("vehicleID"), resultSet.getInt("year"),
								resultSet.getString("make"), resultSet.getString("model"), resultSet.getString("type"),
								resultSet.getInt("numSeats"), resultSet.getString("status"),
								resultSet.getString("image"));

						// Rental Record Array
						recordQuery = "SELECT* FROM " + R_TABLE + " WHERE FKVehicleID LIKE '"
								+ resultSet.getString("vehicleID") + "'";
						try (ResultSet recordSet = stmt.executeQuery(recordQuery)) {
							while (recordSet.next()) {
								RentalRecord record = new RentalRecord();

								// Convert rentDate
								String tempRentDate = record.setDateFromDB(recordSet.getString("rentDate"));
								DateTime convertedRentDate = new DateTime(
										Integer.parseInt(tempRentDate.substring(0, 2)),
										Integer.parseInt(tempRentDate.substring(3, 5)),
										Integer.parseInt(tempRentDate.substring(6)));

								// Convert estimatedReturnDate
								String tempestimatedReturnDate = record
										.setDateFromDB(recordSet.getString("estimatedReturnDate"));
								DateTime convertedestimatedReturnDate = new DateTime(
										Integer.parseInt(tempestimatedReturnDate.substring(0, 2)),
										Integer.parseInt(tempestimatedReturnDate.substring(3, 5)),
										Integer.parseInt(tempestimatedReturnDate.substring(6)));

								// Convert actualReturnDate
								String tempactualReturnDate = record
										.setDateFromDB(recordSet.getString("actualReturnDate"));
								DateTime convertedactualReturnDate = new DateTime(
										Integer.parseInt(tempactualReturnDate.substring(0, 2)),
										Integer.parseInt(tempactualReturnDate.substring(3, 5)),
										Integer.parseInt(tempactualReturnDate.substring(6)));

								// Set record
								String recordIDSplit[] = recordSet.getString("recordID").split("_");
								record.setCustomerID(recordIDSplit[1]);
								record.setRecordID(recordIDSplit[0], recordIDSplit[1], convertedRentDate);
								record.setRentDate(convertedRentDate);
								record.setEstimatedReturnDateDB(convertedestimatedReturnDate);
								record.setActualReturnDate(convertedactualReturnDate);
								record.setRentalFee(recordSet.getDouble("rentalFee"));
								record.setLateFee(recordSet.getDouble("lateFee"));

								v.getRecord().add(0, record);

							}
							vArray.add(v);
						}

					} else if (resultSet.getString("type").equals("Van")) {

						Vehicle v = new Van(resultSet.getString("vehicleID"), resultSet.getInt("year"),
								resultSet.getString("make"), resultSet.getString("model"), resultSet.getString("type"),
								resultSet.getInt("numSeats"), resultSet.getString("status"), null,
								resultSet.getString("image"));

						// Convert lastMaintenanceDate
						String templastMaintenanceDate = v.setDateFromDB(resultSet.getString("lastMaintenance"));
						DateTime convertedlastMaintenanceDate = new DateTime(
								Integer.parseInt(templastMaintenanceDate.substring(0, 2)),
								Integer.parseInt(templastMaintenanceDate.substring(3, 5)),
								Integer.parseInt(templastMaintenanceDate.substring(6)));
						((Van) v).setLastMaintenance(convertedlastMaintenanceDate);

						// Rental Record Array
						recordQuery = "SELECT* FROM " + R_TABLE + " WHERE FKVehicleID LIKE '"
								+ resultSet.getString("vehicleID") + "'";
						try (ResultSet recordSet = stmt.executeQuery(recordQuery)) {
							while (recordSet.next()) {
								RentalRecord record = new RentalRecord();

								// Convert rentDate
								String tempRentDate = record.setDateFromDB(recordSet.getString("rentDate"));
								DateTime convertedRentDate = new DateTime(
										Integer.parseInt(tempRentDate.substring(0, 2)),
										Integer.parseInt(tempRentDate.substring(3, 5)),
										Integer.parseInt(tempRentDate.substring(6)));

								// Convert estimatedReturnDate
								String tempestimatedReturnDate = record
										.setDateFromDB(recordSet.getString("estimatedReturnDate"));
								DateTime convertedestimatedReturnDate = new DateTime(
										Integer.parseInt(tempestimatedReturnDate.substring(0, 2)),
										Integer.parseInt(tempestimatedReturnDate.substring(3, 5)),
										Integer.parseInt(tempestimatedReturnDate.substring(6)));

								// Convert actualReturnDate
								String tempactualReturnDate = record
										.setDateFromDB(recordSet.getString("actualReturnDate"));
								DateTime convertedactualReturnDate = new DateTime(
										Integer.parseInt(tempactualReturnDate.substring(0, 2)),
										Integer.parseInt(tempactualReturnDate.substring(3, 5)),
										Integer.parseInt(tempactualReturnDate.substring(6)));

								// Set record
								String recordIDSplit[] = recordSet.getString("recordID").split("_");
								record.setCustomerID(recordIDSplit[1]);
								record.setRecordID(recordIDSplit[0], recordIDSplit[1], convertedRentDate);
								record.setRentDate(convertedRentDate);
								record.setEstimatedReturnDateDB(convertedestimatedReturnDate);
								record.setActualReturnDate(convertedactualReturnDate);
								record.setRentalFee(recordSet.getDouble("rentalFee"));
								record.setLateFee(recordSet.getDouble("lateFee"));

								v.getRecord().add(0, record);
							}
							vArray.add(v);
						}

					}
				}
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
