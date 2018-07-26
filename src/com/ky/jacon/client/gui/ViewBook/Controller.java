/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.ViewBook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.ky.jacon.api.Model.Issue;
import static com.ky.jacon.client.gui.BookManagement.Controller.bookManagementSelectedBook;
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
    private JFXButton issueBtn;
    @FXML
    private JFXTextField tf1;
    @FXML
    private JFXTextField tf2;
    @FXML
    private JFXTextField tf3;
    @FXML
    private JFXTextField tf4;
    @FXML
    private JFXTextField tf5;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        populateField();
        if (Utils.studSess == null) {
            issueBtn.setVisible(false);
        }
    }
    
    private void populateField() {
        tf1.setText(bookManagementSelectedBook.getBook_name());
        tf2.setText(bookManagementSelectedBook.getBook_author());
        tf3.setText(bookManagementSelectedBook.getBook_subject());
        tf4.setText(bookManagementSelectedBook.getBook_publisher());
        tf5.setText(bookManagementSelectedBook.getBook_isbn());
    }

    @FXML
    private void issueBookAction(ActionEvent event) {
        Utils.fetching(rootPane, loaderSpin, true);
        
        new Thread(() -> {
            try {
                Issue tr = new Issue();

                tr.setTr_student(Utils.studSess);
                tr.setTr_book(bookManagementSelectedBook);
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
    private void cancelAction(ActionEvent event) {
        Utils.closeWindow(rootPane);
    }    
}
