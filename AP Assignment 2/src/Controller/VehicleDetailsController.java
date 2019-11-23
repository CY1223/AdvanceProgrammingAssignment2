package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.time.LocalDate;

import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

import Model.DateTime;

import Model.ThriftyRentSystem;
import Model.Vehicle;

/**
 * Dialog to edit details of a person.
 *
 * @author Marco Jakob
 */
public class VehicleDetailsController {

	@FXML
	private Label idLabel;
	@FXML
	private Label modelLabel;
	@FXML
	private Label makeLabel;
	@FXML
	private Label yearLabel;
	@FXML
	private Label typeLabel;
	@FXML
	private Label seatLabel;
	@FXML
	private Label statusLabel;
	@FXML
	private ImageView imageView;
	@FXML
	private TextArea textArea;
	@FXML
	private TextField rentalReturnDate;
	@FXML
	private TextField customerID;
	@FXML
	private TextField dateRenting;
	@FXML
	private TextField daysRenting;
	@FXML
	private TextField completeMaintainence;
	@FXML
	private DatePicker datePickerRent;
	@FXML
	private DatePicker datePickerReturn;
	@FXML
	private DatePicker datePickerCMain;

	private Stage dialogStage;

	private Vehicle vehicle;

	private boolean okClicked = false;
	ThriftyRentSystem model = ThriftyRentSystem.getInstance();
	MainPageController obj = new MainPageController();

	// Initializes the controller class. This method is automatically called
	// after the fxml file has been loaded.

