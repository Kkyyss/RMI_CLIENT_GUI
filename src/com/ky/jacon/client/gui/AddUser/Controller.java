/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.AddUser;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.ky.jacon.api.Model.Email;
import com.ky.jacon.api.Model.Student;
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
    @FXML
    private JFXTextField tf4;
    @FXML
    private Label lbl4;
    @FXML
    private JFXTextField tf5;
    @FXML
    private Label lbl5;
    @FXML
    private JFXTextField tf6;
    @FXML
    private Label lbl6;
    @FXML
    private JFXTextField tf7;
    @FXML
    private Label lbl7;
    @FXML
    private JFXTextField tf8;
    @FXML
    private Label lbl8;
    @FXML
    private JFXCheckBox tick1;
    @FXML
    private JFXCheckBox tick2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
    
    private void resetUserField() {
        tf1.clear();
        tf2.clear();
        tf3.clear();
        lbl1.setText("");
        lbl2.setText("");
        lbl3.setText("");
    }
    private void resetStudentField() {
        tf4.clear();
        tf5.clear();
        tf6.clear();
        tf7.clear();
        tf8.clear();
        lbl4.setText(""); 
        lbl5.setText("");
        lbl6.setText("");
        lbl7.setText("");
        lbl8.setText("");
    }
    
    private User getUserValidateField() {
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
        user.setActive(tick1.isSelected() ? 1 : 0);
        
        return user;
    }
    
    private Student getStudentValidateField() {
        boolean completeFields;
        String tn = tf4.getText().trim();
        String un = tf5.getText().trim();
        String in = tf6.getText().trim();
        String eml = tf7.getText().trim();
        String ic = tf8.getText().trim();

        completeFields = isCompleteField(
                tn.isEmpty(),
                lbl4,
                "Required Field!"
        );
        completeFields = isCompleteField(
                eml.isEmpty(),
                lbl7,
                "Required Field!"
        );
        completeFields = isCompleteField(
                ic.isEmpty(),
                lbl8,
                "Required Field!"
        );        

        if (!completeFields || (
                tn.isEmpty() ||
                eml.isEmpty()) ||
                ic.isEmpty()
        ) return null;        
        
        Student student = new Student();
        student.setEmail(eml.toLowerCase());
        student.setStudent_no(tn.toUpperCase());
        student.setStudent_name(un.toUpperCase());
        student.setStudent_intake(in.toUpperCase());
        student.setStudent_ic(ic);
        student.setActive(tick2.isSelected() ? 1 : 0);
        return student;
    }        
    

    @FXML
    private void cancelAction(ActionEvent event) {
        Utils.closeWindow(rootPane);
    }

    @FXML
    private void addUserAction(ActionEvent event) {
        User user = getUserValidateField();
        
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
                    resetUserField();
                    Utils.fetching(rootPane, loaderSpin, false);
                    if (u3 != null) {
                        Utils.alertSuccess("User added successfully!");
                        sendEmail(u3);
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

    @FXML
    private void addStudentAction(ActionEvent event) {
        Student student = getStudentValidateField();
        
        if (student == null) return;
        
        Utils.fetching(rootPane, loaderSpin, true);
        new Thread(() -> {
            try {
                Student s = Utils.stubs.getStudent(student.getStudent_no());
                
                if (s != null) {
                    Platform.runLater(() -> {
                        Utils.isCompleteField(true, lbl1, "TP No. exist!");
                    });
                }
                
                User u =  Utils.stubs.getUserByEmail(student.getEmail());

                if (u != null) {
                    Platform.runLater(() -> {
                        Utils.isCompleteField(true, lbl4, "Email exist!");
                    });
                }
                
                if (s != null || u != null) {
                    Utils.fetching(rootPane, loaderSpin, false);
                    return;
                }
                
                Student s2 = Utils.stubs.addStudent(student);

                Platform.runLater(() -> {
                    resetStudentField();
                    Utils.fetching(rootPane, loaderSpin, false);
                    
                    if (s2 != null) {
                        Utils.alertSuccess("Student added successfully!");
                        sendEmail(s2);
                    }
                    else {
                        Utils.alertError("Failed to add student!");
                    }                         
                });                   
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }).start();        
    }
}
