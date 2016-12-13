package ServerConnecttion;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.Bike;
import model.BikeUser;
import model.Bikes;
import model.MainViewInformaiton;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import view.ErrorView;
import view.Main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.apache.http.HttpHeaders.USER_AGENT;

/**
 * Created by Goloconda on 2016-12-01.
 */
public class ServerCallImpl implements ServerCall {
    private String URL_STRING = "http://localhost:8080/text/resources";
    @Override
    public BikeUser login(String userName, String passw) {
        //start: "try login"
        BikeUser user = new BikeUser();
        String urlString = URL_STRING;//"http://localhost:8080/text/resources";
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost requsetPost = new HttpPost(urlString);
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("userName", userName);
        jsonObject.addProperty("passw", passw);
        String valuePair = jsonObject.toString();

        HttpEntity entity = null;
        String json = null;
        String errorText = "";
        int httpResponseCode = 0;//404;
        try {
            entity = new StringEntity(valuePair);
            requsetPost.setEntity(entity);
            requsetPost.addHeader("User-Agent123", USER_AGENT);
            HttpResponse response = client.execute(requsetPost);
            httpResponseCode = response.getStatusLine().getStatusCode();
            System.out.println("Code " + httpResponseCode);
            //TODO borde kolla att det är status 200, annars händer nåt bäääd
            json = EntityUtils.toString(response.getEntity());
            System.out.println(json);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            errorText = e.toString();
            httpResponseCode = 1;
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            errorText = e.toString();
            httpResponseCode = 2;
        } catch (IOException e) {
            e.printStackTrace();
            errorText = e.toString();
            httpResponseCode = 3;
        }
        if(httpResponseCode !=200){ //error in request or connection
            //TODO hej Niklas! Här skulle jag vilja ha ett utskrivet stacktrace, det är lättare att söka fel i tycker jag
            System.out.println("Fel på path eller server..");
            ErrorView.showError("Inloggningsfel-serverCall", "fel vid inloggning", "Fail", 0,new Exception("httpResponseCode" + httpResponseCode + errorText));
        }else {
            Gson gson = new Gson();
            MainViewInformaiton mvi = gson.fromJson(json, MainViewInformaiton.class);
            System.out.println("totalloanslient mm:" + mvi.getCurrentUser().getCurrentBikeLoans() + " & " +  mvi.getCurrentUser().getTotalBikeLoans());

            if(mvi.getCurrentUser().getUserID() > 0){ //login = OK!!
                user = mvi.getCurrentUser();
                System.out.println("user " + user );
                Main.getSpider().getMain().setMvi(mvi);
                return user;
            }else { // wrong password
                System.out.println("Fel lösenord eller användarnam");
                ErrorView.showError("Inloggningsfel", "fel vid inloggning", "Kontrollera era uppgifter", 0,new Exception(httpResponseCode + "Wrong user information." + errorText));
            }
        }
        return null;
    }

    @Override
    public boolean createNewUser(BikeUser newUser) {
        //TODO Niklas: path = "..../newUser  (skicka bara en user..)

        String urlString = URL_STRING + "/newUser"; //"http://localhost:8080/text/resources";

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost requsetPost = new HttpPost(urlString);
        JsonObject jsonObject = new JsonObject();
        Gson gson = new Gson();
        String json = gson.toJson(newUser);


        HttpEntity entity = null;
        String errorText = "";
        int httpResponseCode = 0;//404;
        try {
          entity = new StringEntity(json);
          requsetPost.setEntity(entity);
          requsetPost.addHeader("User-Agent123", USER_AGENT);
          HttpResponse response = client.execute(requsetPost);
          httpResponseCode = response.getStatusLine().getStatusCode();
          System.out.println("Code " + httpResponseCode);
          //TODO borde kolla att det är status 200, annars händer nåt bäääd
          json = EntityUtils.toString(response.getEntity());
          System.out.println(json);
        } catch (UnsupportedEncodingException e) {
          e.printStackTrace();
          errorText = e.toString();
          httpResponseCode = 1;
        } catch (ClientProtocolException e) {
          e.printStackTrace();
          errorText = e.toString();
          httpResponseCode = 2;
        } catch (IOException e) {
          e.printStackTrace();
          errorText = e.toString();
          httpResponseCode = 3;
        }
        if(httpResponseCode !=200){ //error in request or connection
          System.out.println("Fel på path eller server..");
          ErrorView.showError("addUser-serverCall", "fel vid addUser", "Fail", 0,new Exception("httpResponseCode" + httpResponseCode + errorText));
        }else {
          gson = new Gson();
          boolean isAddedOk = gson.fromJson(json, boolean.class);
          System.out.println("tadded OK: " + isAddedOk);

            return isAddedOk;

            //ErrorView.showError("Fel vid added user tyvärr", "Fel vid added user tyvärr", "Kontrollera era uppgifter", 0,new Exception(httpResponseCode + "Wrong user information." + errorText));
         }

      return false;
    }