	@FXML
	private void initialize() {
		// Restricting access to past dates for the DatePicker
		Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker param) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						LocalDate today = LocalDate.now();
						setDisable(empty || item.compareTo(today) < 0);
					}

				};
			}

		};
		datePickerRent.setDayCellFactory(callB);
		datePickerReturn.setDayCellFactory(callB);
		datePickerCMain.setDayCellFactory(callB);

		convertDatePickerFormat(datePickerRent);
		convertDatePickerFormat(datePickerReturn);
	}

	/**
	 * Sets the stage of this dialog.
	 *
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	// Sets the vehicle chosen in the dialog.

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;

		idLabel.setText(vehicle.getVehicleID());
		modelLabel.setText(vehicle.getModel());
		makeLabel.setText(vehicle.getMake());
		yearLabel.setText(Integer.toString(vehicle.getYear()));
		typeLabel.setText(vehicle.getType());
		seatLabel.setText(Integer.toString(vehicle.getNumSeats()));
		statusLabel.setText(vehicle.getStatus());
		Image img = null;
		if (vehicle.getImage() == null) {
			img = new Image("/img/default.png");
		} else
			img = new Image("/img/" + vehicle.getImage());
		imageView.setImage(img);
		textArea.setText(vehicle.getDetails());
	}

	// Returns true if the user clicked OK, false otherwise.
	public boolean isOkClicked() {
		return okClicked;
	}

	// Called when the user clicks cancel.
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	public String getID() {
		return idLabel.getText();
	}

	// method that implements rent vehicle function
	@FXML
	private void rentVehicle() {
		String valid = getID();
		if (statusLabel.getText().equals("available")) {
			for (int i = 0; i < model.getVehicleList().size(); i++) {
				if (valid.equals(model.getVehicleList().get(i).getVehicleID()) && isInputValid()) {
					int days = 0;
					DateTime rentDate = null;

					String custID = customerID.getText();
					try {
						String date = datePickerRent.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
						rentDate = new DateTime(Integer.parseInt(date.substring(0, 2)),
								Integer.parseInt(date.substring(3, 5)), Integer.parseInt(date.substring(6)));
					} catch (Exception e) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.initOwner(dialogStage);
						alert.setTitle("Invalid Fields");
						alert.setHeaderText("Please correct invalid fields");
						alert.setContentText("Please enter rent date!/n");

						alert.showAndWait();
					}

					days = Integer.parseInt(daysRenting.getText());

					model.getVehicleList().get(i).rent(custID, rentDate, days);
				}
			}
		} else {
			JOptionPane.showMessageDialog(null, "Not available to rent");
		}
		// }
	}

	// method that implements return vehicle function
	@FXML
	private void returnVehicle() {
		String valid = getID();
		for (int i = 0; i < model.getVehicleList().size(); i++) {
			if (valid.equals(model.getVehicleList().get(i).getVehicleID())) {
				if (model.getVehicleList().get(i).getStatus() == "rented") {
					DateTime returnDate = null;
					boolean errorDate = true;

					while (errorDate) {
						try {
							String date = datePickerReturn.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

							returnDate = new DateTime(Integer.parseInt(date.substring(0, 2)),
									Integer.parseInt(date.substring(3, 5)), Integer.parseInt(date.substring(6)));
							errorDate = false;
						} catch (StringIndexOutOfBoundsException | NumberFormatException e) {
							Alert alert = new Alert(AlertType.ERROR);
							alert.initOwner(dialogStage);
							alert.setTitle("Invalid Fields");
							alert.setHeaderText("Please correct invalid fields");
							alert.setContentText("Please enter rent date!/n");

							alert.showAndWait();
							break;
						}
					}

					model.getVehicleList().get(i).returnVehicle(returnDate);
				} else if (model.getVehicleList().get(i).getStatus().equals("available")) {
					JOptionPane.showMessageDialog(null, "Vehicle has not been rented yet");
				} else if (model.getVehicleList().get(i).getStatus().equals("under maintenance")) {
					JOptionPane.showMessageDialog(null, "Vehicle is under maintenance. Returning to main menu...");
				}
			}
		}
	}

	// method that implements vehicle maintenance
	@FXML
	private void vehicleMaintainence() {
		String valid = getID();
		for (int i = 0; i < model.getVehicleList().size(); i++) {
			if (valid.equals(model.getVehicleList().get(i).getVehicleID())) {
				model.getVehicleList().get(i).performMaintenance();
			}
		}
	}

	// method that implements complete maintenance function
	@FXML
	private void completeMaintainence() {
		String valid = getID();
		for (int i = 0; i < model.getVehicleList().size(); i++) {
			if (valid.equals(model.getVehicleList().get(i).getVehicleID())) {
				DateTime maintenanceCompletionDate = null;

				boolean errorDate = true;
				while (errorDate) {
					try {
						String date = datePickerCMain.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

						maintenanceCompletionDate = new DateTime(Integer.parseInt(date.substring(0, 2)),
								Integer.parseInt(date.substring(3, 5)), Integer.parseInt(date.substring(6)));
						errorDate = false;
					} catch (Exception e) {
						Alert alert = new Alert(AlertType.ERROR);
						alert.initOwner(dialogStage);
						alert.setTitle("Invalid Fields");
						alert.setHeaderText("Please correct invalid fields");
						alert.setContentText("Please enter complete maintainence date!/n");
						alert.showAndWait();
						break;
					}
				}
				model.getVehicleList().get(i).completeMaintenance(maintenanceCompletionDate);
			}
		}
	}

	// method that checks user input for each field
	private boolean isInputValid() {
		String errorMessage = "";

		if (daysRenting.getText() == null || daysRenting.getText().length() == 0) {
			errorMessage += "Please enter days renting\n";
		} else {
			// try to parse the postal code into an int.
			try {
				Integer.parseInt(daysRenting.getText());
			} catch (NumberFormatException e) {
				errorMessage += "Not a valid format(must be an integer)!\n";
			}
		}
		if (customerID.getText() == null || customerID.getText().length() == 0) {
			errorMessage += "The field 'Customer ID' can't be empty!\n";
		}
		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

	// converts inputs from datepicker to Datetime variable
	public void convertDatePickerFormat(DatePicker input) {
		String pattern = "dd/MM/yyyy";
		input.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		});
	}
}