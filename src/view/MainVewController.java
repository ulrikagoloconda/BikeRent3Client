package view;


import ServerConnecttion.ServerCall;
import ServerConnecttion.ServerCallImpl;
import model.Bike;
import model.BikeUser;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.MainViewInformaiton;
import model.PopulateType;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.*;

/**
 * @author Ulrika Goloconda Fahlén
 * @version 1.0
 * @since 2016-09-15
 */

//Klass som fungerar som controller till mainView. Här finns metoder som motsvarar funktionaliteten i programmets huvudfönster.
public class MainVewController implements Initializable {
    @FXML
    private TableColumn columCykel;
    @FXML
    private TableView<Bike> tableBikeView;
    @FXML
    private TableColumn<Bike, String> year, status, color, type, model, available;
    @FXML
    private GridPane gridPane;
    @FXML
    private ImageView imageView1, imageView2, imageView3;
    @FXML
    private Label messageLabel;
    @FXML
    private Button executeLoanBtn, netBtn, adminBtn, returnBtn;
    @FXML
    private ComboBox<String> combobox;
    @FXML
    private Label userNameLabel, memberLevelLabel, activeLoanLabel, numberOfLoanedBikesLabel, statLabel;

    private Map<Node, Integer> idMap;
    private int selectedFromGrid;
    private ArrayList<Bike> availableBikes;
    private List<Bike> currentListInView;
    private PopulateType currentTypeInView;
    private BikeUser currentUser;
    private MainViewInformaiton mvi;
    private Map<String, Integer> searchMap;
    private Bike selectedBikeSearch;
    private ArrayList<Bike> usersCurrentBikes;
    private ServerCall serverCall;


