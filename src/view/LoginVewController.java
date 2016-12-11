package view;


import ServerConnecttion.ServerCall;
import ServerConnecttion.ServerCallImpl;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.BikeUser;
import helpers.Sound;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.MainViewInformaiton;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.apache.http.HttpHeaders.USER_AGENT;

//import static model.DBUtil.processException;

/**
 * @author Niklas Karlsson
 * @version 1.0
 * @since 2016-09-15
 */
public class LoginVewController implements Initializable {
    @FXML
    private TextField userNameText;
    @FXML
    private PasswordField passwordText;
    @FXML
    private AnchorPane loginPane;
    private BikeUser currentUser;

    private ServerCall serverCall;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.getSpider().setLoginView(this);
        serverCall = new ServerCallImpl();
  }

    public void logInClick(Event event) {
        String userName = userNameText.getText();
        String passw = passwordText.getText();
       // TODO hårdkodad lösning som behöver ändras
        currentUser = serverCall.login( userName,  passw);

        if(currentUser.getUserID() > 0){ //login = OK!!
            showMainGui();
        }else { // wrong ...
            System.out.println("Fel ...");
           }



     /*   try {
            currentUser = dbAccess.logIn(userName, password);
            if (currentUser.getUserID() > 0) {
                Sound pling = new Sound();
                pling.playSoundInThread(Sound.LEAVE_DICE);
                showMainGui();

            } else {
                System.out.println("Fel lösenord eller användarnam");
                ErrorView.showError("Inloggningsfel", "fel vid inloggning", "Kontrollera era uppgifter", 0,new Exception("Wrong user information."));

            }
        } catch (SQLException e) {
            e.printStackTrace();
            Sound pling = new Sound();
            pling.playMp3SoundInThread(Sound.NO);
            processException(e);
           ErrorView.showError("Inloggningsfel", "fel vid inloggning", "Kontrollera era uppgifter", 0,e);

        }*/
  }
    public void showMainGui() {
        if (currentUser == null) {
            currentUser = new BikeUser();
            currentUser.setlName("Override");
            currentUser.setfName("Override");
            currentUser.setUserName("Override");
            currentUser.setMemberLevel(1010);
            currentUser.setPhone(101010);
            currentUser.setEmail("Override@Override.com");
        }
        Main.getSpider().getMain().showMainView();
    }

    public void newUserClick(ActionEvent actionEvent) {
        Main.getSpider().getMain().showNewUserView();
    }

    public BikeUser getCurrentUser() {
        return currentUser;
    }
}
