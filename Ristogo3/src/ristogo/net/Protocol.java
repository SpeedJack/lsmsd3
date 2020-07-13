package ristogo.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;

import ristogo.common.net.entities.CityInfo;
import ristogo.common.net.entities.CuisineInfo;
import ristogo.common.net.entities.Entity;
import ristogo.common.net.entities.PageFilter;
import ristogo.common.net.entities.RestaurantInfo;
import ristogo.common.net.entities.StringFilter;
import ristogo.common.net.entities.UserInfo;
import ristogo.common.net.RequestMessage;
import ristogo.common.net.ResponseMessage;
import ristogo.common.net.enums.ActionRequest;
import ristogo.config.Configuration;

public class Protocol implements AutoCloseable
{
	private Socket socket;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private static Protocol instance;

	private Protocol() throws IOException
	{
		Configuration config = Configuration.getConfig();
		this.socket = new Socket(config.getServerIp(), config.getServerPort());
		inputStream = new DataInputStream(socket.getInputStream());
		outputStream = new DataOutputStream(socket.getOutputStream());
		Logger.getLogger(Protocol.class.getName()).info("Connected to " + config.getServerIp() + ":" + config.getServerPort() + ".");
	}

	public static Protocol getInstance()
	{
		if(instance != null)
			return instance;
		try {
			instance = new Protocol();
		} catch (IOException ex) {
			Logger.getLogger(Protocol.class.getName()).severe("Unable to connect to server: " + ex.getMessage());
			System.exit(1);
		}
		return instance;
	}


	public ResponseMessage performLogin(UserInfo user)
	{
		return sendRequest(ActionRequest.LOGIN, user);
	}

	public ResponseMessage performLogout()
	{
		return sendRequest(ActionRequest.LOGOUT);
	}

	public ResponseMessage registerUser(UserInfo user)
	{
		return sendRequest(ActionRequest.REGISTER_USER, user);
	}

	public ResponseMessage addRestaurant(RestaurantInfo restaurant)
	{
		return sendRequest(ActionRequest.ADD_RESTAURANT, restaurant);
	}

	public ResponseMessage listUsers(StringFilter stringFilter, PageFilter pageFilter)
	{
		return sendRequest(ActionRequest.LIST_USERS, stringFilter, pageFilter);
	}

	public ResponseMessage listUsers(PageFilter pageFilter)
	{
		return sendRequest(ActionRequest.LIST_USERS, pageFilter);
	}

	public ResponseMessage listFollowers(StringFilter stringFilter, PageFilter pageFilter)
	{
		return sendRequest(ActionRequest.LIST_FOLLOWERS, stringFilter, pageFilter);
	}

	public ResponseMessage listFollowers(PageFilter pageFilter)
	{
		return sendRequest(ActionRequest.LIST_FOLLOWERS, pageFilter);
	}

	public ResponseMessage listFollowing(StringFilter stringFilter, PageFilter pageFilter)
	{
		return sendRequest(ActionRequest.LIST_FOLLOWING, stringFilter, pageFilter);
	}

	public ResponseMessage listFollowing(PageFilter pageFilter)
	{
		return sendRequest(ActionRequest.LIST_FOLLOWING, pageFilter);
	}

	public ResponseMessage followUser(StringFilter filter)
	{
		return sendRequest(ActionRequest.FOLLOW_USER, filter);
	}

	public ResponseMessage unfollowUser(StringFilter filter)
	{
		return sendRequest(ActionRequest.UNFOLLOW_USER, filter);
	}

	public ResponseMessage deleteUser(UserInfo user)
	{
		return sendRequest(ActionRequest.DELETE_USER, user);
	}

	public ResponseMessage listRestaurants(StringFilter nameFilter, PageFilter pageFilter)
	{
		return sendRequest(ActionRequest.LIST_RESTAURANTS, nameFilter, pageFilter);
	}

	public ResponseMessage listRestaurants(PageFilter pageFilter)
	{
		return sendRequest(ActionRequest.LIST_RESTAURANTS, pageFilter);
	}

