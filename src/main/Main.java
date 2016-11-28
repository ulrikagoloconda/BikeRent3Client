package main;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.BikeUser;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import static org.apache.http.HttpHeaders.USER_AGENT;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        // URL url=new URL("http://localhost:8080/text/resources");
        // System.out.println(" uerellen " +url);
        //new..start
        String urlString = "http://localhost:8080/text/resources";
        /*HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(urlString);

        // add request header
        request.addHeader("User-Agent123", USER_AGENT);
        HttpResponse response = client.execute(request);
        System.out.println("Response Code : "
                + response.getStatusLine().getStatusCode());
        String json = EntityUtils.toString(response.getEntity());
        System.out.println(json);
          BikeUser user = gson.fromJson(json, BikeUser.class);
        System.out.println(user.getPassw());*/
        Gson gson = new Gson();

        HttpClient client = HttpClientBuilder.create().build();
        HttpPost requsetPost = new HttpPost(urlString);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("userName", "GoloGolo");
        jsonObject.addProperty("passw", "GoloGolo");
        String valuePair = jsonObject.toString();

       HttpEntity entity = new StringEntity(valuePair);
      requsetPost.setEntity(entity);

       requsetPost.addHeader("User-Agent123", USER_AGENT);
        HttpResponse response = client.execute(requsetPost);
        System.out.println("Code " + response.getStatusLine().getStatusCode());
        String json = EntityUtils.toString(response.getEntity());
        //BikeUser user = gson.fromJson(json, BikeUser.class);


        System.out.println("json " + json);


    }

    public static void main(String[] args) {
        launch(args);

    }
}

