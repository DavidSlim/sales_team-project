package sales;

public class getset {

    ///populate products and set products to sales table
    public static class prod_getset {

        private String BCNo, name;
        private double price;
        private int sno;

        public prod_getset() {
            this.BCNo = null;
            this.name = null;
            this.price = 0.00;
            this.sno = 0;
        }

        public String getBCNo() {
            return BCNo;
        }

        public void setBCNo(String BCNo) {
            this.BCNo = BCNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public int getSno() {
            return sno;
        }

        public void setSno(int sno) {
            this.sno = sno;
        }

    }

    //todays total sales
    public static class date_getset {

        private String date;
        private double quantity;

        public date_getset() {
            this.date = null;
            this.quantity = 0.00;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

    }

    ///sales getset
    public static class sales_getset {

        private String BCNo, name, postBy, date, time;
        private double price, quantity, subtotal;
        private int sno;

        public sales_getset() {
            this.BCNo = null;
            this.name = null;
            this.date = null;
            this.postBy = null;
            this.time = null;
            this.price = 0.00;
            this.quantity = 0.00;
            this.subtotal = 0.00;
            this.sno = 0;
        }

        public String getBCNo() {
            return BCNo;
        }

        public void setBCNo(String BCNo) {
            this.BCNo = BCNo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPostBy() {
            return postBy;
        }

        public void setPostBy(String postBy) {
            this.postBy = postBy;
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

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public double getQuantity() {
            return quantity;
        }

        public void setQuantity(double quantity) {
            this.quantity = quantity;
        }

        public double getSubtotal() {
            return price * quantity;
        }

        public void setSubtotal(double subtotal) {
            this.subtotal = subtotal;
        }

        public int getSno() {
            return sno;
        }

        public void setSno(int sno) {
            this.sno = sno;
        }
    }

    //todays total sales
    public static class bcprice_getset {

        private String prodname, bcno;
        private double price;

        public bcprice_getset() {
            this.prodname = null;
            this.price = 0.00;
            this.bcno = null;
        }

        public String getProdname() {
            return prodname;
        }

        public void setProdname(String prodname) {
            this.prodname = prodname;
        }

        public String getBcno() {
            return bcno;
        }

        public void setBcno(String bcno) {
            this.bcno = bcno;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

    }
}
