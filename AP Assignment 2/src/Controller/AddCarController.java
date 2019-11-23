package Controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

import javax.swing.JOptionPane;

import Model.Vehicle;

public class AddCarController {

	@FXML
	private TextField yearField;
	@FXML
	private TextField makeField;
	@FXML
	private TextField modelField;
	@FXML
	private RadioButton numberOfSeatsChoice4;
	@FXML
	private RadioButton numberOfSeatsChoice7;
	@FXML
	private Label actionStatus;
	private ToggleGroup numberOfSeats;

	private Stage dialogStage;
	private Vehicle vehicle;
	private boolean okClicked = false;
	private static int idCounter = 1;

	// Initializes the controller class. This method is automatically called
	// after the fxml file has been loaded.
	@FXML
	private void initialize() {
		// Group the two radio button together
		numberOfSeats = new ToggleGroup();
		this.numberOfSeatsChoice4.setToggleGroup(numberOfSeats);
		this.numberOfSeatsChoice7.setToggleGroup(numberOfSeats);
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
		// sets the car details that the user entered
		if (isInputValid()) {
			vehicle.setVehicleID("C#000" + createID());
			vehicle.setYear(Integer.parseInt(yearField.getText()));
			vehicle.setMake(makeField.getText());
			vehicle.setModel(modelField.getText());
			vehicle.setType("Car");
			if (this.numberOfSeats.getSelectedToggle().equals(this.numberOfSeatsChoice4)) {
				vehicle.setNumSeats(4);
			}
			if (this.numberOfSeats.getSelectedToggle().equals(this.numberOfSeatsChoice7)) {
				vehicle.setNumSeats(7);
			}
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

	// Validates the user input of each fields.
	private boolean isInputValid() {
		String errorMessage = "";

		if (yearField.getText() == null || yearField.getText().length() == 0) {
			errorMessage += "Please enter the year!\n";
		} else {
			try {
				Integer.parseInt(yearField.getText());
			} catch (NumberFormatException e) {
				errorMessage += "Not a valid format for year (must be an integer)!\n";
			}
		}
		if (makeField.getText() == null || makeField.getText().length() == 0) {
			errorMessage += "The field 'make' can't be empty\n";
		}
		if (modelField.getText() == null || modelField.getText().length() == 0) {
			errorMessage += "The field 'model' can't be empty\n";
		}
		if (errorMessage.length() == 0) {
			return true;
		} else {
			// Show the error message if there is an error.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Invalid Fields");
			alert.setHeaderText("Please correct invalid fields");
			alert.setContentText(errorMessage);

			alert.showAndWait();

			return false;
		}
	}

	// Method that calls window explorer to add image
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