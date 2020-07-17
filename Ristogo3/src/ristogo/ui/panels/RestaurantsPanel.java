package ristogo.ui.panels;

import java.util.Optional;

import ristogo.common.net.ResponseMessage;
import ristogo.common.net.entities.RecommendRestaurantInfo;
import ristogo.common.net.entities.RestaurantInfo;
import ristogo.common.net.entities.StringFilter;
import ristogo.net.Protocol;
import ristogo.ui.ErrorBox;
import ristogo.ui.beans.RestaurantBean;
import ristogo.ui.boxes.ButtonControlBox;
import ristogo.ui.boxes.TextAreaDetailsBox;
import ristogo.ui.controls.bars.MenuBar;
import ristogo.ui.dialogs.RestaurantRecommendDialog;
import ristogo.ui.panels.base.TablePanel;
import ristogo.ui.tables.RestaurantsTableView;
import ristogo.ui.tables.base.PagedTableView;

public class RestaurantsPanel extends TablePanel
{
	public RestaurantsPanel(boolean deletable)
	{
		super("Restaurants");
		MenuBar menuBar = new MenuBar();
		ButtonControlBox controlBox = new ButtonControlBox("Like");
		RestaurantsTableView tv = new RestaurantsTableView();
		PagedTableView<RestaurantBean> ptv = new PagedTableView<RestaurantBean>(tv);
		TextAreaDetailsBox detailsBox = new TextAreaDetailsBox("Description:");
		ptv.setFindHint("Name...");
		controlBox.setButtonDisable(true);
		ptv.setDeleteDisable(true);
		menuBar.addMenu("All", () -> {
			controlBox.setButtonDisable(true);
			ptv.setDeleteDisable(true);
			detailsBox.setText("");
			ptv.setFilterFunction(tv::filter);
			tv.filter(null);
			ptv.reset();
		});
		menuBar.addMenu("Liked", () -> {
			controlBox.setButtonDisable(true);
			ptv.setDeleteDisable(true);
			detailsBox.setText("");
			ptv.setFilterFunction(tv::filterLiked);
			tv.filterLiked(null);
			ptv.reset();
		});
		menuBar.addMenu("Recommend", () -> {
			RestaurantRecommendDialog login = new RestaurantRecommendDialog();
			Optional<RecommendRestaurantInfo> result = login.showAndWait();
			result.ifPresentOrElse(
				(data) -> {
					controlBox.setButtonDisable(true);
					ptv.setDeleteDisable(true);
					detailsBox.setText("");
					ptv.setFilterFunction(tv::filterRecommend);
					tv.setRecommendFilter(data);
					tv.filterRecommend(null);
					ptv.reset();
				},
				() -> { }
			);
		});
		setMenuBar(menuBar);
		controlBox.setOnClick(() -> {
			RestaurantBean item = ptv.getSelection();
			if (item == null) {
				controlBox.setButtonDisable(true);
				ptv.setDeleteDisable(true);
				return;
			}
			ResponseMessage resMsg;
			if (!item.isLiked())
				resMsg = Protocol.getInstance().likeRestaurant(new StringFilter(item.getName()));
			else
				resMsg = Protocol.getInstance().unlikeRestaurant(new StringFilter(item.getName()));
			if (!resMsg.isSuccess()) {
				new ErrorBox("Error", "An error has occured while trying to (un)like the restaurant.", resMsg.getErrorMsg()).showAndWait();
				return;
			}
			item.setLiked(!item.isLiked());
			controlBox.setButtonDisable(true);
			ptv.setDeleteDisable(true);
			detailsBox.setText("");
			ptv.refresh();
		});
		setControlBox(controlBox);
		ptv.setOnSelect((item) -> {
			controlBox.setButtonDisable(item == null);
			detailsBox.setText("");
			if (item == null)
				return;
			ResponseMessage resMsg = Protocol.getInstance().getRestaurant(new StringFilter(item.getName()));
			if (!resMsg.isSuccess()) {
				new ErrorBox("Error", "An error has occured while trying to get restaurant's description.", resMsg.getErrorMsg()).showAndWait();
				return;
			}
			item.setLiked(resMsg.getEntity(RestaurantInfo.class).isLiked());
			controlBox.setText(resMsg.getEntity(RestaurantInfo.class).isLiked() ? "Unlike" : "Like");
			detailsBox.setText(resMsg.getEntity(RestaurantInfo.class).getDescription());
		});
		ptv.setDeletable(deletable);
		if (deletable)
			ptv.setOnDelete((item) -> {
				ResponseMessage resMsg = Protocol.getInstance().deleteRestaurant(new StringFilter(item.getName()));
				if (!resMsg.isSuccess()) {
					new ErrorBox("Error", "An error has occured while trying to delete the restaurant.", resMsg.getErrorMsg()).showAndWait();
					return;
				}
				controlBox.setButtonDisable(true);
				ptv.setDeleteDisable(true);
				detailsBox.setText("");
				ptv.refresh();
			});
		setTableView(ptv);
		setDetailsBox(detailsBox);
		showContent();
	}
}
