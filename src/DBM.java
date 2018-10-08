/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package fancyhotels;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javafx.collections.ObservableList;

/**
 *
 * @author nkaru_000
 */
public class DBM {
    private static DBM sharedDBM;
    public static Connection conn;
    public static final String URL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Group_39";
    public static final String USER = "cs4400_Group_39";
    public static final String PASSWORD = "AMXhj8BA";
    public static final String DRIVER_CLASS = "com.mysql.jdbc.Driver";
    public static Statement statement;
    
    private DBM() {
        try {
            Class.forName(DRIVER_CLASS).newInstance();
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            statement = conn.createStatement();
        } catch (Exception error) {
            System.out.println("Problem connecting...");
        }
    }
    
    public static DBM sharedDBM() {
        if (sharedDBM == null) {
            sharedDBM = new DBM();
        }
        return sharedDBM;
    }
    
    public static Connection getConnection(){
	return conn;
    }
    
    public static ResultSet verifyCustomer(String username, String password) throws SQLException {  
        ResultSet name = statement.executeQuery(""
                            + "SELECT Customer.Username "
                            + "FROM Customer "
                            + "WHERE Customer.Username = '" + username + "'"
                            + "AND Customer.Password = '" + password + "'");
        if(!name.next()) {
            return null;
        } 
        return name;
    }
    
    public static ResultSet verifyManager(String username, String password) throws SQLException {

         ResultSet name = statement.executeQuery(""
                            + "SELECT Management.Username "
                            + "FROM Management "
                            + "WHERE Management.Username = '" + username + "' "
                            + "AND Management.Password = '" + password + "'");
         
        
        if(!name.next()) {
            return null;
        } 
        return name;
    }
    
    public static boolean verifyID(int reservationID, String username) throws SQLException {
        boolean valid = false;
        ResultSet results = statement.executeQuery(""
                + "SELECT Reservation.StartDate, Reservation.EndDate, ReservationHasRoom.Location, ReservationHasRoom.RoomNum, Reservation.Username "
                + "FROM Reservation "
                + "NATURAL JOIN ReservationHasRoom "
                + "WHERE Reservation.ReservationID = " + Integer.toString(reservationID) + " "
                + "and Reservation.Username = '" + username + "'");
        if (!results.next()) {
            return valid;
        }
        valid = true;
        return valid;
    }
    
    public static boolean checkUsernameExists(String username) throws SQLException {
        ResultSet results = statement.executeQuery(""
                            + "SELECT Customer.Username "
                            + "FROM Customer "
                            + "WHERE Customer.Username = '" + username + "'");

        if (!results.next()) {
            return false;
        }
        return true;
    }
    
    public void insertNewUser(String username, String password, String email) throws SQLException {
        String insertStatement = "INSERT INTO Customer(Username, Email, Password) "
                            + "VALUES(?, ?, ?)";
       
        PreparedStatement statement = conn.prepareStatement(insertStatement);
     
        statement.setString(1, username);
        statement.setString(3, password);
        statement.setString(2, email);
        statement.execute();
    
    }
    
    public static Set findRooms(String location) throws SQLException{     
        ResultSet allRooms = statement.executeQuery(""
                            + "SELECT DISTINCT (Room.RoomNum) "
                            + "FROM Room "
                            + "WHERE Room.Location = '" + location + "'");
  
        Set<Integer> allRoomsSet = new TreeSet<Integer>();
        while (allRooms.next()) {
            allRoomsSet.add(allRooms.getInt(1));
        }          
        return allRoomsSet;
    }
    
    public static Set findUnavailableRooms(String location, LocalDate startDate, LocalDate endDate)throws SQLException{
        Date reqStart = Date.valueOf(startDate);
        Date reqEnd = Date.valueOf(endDate);
        ResultSet reservationRecords = statement.executeQuery(""
                            + "SELECT Room.RoomNum, Reservation.StartDate, Reservation.EndDate " 
                            + "FROM Room NATURAL JOIN ReservationHasRoom NATURAL JOIN Reservation "
                            + "WHERE Room.Location= '" + location + "' AND Reservation.IsCancelled = false ");
        Set<Integer> Unavailable = new TreeSet<Integer>();
        while (reservationRecords.next()){
            Date resStart = reservationRecords.getDate(2);
            Date resEnd = reservationRecords.getDate(3);
            int roomNum = reservationRecords.getInt(1);
            if ((resStart.compareTo(reqStart) <= 0) && (reqStart.compareTo(resEnd) < 0)) {
                Unavailable.add(roomNum);
            } else if ((resStart.compareTo(reqEnd) < 0) && (reqEnd.compareTo(resEnd) <= 0)){
                Unavailable.add(roomNum);
            } else if ((reqStart.compareTo(resStart) <= 0) && (reqEnd.compareTo(resEnd) >= 0)){
                Unavailable.add(roomNum);
            }
        }
        return Unavailable;
    }
    
