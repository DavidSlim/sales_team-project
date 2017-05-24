package TeamMembers;

import javafx.beans.property.*;

public class tm_getset {

    SimpleIntegerProperty id;
    SimpleStringProperty autocode;
    SimpleStringProperty idno;
    SimpleStringProperty sirname;
    SimpleStringProperty othernames;
    SimpleStringProperty userrank;
    SimpleStringProperty assignedto;
    SimpleStringProperty dob;
    SimpleStringProperty mob;
    SimpleStringProperty yob;
    SimpleStringProperty pinno;
    SimpleStringProperty routeplan;
    SimpleStringProperty phoneno;
    SimpleStringProperty emailadd;
    SimpleStringProperty bank;
    SimpleStringProperty acno;
    SimpleStringProperty dateofreg;
    SimpleStringProperty postBy;
    SimpleStringProperty image_p;
    SimpleObjectProperty image;

    public tm_getset() {
        this.id = new SimpleIntegerProperty();
        this.autocode = new SimpleStringProperty();
        this.idno = new SimpleStringProperty();
        this.sirname = new SimpleStringProperty();
        this.othernames = new SimpleStringProperty();
        this.userrank = new SimpleStringProperty();
        this.assignedto = new SimpleStringProperty();
        this.dob = new SimpleStringProperty();
        this.mob = new SimpleStringProperty();
        this.yob = new SimpleStringProperty();
        this.pinno = new SimpleStringProperty();
        this.routeplan = new SimpleStringProperty();
        this.phoneno = new SimpleStringProperty();
        this.emailadd = new SimpleStringProperty();
        this.bank = new SimpleStringProperty();
        this.acno = new SimpleStringProperty();
        this.dateofreg = new SimpleStringProperty();
        this.postBy = new SimpleStringProperty();
        this.image_p = new SimpleStringProperty();
        this.image = new SimpleObjectProperty();

    }

    public String getImage_p() {
        return image_p.get();
    }

    public void setImage_p(String image_p) {
        this.image_p.set(image_p);
    }

    public int getId() {
        return id.get();
    }

    public String getAutocode() {
        return autocode.get();
    }

    public String getIdno() {
        return idno.get();
    }

    public String getSirname() {
        return sirname.get();
    }

    public String getOthernames() {
        return othernames.get();
    }

    public String getUserrank() {
        return userrank.get();
    }

    public String getAssignedto() {
        return assignedto.get();
    }

    public String getDob() {
        return dob.get();
    }

    public String getMob() {
        return mob.get();
    }

    public String getYob() {
        return yob.get();
    }

    public String getPinno() {
        return pinno.get();
    }

    public String getRouteplan() {
        return routeplan.get();
    }

    public String getPhoneno() {
        return phoneno.get();
    }

    public String getEmailadd() {
        return emailadd.get();
    }

    public String getBank() {
        return bank.get();
    }

    public String getAcno() {
        return acno.get();
    }

    public String getDateofreg() {
        return dateofreg.get();
    }

    public String getPostBy() {
        return postBy.get();
    }

    public Object getImage() {
        return image.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public void setAutocode(String autocode) {
        this.autocode.set(autocode);
    }

    public void setIdNo(String idno) {
        this.idno.set(idno);
    }

    public void setSirname(String sirname) {
        this.sirname.set(sirname);
    }

    public void setOthernames(String othernames) {
        this.othernames.set(othernames);
    }

    public void setUserrank(String userrank) {
        this.userrank.set(userrank);
    }

    public void setAssignedto(String assignedto) {
        this.assignedto.set(assignedto);
    }

    public void setDob(String dob) {
        this.dob.set(dob);
    }

    public void setMob(String mob) {
        this.mob.set(mob);
    }

    public void setYob(String yob) {
        this.yob.set(yob);
    }

    public void setPinno(String pinno) {
        this.pinno.set(pinno);
    }

    public void setRouteplan(String routeplan) {
        this.routeplan.set(routeplan);
    }

    public void setPhoneno(String phoneno) {
        this.phoneno.set(phoneno);
    }

    public void setEmailadd(String emailadd) {
        this.emailadd.set(emailadd);
    }

    public void setBank(String bank) {
        this.bank.set(bank);
    }

    public void setAcno(String acno) {
        this.acno.set(acno);
    }

    public void setDateofreg(String dateofreg) {
        this.dateofreg.set(dateofreg);
    }

    public void setPostBy(String PostBy) {
        this.postBy.set(PostBy);
    }

    public void setImage(Object image) {
        this.image.set(image);
    }

    public StringProperty bankProperty() {
        return bank;
    }
}
