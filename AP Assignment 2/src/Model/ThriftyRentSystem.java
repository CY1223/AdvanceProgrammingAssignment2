package Model;


import java.util.InputMismatchException;
import java.util.Scanner;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;



public class ThriftyRentSystem {
	private int countVehicle = 0;
	private static int idCounter = 1;
//	private Vehicle vehicles[] = new Vehicle[50];


	private ObservableList<Vehicle> vehicles = FXCollections.observableArrayList();

	Scanner console = new Scanner(System.in);

	//Singleton pattern
	private static ThriftyRentSystem instance;

	public static ThriftyRentSystem getInstance( ) {
		if(instance == null) {
			instance = new ThriftyRentSystem();
		}
		return instance;
	}


	public void addVehicle() {

		int option;
		while (true) {

			try {
				System.out.println();
				System.out.println("*********************************");
				System.out.println("           Vehicle type: ");
				System.out.print("   Add a car: 		 1 \n");
				System.out.print("   Add a van: 		 2 \n");
				System.out.println("*********************************");
				System.out.print("   Enter your choice: ");

				option = console.nextInt();

				switch (option) {
				case 1:
					addCar();

				case 2:
					addVan();

				default:
					System.out.println();
					System.out.println("Invalid entry. Please try again.");

					break;
				}
			} catch (InputMismatchException e) {
				System.out.println();
				System.out.println("Please enter a number from the option provided: ");
				console.next();
			}
		}
	}

	public void rentVehicle() {

		if(countVehicle == 0) {
			System.out.println();
			System.out.println("ERROR: No vehicle has been added yet. \n");
			displayMenu();
		}

		System.out.println();
		System.out.println("Please enter the ID of the vehicle to be rented: ");
		String iD = console.next();

		for (int i = 0; i < countVehicle; i++) {
			if (vehicles.get(i).getVehicleID().compareTo(iD) != 0) {
				if (i == countVehicle - 1) {
					System.out.println();
					System.out.println("invalid Vehicle ID. Returning to main menu...");
					break;
				}
				continue;
			} else {
				int days = 0;
				DateTime rentDate = null;

				System.out.println();
				System.out.println("Please enter the customer ID: ");
				console.nextLine();
				String custID = console.nextLine();

				boolean errorDate = true;
				while (errorDate) {
					try {
						System.out.println();
						System.out.println("Please enter the rent date (dd/mm/yyyy): ");
						String date = console.nextLine();

						rentDate = new DateTime(Integer.parseInt(date.substring(0, 2)),
								Integer.parseInt(date.substring(3, 5)), Integer.parseInt(date.substring(6)));
						errorDate = false;
					} catch (StringIndexOutOfBoundsException | NumberFormatException e) {
						System.out.println();
						System.out.print("Invalid date format...");
					}
				}

				boolean errorDays = true;
				while (errorDays) {
					try {
						System.out.println();
						System.out.println("How many days? ");
						days = console.nextInt();
						errorDays = false;
					} catch (InputMismatchException e) {
						System.out.println();
						System.out.print("Invalid input...");
						console.next();
					}
				}

				vehicles.get(i).rent(custID, rentDate, days);
				displayMenu();
			}

		}
	}

	public void returnVehicle() {

		if(countVehicle == 0) {
			System.out.println();
			System.out.println("ERROR: No vehicle has been added yet. \n");
			displayMenu();
		}

		System.out.println();
		System.out.println("Please enter the ID of the vehicle to be returned: ");
		String iD = console.next();

		for (int i = 0; i < countVehicle; i++) {
			if (vehicles.get(i).getVehicleID().compareTo(iD) != 0) {
				if (i == countVehicle - 1) {
					System.out.println();
					System.out.println("invalid Vehicle ID. Returning to main menu...");
					System.out.println();
					break;
				}
				continue;
			} else {
				if (vehicles.get(i).getStatus() == "rented") {
					DateTime returnDate = null;

					boolean errorDate = true;
					while (errorDate) {
						try {
							System.out.println();
							System.out.println("Please enter the return date (dd/mm/yyyy): ");
							String date = console.next();

							returnDate = new DateTime(Integer.parseInt(date.substring(0, 2)),
									Integer.parseInt(date.substring(3, 5)), Integer.parseInt(date.substring(6)));
							errorDate = false;
						} catch (StringIndexOutOfBoundsException | NumberFormatException e) {
							System.out.println();
							System.out.print("Invalid date format...");
						}
					}

					vehicles.get(i).returnVehicle(returnDate);
					displayMenu();
				} else if (vehicles.get(i).getStatus() == "available") {
					System.out.println();
					System.out.println("Vehicle has not been rented. Returning to main menu...");
					displayMenu();
				} else if (vehicles.get(i).getStatus() == "under maintenance") {
					System.out.println();
					System.out.println("Vehicle is under maintenance. Returning to main menu...");
					displayMenu();
				}
			}
		}
	}