    public static ResultSet getReservationTable(Set<Integer> rooms) throws SQLException{
        String roomsNew = rooms.toString().replace("[", "(");
        roomsNew = roomsNew.replace("]", ")"); 
        ResultSet reservationTable = statement.executeQuery(""
                            + "SELECT Room.RoomNum, Room.RoomCategory, Room.NumPeople, Room.CostPerDay, Room.CostOfExtraBedPerDay "
                            + "FROM Room "
                            + "WHERE Room.RoomNum IN " + roomsNew + "");
        return reservationTable;
    }
    
    public static void insertCard(String name, String cardNum, int cvv, LocalDate expDate, String username) throws SQLException {
        String SQL_INSERT = "INSERT INTO PaymentInformation(Name, CardNum, CVV, ExpDate, Username) "
                            + "VALUES(?, ?, ?, ?, ?)";
       
        PreparedStatement statement = conn.prepareStatement(SQL_INSERT);
     
        Date eDate = Date.valueOf(expDate);
        
        statement.setString(1, name);
        statement.setString(2, cardNum);
        statement.setInt(3, cvv);
        statement.setDate(4, eDate);
        statement.setString(5, username);
        
        statement.execute();
         
    }
    
    public static boolean checkIfCanDelete(String cardNum) throws SQLException{
            
        ResultSet DatesEnd =  statement.executeQuery(""
                            + "SELECT MAX(Reservation.EndDate) " 
                            + "FROM Reservation "
                            + "WHERE Reservation.CardNum = '" + cardNum +"' AND Reservation.IsCancelled = false"     
                            );
             
        DatesEnd.next();             
                          
        if (DatesEnd.getInt(1) == 0) {
                 // System.out.println("s1 true");
            return true;
                  
        } else {
            Date LatestDate  = DatesEnd.getDate(1);
            System.out.println(LatestDate);
            Date nowDate = Date.valueOf(LocalDate.now());
            if(LatestDate.compareTo(nowDate) < 0 ) {
                System.out.println("s2 true");
                return true;
            } else {
                System.out.println("s3 false");
                return false;
            }
        }
    }
    
    public static void deleteCard(String cardNum) throws SQLException{
        try {statement.executeUpdate(""
                                    + "DELETE " 
                                    + "FROM PaymentInformation "
                                    + "WHERE PaymentInformation.CardNum = '" + cardNum +"'");
        } catch (SQLException e) {System.out.println(e.getMessage());}
    }
    
    public static String[] getCards(String username) throws SQLException{
        ResultSet cards =  statement.executeQuery(""
                            + "SELECT PaymentInformation.CardNum " 
                            + "FROM PaymentInformation "
                            + "WHERE PaymentInformation.Username = '" + username + "'");
        List<String> cardList = new ArrayList<String>();
        while(cards.next()){
            cardList.add(cards.getString(1));
        }
        String [] cardss = new String[cardList.size()];
        for (int i =0; i< cardss.length; i++){
            cardss[i] = cardList.get(i);
        }
        return cardss;
    }
    
    public static int getNewID() throws SQLException{
        ResultSet MaxID = statement.executeQuery(""
                            + "SELECT MAX(Reservation.ReservationID) " 
                            + "FROM Reservation");
        MaxID.next();
        int newID = MaxID.getInt(1) + 1;
        return newID;
    }
    
    public void insertNewRes(int resID, LocalDate startDate, LocalDate endDate,  float total, String username, String cardNum) throws SQLException {
        String SQL_INSERT = "INSERT INTO Reservation(ReservationID, StartDate, EndDate, IsCancelled, TotalCost, Username, CardNum) " 
        + "VALUES(?, ?, ?, ?, ?, ?, ?)";
       
        PreparedStatement statement = conn.prepareStatement(SQL_INSERT);
        Date sDate = Date.valueOf(startDate);
        Date eDate = Date.valueOf(endDate);
     
        statement.setInt(1, resID);
        statement.setDate(2, sDate);
        statement.setDate(3, eDate);
        statement.setBoolean(4, false);
        statement.setFloat(5, total);
        statement.setString(6, username);
        statement.setString(7, cardNum);
        statement.execute();
    }
    
