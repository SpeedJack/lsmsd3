package ristogo.ui.graphics.controls;

import java.util.function.Consumer;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import ristogo.ui.graphics.controls.base.CitySelector;
import ristogo.ui.graphics.controls.base.ControlBox;
import ristogo.ui.graphics.controls.base.CuisineSelector;
import ristogo.ui.graphics.controls.base.FormButton;

public class AddCuisineControlBox extends ControlBox
{
	private final CuisineSelector addField = new CuisineSelector();
	private final FormButton addButton;

	public AddCuisineControlBox()
	{
		super();
		addField.setPromptText("Cuisine name");
		addButton = new FormButton("Add");
		addButton.setDisable(true);

		addField.textProperty().addListener(this::textChangeListener);

		addControl(addField);
		addControl(addButton);
	}

	public void setButtonText(String text)
	{
		addButton.setText(text);
	}

	public void setOnClick(Consumer<String> handler)
	{
		addButton.setOnAction((event) -> {
			handler.accept(addField.getText());
			addField.clear();
			addButton.setDisable(true);
		});
	}

	public void setButtonDisable(boolean value)
	{
		addButton.setDisable(value);
	}

	private void textChangeListener(ObservableValue<? extends String> observable, String oldValue, String newValue)
	{
		addButton.setDisable(addField.getText() == null || addField.getText().isEmpty());
	}
}
