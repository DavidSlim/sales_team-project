package DashBoard;

import DbConn.DbConn;
import LogOut.LogOut;
import LoginReg.loginRegCtrl;
import LoginReg.passChangeCtrl;
import RoutePlans.RoutePlansController;
import TeamMembers.teamMembersController;
import alerts.alerts;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import products.productsController;
import sales.salesController;
import salesReport.SalesCtrlr;
import targets.TargetsController;

public class DashBoardCtrl implements Initializable {

    @FXML
    Label lblIdCode, lblUserName, lblUserRank;
    @FXML
    ImageView imgv;
    @FXML
    Button btLogOut, btTeam, btProds, btLoginReg, btLoginChange, btRoutePlans, btTargets, btSalesReport, btSales;

    alerts al = new alerts();

    Connection conn = DbConn.dbconn();
    PreparedStatement psmt = null;
    ResultSet rs = null;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        guiSet gs = new guiSet();

        btLogOut.setOnAction(e -> {

            Node node = imgv;
            LogOut lg = new LogOut();
            lg.LogOut(node);

        });
        btTeam.setOnAction(e -> gs.TeamMGui());
        btProds.setOnAction(e -> gs.ProdGui());
        btLoginReg.setOnAction(e -> gs.LoginRegGui());
        btRoutePlans.setOnAction(e -> gs.RoutePlanGui());
        btTargets.setOnAction(e -> gs.TargetsGui());
        btSales.setOnAction(e -> gs.SalesGui());
        btSalesReport.setOnAction(e -> gs.SalesReportGui());
        // btLoginChange.setOnAction(e -> gs.LoginChange());
    }

    private String IdCoSet(String sql) {
        String tmp = lblIdCode.getText();
        try {
            psmt = conn.prepareStatement(sql);
            psmt.setString(1, tmp);
            rs = psmt.executeQuery();
            if (rs.next()) {

                Image img = new Image(rs.getString("image"));

                imgv.setImage(img);

            } else {
                String msg = "This " + lblIdCode.getText() + " Does Not Exist." + "\n" + "\n" + "Please See The System Admin For Assistance";
                al.general(msg);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TargetsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return sql;
    }

    public void txtMappListener() {
        String tmpMembers = lblUserRank.getText();
        if ("Sales Manager".equals(tmpMembers)) {
            String sql = "select * from salesmanager where autocode=?";
            IdCoSet(sql);
        } else if ("Sales Reps".equals(tmpMembers)) {
            String sql = "select * from salesrep where autocode=?";
            IdCoSet(sql);
        } else if ("Merchandizers".equals(tmpMembers)) {
            String sql = "select * from merchandizers where autocode=?";
            IdCoSet(sql);
        } else if ("Board Of Govs".equals(tmpMembers)) {
            String sql = "select * from boardofgovernors where autocode=?";
            IdCoSet(sql);
        } else {
        }

    }

    private class guiSet {

        Node node = imgv;

        private void TeamMGui() {
            boolean stat = lblIdCode.getText() == null || lblUserName.getText() == null || lblUserRank.getText() == null || imgv.getImage() == null;
            if (!stat) {
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/TeamMembers/teamMembersFXML.fxml"));

                    Parent root = (Parent) fxmlLoader.load();

                    ///Pass Login Values To OtherCtrl
                    teamMembersController lgs = fxmlLoader.getController();
                    lgs.setdBoardName(lblUserName.getText());
                    //end of passing login values

                    Scene scene = new Scene(root);
                    stage.getIcons().add(new Image("img/log.jpg"));
                    stage.setTitle(lblUserName.getText() + " : Currently Running The System");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.show();

                    /*                    Window stge = node.getScene().getWindow();
                    stge.hide();*/
                } catch (IOException ex) {
                }
            } else {
                String msg = "Map All Fields First";
                al.general(msg);
            }
        }

        private void ProdGui() {
            boolean stat = lblIdCode.getText() == null || lblUserName.getText() == null || lblUserRank.getText() == null || imgv.getImage() == null;
            if (!stat) {
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/products/productsFXML.fxml"));

                    Parent root = (Parent) fxmlLoader.load();

                    ///Pass Login Values To OtherCtrl
                    productsController lgs = fxmlLoader.getController();
                    lgs.setdBoardName(lblUserName.getText());
                    //end of passing login values

                    Scene scene = new Scene(root);
                    stage.getIcons().add(new Image("img/log.jpg"));
                    stage.setTitle(lblUserName.getText() + " : Currently Running The System");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.show();

                    /*                    Window stge = node.getScene().getWindow();
                    stge.hide();*/
                } catch (IOException ex) {
                }
            } else {
                String msg = "Map All Fields First";
                al.general(msg);
            }
        }

        private void LoginRegGui() {
            boolean stat = lblIdCode.getText() == null || lblUserName.getText() == null || lblUserRank.getText() == null || imgv.getImage() == null;
            if (!stat) {
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoginReg/loginRegFXML.fxml"));

                    Parent root = (Parent) fxmlLoader.load();

                    ///Pass Login Values To OtherCtrl
                    loginRegCtrl lgs = fxmlLoader.getController();
                    lgs.setdBoardName(lblUserName.getText());
                    //end of passing login values

                    Scene scene = new Scene(root);
                    stage.getIcons().add(new Image("img/log.jpg"));
                    stage.setTitle(lblUserName.getText() + " : Currently Running The System");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.show();

                    /*                    Window stge = node.getScene().getWindow();
                    stge.hide();*/
                } catch (IOException ex) {
                }
            } else {
                String msg = "Map All Fields First";
                al.general(msg);
            }
        }

        private void LoginChange() {
            boolean stat = lblIdCode.getText() == null || lblUserName.getText() == null || lblUserRank.getText() == null || imgv.getImage() == null;
            if (!stat) {
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/LoginReg/loginChangeFXML.fxml"));

                    Parent root = (Parent) fxmlLoader.load();

                    ///Pass Login Values To OtherCtrl
                    passChangeCtrl lgs = fxmlLoader.getController();
                    lgs.setIdCo(lblIdCode.getText());
                    lgs.setUserName(lblUserName.getText());

                    //end of passing login values
                    Scene scene = new Scene(root);
                    stage.getIcons().add(new Image("img/log.jpg"));
                    stage.setTitle(lblUserName.getText() + " : Currently Running The System");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.show();

                    /*                    Window stge = node.getScene().getWindow();
                    stge.hide();*/
                } catch (IOException ex) {
                }
            } else {
                String msg = "Map All Fields First";
                al.general(msg);
            }
        }

        private void RoutePlanGui() {
            boolean stat = lblIdCode.getText() == null || lblUserName.getText() == null || lblUserRank.getText() == null || imgv.getImage() == null;
            if (!stat) {
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/RoutePlans/RoutePlans.fxml"));

                    Parent root = (Parent) fxmlLoader.load();

                    ///Pass Login Values To OtherCtrl
                    RoutePlansController lgs = fxmlLoader.getController();
                    lgs.setdBoardName(lblUserName.getText());
                    //end of passing login values

                    Scene scene = new Scene(root);
                    stage.getIcons().add(new Image("img/log.jpg"));
                    stage.setTitle(lblUserName.getText() + " : Currently Running The System");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.show();

                    /*                    Window stge = node.getScene().getWindow();
                    stge.hide();*/
                } catch (IOException ex) {
                }
            } else {
                String msg = "Map All Fields First";
                al.general(msg);
            }
        }

        private void TargetsGui() {
            boolean stat = lblIdCode.getText() == null || lblUserName.getText() == null || lblUserRank.getText() == null || imgv.getImage() == null;
            if (!stat) {
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/targets/targetsFXML.fxml"));

                    Parent root = (Parent) fxmlLoader.load();

                    ///Pass Login Values To OtherCtrl
                    TargetsController lgs = fxmlLoader.getController();
                    lgs.setdBoardName(lblUserName.getText());
                    //end of passing login values

                    Scene scene = new Scene(root);
                    stage.getIcons().add(new Image("img/log.jpg"));
                    stage.setTitle(lblUserName.getText() + " : Currently Running The System");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.show();

                    /*                    Window stge = node.getScene().getWindow();
                    stge.hide();*/
                } catch (IOException ex) {
                }
            } else {
                String msg = "Map All Fields First";
                al.general(msg);
            }
        }

        private void SalesGui() {
            boolean stat = lblIdCode.getText() == null || lblUserName.getText() == null || lblUserRank.getText() == null || imgv.getImage() == null;
            if (!stat) {
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sales/salesFXML.fxml"));

                    Parent root = (Parent) fxmlLoader.load();

                    ///Pass Login Values To OtherCtrl
                    salesController lgs = fxmlLoader.getController();
                    lgs.setdBoardName(lblUserName.getText());
                    //end of passing login values

                    Scene scene = new Scene(root);
                    stage.getIcons().add(new Image("img/log.jpg"));
                    scene.getStylesheets().add("css/general.css");
                    stage.setTitle(lblUserName.getText() + " : Currently Running The System");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setMaximized(true);
                    /*                    stage.setFullScreen(true);
                    stage.setFullScreenExitHint("");
                    stage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("F11"));*/
                    //stage.setAlwaysOnTop(true);
                    stage.setResizable(true);
                    stage.show();

                    /*                    Window stge = node.getScene().getWindow();
                    stge.hide();*/
                } catch (IOException ex) {
                }
            } else {
                String msg = "Map All Fields First";
                al.general(msg);
            }
        }

        private void SalesReportGui() {
            boolean stat = lblIdCode.getText() == null || lblUserName.getText() == null || lblUserRank.getText() == null || imgv.getImage() == null;
            if (!stat) {
                try {
                    Stage stage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/salesReport/SalesReportFXML.fxml"));

                    Parent root = (Parent) fxmlLoader.load();

                    ///Pass Login Values To OtherCtrl
                    SalesCtrlr lgs = fxmlLoader.getController();
                    lgs.setdBoardName(lblUserName.getText());
                    //end of passing login values

                    Scene scene = new Scene(root);
                    stage.getIcons().add(new Image("img/log.jpg"));
                    stage.setTitle(lblUserName.getText() + " : Currently Running The System");
                    stage.setScene(scene);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setResizable(false);
                    stage.show();

                    /*                    Window stge = node.getScene().getWindow();
                    stge.hide();*/
                } catch (IOException ex) {
                }
            } else {
                String msg = "Map All Fields First";
                al.general(msg);
            }
        }

    }

    ////Set User Details From Login Form
    public void setIdCo(String IdCoString) {
        lblIdCode.setText(IdCoString);
    }

    public void setUserNames(String UserNamesString) {
        lblUserName.setText(UserNamesString);
    }

    public void setUserRank(String UserRankString) {
        lblUserRank.setText(UserRankString);
    }
}
