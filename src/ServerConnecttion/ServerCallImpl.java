package ServerConnecttion;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.Bike;
import model.BikeUser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

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
        urlString += "/availableBikes";
        System.out.println(urlString);
        urlString = "http://localhost:8080/text/resources/availableBikes";
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet requsetGet = new HttpGet(urlString);
            requsetGet.addHeader("User-Agent123", USER_AGENT);
            HttpResponse response = client.execute(requsetGet);
            System.out.println("Code " + response.getStatusLine().getStatusCode());
            String json = EntityUtils.toString(response.getEntity());
            System.out.println(json);
           /* user = gson.fromJson(json, BikeUser.class);
            System.out.println("json " + user.getCurrentBikeLoans() + " " + json);*/
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ArrayList<Bike> getBikesFromSearch(String searchString) {
     return null;
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
}
