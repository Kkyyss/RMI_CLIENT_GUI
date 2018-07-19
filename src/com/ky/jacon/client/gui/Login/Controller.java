/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.Login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.ky.jacon.api.Model.Student;
import com.ky.jacon.api.Model.User;
import com.ky.jacon.client.gui.Utils.StageSettings;
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
    private JFXTextField username;
    @FXML
    private Label unErrMsg;
    @FXML
    private JFXPasswordField password;
    @FXML
    private Label pwdErrMsg;
    @FXML
    private JFXButton loginBtn;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXSpinner loaderSpin;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void loginAction(ActionEvent event) {
        User user = getLoginField();
        
        if (user == null) return;
        
        Utils.fetching(rootPane, loaderSpin, true);
        new Thread(() -> {
            try {
                final User u = Utils.stubs.login(user);
                Platform.runLater(() -> {
                    Utils.fetching(rootPane, loaderSpin, false);
                    if (u != null) {
                        Utils.userSess = u;
                        if (Utils.userSess.getRole().getRole_name().equals("student")) {
                            Utils.fetching(rootPane, loaderSpin, true);
                            new Thread(() -> {
                                try {
                                    Utils.studSess = Utils.stubs.getStudentProfile(u.getUser_id());
                                    Platform.runLater(() -> {
                                        Utils.fetching(rootPane, loaderSpin, false);
                                        if (Utils.studSess != null) {
                                            accessGranded();
                                        } else {
                                            Utils.alertError("Student's profile not found!");
                                        }
                                    });
                                } catch (RemoteException ex) {
                                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }).start();
                        } else {
                            accessGranded();                           
                        }
                    } else {
                        Utils.alertError("Access Denied: Invalid Credentials!");
                    }                
                });
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
    
    private void accessGranded() {
        Utils.alertSuccess("Access Granted!");
        StageSettings ss = new StageSettings();
        ss.setTitle("Home");
        ss.setPath("Home/view.fxml");
        Utils.redirect(rootPane, ss);        
    }

    private User getLoginField() {
        boolean completeFields;
        String un = username.getText().trim();
        String pwd = password.getText();

        completeFields = Utils.isCompleteField(
                un.isEmpty(),
                unErrMsg,
                "Required Field!"
        );
        completeFields = Utils.isCompleteField(
                pwd.isEmpty(),
                pwdErrMsg,
                "Required Field!"
        );

        if (!completeFields || (
                un.isEmpty() ||
                pwd.isEmpty())
        ) return null;        
        
        User user = new User();
        user.setUsername(un);
        user.setPassword(pwd);
        
        return user;
    }
}
