package Controller;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

import Model.DateTime;
import Model.Van;
import Model.Vehicle;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class AddVanController {

	@FXML
	private TextField yearField;
	@FXML
	private TextField makeField;
	@FXML
	private TextField modelField;
	@FXML
	private DatePicker lastMaintainenceDate;
	@FXML
	private Label actionStatus;

	private Stage dialogStage;
	private Vehicle vehicle;
	private boolean okClicked = false;
	private static int idCounter = 1;
	VehicleDetailsController obj = new VehicleDetailsController();

	// Initializes the controller class. This method is automatically called
	// after the fxml file has been loaded.
	@FXML
	private void initialize() {
		// Restricting access to future dates for the DatePicker
		Callback<DatePicker, DateCell> callB = new Callback<DatePicker, DateCell>() {
			@Override
			public DateCell call(final DatePicker param) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						LocalDate today = LocalDate.now();
						setDisable(empty || item.compareTo(today) > 0);
					}

				};
			}

		};
		lastMaintainenceDate.setDayCellFactory(callB);
		obj.convertDatePickerFormat(lastMaintainenceDate);
	}

	// Sets the stage of this dialog.
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	// Sets the person to be edited in the dialog.

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	// Returns true if the user clicked OK, false otherwise.
	public boolean isOkClicked() {
		return okClicked;
	}

	// Called when the user clicks ok.
	@FXML
	private void handleOk() {
		// sets the van details that the user entered
		if (isInputValid()) {
			vehicle.setVehicleID("V#000" + createID());
			vehicle.setYear(Integer.parseInt(yearField.getText()));
			vehicle.setMake(makeField.getText());
			vehicle.setModel(modelField.getText());
			vehicle.setType("Van");
			vehicle.setNumSeats(15);
			String date = lastMaintainenceDate.getValue().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			// Converting String date to DateTime
			DateTime tempLastMaintenanceDate = new DateTime(Integer.parseInt(date.substring(0, 2)),
					Integer.parseInt(date.substring(3, 5)), Integer.parseInt(date.substring(6)));

			((Van) vehicle).setLastMaintenance(tempLastMaintenanceDate);

			okClicked = true;
			dialogStage.close();
		} else
			JOptionPane.showMessageDialog(null, "Add Vehicle Cancel");
	}

	// Called when the user clicks cancel.
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	// Validates the user input in the text fields.
	private boolean isInputValid() {
		String errorMessage = "";

		if (yearField.getText() == null || yearField.getText().length() == 0) {
			errorMessage += "Please enter the year!\n";
		} else {
			// try to parse the postal code into an int.
			try {
				Integer.parseInt(yearField.getText());
			} catch (NumberFormatException e) {
				errorMessage += "Not a valid format for year (must be an integer)!\n";
			}
		}
		if (makeField.getText() == null || makeField.getText().length() == 0) {
			errorMessage += "The field 'make' can't be empty!\n";
		}
		if (modelField.getText() == null || modelField.getText().length() == 0) {
			errorMessage += "The field 'model' can't be empty!\n";
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

	// Method that adds vehicle image for van
	public void addImage() {
		FileChooser fileChooser = new FileChooser();
		File selectedFile = fileChooser.showOpenDialog(null);

		if (selectedFile != null) {

			actionStatus.setText("File selected: " + selectedFile.getName());
			vehicle.setImage(selectedFile.getName());

		} else {
			actionStatus.setText("File selection cancelled.");
		}
	}

	public static synchronized String createID() {
		return String.valueOf(idCounter++);
	}
}