    private String errorTitle = "Fel i huvudfönster";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Main.getSpider().setMainView(this);
        currentUser = Main.getSpider().getMain().getMvi().getCurrentUser();
        System.out.println("initialize, currentUser =  " + currentUser.getfName());
        populateUserTextInGUI(currentUser);
        if (currentUser.getMemberLevel() != 10) {
            adminBtn.setVisible(false);
        }
        netBtn.setDisable(true);
        executeLoanBtn.setDisable(true);
        combobox.setEditable(true);
        idMap = new HashMap<>();
        returnBtn.setVisible(false);
        currentListInView = new ArrayList<>();
        serverCall = new ServerCallImpl();
    netBtn.setDisable(true);
    executeLoanBtn.setDisable(true);
    combobox.setEditable(true);
    idMap = new HashMap<>();
    returnBtn.setVisible(false);
    currentListInView = new ArrayList<>();
    serverCall = new ServerCallImpl();
  }

  public void populateUserTextInGUI(BikeUser bikeUser) {
    ArrayList<Bike> bikesInUse = currentUser.getCurrentBikeLoans();
    ArrayList<Integer> totalBikes = currentUser.getTotalBikeLoans();
    userNameLabel.setText(bikeUser.getUserName());
    memberLevelLabel.setText("* " + bikeUser.getMemberLevel() + " *");
    activeLoanLabel.setText("" + bikesInUse.size());
    System.out.println(totalBikes.size() + " ");
    numberOfLoanedBikesLabel.setText("" + totalBikes.size());
    setStatLabel();

  }

  private void setStatLabel() {
    mvi = Main.getSpider().getMain().getMvi();
    float total = mvi.getTotalBikes();
    float free = total - mvi.getRentedBikes();
    System.out.println("free" + " " + free);
    System.out.println("tot: " + total);
    float poc = free / total;
    System.out.println("poc: " + poc);
    poc = poc * 100;
    System.out.println(poc);
    statLabel.setText("" + poc + " %");
  }


  public void searchAvailableBikes(ActionEvent actionEvent) {
    executeLoanBtn.setDisable(true);
    netBtn.setVisible(false);
    availableBikes = serverCall.getAvailableBikes();
    if (availableBikes.size() > 3) {
      currentListInView = availableBikes.subList(0, 3);
      populateGridPane(PopulateType.AVAILABLE_BIKES, currentListInView);
    } else {
      populateGridPane(PopulateType.AVAILABLE_BIKES, availableBikes);
    }
  }

  public void showAdminView(ActionEvent actionEvent) {
    Main.getSpider().getMain().showAdeminView();
  }

  public boolean populateGridPane(PopulateType type, List<Bike> bikeArray) {
    gridPane.getChildren().clear();
    returnBtn.setVisible(false);
    currentTypeInView = type;
    if (type == PopulateType.AVAILABLE_BIKES) {
      if (availableBikes.size() <= 3) {
        netBtn.setVisible(false);
      } else {
        netBtn.setDisable(false);
        netBtn.setVisible(true);
      }
    }
    if (type == PopulateType.USERS_CURRENT_BIKES) {
      if (usersCurrentBikes.size() <= 3) {
        netBtn.setVisible(false);
      } else {
        netBtn.setDisable(false);
        netBtn.setVisible(true);
      }
    }
    String[] topList = {"Bild", "Årsmodell", "Färg", "Cykeltyp", "Modell", "Ledig?"};
    ArrayList<String> values = new ArrayList<>();
    for (int i = 0; i < 6; i++) {
      Label l = new Label();
      l.setText(topList[i]);
      l.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
      gridPane.add(l, i, 0);
    }
    int j = 1;
    try {
      for (Bike b : bikeArray) {
        values.clear();
        values.add("" + b.getModelYear());
        values.add(b.getColor());
        values.add(b.getType());
        values.add(b.getBrandName());
        values.add("" + b.isAvailable());
        System.out.println(b + " b " + bikeArray.size());
        for (int i = 0; i < 6; i++) {
          if (i == 0) {
            BufferedImage theImage = ImageIO.read(b.getImageStream());
            b.getImageStream().close();
            System.out.println("the image "+theImage);
            System.out.println(b);
            System.out.println(b.getImageStream());
            Image image = SwingFXUtils.toFXImage(theImage, null);
            ImageView iv = new ImageView();
            iv.setFitHeight(65);
            iv.setFitWidth(95);
            iv.setOnMouseClicked(new EventHandler<MouseEvent>() {
              @Override
              public void handle(MouseEvent event) {
                Node n = (Node) event.getSource();
                onClickActions(n);
              }
            });

            idMap.put(iv, b.getBikeID());
            iv.setImage(image);
            gridPane.add(iv, i, j);
          } else {
            Label k = new Label();
            k.setOnMouseClicked(new EventHandler<MouseEvent>() {

              @Override
              public void handle(MouseEvent event) {
                Node n = (Node) event.getSource();
                onClickActions(n);
              }
            });
            k.setText(values.get(i - 1));
            Font f = new Font(16);
            k.setFont(f);
            idMap.put(k, b.getBikeID());
            gridPane.add(k, i, j);
          }
        }
        j++;
        if (j > 3) {
          return false;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }


  public void onClickActions(Node n) {
    if (selectedBikeSearch != null) {
      executeLoanBtn.setVisible(true);
      selectedFromGrid = selectedBikeSearch.getBikeID();
      String available = "";
      if (selectedBikeSearch.isAvailable()) {
        available = "Ja";
        executeLoanBtn.setDisable(false);
      } else {
        available = "Nej";
        executeLoanBtn.setDisable(true);
      }

      String s = "Årsmodell: " + selectedBikeSearch.getModelYear() + " Färg: " + selectedBikeSearch.getColor() + " Cykeltyp: " +
          selectedBikeSearch.getType() + " Ledig? " + available;
      messageLabel.setText(s);
      executeLoanBtn.setVisible(true);
    } else if (usersCurrentBikes != null) {
      selectedFromGrid = idMap.get(n);
      for (Bike b : usersCurrentBikes) {
        if (b.getBikeID() == selectedFromGrid) {
          String s = "Årsmodell: " + b.getModelYear() + " Färg: " + b.getColor() + " Cykeltyp: " +
              b.getType() + " Återlämningsdag: " + b.getDayOfReturn();
          messageLabel.setText(s);
          returnBtn.setVisible(true);
        }
      }

    } else {
      selectedFromGrid = idMap.get(n);
      String available = "";
      for (Bike b : availableBikes) {
        if (b.isAvailable()) {
          available = "Ja";
          executeLoanBtn.setDisable(false);
        } else {
          available = "Nej";
          executeLoanBtn.setDisable(true);
        }
        if (b.getBikeID() == selectedFromGrid) {

          String s = "Årsmodell: " + b.getModelYear() + " Färg: " + b.getColor() + " Cykeltyp: " +
              b.getType() + " Ledig? " + available;
          messageLabel.setText(s);
          executeLoanBtn.setVisible(true);
        }
      }
    }
  }

  public void nextBikesOnList(ActionEvent actionEvent) {
    gridPane.getChildren().clear();
    currentListInView.clear();
    if (currentTypeInView == PopulateType.AVAILABLE_BIKES) {
      if (availableBikes.size() >= 3) {
        currentListInView = availableBikes.subList(0, 3);
      } else {
        currentListInView = availableBikes.subList(0, availableBikes.size());
      }
    } else if (currentTypeInView == PopulateType.USERS_CURRENT_BIKES) {
      if (usersCurrentBikes.size() >= 3) {
        currentListInView = usersCurrentBikes.subList(0, 3);
      } else {
        currentListInView = usersCurrentBikes.subList(0, usersCurrentBikes.size());
      }
    }
    populateGridPane(currentTypeInView, currentListInView);

  }

  public void showChangeUserView(ActionEvent actionEvent) {
    Main.getSpider().getMain().showChangeUserView();
  }

  public void executeBikeLoan(ActionEvent actionEvent) {
    Bike b = serverCall.getSingleBike(selectedFromGrid);
    if(b.isAvailable()) {
      Bike rentedBike = serverCall.executeBikeLoan(b.getBikeID());

      messageLabel.setText("Cykeln är lånad till och med " + rentedBike.getDayOfReturn());
      rentedBike.setAvailable(false);
      List<Bike> bikeList = new ArrayList<>();
      bikeList.add(rentedBike);
      populateGridPane(PopulateType.RENTED_BIKE,bikeList);
      //TODO tänk hut hur userTextGUI ska populeras
      //populateUserTextInGUI(currentUser);
      setStatLabel();
    }else {
      messageLabel.setText("Cykeln är tyvärr inte ledig");
    }
  }



    public void popuateComboBox(Event event) {
        searchMap = serverCall.getBikesFromSearch(combobox.getEditor().getText());
        int count = 0;
        combobox.getItems().clear();
        for (Map.Entry<String, Integer> entry : searchMap.entrySet()) {
            if (count > 10) {
                break;
            }
            combobox.getItems().add(entry.getKey());
            count++;
      }
    }


  public void setSearchResult(ActionEvent actionEvent) {
    if (combobox.getSelectionModel().getSelectedItem() != null) {
      String selected = combobox.getSelectionModel().getSelectedItem().toString();
      if (searchMap.containsKey(selected)) {
        int bikeID = searchMap.get(selected);
        selectedBikeSearch = serverCall.getSingleBike(bikeID);
        List<Bike> bike = new ArrayList<>();
        bike.add(selectedBikeSearch);
        populateGridPane(PopulateType.SEARCH_RESULTS, bike);

      }
    }
  }

  public void showStatClick(ActionEvent actionEvent) {
    Main.getSpider().getMain().showStatView();
  }

  public void showUsersBikes(ActionEvent actionEvent) {
    usersCurrentBikes = currentUser.getCurrentBikeLoans();

    if (usersCurrentBikes.size() > 3) {
      currentListInView = usersCurrentBikes.subList(0, 3);
      populateGridPane(PopulateType.USERS_CURRENT_BIKES, currentListInView);
    } else {
      populateGridPane(PopulateType.USERS_CURRENT_BIKES, usersCurrentBikes);
    }
  }

  public void returnBike(ActionEvent actionEvent) {
    Bike returnedBike = null;
    for (Bike b : usersCurrentBikes) {
      if (b.getBikeID() == selectedFromGrid) {
        returnedBike = b;
      }
    }
    boolean bool = serverCall.returnBike(returnedBike);
    if (bool) {
      populateUserTextInGUI(mvi.getCurrentUser());
      setStatLabel();
      returnedBike.setAvailable(true);
      List<Bike> bike = new ArrayList<>();
      bike.add(returnedBike);
      populateGridPane(PopulateType.RETURNED_BIKE, bike);

    }
  }

  public void closeSession(ActionEvent actionEvent) {
    serverCall.closeSession();
    Main.getSpider().getMain().showLoginView();
  }
}

