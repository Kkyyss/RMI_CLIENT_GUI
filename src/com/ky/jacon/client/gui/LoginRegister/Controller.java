/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.LoginRegister;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.ky.jacon.api.Model.Email;
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
    private JFXButton regBtn;
    @FXML
    private JFXTextField regUsername;
    @FXML
    private Label regUnErrMsg;
    @FXML
    private JFXPasswordField regPassword;
    @FXML
    private Label regPwdErrMsg;
    @FXML
    private JFXTextField email;
    @FXML
    private Label emlErrMsg;


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
        rootPane.setDisable(true);
        new Thread(() -> {
            try {
                final User u = Utils.stubs.login(user);
                Platform.runLater(() -> {
                    rootPane.setDisable(false);
                    if (u != null) {
                        Utils.alertSuccess("Access Granted!");
                        Utils.userSess = u;
                        StageSettings ss = new StageSettings();
                        ss.setTitle("Home");
                        ss.setPath("Home/view.fxml");
                        Utils.redirect(rootPane, ss);
                    } else {
                        Utils.alertError("Access Denied: Invalid Credentials!");
                    }                
                });
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }

    @FXML
    private void registerAction(ActionEvent event) {
        User user = getRegisterField();
        
        if (user == null) return;
        rootPane.setDisable(true);
        new Thread(() -> {
            try {
                final User u = Utils.stubs.addUser(user);


                Platform.runLater(() -> {
                    rootPane.setDisable(false);
                    if (u != null) {
                        Utils.alertSuccess("Register successfully! Logged In...");
                        new Thread(() -> {
                            Email mail = new Email();
                            mail.setTo(u.getEmail());
                            mail.setSubject("Library Management System User Registration");
                            mail.setContent(""
                                    + "Dear " + u.getUsername() + ",\n\n\n"
                                    + "Registered successfully!"
                                            + "\n\n\n"
                                            + "Thanks,\n"
                                            + "Regards,\n"
                                            + "KyMail");
                            try {
                                boolean sent = Utils.stubs.sendEmail(mail);
                                Platform.runLater(() -> {
                                    if (sent) {
                                        Utils.alertSuccess("Norification Email sent successfully!");
                                        
                                    } else {
                                        Utils.alertSuccess("Failed to send notifcation email!");
                                    }
                                });
                            } catch (RemoteException ex) {
                                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }).start();
                        Utils.userSess = u;
                        StageSettings ss = new StageSettings();
                        ss.setTitle("Home");
                        ss.setPath("Home/view.fxml");
                        Utils.redirect(rootPane, ss);
                    }
                    else {
                        Utils.alertError("Register failed: username/email exists!");
                    }                         
                });                   
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }).start();
        
        
    }
    
    private User getLoginField() {
        boolean completeFields;
        String un = username.getText().trim();
        String pwd = password.getText();

        completeFields = isCompleteField(
                un.isEmpty(),
                unErrMsg,
                "Required Field!"
        );
        completeFields = isCompleteField(
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
    
    private User getRegisterField() {
        boolean completeFields;
        String un = regUsername.getText().trim();
        String pwd = regPassword.getText();
        String eml = email.getText().trim();

        completeFields = isCompleteField(
                un.isEmpty(),
                regUnErrMsg,
                "Required Field!"
        );
        completeFields = isCompleteField(
                pwd.isEmpty(),
                regPwdErrMsg,
                "Required Field!"
        );
        completeFields = isCompleteField(
                eml.isEmpty(),
                emlErrMsg,
                "Required Field!"
        );        

        if (!completeFields || (
                un.isEmpty() ||
                pwd.isEmpty()) ||
                eml.isEmpty()
        ) return null;        
        
        User user = new User();
        user.setUsername(un);
        user.setPassword(pwd);
        user.setEmail(eml);
        
        return user;        
    }
        
    public boolean isCompleteField(boolean empty, Label errMsgLbl, String errMsg) {
        if (empty) {
          errMsgLbl.setText(errMsg);
          return false;
        }
        errMsgLbl.setText(null);
        return true;
    }
}
