/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.ViewBook;

import com.jfoenix.controls.JFXProgressBar;
import com.ky.jacon.api.Model.Book;
import com.ky.jacon.client.gui.Utils.Utils;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kys
 */
public class Controller implements Initializable {
    
    private ObservableList<Book> sources = FXCollections.observableArrayList();

    @FXML
    private TableView<Book> t1;    
    @FXML
    private TableColumn<Book, String> col1;
    @FXML
    private TableColumn<Book, String> col2;
    @FXML
    private TableColumn<Book, String> col3;
    @FXML
    private TableColumn<Book, String> col4;
    @FXML
    private TableColumn<Book, String> col5;
    @FXML
    private TableColumn<Book, String> col6;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXProgressBar loaderBar;
    

    /**
     * Initializes the controller class.
     */
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initialTable();
        
        updateTable();

        Thread autoLoaderThread = new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    updateTable();
                } catch (InterruptedException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        autoLoaderThread.setDaemon(true);
        
        autoLoaderThread.start();
    }
    
    private void initialTable() {
        t1.setPlaceholder(new Label("No book available!"));
        
        col1.setCellValueFactory(new PropertyValueFactory<>("book_name"));
        col2.setCellValueFactory(new PropertyValueFactory<>("book_author"));
        col3.setCellValueFactory(new PropertyValueFactory<>("book_subject"));
        col4.setCellValueFactory(new PropertyValueFactory<>("book_publisher"));
        col5.setCellValueFactory(new PropertyValueFactory<>("book_isbn"));
        col6.setCellValueFactory(new PropertyValueFactory<>("book_quantity"));
    }
    
    private void updateTable() {
        Utils.fetching(rootPane, loaderBar, true);
        new Thread(() -> {
            try {
                List<Book> bookList = Utils.stubs.getBooks();
                Platform.runLater(() -> {
                    
                    sources.removeAll(sources);

                    for (int i = 0; i < bookList.size(); i++) {
                        Book book = bookList.get(i);

                        sources.add(book);
                    }
                    t1.setItems(sources);
                    Utils.fetching(rootPane, loaderBar, false);
                });
            } catch (RemoteException ex) {
                Logger.getLogger(com.ky.jacon.client.gui.Home.Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();        
    }

    @FXML
    private void reloadAction(ActionEvent event) {
        updateTable();
    }
    
}
