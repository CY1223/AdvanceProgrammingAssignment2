package Controller;

import javafx.collections.transformation.FilteredList;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import Model.Car;
import Model.DateTime;
import Model.RentalRecord;
import Model.ThriftyRentSystem;
import Model.Van;
import Model.Vehicle;
import View.Main;

public class MainPageController {

	@FXML
	private ListView<Vehicle> listView;
	@FXML
	private ChoiceBox<String> cbType;
	@FXML
	private ChoiceBox<Integer> cbSeats;
	@FXML
	private ChoiceBox<String> cbStatus;
	@FXML
	private ChoiceBox<String> cbMake;

	private Vehicle selectedVehicle;

	Main obj = new Main();
	ThriftyRentSystem model = ThriftyRentSystem.getInstance();
	// Filtered list for filter function
	private FilteredList<Vehicle> filteredData = new FilteredList<>(model.getVehicleList(), s -> true);

	// Constructor
	public MainPageController() {
	}

	// Initializes the controller class. This method is automatically called
	// after the fxml file has been loaded.
	@FXML
	public void initialize() throws ClassNotFoundException, SQLException {
		// importing data from database when the program loads
		handleImportDB();
		// setting data from filtered list to the list view
		listView.setItems(filteredData);
		// implementing custom cell list for each row of the cells in the list
		// view
		listView.setCellFactory(studentListView -> new VehicleListViewCell());
		// Adding and setting the necessary elements to the choice box for the
		// filtering function
		cbSeats.getItems().addAll(0, 4, 7, 15);
		cbType.setValue("All");
		cbSeats.setValue(0);
		cbStatus.setValue("All");
		cbMake.setValue("All");
		// implementing the filtering function
		cbType.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			System.out.println("Released: " + newValue);
			filteredData.setPredicate((t) -> {
				return (cbSeats.getValue().equals(0) ? true : t.getNumSeats() == (cbSeats.getValue()))
						&& (cbStatus.getValue().equals("All") ? true : t.getStatus().equals(cbStatus.getValue()))
						&& (cbMake.getValue().equals("All") ? true : t.getMake().equals(cbMake.getValue()))
						&& (cbType.getValue().equals("All") ? true : t.getType().equals(cbType.getValue()));
			});
		});

		cbSeats.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			System.out.println("Branch: " + newValue);

			filteredData.setPredicate((t) -> {
				return (cbSeats.getValue().equals(0) ? true : t.getNumSeats() == (newValue))
						&& (cbStatus.getValue().equals("All") ? true : t.getStatus().equals(cbStatus.getValue()))
						&& (cbMake.getValue().equals("All") ? true : t.getMake().equals(cbMake.getValue()))
						&& (cbType.getValue().equals("All") ? true : t.getType().equals(cbType.getValue()));
			});
		});

		cbStatus.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			System.out.println("Genre: " + newValue);
			filteredData.setPredicate((t) -> {
				return (cbSeats.getValue().equals(0) ? true : t.getNumSeats() == (cbSeats.getValue()))
						&& (cbStatus.getValue().equals("All") ? true : t.getStatus().equals(cbStatus.getValue()))
						&& (cbMake.getValue().equals("All") ? true : t.getMake().equals(cbMake.getValue()))
						&& (cbType.getValue().equals("All") ? true : t.getType().equals(cbType.getValue()));
			});
		});

		cbMake.getSelectionModel().selectedItemProperty().addListener((obs, oldValue, newValue) -> {
			System.out.println("parent company: " + newValue);
			filteredData.setPredicate((t) -> {
				return (cbSeats.getValue().equals(0) ? true : t.getNumSeats() == (cbSeats.getValue()))
						&& (cbStatus.getValue().equals("All") ? true : t.getStatus().equals(cbStatus.getValue()))
						&& (cbMake.getValue().equals("All") ? true : t.getMake().equals(cbMake.getValue()))
						&& (cbType.getValue().equals("All") ? true : t.getType().equals(cbType.getValue()));
			});
		});
	}

	// Method calling the add car UI when the add car button is pressed
	@FXML
	private void addCar() {
		Vehicle tempPerson = new Car();
		boolean okClicked = obj.addCar(tempPerson);
		if (okClicked) {
			model.getVehicleList().add(tempPerson);
		}
	}

	// Method calling the add van UI when the add van button is pressed
	@FXML
	private void addVan() {
		Vehicle tempPerson = new Van();
		boolean okClicked = obj.addVan(tempPerson);
		if (okClicked) {
			model.getVehicleList().add(tempPerson);
		}
	}

	// Method that calls the vehicle details UI
	@FXML
	private void vehicleDetails() {
		selectedVehicle = listView.getSelectionModel().getSelectedItem();
		if (selectedVehicle != null) {
			boolean okClicked = obj.displayVehicleDetailsDialog(selectedVehicle);
		} else {
			// Nothing selected.
			Alert alert = new Alert(AlertType.WARNING);
			alert.initOwner(obj.getPrimaryStage());
			alert.setTitle("No Selection");
			alert.setHeaderText("No Vehicle Selected");
			alert.setContentText("Please select a Vehicle in the table.");
			alert.showAndWait();
		}
	}

	// method that handles exporting vehicle details to database
	@FXML
	private void handleExportDB() {
		DatabaseController storage = new DatabaseController();
		storage.dbExists();
		storage.updateDBAll(model.getVehicleList());
		JOptionPane.showMessageDialog(null, "Exported to database");
	}

	// method that handles importing vehicle details from database
	private void handleImportDB() {
		DatabaseController storage = new DatabaseController();
		storage.updateArray(model.getVehicleList());
		System.out.println("DB Imported");
	}

	// method that handles vehicle details to TXT file
	@FXML
	private void exportToTXT() {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < model.getVehicleList().size(); i++) {
			str.append(model.getVehicleList().get(i));
			str.append(System.getProperty("line.separator"));
			for (int j = 0; j < model.getVehicleList().get(i).getRecord().size(); j++) {
				if (model.getVehicleList().get(i).getRecord().get(j) != null) {
				}
				str.append(model.getVehicleList().get(i).getRecord().get(j));
				str.append(System.getProperty("line.separator"));
			}
		}
		String strToString = str.toString();

		FileChooser fileChooser = new FileChooser();

		// Set extension filter for text files
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		File file = fileChooser.showSaveDialog(null);

		if (file != null) {
			saveTextToFile(strToString, file);
		}
	}

	private void saveTextToFile(String content, File file) {
		try {
			PrintWriter writer;
			writer = new PrintWriter(file);
			writer.println(content);
			writer.close();
		} catch (IOException ex) {
			Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	// method that imports car details from TXT file
	@FXML
	private void loadTextFromFileCar() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter for text files
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showOpenDialog(null);

		ArrayList<String> arr = new ArrayList<String>();
		if (file != null) {
			try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {
				String sCurrentLine;

				while ((sCurrentLine = br.readLine()) != null) {
					Vehicle car = new Car();
					if (sCurrentLine.contains(".png")) {
						String vehicleSplit[] = sCurrentLine.toString().split(":");
						if (vehicleSplit[1].equals("Car")) {

							car.setVehicleID(vehicleSplit[0]);
							car.setType(vehicleSplit[1]);
							car.setYear(Integer.parseInt(vehicleSplit[2]));
							car.setMake(vehicleSplit[3]);
							car.setModel(vehicleSplit[4]);
							car.setNumSeats(Integer.parseInt(vehicleSplit[5]));
							car.setStatus(vehicleSplit[6]);
							car.setImage(vehicleSplit[7]);
							model.getVehicleList().add(car);
							while ((sCurrentLine = br.readLine()) != null) {
								if (sCurrentLine.contains("_")) {
									RentalRecord record = new RentalRecord();
									String recordSplit[] = sCurrentLine.toString().split(":");
									String tempRentDate = recordSplit[1];
									DateTime convertedRentDate = new DateTime(
											Integer.parseInt(tempRentDate.substring(0, 2)),
											Integer.parseInt(tempRentDate.substring(3, 5)),
											Integer.parseInt(tempRentDate.substring(6)));

									// Convert estimatedReturnDate
									String tempestimatedReturnDate = recordSplit[2];
									DateTime convertedestimatedReturnDate = new DateTime(
											Integer.parseInt(tempestimatedReturnDate.substring(0, 2)),
											Integer.parseInt(tempestimatedReturnDate.substring(3, 5)),
											Integer.parseInt(tempestimatedReturnDate.substring(6)));

									// Convert actualReturnDate
									String tempactualReturnDate = recordSplit[3];
									DateTime convertedactualReturnDate = new DateTime(
											Integer.parseInt(tempactualReturnDate.substring(0, 2)),
											Integer.parseInt(tempactualReturnDate.substring(3, 5)),
											Integer.parseInt(tempactualReturnDate.substring(6)));

									String recordID[] = recordSplit[0].split("_");
									record.setCustomerID(recordID[1]);
									record.setRecordID(recordID[0], recordID[1], convertedRentDate);

									record.setRentDate(convertedRentDate);
									record.setEstimatedReturnDateDB(convertedestimatedReturnDate);
									record.setActualReturnDate(convertedactualReturnDate);
									record.setRentalFee(Double.parseDouble(recordSplit[4]));
									record.setLateFee(Double.parseDouble(recordSplit[5]));
									car.getRecord().add(record);
								}
							}
							// model.getVehicleList().add(car);

						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			JOptionPane.showMessageDialog(null, "Import Canceled");
		for (String ar : arr) {
			System.out.println(ar);
		}

	}

	// method that imports van details from TXT file
	@FXML
	private void loadTextFromFileVan() {
		FileChooser fileChooser = new FileChooser();

		// Set extension filter for text files
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);

		// Show save file dialog
		File file = fileChooser.showOpenDialog(null);
		if (file != null) {
			try (BufferedReader br = new BufferedReader(new FileReader(file.getAbsolutePath()))) {

				String sCurrentLine;

				while ((sCurrentLine = br.readLine()) != null) {
					Vehicle van = new Van();
					if (sCurrentLine.contains(".png")) {
						String vehicleSplit[] = sCurrentLine.toString().split(":");
						if (vehicleSplit[1].equals("Van")) {

							van.setVehicleID(vehicleSplit[0]);
							van.setType(vehicleSplit[1]);
							van.setYear(Integer.parseInt(vehicleSplit[2]));
							van.setMake(vehicleSplit[3]);
							van.setModel(vehicleSplit[4]);
							van.setNumSeats(Integer.parseInt(vehicleSplit[5]));
							van.setStatus(vehicleSplit[6]);
							String lMDate = (vehicleSplit[7]);
							DateTime convertedLMDate = new DateTime(Integer.parseInt(lMDate.substring(0, 2)),
									Integer.parseInt(lMDate.substring(3, 5)), Integer.parseInt(lMDate.substring(6)));
							((Van) van).setLastMaintenance(convertedLMDate);
							van.setImage(vehicleSplit[8]);

							while ((sCurrentLine = br.readLine()) != null) {
								if (sCurrentLine.contains("_")) {
									RentalRecord record = new RentalRecord();
									String recordSplit[] = sCurrentLine.toString().split(":");
									String tempRentDate = recordSplit[1];
									DateTime convertedRentDate = new DateTime(
											Integer.parseInt(tempRentDate.substring(0, 2)),
											Integer.parseInt(tempRentDate.substring(3, 5)),
											Integer.parseInt(tempRentDate.substring(6)));

									// Convert estimatedReturnDate
									String tempestimatedReturnDate = recordSplit[2];
									DateTime convertedestimatedReturnDate = new DateTime(
											Integer.parseInt(tempestimatedReturnDate.substring(0, 2)),
											Integer.parseInt(tempestimatedReturnDate.substring(3, 5)),
											Integer.parseInt(tempestimatedReturnDate.substring(6)));

									// Convert actualReturnDate
									String tempactualReturnDate = recordSplit[3];
									DateTime convertedactualReturnDate = new DateTime(
											Integer.parseInt(tempactualReturnDate.substring(0, 2)),
											Integer.parseInt(tempactualReturnDate.substring(3, 5)),
											Integer.parseInt(tempactualReturnDate.substring(6)));

									String recordID[] = recordSplit[0].split("_");
									record.setCustomerID(recordID[1]);
									record.setRecordID(recordID[0], recordID[1], convertedRentDate);

									record.setRentDate(convertedRentDate);
									record.setEstimatedReturnDateDB(convertedestimatedReturnDate);
									record.setActualReturnDate(convertedactualReturnDate);
									record.setRentalFee(Double.parseDouble(recordSplit[4]));
									record.setLateFee(Double.parseDouble(recordSplit[5]));
									van.getRecord().add(record);
								}
							}
							model.getVehicleList().add(van);

						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else
			JOptionPane.showMessageDialog(null, "Import Canceled");
	}

}
