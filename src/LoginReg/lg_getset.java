package LoginReg;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class lg_getset {

    SimpleStringProperty id;
    SimpleStringProperty idCode;
    SimpleStringProperty username;
    SimpleStringProperty loginname;
    SimpleStringProperty loginpass;
    SimpleStringProperty userrank;
    SimpleStringProperty dor;

    //Name from Dashboard
    SimpleStringProperty dBoardName;

    public lg_getset() {
        this.id = new SimpleStringProperty();
        this.idCode = new SimpleStringProperty();
        this.username = new SimpleStringProperty();
        this.loginname = new SimpleStringProperty();
        this.loginpass = new SimpleStringProperty();
        this.userrank = new SimpleStringProperty();
        this.dor = new SimpleStringProperty();

        this.dBoardName = new SimpleStringProperty();
    }

    public String getId() {
        return id.get();
    }

    public String getIdCode() {
        return idCode.get();
    }

    public String getUsername() {
        return username.get();
    }

    public String getUserrank() {
        return userrank.get();
    }

    public String getLoginname() {
        return loginname.get();
    }

    public String getLoginpass() {
        return loginpass.get();
    }

    public String getDor() {
        return dor.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public void setIdCode(String IdCode) {
        this.idCode.set(IdCode);
    }

    public void setUsername(String UserName) {
        this.username.set(UserName);
    }

    public void setLoginname(String LoginName) {
        this.loginname.set(LoginName);
    }

    public void setLoginpass(String LoginPass) {
        this.loginpass.set(LoginPass);
    }

    public void setUserrank(String userrank) {
        this.userrank.set(userrank);
    }

    public void setDor(String Dor) {
        this.dor.set(Dor);
    }

    ///Name from DashBpard
    public String getdBoardName() {
        return dBoardName.get();
    }

    public void setdBoardName(String value) {
        dBoardName.set(value);
    }

    public StringProperty dBoardNameProperty() {
        return dBoardName;
    }

}
