package Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.io.IOException;

import Model.Vehicle;
import View.Main;

//custom list cell class
public class VehicleListViewCell extends ListCell<Vehicle> {

	@FXML
	private Label make;

	@FXML
	private Label status;

	@FXML
	private GridPane gridPane;
	@FXML
	private ImageView imageView;
	@FXML
	private Button display;

	private FXMLLoader Obj;
	Main obj = new Main();

	@Override
	protected void updateItem(Vehicle vehicle, boolean empty) {
		super.updateItem(vehicle, empty);
		//if no vehicle to display set everything to null
		if (empty || vehicle == null) {

			setText(null);
			setGraphic(null);

		} else {
			if (Obj == null) {
				//loading custom list cell FXML file
				Obj = new FXMLLoader(getClass().getResource("/Controller/ListCell.fxml"));
				Obj.setController(this);

				try {
					Obj.load();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}
			//Setting vehicle Images, make and status to the UI
			Image img = null;
			if (vehicle.getImage() == null) {
				img = new Image("/img/default.png");
			} else
				img = new Image("/img/" + vehicle.getImage());
			make.setText(String.valueOf(vehicle.getMake()));
			status.setText(String.valueOf(vehicle.getStatus()));

			imageView.setImage(img);

			setText(null);
			setGraphic(gridPane);
		}

	}
}
