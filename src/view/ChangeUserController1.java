package view;

import ServerConnecttion.ServerCallImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.BikeUser;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ulrika Goloconda Fahlén
 * @version 1.0
 * @since 2016-10-18
 */
public class ChangeUserController1 implements Initializable {
    private BikeUser currentUser;
    private String errorTitle;
    private Integer userID;
  private ServerCallImpl serverCall;
    @FXML
    private TextField userNameText, fNameText,lNameText,mailText, phoneText, passwordText, passwordCheckerText;
    @FXML
    private Label uniqeTextIdLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       Main.getSpider().setChangeUserVewController(this);
        currentUser = (Main.getSpider().getLoginView().getCurrentUser());
        errorTitle = "fel i uppdatera användare";
        userID = currentUser.getUserID();
        populateText();
      serverCall = new ServerCallImpl();

    }

    private void populateText() {
        userNameText.setText(currentUser.getUserName());
        fNameText.setText(currentUser.getfName());
        lNameText.setText(currentUser.getlName());
        mailText.setText(currentUser.getEmail());
        phoneText.setText(Integer.toString(currentUser.getPhone()));
        passwordText.setText("");
        passwordCheckerText.setText("");
    }


    public void updateUserClick(ActionEvent actionEvent) {
        String userName = userNameText.getText();
        String fName = fNameText.getText();
        String lName = lNameText.getText();
        String email = mailText.getText();
        String phoneString = phoneText.getText();
        String password = passwordText.getText();
        String passwordChecker = passwordCheckerText.getText();
        phoneString.replace("-", "");
        phoneString.replace("+", "");

      boolean isAddUserOK = false;
      if (userName.length() < 5) {
                System.out.println("username to short");
                  ErrorView.showError(errorTitle, "fel vid uppdatering", "Kontrollera era uppgifter", userID, new Exception("username is to short!"));
            } else {
              int phone = Integer.parseInt(phoneString);
              System.out.println("we can now add some info");
              int in_memberlevel = 1;
              BikeUser newUser = new BikeUser(
                  fName, lName, in_memberlevel, email, phone, userName, password);
              isAddUserOK = serverCall.createNewUser(newUser);
            }
                if (!isAddUserOK) {
                    ErrorView.showError(errorTitle, "fel vid inläsning", "Kontrollera era uppgifter", userID, new Exception(" :-( kunde inte lägga till användare"));
                }
                if (isAddUserOK) {
                    //boolean d = DialogView.showSimpleInfo("Ny användare upplaggd", "Lyckades", "Ny användare är nu upplagd");
                    Main.getSpider().getMain().showLoginView();
                }
            }


    public void abortClick(ActionEvent actionEvent) {
        Main.getSpider().getMain().showMainView();
    }

    public void dissableClick(ActionEvent actionEvent) {
      /*  System.out.println("dessable click");
        userNameText.setText(currentUser.getUserName());
        int in_memberlevel = 0;
        boolean isUpdateUserOK = false;
        try {
            isUpdateUserOK = dbAccess.UpdateUser(currentUser.getfName(), currentUser.getlName(), in_memberlevel, currentUser.getEmail(), currentUser.getPhone(), currentUser.getUserName(), "1234");
        } catch (SQLException e) {
            e.printStackTrace();
            ErrorView.showError(errorTitle, "fel vid dissable account..", "starta om denna session.. pw 1234", currentUser.getUserID(), e);
        }
        if (!isUpdateUserOK) {
            ErrorView.showError(errorTitle, "fel vid inläsning", "Kontrollera era uppgifter", currentUser.getUserID(), new IOException(" :-( kunde inte uppdatera uppgifter"));
        }
        if (isUpdateUserOK) {
            boolean d = DialogView.showSimpleInfo("kontot har blivid av-aktiverat", "Lyckades", "Nu ärkonott avaktiverat med lösenord: 1234");
            boolean whantToSentMail = DialogView.showOK_CANCEL_Dialog("kontot har blivid av-aktiverat", "Lyckades, vill ni skicka iväg mail? ", "avaktiveringsmail admin");
            if (whantToSentMail) {
                boolean isSentMailOK = SentMail.sendDelRQ(currentUser.getUserName(), currentUser.getEmail());
                if (isSentMailOK) {
                    System.out.println("Mail ok");
                }
            }

            currentUser.setMemberLevel(0);
            populateText();
            Main.getSpider().getMainView().populateUserTextInGUI(currentUser);
            Main.getSpider().getMain().showLoginView();
        }*/
    }
}
