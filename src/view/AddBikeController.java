package view;

import model.Bike;
import model.BikeUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
//import org.apache.commons.io.FileUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Ulrika Goloconda Fahlén
 * @version 1.0
 * @since 2016-09-19
 */
public class AddBikeController implements Initializable {

  private Bike newBike;
  private LoginVewController loginView;
  private BikeUser currentUser;
  //private BikesFifoue newBikesFifoue;
  @FXML
  private Label urlLabel, messageLabel;
  @FXML
  private TextField brandText, modelYearText, colorText, typeText, sizeText;
  @FXML
  private GridPane gridDelBike;
  @FXML
  private AnchorPane deletePane, addBikePane;
  @FXML
  private Pane editPane;
  @FXML
  private Button btnQueRunner;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    Main.getSpider().setAddBikeView(this);
  }

  public void showDeleteView(ActionEvent actionEvent) {
    Main.getSpider().getLoginView().showMainGui();
  }


  public void addBike(ActionEvent actionEvent) {
    prepairBikeForAdd();
   // AccessBike.insertNewBike(newBike);
    brandText.setText("");
    modelYearText.setText("");
    colorText.setText("");
    typeText.setText("");
    sizeText.setText("");
    messageLabel.setText("Cykeln har lagts till");
    urlLabel.setText("");
  }

  public void addPicture(ActionEvent actionEvent) {
    if (newBike == null) {
      newBike = new Bike();
    }
    FileChooser fc = new FileChooser();
    File selected = fc.showOpenDialog(null);
    urlLabel.setText(selected.getName());
   /* if (selected != null) {
      try {
        ByteArrayInputStream in = new ByteArrayInputStream(FileUtils.readFileToByteArray(selected));
        newBike.setImageStream(in);
        newBike.setCreatedBy(currentUser);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }*/
  }

  public void showMainGui(ActionEvent actionEvent) {
    Main.getSpider().getLoginView().showMainGui();

  }

  public void addBikeInQue(ActionEvent actionEvent) throws InterruptedException {
    prepairBikeForAdd();
    messageLabel.setText("Cykeln lägs till i kön...");
    Thread.sleep(500);
   // BikesFifoQue.enqueue(newBike); //add bike in FIFO
    urlLabel.setText("");
    newBike = new Bike();
    btnQueRunner.setDisable(false);


    messageLabel.setText("Önskar du tömma kön, eller lägga till en till?");
  }

  private void prepairBikeForAdd() {
    if (newBike.equals(null)) {
      newBike = new Bike();
    } else {

      if (brandText.getText().length() > 0) {
        newBike.setBrandName(brandText.getText());
      }
      if (modelYearText.getText().length() == 4) {
        String s = modelYearText.getText();
        for (int i = 0; i < 4; i++) {
          if (!Character.isDigit(s.charAt(i))) {
            modelYearText.setText("");
            break;
          } else {
            int yearInt = Integer.valueOf(s);

            newBike.setModelYear(yearInt);
          }
        }
      }
      if (colorText.getText().length() > 0) {
        newBike.setColor(colorText.getText());
      }
      if (typeText.getText().length() > 0) {
        newBike.setType(typeText.getText());
      }

      if (sizeText.getText().length() <= 2) {
        String s = sizeText.getText();
        if (Character.isDigit(s.charAt(0)) && Character.isDigit(s.charAt(1))) {
          int i = Integer.valueOf(s);
          newBike.setSize(i);
        }
      }
    }
  }


  public void addBikeFromQue(ActionEvent actionEvent) throws InterruptedException {
    /*int counters = 0;
    while (!BikesFifoQue.isEmty()) {
      Bike b = BikesFifoQue.dequeue();
      System.out.println(b.toString());
      AccessBike.insertNewBike(b);
      counters++;
    }
    messageLabel.setText("Totalt har " + counters + "Cyklar lagts till i kön");
    Thread.sleep(2000);
    BikesFifoQue.enqueue(newBike);
    messageLabel.setText("Önskar lägga till fler?");
    btnQueRunner.setDisable(true);

*/
  }
}
