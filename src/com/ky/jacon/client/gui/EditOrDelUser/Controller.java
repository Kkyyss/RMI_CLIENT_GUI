/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.EditOrDelUser;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.ky.jacon.api.Model.User;
import com.ky.jacon.client.gui.Utils.Utils;
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
    private JFXTextField tf1;
    @FXML
    private Label elbl1;
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
    private JFXButton delBtn;
    @FXML
    private JFXButton savePwdBtn;
    
    private User targetedUser;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void searchAction(ActionEvent event) {
        boolean isCompleted = false;
        
        String searchTxt = tf1.getText().trim().toLowerCase();
        
        isCompleted = Utils.isCompleteField(searchTxt.isEmpty(), elbl1, "Required field!");
        
        if (!isCompleted ||
                searchTxt.isEmpty())
            return;
        
        Utils.fetching(rootPane, loaderSpin, true);
        new Thread(() -> {
        
            try {
                targetedUser = Utils.stubs.getUserByUsername(searchTxt);
                
                Platform.runLater(() -> {
                    Utils.fetching(rootPane, loaderSpin, false);
                    if (targetedUser != null) {
                        populateEditField();
                    } else {
                        resetEditField();
                        Utils.isCompleteField(true, elbl1, "No such user!");
                    }  
                });
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
    
    private void resetEditField() {
        targetedUser = null;
        delBtn.setDisable(true);
        saveInfoBtn.setDisable(true);
        savePwdBtn.setDisable(true); 
        
        tf2.setEditable(false);
        tf3.setEditable(false);
        tf4.setEditable(false);
        
        tf2.clear();
        tf3.clear();
        tf4.clear();
        
        elbl2.setText("");
        elbl3.setText("");
        elbl4.setText("");
    }
    
    private void populateEditField() {
        delBtn.setDisable(false);
        saveInfoBtn.setDisable(false);
        savePwdBtn.setDisable(false);
        
        tf2.setText(targetedUser.getUsername());
        tf3.setText(targetedUser.getEmail());
        tf4.clear();
        tf2.setEditable(true);
        tf3.setEditable(true);        
        tf4.setEditable(true);
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
                
                if (u != null && !u.getUser_id().equals(targetedUser.getUser_id())) {
                    Platform.runLater(() -> {
                        Utils.isCompleteField(true, elbl2, "Username exist!");
                    });
                }
                
                User u2 =  Utils.stubs.getUserByEmail(eml);
                System.out.println(u2);

                if (u2 != null && !u2.getEmail().equals(targetedUser.getEmail())) {
                    Platform.runLater(() -> {
                        Utils.isCompleteField(true, elbl3, "Email exist!");
                    });
                }
                
                if ((u != null && !u.getUser_id().equals(targetedUser.getUser_id()))
                        || 
                        (u2 != null && !u2.getEmail().equals(targetedUser.getEmail()))
                   ) {
                    Utils.fetching(rootPane, loaderSpin, false);
                    return;
                }                
                
                targetedUser.setUsername(un);
                targetedUser.setEmail(eml);
                String rs = Utils.stubs.saveUserInfo(targetedUser);
                
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
    private void delAction(ActionEvent event) {
        if (Utils.alertConfirm("Delete user", "Are you sure to delete this user?")) {
            Utils.fetching(rootPane, loaderSpin, true);
            
            new Thread(() -> {
                try {
                    String rs = Utils.stubs.deleteUser(targetedUser.getUser_id());
                    
                    Platform.runLater(() -> {
                        Utils.fetching(rootPane, loaderSpin, false);
                        if (rs == null) {
                            Utils.alertSuccess("User deleted successfully!");
                            resetEditField();
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
                targetedUser.setPassword(pwd);
                String rs = Utils.stubs.saveUserPwd(targetedUser);
                
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
