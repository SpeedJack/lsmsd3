package ristogo.ui.graphics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ristogo.common.entities.Customer;
import ristogo.common.entities.Entity;
import ristogo.common.entities.Restaurant;
import ristogo.common.entities.User;
import ristogo.common.entities.enums.Price;
import ristogo.common.net.ResponseMessage;
import ristogo.net.Protocol;
import ristogo.ui.graphics.beans.*;
import ristogo.ui.graphics.config.GUIConfig;


public class UserTableView extends TableView<UserBean>{
	
	
	private final ObservableList<UserBean> userList;

	@SuppressWarnings("unchecked")
	UserTableView()
	{
		userList = FXCollections.observableArrayList();

		setEditable(false);
		setFixedCellSize(35);
		setMinWidth(600);
		setMaxWidth(600);
		setMaxHeight(GUIConfig.getMaxRowDisplayable() * getFixedCellSize());

		TableColumn<UserBean, String> nameColumn = new TableColumn<UserBean, String>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setStyle(GUIConfig.getCSSTableColumnStyle(false));
		nameColumn.setMinWidth(300);
		nameColumn.setMaxWidth(300);

		TableColumn<UserBean, String> cityColumn = new TableColumn<UserBean, String>("City");
		cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
		cityColumn.setStyle(GUIConfig.getCSSTableColumnStyle());
		cityColumn.setMinWidth(300);
		cityColumn.setMaxWidth(300);


		getColumns().addAll(nameColumn, cityColumn);
		setItems(userList);

	}

	User getSelectedEntity()
	{
		UserBean userBean = getSelectionModel().getSelectedItem();
		return userBean == null ? null : userBean.toEntity();
	}

	void loadFriends(User loggedUser)
	{
		userList.clear();
		ResponseMessage resMsg = Protocol.getInstance().getFriends(loggedUser);
		if (resMsg.isSuccess())
			for (Entity entity : resMsg.getEntities())
				userList.add(UserBean.fromEntity((User)entity));
		else
			new ErrorBox("Error", "An error has occured while fetching the list of users.", resMsg.getErrorMsg()).showAndWait();
	}
	
	void loadUser()
	{
		loadUser(null);
	}
	

	void loadUser(String findFriend)
	{
		userList.clear();
		ResponseMessage resMsg;
		if(findFriend == null || findFriend.isBlank()) {
			resMsg = Protocol.getInstance().getUsers();
		} else {
			User user = new Customer(findFriend);
			resMsg = Protocol.getInstance().getUsers(user);
		}
		if (resMsg.isSuccess())
			for (Entity entity : resMsg.getEntities())
				userList.add(UserBean.fromEntity((User)entity));
		else
			new ErrorBox("Error", "An error has occured while fetching the list of users.", resMsg.getErrorMsg()).showAndWait();
	}

}
