package Combos;

import DbConn.DbConn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Combos {

    Connection conn = DbConn.dbconn();
    PreparedStatement psmt = null;
    ResultSet rs = null;

    public ObservableList<String> months() {
        ObservableList<String> months = FXCollections.observableArrayList("Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec");
        return months;
    }

    public ArrayList<String> days() {
        ArrayList<String> days = new ArrayList();
        for (int day = 1; day <= 31; day++) {
            ArrayList<String> arryr = new ArrayList(Arrays.asList(String.format("%02d", day)));
            days.addAll(arryr);
        }
        return days;
    }

    public ArrayList<String> years() {
        ArrayList<String> yrs = new ArrayList();
        for (int yr = (LocalDate.now().getYear() - 65); yr <= (LocalDate.now().getYear() - 18); yr++) {
            ArrayList<String> arryr = new ArrayList(Arrays.asList(yr));
            yrs.addAll(arryr);
        }
        return yrs;
    }

    public ObservableList<String> dashBoard() {
        ObservableList<String> dboard = FXCollections.observableArrayList("Team Members", "Customers", "Products", "Route Plans");
        return dboard;
    }

    public ObservableList<String> userRanks() {
        ObservableList<String> rank = FXCollections.observableArrayList("Sales Manager", "Sales Reps", "Merchandizers", "Board Of Govs");
        return rank;
    }

    public ObservableList<String> location() {

        try {
            String sql = "select location from routeplans";

            ObservableList<String> ob = FXCollections.observableArrayList();

            psmt = conn.prepareStatement(sql);
            rs = psmt.executeQuery();
            while (rs.next()) {
                ob.add(rs.getString("location"));
            }
            return ob;

        } catch (Exception e) {
            return null;
        } finally {
        }

    }

    public ObservableList<String> bog() {
        ObservableList<String> assto = FXCollections.observableArrayList("Company");
        return assto;
    }

    public ObservableList<String> manager() {
        ObservableList<String> assto = FXCollections.observableArrayList("Company Manager", "Senior Manager");
        return assto;
    }

    public ObservableList<String> salesmanager() {

        try {
            String sql = "select othernames from salesmanager";

            ObservableList<String> ob = FXCollections.observableArrayList();

            psmt = conn.prepareStatement(sql);
            rs = psmt.executeQuery();
            while (rs.next()) {
                ob.add(rs.getString("othernames"));
            }
            return ob;

        } catch (Exception e) {
            return null;
        } finally {
        }

    }

    public ObservableList<String> salesrep() {

        try {
            String sql = "select othernames from salesrep";

            ObservableList<String> ob = FXCollections.observableArrayList();

            psmt = conn.prepareStatement(sql);
            rs = psmt.executeQuery();
            while (rs.next()) {
                ob.add(rs.getString("othernames"));
            }
            return ob;

        } catch (Exception e) {
            return null;
        } finally {
        }

    }

    public ObservableList<String> merchandizers() {

        try {
            String sql = "select othernames from merchandizers";

            ObservableList<String> ob = FXCollections.observableArrayList();

            psmt = conn.prepareStatement(sql);
            rs = psmt.executeQuery();
            while (rs.next()) {
                ob.add(rs.getString("othernames"));
            }
            return ob;

        } catch (Exception e) {
            return null;
        } finally {
        }

    }

    public ObservableList<String> bogovs() {

        try {
            String sql = "select othernames from boardofgovernors";

            ObservableList<String> ob = FXCollections.observableArrayList();

            psmt = conn.prepareStatement(sql);
            rs = psmt.executeQuery();
            while (rs.next()) {
                ob.add(rs.getString("othernames"));
            }
            return ob;

        } catch (Exception e) {
            return null;
        } finally {
        }

    }

    public ObservableList<String> products() {

        try {
            String sql = "select prodname from products";

            ObservableList<String> ob = FXCollections.observableArrayList();

            psmt = conn.prepareStatement(sql);
            rs = psmt.executeQuery();
            while (rs.next()) {
                ob.add(rs.getString("prodname"));
            }
            return ob;

        } catch (Exception e) {
            return null;
        } finally {
        }

    }

}
