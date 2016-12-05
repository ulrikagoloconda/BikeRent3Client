package ServerConnecttion;

import model.Bike;
import model.BikeUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Goloconda on 2016-12-01.
 */
public interface ServerCall {
    BikeUser login(String userName, String passw);
    boolean createNewUser(BikeUser newUser);
    boolean updateUser(BikeUser oldUser, BikeUser newUser);
    boolean errorEndpoint(String html,int userID);
    ArrayList<Bike> getAvailableBikes();
    Map<String,Integer> getBikesFromSearch(String searchString);
    Bike addBikeToDB(Bike newBike);
    boolean removeBikeFromDB(int bikeID);
    Bike executeBikeLoan(int bikeID);
    boolean returnBike(Bike bikeToReturn);
    Bike getSingleBike(int bikeID);
    void closeSession();

    /*login (returnerar userObjekt med currentLoan inbäddat + statistikgrundande intar)
    newUser (startar inte session! returnerar booelan)
    updateUser (returnerar boolean)
    errorEndpiont (Skickar html för sparande i databas, och returnerar en boolean)
    getAvaiableBies (Returnerar lista på objetBike)
    getBikesFromSearch (Returnerar list på bikeObject)
    addBikeToDB( returnerar bikeObject med id)
    removeBikeFromDB(returnerar boolean)*/
}
