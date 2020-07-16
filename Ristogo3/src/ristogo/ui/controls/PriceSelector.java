package ristogo.ui.controls;

import javafx.scene.control.ChoiceBox;
import ristogo.common.net.entities.enums.Price;

public class PriceSelector extends ChoiceBox<Price>
{
	public PriceSelector()
	{
		super();
		getItems().addAll(Price.values());
	}
}
