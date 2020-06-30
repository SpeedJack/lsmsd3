package ristogo.ui.graphics;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import ristogo.common.entities.City;
import ristogo.common.entities.Cuisine;
import ristogo.ui.graphics.beans.*;
import ristogo.ui.graphics.config.GUIConfig;


public class CityTableView extends TableView<CityBean>{
	
	
	private final ObservableList<CityBean> cityList;

	@SuppressWarnings("unchecked")
	CityTableView()
	{
		cityList = FXCollections.observableArrayList();

		setEditable(false);
		setFixedCellSize(35);
		setMinWidth(600);
		setMaxWidth(600);
		setMaxHeight(GUIConfig.getMaxRowDisplayable() * getFixedCellSize());

		TableColumn<CityBean, String> nameColumn = new TableColumn<CityBean, String>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setStyle(GUIConfig.getCSSTableColumnStyle(false));
		nameColumn.setMinWidth(200);
		nameColumn.setMaxWidth(200);

		TableColumn<CityBean, Double> latitudeColumn = new TableColumn<CityBean, Double>("Latitude");
		latitudeColumn.setCellValueFactory(new PropertyValueFactory<>("latitude"));
		latitudeColumn.setStyle(GUIConfig.getCSSTableColumnStyle(false));
		latitudeColumn.setMinWidth(200);
		latitudeColumn.setMaxWidth(200);
		
		TableColumn<CityBean, Double> longitudeColumn = new TableColumn<CityBean, Double>("Longitude");
		longitudeColumn.setCellValueFactory(new PropertyValueFactory<>("longitude"));
		longitudeColumn.setStyle(GUIConfig.getCSSTableColumnStyle(false));
		longitudeColumn.setMinWidth(200);
		longitudeColumn.setMaxWidth(200);
		
		getColumns().addAll(nameColumn, latitudeColumn, longitudeColumn);
		setItems(cityList);

	}

	City getSelectedEntity()
	{
		CityBean cityBean = getSelectionModel().getSelectedItem();
		return cityBean == null ? null : cityBean.toEntity();
	}

}
