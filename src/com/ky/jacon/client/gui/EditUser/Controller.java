package com.ky.jacon.client.gui.EditUser;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.ky.jacon.api.Model.User;
import com.ky.jacon.client.gui.Utils.Utils;
import static com.ky.jacon.client.gui.ViewUser.Controller.userManagementSelectedTarget;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author kys
 */
public class Controller implements Initializable {

    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXSpinner loaderSpin;
    @FXML
    private Label lbl1;
    @FXML
    private JFXTextField tf2;
    @FXML
    private Label elbl2;
    @FXML
    private JFXTextField tf3;
    @FXML
    private Label elbl3;
    @FXML
    private Label lbl11;
    @FXML
    private JFXPasswordField tf4;
    @FXML
    private Label elbl4;
    @FXML
    private JFXButton saveInfoBtn;
    @FXML
    private JFXButton savePwdBtn;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        tf2.setText(userManagementSelectedTarget.getUsername());
        tf3.setText(userManagementSelectedTarget.getEmail());
        tf4.setText(userManagementSelectedTarget.getPassword());
    }

    @FXML
    private void cancelAction(ActionEvent event) {
        Utils.closeWindow(rootPane);
    }

    @FXML
    private void saveInfoAction(ActionEvent event) {
        boolean isCompleted = false;
        
        String un = tf2.getText().trim().toLowerCase();
        String eml = tf3.getText().trim().toLowerCase();
        
        isCompleted = Utils.isCompleteField(un.isEmpty(), elbl2, "Required field!");
        isCompleted = Utils.isCompleteField(eml.isEmpty(), elbl3, "Required field!");
        
        if (!isCompleted ||
                un.isEmpty() ||
                eml.isEmpty())
            return;
        
        Utils.fetching(rootPane, loaderSpin, true);
        new Thread(() -> {
        
            try {
                User u = Utils.stubs.getUserByUsername(un);
                
                if (u != null && !u.getUser_id().equals(userManagementSelectedTarget.getUser_id())) {
                    Platform.runLater(() -> {
                        Utils.isCompleteField(true, elbl2, "Username exist!");
                    });
                }
                
                User u2 =  Utils.stubs.getUserByEmail(eml);
                System.out.println(u2);

                if (u2 != null && !u2.getEmail().equals(userManagementSelectedTarget.getEmail())) {
                    Platform.runLater(() -> {
                        Utils.isCompleteField(true, elbl3, "Email exist!");
                    });
                }
                
                if ((u != null && !u.getUser_id().equals(userManagementSelectedTarget.getUser_id()))
                        || 
                        (u2 != null && !u2.getEmail().equals(userManagementSelectedTarget.getEmail()))
                   ) {
                    Utils.fetching(rootPane, loaderSpin, false);
                    return;
                }                
                
                userManagementSelectedTarget.setUsername(un);
                userManagementSelectedTarget.setEmail(eml);
                String rs = Utils.stubs.saveUserInfo(userManagementSelectedTarget);
                
                Platform.runLater(() -> {
                    Utils.fetching(rootPane, loaderSpin, false);
                    if (rs == null) {
                        Utils.alertSuccess("User info updated successfully!");
                    } else {
                        Utils.alertError(rs);
                    }  
                });
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }


    @FXML
    private void savePwdAction(ActionEvent event) {
        boolean isCompleted = false;
        
        String pwd = tf4.getText().trim();
        
        isCompleted = Utils.isCompleteField(pwd.isEmpty(), elbl4, "Required field!");
        
        if (!isCompleted ||
                pwd.isEmpty())
            return;
        
        Utils.fetching(rootPane, loaderSpin, true);
        new Thread(() -> {
        
            try {
                userManagementSelectedTarget.setPassword(pwd);
                String rs = Utils.stubs.saveUserPwd(userManagementSelectedTarget);
                
                Platform.runLater(() -> {
                    Utils.fetching(rootPane, loaderSpin, false);
                    if (rs == null) {
                        Utils.alertSuccess("User password updated successfully!");
                    } else {
                        Utils.alertError(rs);
                    }
                });
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();        
    }
}
