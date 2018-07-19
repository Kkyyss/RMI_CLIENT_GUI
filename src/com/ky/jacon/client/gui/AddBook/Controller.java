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
import static com.ky.jacon.client.gui.Utils.Utils.isCompleteField;
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
    private JFXSpinner loaderSpin;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initialInterface();
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
    private void addBookAction(ActionEvent event) {
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
                            resetField();
                            Utils.alertSuccess(myBook.getBook_name() + " successfully added!");
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
    
    private void resetField() {
        tf1.clear();
        tf2.clear();
        tf3.clear();
        tf4.clear();
        tf5.clear();
        tf6.setText("0");
        lbl1.setText("");
        lbl2.setText("");
        lbl3.setText("");
        lbl4.setText("");
        lbl5.setText("");
    }    
    
    @FXML
    private void cancelAction(ActionEvent event) {
        Utils.closeWindow(rootPane);
    }
}
