package products;

public final class product_getset {

    private int id;
    private String BCNo, prodName, prodDesc, prodPrice, dateOfReg;

    public product_getset() {
        this.id = 0;
        this.BCNo = null;
        this.prodName = null;
        this.prodDesc = null;
        this.prodPrice = null;
        this.dateOfReg = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }

    public String getDateOfReg() {
        return dateOfReg;
    }

    public void setDateOfReg(String dateOfReg) {
        this.dateOfReg = dateOfReg;
    }

    public String getBCNo() {
        return BCNo;
    }

    public void setBCNo(String BCNo) {
        this.BCNo = BCNo;
    }
}