	public void vehicleMaintenance() {

		if(countVehicle == 0) {
			System.out.println();
			System.out.println("ERROR: No vehicle has been added yet. \n");
			displayMenu();
		}

		System.out.println();
		System.out.println("Please enter the ID of the vehicle to be sent for maintenance: ");
		String iD = console.next();

		for (int i = 0; i < countVehicle; i++) {
			if (vehicles.get(i).getVehicleID().compareTo(iD) != 0) {
				if (i == countVehicle - 1) {
					System.out.println();
					System.out.println("Vehicle ID does not exist. Returning to main menu...");
					displayMenu();
					break;
				}
				continue;
			} else {
				vehicles.get(i).performMaintenance();
				displayMenu();
			}
		}
		System.out.println();
		System.out.println("No vehicle has been added yet. ");
	}

	public void completeMaintenance() {

		if(countVehicle == 0) {
			System.out.println();
			System.out.println("ERROR: No vehicle has been added yet. \n");
			displayMenu();
		}

		System.out.println();
		System.out.println("Please enter the ID of the vehicle to have maintenance completed: ");
		String iD = console.next();

		for (int i = 0; i < countVehicle; i++) {
			if (vehicles.get(i).getVehicleID().compareTo(iD) != 0) {
				if (i == countVehicle - 1) {
					System.out.println();
					System.out.println("Vehicle ID does not exist. Returning to main menu...");
					displayMenu();
					break;
				}
				continue;
			} else {
				DateTime maintenanceCompletionDate = null;

				boolean errorDate = true;
				while (errorDate) {
					try {
						System.out.println();
						System.out.println("Please enter the maintenance completion date (dd/mm/yyyy): ");
						String date = console.next();

						maintenanceCompletionDate = new DateTime(Integer.parseInt(date.substring(0, 2)),
								Integer.parseInt(date.substring(3, 5)), Integer.parseInt(date.substring(6)));
						errorDate = false;
					} catch (StringIndexOutOfBoundsException | NumberFormatException e) {
						System.out.println();
						System.out.print("Invalid date format...");
					}
				}

				vehicles.get(i).completeMaintenance(maintenanceCompletionDate);
				displayMenu();
			}
		}
	}

	public void displayAllVehicles() {

		if(countVehicle == 0) {
			System.out.println();
			System.out.println("ERROR: No vehicle has been added yet. \n");
			displayMenu();
		}

		for (int i = 0; i < vehicles.size(); i++) {
			System.out.println(vehicles.get(i).getDetails());
			if (i == vehicles.size() - 1) {
				System.out.println();
				displayMenu();
			}
		}
	}

	public void displayMenu() {

		int option;

		while (true) {

			try {
				System.out.println("**** ThriftyRent SYSTEM MENU ****");
				System.out.print("   Add vehicle:               1 \n");
				System.out.print("   Rent vehicle:              2 \n");
				System.out.print("   Return vehicle:            3 \n");
				System.out.print("   Vehicle Maintenance:       4 \n");
				System.out.print("   Complete Maintenance:      5 \n");
				System.out.print("   Display All Vehicles:      6 \n");
				System.out.print("   Exit Program:              7 \n");
				System.out.println("*********************************");
				System.out.print("   Enter your choice: ");

				option = console.nextInt();

				switch (option) {
				case 1:
					addVehicle();
					System.out.println();

					break;

				case 2:
					rentVehicle();
					System.out.println();

					break;

				case 3:
					returnVehicle();
					System.out.println();

					break;

				case 4:
					vehicleMaintenance();
					System.out.println();

					break;

				case 5:
					completeMaintenance();
					System.out.println();

					break;

				case 6:
					displayAllVehicles();
					System.out.println();

					break;

				case 7:
					System.out.println("Exiting program");
					System.out.println();

					System.exit(0);

				default:
					System.out.println("This is not a valid menu option. Please select another: ");
					System.out.println();

					break;

				}
			} catch (InputMismatchException e) {
				System.out.println();
				System.out.println("Please enter a number from the option provided: ");
				System.out.println();

				console.next();
			}
		}
	}

