/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.AddBook;

import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.ky.jacon.api.Model.Book;
import com.ky.jacon.client.gui.Utils.Utils;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
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
    private Label abE1;
    @FXML
    private Label abE2;
    @FXML
    private Label abE3;
    @FXML
    private Label abE4;
    @FXML
    private Label abE5;
    @FXML
    private JFXSpinner loaderSpin;
    @FXML
    private JFXTextField nametf;
    @FXML
    private JFXTextField authortf;
    @FXML
    private JFXTextField subjecttf;
    @FXML
    private JFXTextField publishertf;
    @FXML
    private JFXTextField isbntf;
    @FXML
    private JFXTextField quantitytf;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initialInterface();
    }    

    private void initialInterface() {
        quantitytf.textProperty().addListener(
                (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                quantitytf.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });        
    }
    
    @FXML
    private void addBookAction(ActionEvent event) {
        boolean completeFields;
        String name = nametf.getText().trim();
        String author = authortf.getText().trim();
        String subject = subjecttf.getText().trim();
        String pub = publishertf.getText().trim();
        String isbn = isbntf.getText().trim();
        int quantity = Integer.parseInt(quantitytf.getText());

        completeFields = isCompleteField(
                name.isEmpty(),
                abE1,
                "Required Field!"
        );
        completeFields = isCompleteField(
                isbn.isEmpty(),
                abE5,
                "Required Field!"
        );
        if (!completeFields || (
                name.isEmpty() ||
                isbn.isEmpty())
        ) return; 

        Book book = new Book();
        book.setBook_author(author);
        book.setBook_isbn(isbn);
        book.setBook_name(name);
        book.setBook_publisher(pub);
        book.setBook_quantity(quantity);
        book.setBook_subject(subject);

        if (Utils.alertConfirm("Add Book", "Confirm add book?")) {
            Utils.fetching(rootPane, loaderSpin, true);
            new Thread(() -> {
                try {
                    final Book myBook = Utils.stubs.addBook(book);

                    Platform.runLater(() -> {
                        Utils.fetching(rootPane, loaderSpin, false);
                        if (myBook != null) {
                            Utils.alertSuccess(myBook.getBook_name() + " successfully added!");
                            Utils.closeWindow(rootPane);
                        } else {
                            Utils.alertError("ISBN exist!");
                        }
                    });
                } catch (RemoteException ex) {
                    Logger.getLogger(com.ky.jacon.client.gui.Home.Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();            
         
        }
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
}
