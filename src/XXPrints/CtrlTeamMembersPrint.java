package XXPrints;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

public class CtrlTeamMembersPrint implements Initializable {

    @FXML
    Label lblPostBy, lblRegDate, lblYear, lblTime, lblDate, lblIdCo, lblIdNat, lblUserRank, lblEmail,
            lblAcNo, lblBank, lblPhone, lblAssTo, lblPin, lblRPlan, lblDob, lblOName, lblFName;
    @FXML
    ImageView imgv;
    @FXML
    HBox hbMain;
    @FXML
    Button btPrint;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void setFName(String FNameString) {
        lblFName.setText(FNameString);
        btPrint.setText(FNameString.toUpperCase());
    }

    public void setOName(String ONameString) {
        lblOName.setText(ONameString);
    }

    public void setDob(String DobString) {
        lblDob.setText(DobString);
    }

    public void setRPlan(String RPlanString) {
        lblRPlan.setText(RPlanString);
    }

    public void setPin(String PinString) {
        lblPin.setText(PinString);
    }

    public void setAssTo(String AssToString) {
        lblAssTo.setText(AssToString);
    }

    public void setPhone(String PhoneString) {
        lblPhone.setText(PhoneString);
    }

    public void setBank(String BankString) {
        lblBank.setText(BankString);
    }

    public void setAcNo(String AcNoString) {
        lblAcNo.setText(AcNoString);
    }

    public void setEmail(String EmailString) {
        lblEmail.setText(EmailString);
    }

    public void setUserRank(String UserRankString) {
        lblUserRank.setText(UserRankString.toUpperCase());
    }

    public void setIdNat(String IdNatString) {
        lblIdNat.setText(IdNatString);
    }

    public void setIdCo(String IdCoString) {
        lblIdCo.setText(IdCoString);
    }

    public void setDate(String DateString) {
        lblDate.setText(DateString);
    }

    public void setTime(String TimeString) {
        lblTime.setText(TimeString);
    }

    public void setYear(String YearString) {
        lblYear.setText(YearString);
    }

    public void setRegDate(String RegDateString) {
        lblRegDate.setText(RegDateString.toUpperCase());
    }

    public void setPostBy(String PostBy) {
        lblPostBy.setText(PostBy);
    }

    public void setImg_path(String img_path) {
        imgv.setImage(new Image(img_path));
    }

    public void setImage(Object ImageObject) {
        imgv.setImage((Image) ImageObject);
    }

    ///print node
    public void printJ() {
        PrintER pr = new PrintER();
        pr.printJob(hbMain);
    }

    public static class tt {
        public void ss() {
            System.out.println("CARRIED FORWARD = ");
        }
    }

}
