package ristogo.ui.graphics;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ristogo.common.entities.User;
import ristogo.ui.graphics.config.GUIConfig;

public class CityViewer extends VBox {
	
	private final Label cityTableTitle = new Label();
	private final Button operationButton = new Button();
	private final Button flushButton = new Button();
	private final CityForm cityForm = new CityForm();
	private final UserTableView userTable = new UserTableView(); //TODO: Inserire la table view corretta
	
	private final TextField findField = new TextField();
	private final Button find = new Button();
	
	public CityViewer () {
		
		super(10);
		
		cityTableTitle.setText("List of Locations");
		cityTableTitle.setFont(GUIConfig.getFormTitleFont());
		cityTableTitle.setTextFill(GUIConfig.getFgColor());
		cityTableTitle.setStyle(GUIConfig.getCSSFormTitleStyle());
		
		operationButton.setText("Add");
		operationButton.setFont(GUIConfig.getButtonFont());
		operationButton.setTextFill(GUIConfig.getInvertedFgColor());
		operationButton.setStyle(GUIConfig.getInvertedCSSButtonBgColor());
		
		flushButton.setText("Flush");
		flushButton.setFont(GUIConfig.getButtonFont());
		flushButton.setTextFill(GUIConfig.getInvertedFgColor());
		flushButton.setStyle(GUIConfig.getInvertedCSSButtonBgColor());
		
		findField.setPromptText("search Locations by city name");
		findField.setMinSize(200, 30);
		findField.setMaxSize(200, 30);
		
		find.setText("Find");
		find.setFont(GUIConfig.getButtonFont());
		find.setTextFill(GUIConfig.getInvertedFgColor());
		find.setStyle(GUIConfig.getInvertedCSSButtonBgColor());
		HBox findBox = new HBox(10);
		findBox.getChildren().addAll(findField, find);
		
		HBox buttonBox = new HBox(60);
		buttonBox.setAlignment(Pos.CENTER);
		buttonBox.setMargin(operationButton, new Insets(0, 0, 0, 100));
		buttonBox.getChildren().addAll(operationButton, flushButton);

		HBox cityBox = new HBox(10);
		cityBox.getChildren().addAll(cityForm, buttonBox);
		
		this.getChildren().addAll(cityTableTitle, cityBox, userTable, findBox);
	
		//userTable.refreshRestaurants();
	
		userTable.setOnMouseClicked((event) -> {
			User user = userTable.getSelectedEntity();
			if (user == null)
				return;
			operationButton.setText("Remove");
		});
		
		cityForm.setOnMouseClicked((event) -> {
			operationButton.setText("Add");
		});

		operationButton.setOnAction(this::handleOperationButtonAction);
		flushButton.setOnAction(this::handleFlushButtonAction);
	}
	
	
	private void handleOperationButtonAction(ActionEvent event)
	{
		String cityName = cityForm.getCity();
		String countryName = cityForm.getCountry();
		String stateName = cityForm.getState();
		
		if (cityName == null  || countryName == null || stateName == null)
			return;
		//TODO: gestire azioni
	}
	
	private void handleFlushButtonAction(ActionEvent event)
	{
		cityForm.setCity("");
		cityForm.setCountry("");
		cityForm.setState("");
	}
	
	public void changeConfigurationCityViewer(int config) {
		
		switch(config) {
		
		case 0:
			operationButton.setText("Add");
			//TODO: refresh-table
			break;
		case 1:
		case 2:
			operationButton.setText("Remove");
			//TODO: refresh-table
		default:
			break;
		}
	}
	

}