    @Override
    public boolean updateUser(BikeUser oldUser, BikeUser alteredUser) {
      //TODO Niklas: path = "..../alterUser (finns i mainV.info..)
      Gson gson = new Gson();
      URL_STRING = URL_STRING + "/alterUser";
      try {
        HttpClient client = HttpClientBuilder.create().build();
        HttpPost requsetPost = new HttpPost(URL_STRING);
        requsetPost.addHeader("User-Agent123", USER_AGENT);
        String token = Main.getSpider().getMain().getMainVI().getCurrentUser().getSessionToken();
        int userID = Main.getSpider().getMain().getMainVI().getCurrentUser().getUserID();
        MainViewInformaiton mvi = new MainViewInformaiton();
        mvi.setOldUser(oldUser);
        mvi.setAlteredUser(alteredUser);
        String json = gson.toJson(mvi);
        HttpEntity entity = new StringEntity(json);
        requsetPost.setEntity(entity);
        HttpResponse response = client.execute(requsetPost);
        System.out.println("Code " + response.getStatusLine().getStatusCode());
        String returnedJson = EntityUtils.toString(response.getEntity());
        System.out.println(returnedJson);
        Gson gson1 = new Gson();
        boolean isUpdateOk = gson1.fromJson(returnedJson, boolean.class);
        return isUpdateOk;
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
    }

    @Override
    public boolean errorEndpoint(String html, int userID) {
        //TODO AGILT..
        return false;
    }

    @Override
    public ArrayList<Bike> getAvailableBikes() {
        Gson gson = new Gson();
        Bikes bikes = null;
        URL_STRING = "http://localhost:8080/text/resources/availableBikes";
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost requsetPost = new HttpPost(URL_STRING);
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
           if(response.getStatusLine().getStatusCode()==200) {
               String json = EntityUtils.toString(response.getEntity());
               System.out.println(json);
               bikes = gson.fromJson(json, Bikes.class);
               System.out.println("json " + bikes.getBikes() + " " + json);
               return bikes.getBikes();
           } else {
               Main.getSpider().getMain().showLoginView();
           }
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public Map<String,Integer> getBikesFromSearch(String searchString) {
        Map<String,Integer> returnMap = new HashMap<>();
        Gson gson = new Gson();
        URL_STRING = "http://localhost:8080/text/resources/search";
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost requsetPost = new HttpPost(URL_STRING);
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
       String urlString = "http://localhost:8080/text/resources/newBike";
        try {
            Gson gson = new Gson();
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost requsetPost = new HttpPost(urlString);
            requsetPost.addHeader("Add_bike", USER_AGENT);
            MainViewInformaiton mvi = new MainViewInformaiton();
            String token = Main.getSpider().getMain().getMainVI().getCurrentUser().getSessionToken();
            int userID = Main.getSpider().getMain().getMainVI().getCurrentUser().getUserID();
            BikeUser user = new BikeUser();
            user.setSessionToken(token);
            user.setUserID(userID);
            mvi.setCurrentUser(user);
            mvi.setNewBike(newBike);
            String json = gson.toJson(mvi);
            HttpEntity entity = new StringEntity(json);
            requsetPost.setEntity(entity);
            HttpResponse response = client.execute(requsetPost);
            System.out.println("Code " + response.getStatusLine().getStatusCode());
         if(response.getStatusLine().getStatusCode() == 200) {
             String returnedJson = EntityUtils.toString(response.getEntity());
             Gson gson1 = new Gson();
           Bike returnedBike = gson1.fromJson(returnedJson,Bike.class);
             return returnedBike;
         } else {
             return null;
         }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String removeBikeFromDB(int bikeID) {
      String  urlString = "http://localhost:8080/text/resources/removeBike";
        try {
            HttpClient client = HttpClientBuilder.create().build();
            String token = Main.getSpider().getMain().getMainVI().getCurrentUser().getSessionToken();
            int userID = Main.getSpider().getMain().getMainVI().getCurrentUser().getUserID();
            urlString += "/"+userID+"/"+token+"/"+bikeID;
            System.out.println(urlString);
            HttpGet requsetGet = new HttpGet(urlString);
            requsetGet.addHeader("remove_bike", USER_AGENT);
            HttpResponse response = client.execute(requsetGet);
            System.out.println("Code " + response.getStatusLine().getStatusCode());
            if(response.getStatusLine().getStatusCode() == 200) {
                String mess = EntityUtils.toString(response.getEntity());
                return mess;
            } else {
                return "Något har gått fel";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "Något har gått fel";
        }
    }

    @Override
    public Bike executeBikeLoan(int bikeID) {
        URL_STRING = "http://localhost:8080/text/resources/executeRental";
        Gson gson = new Gson();
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost requsetPost = new HttpPost(URL_STRING);
            requsetPost.addHeader("BikeToRent", USER_AGENT);
            String token = Main.getSpider().getMain().getMainVI().getCurrentUser().getSessionToken();
            int userID = Main.getSpider().getMain().getMainVI().getCurrentUser().getUserID();
            MainViewInformaiton mvi = new MainViewInformaiton();
            BikeUser user = new BikeUser();
            user.setSessionToken(token);
            user.setUserID(userID);
            mvi.setCurrentUser(user);
            mvi.setBikeToRentID(bikeID);
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
    public boolean returnBike(Bike bikeToReturn) {
        //TODO Niklas: path = "..../ReturnLoanBike (skickas som en bikeId eller vad metoden kräver!! :-) ..)

        return false;
    }

    @Override
    public Bike getSingleBike(int bikeID) {
        Gson gson = new Gson();
        URL_STRING = "http://localhost:8080/text/resources/getBike";
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost requsetPost = new HttpPost(URL_STRING);
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
        URL_STRING = "http://localhost:8080/text/resources/closeSession";
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost requsetPost = new HttpPost(URL_STRING);
            requsetPost.addHeader("User-Agent123", USER_AGENT);
            JsonObject jsonObject = new JsonObject();
            String token = Main.getSpider().getMain().getMainVI().getCurrentUser().getSessionToken();
            if(!token.equals("-1")) {
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
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Bike> getAllBikes() {
        String urlString = "http://localhost:8080/text/resources/getAllBikes";
        Bikes bikes = null;
        try {
            Gson gson = new Gson();
            HttpClient client = HttpClientBuilder.create().build();
            HttpPost requsetPost = new HttpPost(urlString);
            requsetPost.addHeader("getAllBikes", USER_AGENT);
            String token = Main.getSpider().getMain().getMainVI().getCurrentUser().getSessionToken();
            int userID = Main.getSpider().getMain().getMainVI().getCurrentUser().getUserID();
            int memberLevel = Main.getSpider().getMain().getMainVI().getCurrentUser().getMemberLevel();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("sessionToken", token);
            jsonObject.addProperty("userID", userID);
            jsonObject.addProperty("memberLevel", memberLevel);
            HttpEntity entity = new StringEntity(jsonObject.toString());
            requsetPost.setEntity(entity);
            HttpResponse response = client.execute(requsetPost);
            System.out.println("Code " + response.getStatusLine().getStatusCode());

            String code = response.getStatusLine().getStatusCode() + "";
            if (response.getStatusLine().getStatusCode() == 200) {
                String returnedJson = EntityUtils.toString(response.getEntity());
                Gson gson1 = new Gson();
                bikes = gson1.fromJson(returnedJson, Bikes.class);
                return bikes.getBikes();
            } else if (code.charAt(0) == '5') {
                ErrorView.showError("Serverfel", "Fel hos servern ", "Försök igen senare", 0, new Exception(code + "Fel hos server." + ""));
                closeSession();
                Main.getSpider().getMain().showLoginView();
            } else if (code.charAt(0) == '4') {
                ErrorView.showError("Klientens kontakt med servern felar", "Ingen endpoint funnen " , "Försök igen senare", 0, new Exception(code + "Ingen endpoint funnen" + ""));
                closeSession();
                Main.getSpider().getMain().showLoginView();
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorView.showError("Serverfel", "Fel hos servern", "Försök igen senare", 0, new Exception(500 + "Fel hos server." + ""));
            closeSession();
            Main.getSpider().getMain().showLoginView();
            return null;
        }
        return bikes.getBikes();
    }
}