    public void insertNewResHasRoom(int resID, int roomNum, String location, boolean hasExtraBed ) throws SQLException {
     String SQL_INSERT = "INSERT INTO ReservationHasRoom(ReservationID, RoomNum, Location, HasExtraBed) " 
    + "VALUES(?, ?, ?, ?)";
       
        PreparedStatement statement = conn.prepareStatement(SQL_INSERT);
        
        statement.setInt(1, resID);
        statement.setInt(2, roomNum);
        statement.setString(3, location);
        statement.setBoolean(4, hasExtraBed);
        statement.execute();
    }
 
     
    
    public static void addReview(String comment, String rating, String location, String username) throws SQLException {
        ResultSet results = statement.executeQuery( ""
                + "SELECT MAX(ReviewNum) "
                + "AS MaxReviewNum "
                + "FROM HotelReview");
        results.next();
        Integer reviewNum  = ((Integer) results.getInt(1)) + 1;
        PreparedStatement ps = conn.prepareStatement("" 
                + "INSERT INTO HotelReview "
                + "VALUES (?, ?, ?, ?, ?)");
        ps.setString(1, reviewNum.toString());
        ps.setString(2, comment);
        ps.setString(3, rating);
        ps.setString(4, location);
        ps.setString(5, username);
        ps.executeUpdate();
    }
    
    public static ResultSet getReviews(String location) throws SQLException {
        ResultSet results = statement.executeQuery(""
                + "SELECT HotelReview.Rating, HotelReview.Comment "
                + "FROM HotelReview "
                + "WHERE HotelReview.Location = '" + location + "'");
        return results;
    }
    
    public static ResultSet getAugReservationReport() throws SQLException {
        ResultSet results = statement.executeQuery(""
                + "SELECT Location, "
                + "COUNT(DISTINCT Reservation.ReservationID) "
                + "FROM Reservation "
                + "NATURAL JOIN ReservationHasRoom "
                + "WHERE Reservation.StartDate "
                + "LIKE '_____08___' "
                + "GROUP BY ReservationHasRoom.Location");
        return results;
    }
    
    public static ResultSet getSeptReservationReport() throws SQLException {
        ResultSet results = statement.executeQuery(""
                + "SELECT Location, "
                + "COUNT(DISTINCT Reservation.ReservationID) "
                + "FROM Reservation "
                + "NATURAL JOIN ReservationHasRoom "
                + "WHERE Reservation.StartDate "
                + "LIKE '_____09___' "
                + "GROUP BY ReservationHasRoom.Location");
        return results;
    }
    
    public static ResultSet getAugPopularRooms() throws SQLException {
        ResultSet results = statement.executeQuery(""
                + "SELECT Room.RoomCategory, Room.Location, "
                + "COUNT(DISTINCT Reservation.ReservationID) "
                + "FROM Reservation "
                + "NATURAL JOIN Room "
                + "NATURAL JOIN ReservationHasRoom "
                + "WHERE Reservation.StartDate "
                + "LIKE '_____08___' "
                + "AND Room.RoomCategory = ("
                    + "SELECT Room.RoomCategory "
                    + "FROM Reservation "
                    + "NATURAL JOIN Room "
                    + "GROUP BY Room.RoomCategory "
                    + "ORDER BY COUNT(Room.RoomCategory) DESC LIMIT 1) "
                    + "GROUP BY Room.Location");
        return results;
    }
    
    public static ResultSet getSeptPopularRooms() throws SQLException {
        ResultSet results = statement.executeQuery(""
                + "SELECT Room.RoomCategory, Room.Location, "
                + "COUNT(DISTINCT Reservation.ReservationID) "
                + "FROM Reservation "
                + "NATURAL JOIN Room "
                + "NATURAL JOIN ReservationHasRoom "
                + "WHERE Reservation.StartDate "
                + "LIKE '_____09___' "
                + "AND Room.RoomCategory = ("
                    + "SELECT Room.RoomCategory "
                    + "FROM Reservation "
                    + "NATURAL JOIN Room "
                    + "GROUP BY Room.RoomCategory "
                    + "ORDER BY COUNT(Room.RoomCategory) DESC LIMIT 1) "
                    + "GROUP BY Room.Location");
        return results;
    }
    
    public static ResultSet getAugRevenueReport() throws SQLException {
        ResultSet results = statement.executeQuery(""
                + "SELECT x.Location, "
                + "SUM(x.TotalCost) "
                + "FROM (SELECT * FROM Reservation "
                + "NATURAL JOIN ReservationHasRoom GROUP BY Reservation.ReservationID) AS x "
                + "WHERE x.StartDate "
                + "LIKE '_____08___' "
                + "GROUP BY x.Location");
        return results;
    }
    
