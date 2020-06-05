package ristogo.ui.graphics;

import java.util.Optional;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import ristogo.common.entities.Reservation;
import ristogo.common.entities.Restaurant;
import ristogo.common.entities.User;
import ristogo.common.net.ResponseMessage;
import ristogo.net.Protocol;
import ristogo.ui.graphics.config.GUIConfig;
import ristogo.ui.graphics.controls.FormButton;

public final class RistogoGUI extends Application
{
	private static User loggedUser;
	private static Restaurant restaurant;

	private static ModifyRestaurantForm restaurantForm;

	@Override
	public void start(Stage primaryStage)
	{
		LoginDialog login = new LoginDialog();
		Optional<User> result = login.showAndWait();
		result.ifPresentOrElse(
			data -> { loggedUser = data; },
			() -> { System.exit(0); }
		);

		HBox applicationInterface;
		applicationInterface = loggedUser.isOwner() ? buildOwnerInterface() : buildCustomerInterface();

		Scene scene = new Scene(new Group(applicationInterface));
		scene.setFill(GUIConfig.getBgColor());

		primaryStage.setTitle("RistoGo");
		primaryStage.setResizable(true);
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("/resources/logo.png"));
		primaryStage.show();
	}

	@Override
	public void stop()
	{
		Protocol.getInstance().performLogout();
		System.exit(0);
	}

	private HBox buildCustomerInterface()
	{
		HBox applicationInterface = new HBox(10);
		GridPane title = generateTitle();
		Label reservationsTableTitle = new Label("List of Restaurant: you can find restaurants searching by city");
		reservationsTableTitle.setFont(GUIConfig.getFormTitleFont());
		reservationsTableTitle.setTextFill(GUIConfig.getFgColor());
		reservationsTableTitle.setStyle(GUIConfig.getCSSFormTitleStyle());

		TableViewReservation reservationsTable = new TableViewReservation();
		reservationsTable.refreshReservations();

		BookForm bookForm = new BookForm(reservationsTable::refreshReservations);

		TextField findCityField = new TextField();
		findCityField.setPromptText("insert a name of a city");
		findCityField.setMinSize(200, 30);
		findCityField.setMaxSize(200, 30);
		Button find = new Button("Find");
		find.setFont(GUIConfig.getButtonFont());
		find.setTextFill(GUIConfig.getInvertedFgColor());
		find.setStyle(GUIConfig.getInvertedCSSButtonBgColor());

		HBox findBox = new HBox(10);
		findBox.getChildren().addAll(findCityField, find);

		TableViewRestaurant restaurantsTable = new TableViewRestaurant();
		restaurantsTable.refreshRestaurants();

		Label descriptionLabel = new Label("Description: ");
		descriptionLabel.setFont(GUIConfig.getBoldVeryTinyTextFont());
		descriptionLabel.setTextFill(GUIConfig.getFgColor());
		TextArea descriptionField = new TextArea();
		descriptionField.setWrapText(true);
		descriptionField.setEditable(false);
		descriptionField.setMinSize(480, 100);
		descriptionField.setMaxSize(480, 100);

		HBox descriptionBox = new HBox(20);
		descriptionBox.getChildren().addAll(descriptionLabel, descriptionField);

		Label restaurantsTableTitle = new Label("My Reservation");
		restaurantsTableTitle.setFont(GUIConfig.getFormTitleFont());
		restaurantsTableTitle.setTextFill(GUIConfig.getFgColor());
		restaurantsTableTitle.setStyle(GUIConfig.getCSSFormTitleStyle());

		restaurantsTable.setOnMouseClicked((event) -> {
			Restaurant restaurant = restaurantsTable.getSelectedEntity();
			if (restaurant == null)
				return;
			bookForm.fill(restaurant);
			descriptionField.setText(restaurant.getDescription());
		});

		reservationsTable.setOnMouseClicked((event) -> {
			Reservation reservation = reservationsTable.getSelectedEntity();
			if (reservation == null)
				return;
			bookForm.fill(reservation);
		});

		find.setOnAction((event) -> {
			String city = findCityField.getText();
			if (city == null)
				return;
			restaurantsTable.refreshRestaurants(city);
		});

		VBox leftPart = new VBox(10);
		VBox rightPart = new VBox(10);

		leftPart.getChildren().addAll(title, bookForm);
		rightPart.getChildren().addAll(reservationsTableTitle,findBox, restaurantsTable, descriptionBox, restaurantsTableTitle, reservationsTable);
		applicationInterface.getChildren().addAll(leftPart, rightPart);

		leftPart.setStyle(GUIConfig.getCSSInterfacePartStyle());
		rightPart.setStyle(GUIConfig.getCSSInterfacePartStyle());
		leftPart.setPrefSize(400, 600);
		rightPart.setPrefSize(600, 600);
		applicationInterface.setPrefSize(1000, 600);

		return applicationInterface;
	}

	private HBox buildOwnerInterface()
	{
		HBox applicationInterface = new HBox(10);

		GridPane title = generateTitle();
		restaurantForm = new ModifyRestaurantForm(this::getOwnRestaurant);
		getOwnRestaurant();

		Label reservationsTableTitle = new Label("List of Reservations at your restaurant");
		reservationsTableTitle.setFont(GUIConfig.getFormTitleFont());
		reservationsTableTitle.setTextFill(GUIConfig.getFgColor());
		reservationsTableTitle.setStyle(GUIConfig.getCSSFormTitleStyle());

		TableViewReservation reservationsTable = new TableViewReservation(restaurant);
		reservationsTable.refreshReservations();

		FormButton refreshButton = new FormButton("Refresh");

		refreshButton.setOnAction((ActionEvent ev) -> {
			reservationsTable.refreshReservations();
		});

		VBox leftPart = new VBox(10);
		VBox rightPart = new VBox(10);

		leftPart.getChildren().addAll(title, restaurantForm);
		rightPart.getChildren().addAll(reservationsTableTitle, reservationsTable, refreshButton);
		applicationInterface.getChildren().addAll(leftPart, rightPart);

		leftPart.setStyle(GUIConfig.getCSSInterfacePartStyle());
		rightPart.setStyle(GUIConfig.getCSSInterfacePartStyle());
		leftPart.setPrefSize(400, 600);
		rightPart.setPrefSize(600, 600);
		applicationInterface.setPrefSize(1000, 600);
		rightPart.setAlignment(Pos.CENTER);

		return applicationInterface;
	}

	private GridPane generateTitle()
	{
		Label title = new Label("RistoGo");
		title.setFont(GUIConfig.getTitleFont());
		title.setTextFill(GUIConfig.getFgColor());

		ImageView icon = new ImageView(getClass().getResource("/resources/logo.png").toString());
		icon.setFitHeight(30);
		icon.setFitWidth(30);

		Label welcomeLabel = new Label("Welcome");
		welcomeLabel.setFont(GUIConfig.getWelcomeFont());
		welcomeLabel.setTextFill(GUIConfig.getFgColor());

		Label usernameLabel = new Label(loggedUser.getUsername());
		usernameLabel.setFont(GUIConfig.getUsernameFont());
		usernameLabel.setTextFill(GUIConfig.getFgColor());

		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(30);
		grid.setPadding(new Insets(1, 1, 5, 1));

		grid.add(title, 0, 0);
		grid.add(icon, 1, 0);
		grid.add(welcomeLabel, 0, 1);
		grid.add(usernameLabel, 1, 1);

		return grid;
	}

	public static void launch(String... args)
	{
		Application.launch(args);
	}

	private void getOwnRestaurant()
	{
		if (restaurantForm == null)
			return;
		ResponseMessage resMsg = Protocol.getInstance().getOwnRestaurant();
		if (!resMsg.isSuccess()) {
			new ErrorBox("Error", "An error has occured while fetching the informations about your restaurant.", resMsg.getErrorMsg()).showAndExit();
			restaurant = null;
			return;
		}
		restaurant = (Restaurant)resMsg.getEntity();
		restaurantForm.setRestaurant(restaurant);
	}
}