	public ResponseMessage listLikedRestaurants(StringFilter nameFilter, PageFilter pageFilter)
	{
		return sendRequest(ActionRequest.LIST_LIKED_RESTAURANTS, nameFilter, pageFilter);
	}

	public ResponseMessage listLikedRestaurants(PageFilter pageFilter)
	{
		return sendRequest(ActionRequest.LIST_LIKED_RESTAURANTS, pageFilter);
	}

	public ResponseMessage putLikeRestaurant(RestaurantInfo restaurant)
	{
		return sendRequest(ActionRequest.PUT_LIKE_RESTAURANT, restaurant);
	}

	public ResponseMessage removeLikeRestaurant(RestaurantInfo restaurant)
	{
		return sendRequest(ActionRequest.REMOVE_LIKE_RESTAURANT, restaurant);
	}

	public ResponseMessage listOwnRestaurants()
	{
		return sendRequest(ActionRequest.LIST_OWN_RESTAURANTS);
	}

	public ResponseMessage editRestaurant(StringFilter nameFilter, RestaurantInfo restaurant)
	{
		return sendRequest(ActionRequest.EDIT_RESTAURANT, nameFilter, restaurant);
	}

	public ResponseMessage getStatisticRestaurant(RestaurantInfo restaurant)
	{
		return sendRequest(ActionRequest.LIST_USERS, restaurant);
	}

	public ResponseMessage deleteRestaurant(StringFilter nameFilter)
	{
		return sendRequest(ActionRequest.DELETE_RESTAURANT, nameFilter);
	}

	public ResponseMessage getCuisines()
	{
		return sendRequest(ActionRequest.LIST_CUISINES);
	}

	public ResponseMessage getCuisines(CuisineInfo cuisine)
	{
		return sendRequest(ActionRequest.LIST_CUISINES, cuisine);
	}

	public ResponseMessage addCuisine(CuisineInfo cuisine)
	{
		return sendRequest(ActionRequest.ADD_CUISINE, cuisine);
	}

	public ResponseMessage deleteCuisine(CuisineInfo cuisine)
	{
		return sendRequest(ActionRequest.DELETE_CUISINE, cuisine);
	}

	public ResponseMessage putLikeCuisine(CuisineInfo cuisine)
	{
		return sendRequest(ActionRequest.PUT_LIKE_CUISINE, cuisine);
	}

	public ResponseMessage removeLikeCuisine(CuisineInfo cuisine)
	{
		return sendRequest(ActionRequest.REMOVE_LIKE_CUISINE, cuisine);
	}

	public ResponseMessage getCities()
	{
		return sendRequest(ActionRequest.LIST_CITIES);
	}

	public ResponseMessage getCities(StringFilter filter)
	{
		return sendRequest(ActionRequest.LIST_CITIES, filter);
	}

	public ResponseMessage addCity(CityInfo city)
	{
		return sendRequest(ActionRequest.ADD_CITY, city);
	}

	public ResponseMessage deleteCity(CityInfo city)
	{
		return sendRequest(ActionRequest.DELETE_CITY, city);
	}

	private ResponseMessage sendRequest(ActionRequest actionRequest, Entity... entities)
	{
		Logger.getLogger(Protocol.class.getName()).entering(Protocol.class.getName(), "sendRequest", entities);
		new RequestMessage(actionRequest, entities).send(outputStream);
		ResponseMessage resMsg = ResponseMessage.receive(inputStream);
		Logger.getLogger(Protocol.class.getName()).exiting(Protocol.class.getName(), "sendRequest", entities);
		return resMsg != null && resMsg.isValid(actionRequest) ? resMsg : getProtocolErrorMessage();
	}

	private ResponseMessage getProtocolErrorMessage()
	{
		Logger.getLogger(Protocol.class.getName()).warning("Received an invalid response from server.");
		return new ResponseMessage("Invalid response from server.");
	}

	@Override
	public void close() throws IOException
	{
		inputStream.close();
		outputStream.close();
		socket.close();
	}
}
