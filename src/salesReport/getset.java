package salesReport;

public class getset {

    private String BCNO, prodName, postedBy, date, time, sno;
    private double quantity, price, subTt;
    private int id;

    public getset() {
        this.BCNO = null;
        this.prodName = null;
        this.postedBy = null;
        this.date = null;
        this.time = null;
        this.sno = null;
        this.quantity = 0.00;
        this.price = 0.00;
        this.subTt = 0.00;
        this.id = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBCNO() {
        return BCNO;
    }

    public void setBCNO(String BCNO) {
        this.BCNO = BCNO;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSubTt() {
        return subTt;
    }

    public void setSubTt(double subTt) {
        this.subTt = subTt;
    }
}
