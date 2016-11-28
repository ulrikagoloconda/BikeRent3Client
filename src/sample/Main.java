package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import static org.apache.http.HttpHeaders.USER_AGENT;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        URL url=new URL(""+
            //"http://localhost:8080/BikeRent3Server/text/resources"
            "http://localhost:28080/text/resources"
        );
      //new..start
      String urlString = "http://localhost:28080/text/resources";
      HttpClient client = HttpClientBuilder.create().build();
      HttpGet request = new HttpGet(urlString);
      // add request header
      request.addHeader("User-Agent123", USER_AGENT);
      HttpResponse response = client.execute(request);
      System.out.println("Response Code : "
          + response.getStatusLine().getStatusCode());


      //new end..
        BufferedReader in = new BufferedReader(
                new InputStreamReader(url.openStream()));

        String inputLine;
        StringBuilder build = new StringBuilder();

        while ((inputLine=in.readLine()) != null)
            build.append(inputLine);
        in.close();
        String xml=build.toString();
        System.out.println(xml + " I javafx ");

    }

    public static void main(String[] args) {
        launch(args);

    }
}

