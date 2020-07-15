package ristogo.ui.graphics.oldcode;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import ristogo.common.net.entities.UserInfo;
import ristogo.net.Protocol;
import ristogo.ui.graphics.config.GUIConfig;
import ristogo.ui.graphics.controls.base.FormLabel;

public class UserViewer extends VBox {
	/*
	private final FormLabel userTableTitle = new FormLabel("List of Users");
	private final TextField findField = new TextField();
	private final Button find = new Button();
	private final Button followButton = new Button();
	private final UserTableView userTable = new UserTableView();
	private final FormLabel cuisinesLabel = new FormLabel("Cusisines that selected user likes:");
	private final TextArea cuisinesField = new TextArea();
	
	private UserInfo loggedUser;
	
	public UserViewer (UserInfo loggedUser) {
		
		super(10);
		
		userTableTitle.setFont(GUIConfig.getFormTitleFont());
		userTableTitle.setTextFill(GUIConfig.getFgColor());
		userTableTitle.setStyle(GUIConfig.getCSSFormTitleStyle());
		
		findField.setPromptText("insert a name of a user");
		findField.setMinSize(200, 30);
		findField.setMaxSize(200, 30);
		
		find.setText("Find");
		find.setFont(GUIConfig.getButtonFont());
		find.setTextFill(GUIConfig.getInvertedFgColor());
		find.setStyle(GUIConfig.getInvertedCSSButtonBgColor());
		
		followButton.setText("Follow");
		followButton.setFont(GUIConfig.getButtonFont());
		followButton.setTextFill(GUIConfig.getInvertedFgColor());
		followButton.setStyle(GUIConfig.getInvertedCSSButtonBgColor());
		followButton.setDisable(true);
		
		cuisinesField.setWrapText(true);
		cuisinesField.setEditable(false);
		cuisinesField.setMinSize(480, 100);
		cuisinesField.setMaxSize(480, 100);

		HBox findBox = new HBox(10);
		findBox.getChildren().addAll(findField, find, followButton);
				
		this.getChildren().addAll(userTableTitle, findBox, userTable, cuisinesLabel, cuisinesField);
		
		userTable.loadFriends(loggedUser);

		userTable.setOnMouseClicked((event) -> {
			UserInfo user = userTable.getSelectedEntity();
			if (user == null)
				return;
			followButton.setDisable(false);
			cuisinesField.setText(""/*convert cuisines that user likes to string");
		});

		find.setOnAction(this::handleFindButtonAction);
		followButton.setOnAction(this::handleLikeButtonAction);
	}
	
	private void handleFindButtonAction(ActionEvent event)
	{
		String name = findField.getText();
		if (name == null)
			userTable.loadUser();
		else
		{
			userTable.loadUser(name);
		}
	}
	
	private void handleLikeButtonAction(ActionEvent event)
	{
		UserInfo selectedUser = userTable.getSelectedEntity();
		if(selectedUser == null)
			return;
		if(followButton.getText().equals("Follow"))
			Protocol.getInstance().followUser(selectedUser);
		else
			Protocol.getInstance().unfollowUser(selectedUser);
		userTable.loadFriends(loggedUser);
	}


	public Button getFollowButton() {
		return followButton;
	}

	public Label getUserTableTitle() {
		return userTableTitle;
	}
	
	public UserTableView getTable() {
		return userTable;
	}
*/
	

}