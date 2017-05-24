package targets;

public class getset {

    public static class tg_getset {

        private int id;
        private double week1, week2, week3, week4, settarget, achtarget, percentage;
        private String usernames, userrank, assignedto, dateofreg;

        public tg_getset() {
            this.id = 0;
            this.usernames = null;
            this.userrank = null;
            this.assignedto = null;
            this.week1 = 0.00;
            this.week2 = 0.00;
            this.week3 = 0.00;
            this.week4 = 0.00;
            this.settarget = 0.00;
            this.achtarget = 0.00;
            this.percentage = 0.00;
            this.dateofreg = null;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public double getWeek1() {
            return week1;
        }

        public void setWeek1(double week1) {
            this.week1 = week1;
        }

        public double getWeek2() {
            return week2;
        }

        public void setWeek2(double week2) {
            this.week2 = week2;
        }

        public double getWeek3() {
            return week3;
        }

        public void setWeek3(double week3) {
            this.week3 = week3;
        }

        public double getWeek4() {
            return week4;
        }

        public void setWeek4(double week4) {
            this.week4 = week4;
        }

        public double getSettarget() {
            return settarget;
        }

        public void setSettarget(double settarget) {
            this.settarget = settarget;
        }

        public double getAchtarget() {
            return achtarget;
        }

        public void setAchtarget(double achtarget) {
            this.achtarget = achtarget;
        }

        public double getPercentage() {
            return percentage;
        }

        public void setPercentage(double percentage) {
            this.percentage = percentage;
        }

        public String getUsernames() {
            return usernames;
        }

        public void setUsernames(String usernames) {
            this.usernames = usernames;
        }

        public String getUserrank() {
            return userrank;
        }

        public void setUserrank(String userrank) {
            this.userrank = userrank;
        }

        public String getAssignedto() {
            return assignedto;
        }

        public void setAssignedto(String assignedto) {
            this.assignedto = assignedto;
        }

        public String getDateofreg() {
            return dateofreg;
        }

        public void setDateofreg(String dateofreg) {
            this.dateofreg = dateofreg;
        }
    }

}
