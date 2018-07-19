/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.AddStudent;

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
    private JFXTextField tf3;
    @FXML
    private Label lbl3;
    @FXML
    private JFXSpinner loaderSpin;
    @FXML
    private JFXTextField tf2;
    @FXML
    private Label lbl2;
    @FXML
    private JFXTextField tf4;
    @FXML
    private Label lbl4;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTextField tf5;
    @FXML
    private Label lbl5;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void addAction(ActionEvent event) {
        Student student = getValidateField();
        
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
                    resetField();
                    Utils.fetching(rootPane, loaderSpin, false);
                    
                    if (s2 != null) {
                        Utils.alertSuccess("Student added successfully!");
                        // sendEmail(s2);
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
    
    private void sendEmail(Student s) {
        new Thread(() -> {
            Email mail = new Email();
            mail.setTo(s.getEmail());
            mail.setSubject("Library Management System User Registration");
            mail.setContent(""
                    + "Dear " + s.getUsername() + ",\n\n\n"
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
        tf4.clear();
        tf5.clear();
        lbl1.setText(""); 
        lbl2.setText("");
        lbl3.setText("");
        lbl4.setText("");
        lbl5.setText("");
    }
    
    private Student getValidateField() {
        boolean completeFields;
        String tn = tf1.getText().trim();
        String un = tf2.getText().trim();
        String in = tf3.getText().trim();
        String eml = tf4.getText().trim();
        String ic = tf5.getText().trim();

        completeFields = isCompleteField(
                tn.isEmpty(),
                lbl1,
                "Required Field!"
        );
        completeFields = isCompleteField(
                eml.isEmpty(),
                lbl4,
                "Required Field!"
        );
        completeFields = isCompleteField(
                ic.isEmpty(),
                lbl5,
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
        return student;
    }        

    @FXML
    private void cancelAction(ActionEvent event) {
        Utils.closeWindow(rootPane);
    }
}
