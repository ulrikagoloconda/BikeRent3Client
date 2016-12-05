package ServerConnecttion;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.Bike;
import model.BikeUser;
import model.Bikes;
import model.MainViewInformaiton;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import view.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.http.HttpHeaders.USER_AGENT;

/**
 * Created by Goloconda on 2016-12-01.
 */
public class ServerCallImpl implements ServerCall {
    private String urlString = "http://localhost:8080/text/resources";
    @Override
    public BikeUser login(String userName, String passw) {
        return null;
    }

    @Override
    public boolean createNewUser(BikeUser newUser) {
        return false;
    }

    @Override
    public boolean updateUser(BikeUser oldUser, BikeUser newUser) {
        return false;
    }

    @Override
    public boolean errorEndpoint(String html, int userID) {
        return false;
    }

    @Override
    public ArrayList<Bike> getAvailableBikes() {
        Gson gson = new Gson();
        Bikes bikes = null;
        urlString = "http://localhost:8080/text/resources/availableBikes";
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost requsetPost = new HttpPost(urlString);
            requsetPost.addHeader("User-Agent123", USER_AGENT);
            JsonObject jsonObject = new JsonObject();
            String token = Main.getSpider().getMain().getMainVI().getCurrentUser().getSessionToken();
            int userID = Main.getSpider().getMain().getMainVI().getCurrentUser().getUserID();
            jsonObject.addProperty("sessionToken", token);
            jsonObject.addProperty("userID", userID);
            String valuePair = jsonObject.toString();
            HttpEntity entity = new StringEntity(valuePair);
            requsetPost.setEntity(entity);
            HttpResponse response = client.execute(requsetPost);
            System.out.println("Code " + response.getStatusLine().getStatusCode());
            String json = EntityUtils.toString(response.getEntity());
            System.out.println(json);
            bikes = gson.fromJson(json,Bikes.class);
            System.out.println("json " + bikes.getBikes() + " " + json);
        }catch (Exception e){
            e.printStackTrace();
        }
        return bikes.getBikes();
    }

    @Override
    public Map<String,Integer> getBikesFromSearch(String searchString) {
        Map<String,Integer> returnMap = new HashMap<>();
        Gson gson = new Gson();
        urlString = "http://localhost:8080/text/resources/search";
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost requsetPost = new HttpPost(urlString);
            requsetPost.addHeader("User-Agent123", USER_AGENT);
           MainViewInformaiton mvi = new MainViewInformaiton();
            String token = Main.getSpider().getMain().getMainVI().getCurrentUser().getSessionToken();
            int userID = Main.getSpider().getMain().getMainVI().getCurrentUser().getUserID();
            BikeUser user = new BikeUser();
            user.setSessionToken(token);
            user.setUserID(userID);
            mvi.setCurrentUser(user);
            mvi.setSearchValue(searchString);
            String json = gson.toJson(mvi);
            HttpEntity entity = new StringEntity(json);
            requsetPost.setEntity(entity);
            HttpResponse response = client.execute(requsetPost);
            System.out.println("Code " + response.getStatusLine().getStatusCode());
            String returnedJson = EntityUtils.toString(response.getEntity());
            Gson gson1 = new Gson();
            Bikes bikes = gson1.fromJson(returnedJson,Bikes.class);
            returnMap = bikes.getSearchResults();
            return returnMap;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Bike addBikeToDB(Bike newBike) {
        return null;
    }

    @Override
    public boolean removeBikeFromDB(int bikeID) {
        return false;
    }

    @Override
    public Bike executeBikeLoan(Bike bikeToRent) {
        return null;
    }

    @Override
    public boolean returnBike(Bike bikeToReturn) {
        return false;
    }

    @Override
    public Bike getSingleBike(int bikeID) {
        Gson gson = new Gson();
        urlString = "http://localhost:8080/text/resources/getBike";
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost requsetPost = new HttpPost(urlString);
            requsetPost.addHeader("User-Agent123", USER_AGENT);
            String token = Main.getSpider().getMain().getMainVI().getCurrentUser().getSessionToken();
            int userID = Main.getSpider().getMain().getMainVI().getCurrentUser().getUserID();
            MainViewInformaiton mvi = new MainViewInformaiton();
            BikeUser user = new BikeUser();
            user.setSessionToken(token);
            user.setUserID(userID);
            mvi.setCurrentUser(user);
            mvi.setSingleBikeID(bikeID);
            String json = gson.toJson(mvi);
            HttpEntity entity = new StringEntity(json);
            requsetPost.setEntity(entity);
            HttpResponse response = client.execute(requsetPost);
            System.out.println("Code " + response.getStatusLine().getStatusCode());
            String returnedJson = EntityUtils.toString(response.getEntity());
            System.out.println(returnedJson);
            Gson gson1 = new Gson();
           Bike bike = gson1.fromJson(returnedJson, Bike.class);
            return bike;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void closeSession() {
        Gson gson = new Gson();
        urlString = "http://localhost:8080/text/resources/closeSession";
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost requsetPost = new HttpPost(urlString);
            requsetPost.addHeader("User-Agent123", USER_AGENT);
            JsonObject jsonObject = new JsonObject();
            String token = Main.getSpider().getMain().getMainVI().getCurrentUser().getSessionToken();
            int userID = Main.getSpider().getMain().getMainVI().getCurrentUser().getUserID();
            jsonObject.addProperty("sessionToken", token);
            jsonObject.addProperty("userID", userID);
            String valuePair = jsonObject.toString();
            HttpEntity entity = new StringEntity(valuePair);
            requsetPost.setEntity(entity);
            HttpResponse response = client.execute(requsetPost);
            System.out.println("Code " + response.getStatusLine().getStatusCode());
            String json = EntityUtils.toString(response.getEntity());
            System.out.println(json);
            BikeUser user = gson.fromJson(json, BikeUser.class);
           Main.getSpider().getMain().getMainVI().getCurrentUser().setSessionToken(user.getSessionToken());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