    public static ResultSet getSeptRevenueReport() throws SQLException {
        ResultSet results = statement.executeQuery(""
                + "SELECT ReservationHasRoom.Location, "
                + "SUM(Reservation.TotalCost) "
                + "FROM Reservation "
                + "NATURAL JOIN ReservationHasRoom "
                + "WHERE Reservation.StartDate "
                + "LIKE '_____09___' "
                + "GROUP BY ReservationHasRoom.Location");
        return results;
    }
    
    public static ResultSet getReservation(int reservationID, String username) throws SQLException {
        ResultSet results = statement.executeQuery(""
                + "SELECT Reservation.StartDate, Reservation.EndDate, ReservationHasRoom.Location, ReservationHasRoom.RoomNum "
                + "FROM Reservation "
                + "NATURAL JOIN ReservationHasRoom "
                + "WHERE Reservation.ReservationID = " + Integer.toString(reservationID) + " "
                + "AND Reservation.Username = '" + username + "' "
                + "AND Reservation.IsCancelled = false");
        return results;
    }
    
    public static ArrayList<ResultSet> displayUpdateOptions(int reservationID, LocalDate newStart, LocalDate newEnd, String location, ArrayList<Integer> roomNums) throws SQLException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        for (int i = 0; i < roomNums.size(); i++) {
            ResultSet er = statement.executeQuery(""
                    + "SELECT Reservation.StartDate, Reservation.EndDate "
                    + "FROM Reservation "
                    + "NATURAL JOIN ReservationHasRoom "
                    + "NATURAL JOIN Room "
                    + "WHERE ReservationHasRoom.Location = '" + location + "' "
                    + "AND ReservationHasRoom.RoomNum= '" + roomNums.get(i).toString() + "' "
                    + "AND Reservation.ReservationID != " + Integer.toString(reservationID) + " "
                    + "AND Reservation.IsCancelled = false");
            while(er.next()) {
                
                String strStartDate = er.getString("StartDate");
                LocalDate startDate = LocalDate.parse(strStartDate, formatter);
                
                String strEndDate = er.getString("EndDate");
                LocalDate endDate = LocalDate.parse(strEndDate, formatter);
                
                if ((startDate.isBefore(newStart) && newStart.isBefore(newEnd))
                        || (startDate.isBefore(newEnd) && newEnd.isBefore(endDate))
                        || (newStart.isBefore(startDate) && newEnd.isAfter(endDate))) {
                    return null;
                }
            }
        }
        
        ArrayList<ResultSet> available = new ArrayList<>();
        for (int i = 0; i < roomNums.size(); i++) {   
            ResultSet avail = statement.executeQuery(""
                    + "SELECT Room.RoomNum, Room.RoomCategory, Room.NumPeople, Room.CostPerDay, Room.CostOfExtraBedPerDay, ReservationHasRoom.HasExtraBed "
                    + "FROM Room "
                    + "NATURAL JOIN ReservationHasRoom "
                    + "WHERE Room.RoomNum = " + roomNums.get(i).toString() + " "
                    + "AND Room.Location = '" + location + "'"
                    + "AND ReservationHasRoom.ReservationID = " + Integer.toString(reservationID));
            available.add(avail);            
        }
        
        return available;

    }
    
    public void updateReservation(LocalDate newStart, LocalDate newEnd, String newCost, String reservationID) throws SQLException {
            statement.executeUpdate(""
                    + "UPDATE Reservation "
                    + "SET Reservation.StartDate = '" + newStart.toString() + "', " + "Reservation.EndDate = '" + newEnd.toString() + "', "
                        + "Reservation.TotalCost = " + newCost + " "
                    + "WHERE Reservation.ReservationID = " + reservationID);
    }
    
    public ResultSet getCancellationInfo(String reservationID, String username) throws SQLException {
        ResultSet results = statement.executeQuery(""
                + "SELECT Reservation.StartDate, Reservation.EndDate, Reservation.TotalCost, Room.RoomNum, Room.RoomCategory, Room.NumPeople, Room.CostPerDay, Room.CostOfExtraBedPerDay, ReservationHasRoom.HasExtraBed "
                + "FROM Room "
                + "NATURAL JOIN ReservationHasRoom "
                + "NATURAL JOIN Reservation "
                + "WHERE Reservation.ReservationID = " + reservationID + " "
                + "AND Reservation.Username = '" + username + "' "
                + "AND Reservation.IsCancelled = false");
        return results;
    }
    
    public void cancelReservation(float updatedTotalCost, String reservationID) throws SQLException {
        statement.executeUpdate(""
                + "UPDATE Reservation "
                + "SET Reservation.IsCancelled = true, Reservation.TotalCost = " + Float.toString(updatedTotalCost) + " "
                + "WHERE Reservation.ReservationID = " + reservationID);
    }
}
