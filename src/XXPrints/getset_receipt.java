package XXPrints;

public class getset_receipt {

    private String bc_name;
    private double pro_price, pro_quantity, pro_subtotal;
    private int pro_sno;

    public getset_receipt() {

        this.bc_name = null;
        this.pro_price = 0.00;
        this.pro_quantity = 0.00;
        this.pro_subtotal = 0.00;
        this.pro_sno = 0;
    }

    public String getBc_name() {
        return bc_name;
    }

    public void setBc_name(String bc_name) {
        this.bc_name = bc_name;
    }

    public double getPro_price() {
        return pro_price;
    }

    public void setPro_price(double pro_price) {
        this.pro_price = pro_price;
    }

    public double getPro_quantity() {
        return pro_quantity;
    }

    public void setPro_quantity(double pro_quantity) {
        this.pro_quantity = pro_quantity;
    }

    public double getPro_subtotal() {
        return pro_subtotal;
    }

    public void setPro_subtotal(double pro_subtotal) {
        this.pro_subtotal = pro_subtotal;
    }

    public int getPro_sno() {
        return pro_sno;
    }

    public void setPro_sno(int pro_sno) {
        this.pro_sno = pro_sno;
    }
}