	public void addCar() {
		Vehicle tempCar = new Car(" ", 0, " ", " ", "car", 0, "available", "");

		// Auto Generate unique Vehicle ID for Car
		System.out.println();
		tempCar.setVehicleID("C_000" + createID());

		System.out.println("Creating database for new car under Vehicle ID: " + tempCar.getVehicleID());

		// Set Year of the car and catch invalid entries
		boolean errorYear = true;
		while (errorYear) {
			try {
				System.out.println();
				System.out.println("Please enter the year of the car you are adding: ");

				tempCar.setYear(console.nextInt());

				while (tempCar.getYear() < 1885 || tempCar.getYear() > 2020) {
					System.out.println("Invalid car Year. please try again: ");
					tempCar.setYear(console.nextInt());
				}
				errorYear = false;
			} catch (InputMismatchException e) {
				System.out.println();
				System.out.print("Invalid input...");
				console.next();
			}
		}

		// Set Make of the car
		System.out.println();
		System.out.println("Please enter the Make of the car you are adding: ");
		console.nextLine();
		tempCar.setMake(console.nextLine());

		// Set Model of the car
		System.out.println();
		System.out.println("Please enter the Model of the car you are adding: ");

		tempCar.setModel(console.next());

		// Set the number of seats the car has
		boolean errorSeats = true;
		while (errorSeats) {
			try {
				System.out.println();
				System.out.println("Please enter the number of seats the car has: ");

				tempCar.setNumSeats(console.nextInt());

				while (tempCar.getNumSeats() != 4 && tempCar.getNumSeats() != 7) {
					System.out.println();
					System.out.println("Invalid input. Car has either 4 or 7 seats. Please try again: ");

					tempCar.setNumSeats(console.nextInt());
				}
				errorSeats = false;
			} catch (InputMismatchException e) {
				System.out.println();
				System.out.print("Invalid Input...");
				console.next();
			}
		}

				vehicles.add(tempCar);
				countVehicle++;

				System.out.println();
				System.out.println(
						"Car " + vehicles.get(vehicles.size()-1).getVehicleID() + " has been added. Returning to main menu...	 \n");


		displayMenu();
	}

	public void addVan() {

		Vehicle tempVan = new Van(" ", 0, " ", " ", "van", 15, "available", null, "");

		// Auto Generate unique Vehicle ID for Van
		System.out.println();
		tempVan.setVehicleID("V_000" + createID());

		System.out.println("Creating database for new van under Vehicle ID: " + tempVan.getVehicleID());

		// Set Year of the car and catch invalid entries
		boolean errorYear = true;
		while (errorYear) {
			try {
				System.out.println();
				System.out.println("Please enter the year of the car you are adding: ");

				tempVan.setYear(console.nextInt());

				while (tempVan.getYear() < 1885 || tempVan.getYear() > 2020) {
					System.out.println("Invalid car Year. please try again: ");
					tempVan.setYear(console.nextInt());
				}
				errorYear = false;
			} catch (InputMismatchException e) {
				System.out.println();
				System.out.print("Invalid input...");
				console.next();
			}
		}

		// Set Make of the car
		System.out.println();
		System.out.println("Please enter the Make of the van you are adding: ");

		console.nextLine();
		tempVan.setMake(console.nextLine());

		// Set Model of the car
		System.out.println();
		System.out.println("Please enter the Model of the van you are adding: ");

		tempVan.setModel(console.nextLine());

		// Set Last Maintenance Date of Van
		boolean errorDate = true;
		while (errorDate) {
			try {
				System.out.println();
				System.out.println("Please enter the date of this van's last maintenance (dd/mm/yyyy): ");
				String date = console.next();

				DateTime tempLastMaintenanceDate = new DateTime(Integer.parseInt(date.substring(0, 2)),
						Integer.parseInt(date.substring(3, 5)), Integer.parseInt(date.substring(6)));

				((Van) tempVan).setLastMaintenance(tempLastMaintenanceDate);

				errorDate = false;
			} catch (StringIndexOutOfBoundsException | NumberFormatException e) {
				System.out.println();
				System.out.print("Invalid date format...");
			}
		}


				vehicles.add(tempVan);

				countVehicle++;

				System.out.println();
				System.out.println("Van has been added. Returning to main menu...	 \n");


		// Return to main menu
		displayMenu();
	}

	//Method to create Unique ID for Vehicles
	public static synchronized String createID() {
		return String.valueOf(idCounter++);
	}

	//getter for vehicles observablelist
	public ObservableList<Vehicle> getVehicleList(){
		return vehicles;
	}
}

