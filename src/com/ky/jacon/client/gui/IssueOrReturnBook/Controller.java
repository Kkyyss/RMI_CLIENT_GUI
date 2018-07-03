/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.IssueOrReturnBook;

import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.ky.jacon.api.Model.Book;
import com.ky.jacon.api.Model.Issue;
import com.ky.jacon.api.Model.Student;
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
    private Label abE1;
    @FXML
    private Label abE2;
    @FXML
    private JFXSpinner loaderSpin;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXTextField tf1;
    @FXML
    private JFXTextField tf2;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public boolean isCompleteField(boolean empty, Label errMsgLbl, String errMsg) {
        if (empty) {
          errMsgLbl.setText(errMsg);
          return false;
        }
        errMsgLbl.setText(null);
        return true;
    }

    @FXML
    private void cancelAction(ActionEvent event) {
        Utils.closeWindow(rootPane);
    }
    
    private boolean invalidField() {
        boolean completeFields;
        String studentNo = tf1.getText().trim().toLowerCase();
        String bookISBN = tf2.getText().trim().toLowerCase();

        completeFields = isCompleteField(
                studentNo.isEmpty(),
                abE1,
                "Required Field!"
        );
        completeFields = isCompleteField(
                bookISBN.isEmpty(),
                abE2,
                "Required Field!"
        );
        return !completeFields || (
                studentNo.isEmpty() ||
                bookISBN.isEmpty());         
    }

    @FXML
    private void issueAction(ActionEvent event) {
       
        if (invalidField()) return;
        
        String studentNo = tf1.getText().trim().toLowerCase();
        String bookISBN = tf2.getText().trim().toLowerCase();        
        
        Utils.fetching(rootPane, loaderSpin, true);
        
        new Thread(() -> {
            try {
                boolean isCompleted = false;
                Student student = Utils.stubs.getStudent(studentNo);
                
                if (student == null) {
                    Platform.runLater(() -> {
                        isCompleteField(true, abE1, "Student not exist!");                  
                    });
                }
                
                Book book = Utils.stubs.getBook(bookISBN);
                
                if (book == null) {
                    Platform.runLater(() -> {
                        isCompleteField(true, abE2, "Book not exist!");               
                    });
                }
                
                if (student == null || book == null) {
                    Utils.fetching(rootPane, loaderSpin, false);
                    return;
                }
                
                Issue tr = new Issue();

                tr.setTr_student(student);
                tr.setTr_book(book);    
                String rs = Utils.stubs.addIssue(tr);

                Platform.runLater(() -> {
                    if (rs == null) {
                        Utils.alertSuccess("Issue created successfully!");                      
                    } else {
                        Utils.alertError(rs);
                    }
                    Utils.fetching(rootPane, loaderSpin, false);                    
                });
            } catch (RemoteException ex) {
                Logger.getLogger(com.ky.jacon.client.gui.Home.Controller.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }).start();        
    }

    @FXML
    private void returnAction(ActionEvent event) {
       
        if (invalidField()) return;
        
        String studentNo = tf1.getText().trim().toLowerCase();
        String bookISBN = tf2.getText().trim().toLowerCase();        
        
        Utils.fetching(rootPane, loaderSpin, true);
        
        new Thread(() -> {
            try {
                boolean isCompleted = false;
                Student student = Utils.stubs.getStudent(studentNo);
                
                if (student == null) {
                    Platform.runLater(() -> {
                        isCompleteField(true, abE1, "Student not exist!");                  
                    });
                }
                
                Book book = Utils.stubs.getBook(bookISBN);
                
                if (book == null) {
                    Platform.runLater(() -> {
                        isCompleteField(true, abE2, "Book not exist!");               
                    });
                }
                
                if (student == null || book == null) {
                    Utils.fetching(rootPane, loaderSpin, false);
                    return;
                }
                
                Issue tr = new Issue();

                tr.setTr_student(student);
                tr.setTr_book(book);   
                
                String rs = Utils.stubs.returnBook(tr);
                
                Platform.runLater(() -> {
                    if (rs == null) {
                        Utils.alertSuccess("Book returned successfully!");
                    } else {
                        Utils.alertError(rs);
                    }
                    Utils.fetching(rootPane, loaderSpin, false);                    
                });
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }).start();         
    }
}
