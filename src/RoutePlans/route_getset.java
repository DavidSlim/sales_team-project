package RoutePlans;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class route_getset {

    SimpleStringProperty id;
    SimpleStringProperty country;
    SimpleStringProperty region;
    SimpleStringProperty county;
    SimpleStringProperty location;
    SimpleStringProperty dateofreg;

    public route_getset() {
        this.id = new SimpleStringProperty();
        this.country = new SimpleStringProperty();
        this.county = new SimpleStringProperty();
        this.dateofreg = new SimpleStringProperty();
        this.location = new SimpleStringProperty();
        this.region = new SimpleStringProperty();
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public StringProperty IdProperty() {
        return id;
    }

    public String getCountry() {
        return country.get();
    }

    public void setCountry(String country) {
        this.country.set(country);
    }

    public StringProperty CountryProperty() {
        return country;
    }

    public String getRegion() {
        return region.get();
    }

    public void setRegion(String region) {
        this.region.set(region);
    }

    public StringProperty RegionProperty() {
        return region;
    }

    public String getCounty() {
        return county.get();
    }

    public void setCounty(String county) {
        this.county.set(county);
    }

    public StringProperty CountyProperty() {
        return county;
    }

    public String getLocation() {
        return location.get();
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public StringProperty LocationProperty() {
        return location;
    }

    public String getDateofreg() {
        return dateofreg.get();
    }

    public void setDateofreg(String dateofreg) {
        this.dateofreg.set(dateofreg);
    }

    public StringProperty DateOfRegProperty() {
        return dateofreg;
    }

}
