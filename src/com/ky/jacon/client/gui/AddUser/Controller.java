/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.AddUser;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.ky.jacon.api.Model.Email;
import com.ky.jacon.api.Model.User;
import com.ky.jacon.client.gui.Utils.Utils;
import static com.ky.jacon.client.gui.Utils.Utils.isCompleteField;
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
    private JFXTextField tf1;
    @FXML
    private Label lbl1;
    @FXML
    private JFXPasswordField tf2;
    @FXML
    private Label lbl2;
    @FXML
    private JFXTextField tf3;
    @FXML
    private Label lbl3;
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
    private void addAction(ActionEvent event) {
        User user = getValidateField();
        
        if (user == null) return;
        
        Utils.fetching(rootPane, loaderSpin, true);
        new Thread(() -> {
            try {
                User u = Utils.stubs.getUserByUsername(user.getUsername());
                
                if (u != null) {
                    Platform.runLater(() -> {
                        Utils.isCompleteField(true, lbl1, "Username exist!");
                    });
                }
                
                User u2 =  Utils.stubs.getUserByEmail(user.getEmail());

                if (u2 != null) {
                    Platform.runLater(() -> {
                        Utils.isCompleteField(true, lbl3, "Email exist!");
                    });
                }
                
                if (u != null || u2 != null) {
                    Utils.fetching(rootPane, loaderSpin, false);
                    return;
                }

                User u3 = Utils.stubs.addUser(user);

                Platform.runLater(() -> {
                    resetField();
                    Utils.fetching(rootPane, loaderSpin, false);
                    if (u3 != null) {
                        Utils.alertSuccess("User added successfully!");
                        // sendEmail(u3);
                    }
                    else {
                        Utils.alertError("Failed to add user!");
                    }                         
                });                   
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }).start();        
    }
    
    private void sendEmail(User u) {
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
    }
    
    private void resetField() {
        tf1.clear();
        tf2.clear();
        tf3.clear();
        lbl1.setText("");
        lbl2.setText("");
        lbl3.setText("");
    }
    
    private User getValidateField() {
        boolean completeFields;
        String un = tf1.getText().trim();
        String pwd = tf2.getText();
        String eml = tf3.getText().trim();

        completeFields = isCompleteField(
                un.isEmpty(),
                lbl1,
                "Required Field!"
        );
        completeFields = isCompleteField(
                pwd.isEmpty(),
                lbl2,
                "Required Field!"
        );
        completeFields = isCompleteField(
                eml.isEmpty(),
                lbl3,
                "Required Field!"
        );        

        if (!completeFields || (
                un.isEmpty() ||
                pwd.isEmpty()) ||
                eml.isEmpty()
        ) return null;        
        
        User user = new User();
        user.setUsername(un.toLowerCase());
        user.setPassword(pwd);
        user.setEmail(eml.toLowerCase());
        
        return user;
    }    

    @FXML
    private void cancelAction(ActionEvent event) {
        Utils.closeWindow(rootPane);
    }
}
