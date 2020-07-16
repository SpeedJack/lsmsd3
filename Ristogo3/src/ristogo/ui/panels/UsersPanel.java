package ristogo.ui.panels;

import java.util.List;
import java.util.Optional;

import ristogo.common.net.ResponseMessage;
import ristogo.common.net.entities.CuisineInfo;
import ristogo.common.net.entities.RecommendUserInfo;
import ristogo.common.net.entities.StringFilter;
import ristogo.net.Protocol;
import ristogo.ui.ErrorBox;
import ristogo.ui.beans.UserBean;
import ristogo.ui.boxes.ButtonControlBox;
import ristogo.ui.boxes.TextAreaDetailsBox;
import ristogo.ui.controls.bars.MenuBar;
import ristogo.ui.dialogs.UserRecommendDialog;
import ristogo.ui.panels.base.TablePanel;
import ristogo.ui.tables.UsersTableView;
import ristogo.ui.tables.base.PagedTableView;

public class UsersPanel extends TablePanel
{
	public UsersPanel(boolean deletable)
	{
		super("Users");
		MenuBar menuBar = new MenuBar();
		ButtonControlBox controlBox = new ButtonControlBox("Follow");
		UsersTableView tv = new UsersTableView();
		PagedTableView<UserBean> ptv = new PagedTableView<UserBean>(tv);
		TextAreaDetailsBox detailsBox = new TextAreaDetailsBox("Cuisines liked by the selected user:");
		ptv.setFindHint("Username...");
		controlBox.setButtonDisable(true);
		menuBar.addMenu("All", () -> {
			controlBox.setButtonDisable(true);
			ptv.setDeleteDisable(true);
			detailsBox.setText("");
			ptv.setFilterFunction(tv::filter);
			tv.filter(null);
			ptv.reset();
		});
		menuBar.addMenu("Followers", () -> {
			controlBox.setButtonDisable(true);
			ptv.setDeleteDisable(true);
			detailsBox.setText("");
			ptv.setFilterFunction(tv::filterFollowers);
			tv.filterFollowers(null);
			ptv.reset();
		});
		menuBar.addMenu("Following", () -> {
			controlBox.setButtonDisable(true);
			ptv.setDeleteDisable(true);
			detailsBox.setText("");
			ptv.setFilterFunction(tv::filterFollowing);
			tv.filterFollowing(null);
			ptv.reset();
		});
		menuBar.addMenu("Recommend", () -> {
			UserRecommendDialog login = new UserRecommendDialog();
			Optional<RecommendUserInfo> result = login.showAndWait();
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
			UserBean item = ptv.getSelection();
			if (item == null) {
				controlBox.setButtonDisable(true);
				ptv.setDeleteDisable(true);
				return;
			}
			ResponseMessage resMsg;
			if (!item.isFollowing())
				resMsg = Protocol.getInstance().followUser(new StringFilter(item.getUsername()));
			else
				resMsg = Protocol.getInstance().unfollowUser(new StringFilter(item.getUsername()));
			if (!resMsg.isSuccess()) {
				new ErrorBox("Error", "An error has occured while trying to (un)follow the user.", resMsg.getErrorMsg()).showAndWait();
				return;
			}
			item.setFollowing(!item.isFollowing());
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
			ResponseMessage resMsg = Protocol.getInstance().getUser(new StringFilter(item.getUsername()));
			if (!resMsg.isSuccess()) {
				new ErrorBox("Error", "An error has occured while trying to get user's liked cuisines.", resMsg.getErrorMsg()).showAndWait();
				return;
			}
			controlBox.setText(item.isFollowing() ? "Unfollow" : "Follow");
			List<CuisineInfo> cuisines = resMsg.getEntities(CuisineInfo.class);
			StringBuilder sb = new StringBuilder();
			for (CuisineInfo cuisine: cuisines)
				sb.append(cuisine.getName() + System.lineSeparator());
			detailsBox.setText(sb.toString());
		});
		ptv.setDeletable(deletable);
		if (deletable)
			ptv.setOnDelete((item) -> {
				ResponseMessage resMsg = Protocol.getInstance().deleteUser(new StringFilter(item.getUsername()));
				if (!resMsg.isSuccess()) {
					new ErrorBox("Error", "An error has occured while trying to delete the user.", resMsg.getErrorMsg()).showAndWait();
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
