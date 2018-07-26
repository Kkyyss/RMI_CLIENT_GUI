/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.EditBook;

import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.ky.jacon.api.Model.Book;
import com.ky.jacon.client.gui.Utils.Utils;
import static com.ky.jacon.client.gui.Utils.Utils.isCompleteField;
import static com.ky.jacon.client.gui.BookManagement.Controller.bookManagementSelectedBook;
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
    private JFXTextField tf1;
    @FXML
    private JFXTextField tf2;
    @FXML
    private JFXTextField tf3;
    @FXML
    private JFXTextField tf4;
    @FXML
    private JFXTextField tf5;
    @FXML
    private JFXTextField tf6;
    @FXML
    private Label lbl1;
    @FXML
    private Label lbl2;
    @FXML
    private Label lbl3;
    @FXML
    private Label lbl4;
    @FXML
    private Label lbl5;
    @FXML
    private JFXSpinner loaderSpin;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initialInterface();
        initializeField();
    }    

    private void initialInterface() {
        tf6.textProperty().addListener(
                (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                tf6.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });        
    }

    @FXML
    private void saveBookAction(ActionEvent event) {
        boolean completeFields;
        String name = tf1.getText().trim();
        String author = tf2.getText().trim();
        String subject = tf3.getText().trim();
        String pub = tf4.getText().trim();
        String isbn = tf5.getText().trim();
        int quantity = Integer.parseInt(tf6.getText());

        completeFields = isCompleteField(
                name.isEmpty(),
                lbl1,
                "Required Field!"
        );
        completeFields = isCompleteField(
                isbn.isEmpty(),
                lbl5,
                "Required Field!"
        );
        if (!completeFields || (
                name.isEmpty() ||
                isbn.isEmpty())
        ) return; 

        bookManagementSelectedBook.setBook_author(author);
        bookManagementSelectedBook.setBook_isbn(isbn);
        bookManagementSelectedBook.setBook_name(name);
        bookManagementSelectedBook.setBook_publisher(pub);
        bookManagementSelectedBook.setBook_quantity(quantity);
        bookManagementSelectedBook.setBook_subject(subject);

        Utils.fetching(rootPane, loaderSpin, true);
            new Thread(() -> {
                try {
                    String res = Utils.stubs.saveBook(bookManagementSelectedBook);

                    Platform.runLater(() -> {
                        Utils.fetching(rootPane, loaderSpin, false);
                        if (res == null) {
                            Utils.alertSuccess("Updated successfully!");
                        } else {
                            Utils.alertError(res);
                        }
                    });
                } catch (RemoteException ex) {
                    Logger.getLogger(com.ky.jacon.client.gui.Home.Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
        }).start();      
    }
    
    private void initializeField() {
        tf1.setText(bookManagementSelectedBook.getBook_name());
        tf2.setText(bookManagementSelectedBook.getBook_author());
        tf3.setText(bookManagementSelectedBook.getBook_subject());
        tf4.setText(bookManagementSelectedBook.getBook_publisher());
        tf5.setText(bookManagementSelectedBook.getBook_isbn());
        tf6.setText(bookManagementSelectedBook.getBook_quantity() + "");
    }     

    @FXML
    private void cancelAction(ActionEvent event) {
        Utils.closeWindow(rootPane);
    }
}
