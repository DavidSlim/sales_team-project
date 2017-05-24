package alerts;

import javafx.scene.control.Alert;

public class alerts {

    public void inserted() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Data Insertion");
        alert.setHeaderText("Data Inserted");
        alert.showAndWait();
        alert.setResizable(false);
    }

    public void deleted() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Data Deletion");
        alert.setHeaderText("Data Deleted");
        alert.showAndWait();
        alert.setResizable(false);
    }

    public void deletedNot() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Cancel Deletion");
        alert.setHeaderText("Data Deletion Cancelled");
        alert.showAndWait();
        alert.setResizable(false);
    }

    public void update() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Data Update");
        alert.setHeaderText("Data Updated");
        alert.showAndWait();
        alert.setResizable(false);
    }

    public void updateNot() {
        Alert alert = new Alert(Alert.AlertType.NONE);
        alert.setTitle("Cancel Update");
        alert.setHeaderText("Data Update Cancelled");
        alert.showAndWait();
        alert.setResizable(false);
    }

    public void email() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Email Error");
        alert.setHeaderText(null);
        alert.setContentText("Your Email Pattern Is Wrong Input Correct Email" + "\n" + "\n" + "If no email provide null@null.com");
        alert.setResizable(false);
        alert.showAndWait();
    }

    public void web() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("URL Error");
        alert.setHeaderText(null);
        alert.setContentText("Your Website Pattern Is Wrong Input Correct Email" + "\n" + "\n" + "If no email provide null.com");
        alert.setResizable(false);
        alert.showAndWait();
    }

    public void txtNull() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Empty Fields");
        alert.setHeaderText(null);
        alert.setContentText("Ensure all necessary fields and images are provided");
        alert.setResizable(false);
        alert.showAndWait();
    }

    public void txtNullHide() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Empty Week");
        alert.setHeaderText(null);
        alert.setContentText("Ensure all week fields are provided");
        alert.setResizable(false);
        alert.show();
    }

    public void cleared() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("System Cleared");
        alert.setHeaderText(null);
        alert.setContentText("All Fields Have Been Cleared");
        alert.setResizable(false);
        alert.showAndWait();
    }

    public void clearNot() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Clearing Cancelled");
        alert.setHeaderText(null);
        alert.setContentText("clearing cancelled");
        alert.setResizable(false);
        alert.showAndWait();
    }

    public void popInfo() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Object Not Populated.. Please populate object to view records");
    }

    public String nav(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Navigation Message");
        alert.setContentText(msg + " Of Records");
        alert.showAndWait();
        alert.setResizable(false);
        return msg;
    }

    public void print() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Print Error");
        alert.setHeaderText(null);
        alert.setContentText("Your Fields Are Not Yet Populated" + "\n" + "\n" + "Select User Level ->" + "\n" + "\n" + "Populate ->" + "\n" + "\n" + "Click On The User To Print From The Table Provided -> " + "\n" + "\n" + " Finally Click On Print To Print");
        alert.setResizable(false);
        alert.showAndWait();
    }

    public String general(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(msg);
        alert.showAndWait();
        alert.setResizable(false);
        return msg;
    }

    public String conn_err(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR!!!");
        alert.setHeaderText("Sorry You Seem To Be Having An Error");
        alert.setContentText(msg);
        alert.showAndWait();
        alert.setResizable(false);
        return msg;
    }

}
