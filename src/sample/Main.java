package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        URL url=new URL("http://localhost:8080/BikeRent3Server/text/resources");
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

