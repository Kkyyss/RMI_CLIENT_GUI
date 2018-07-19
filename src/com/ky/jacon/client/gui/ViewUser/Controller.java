/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.ViewUser;

import com.jfoenix.controls.JFXProgressBar;
import com.ky.jacon.api.Model.User;
import com.ky.jacon.client.gui.Utils.Utils;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
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

/**
 * FXML Controller class
 *
 * @author kys
 */
public class Controller implements Initializable {
    private ObservableList<User> sources = FXCollections.observableArrayList();

    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXProgressBar loaderBar;
    @FXML
    private TableView<User> t1;
    @FXML
    private TableColumn<User, String> col1;
    @FXML
    private TableColumn<User, String> col2;
    @FXML
    private TableColumn<User, String> col3;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initialTable();
        
        updateTable();

        Thread autoLoaderThread = new Thread(() -> {
            while (true) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    updateTable();
                } catch (InterruptedException ex) {
                    Logger.getLogger(com.ky.jacon.client.gui.ViewBook.Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        autoLoaderThread.setDaemon(true);
        
        autoLoaderThread.start();
    }    
    
    private void initialTable() {
        t1.setPlaceholder(new Label("No user available!"));
        
        col1.setCellValueFactory(new PropertyValueFactory<>("username"));
        col2.setCellValueFactory(new PropertyValueFactory<>("email"));
        col3.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getRole().getRole_name()));
    }
    
    private void updateTable() {
        Utils.fetching(rootPane, loaderBar, true);
        new Thread(() -> {
            try {
                List<User> ls = 
                        Utils.stubs.getUsers();
                
                Platform.runLater(() -> {
                    sources.removeAll(sources);
                    if (ls != null) {
                        for (int i = 0; i < ls.size(); i++) {
                            User tr = ls.get(i);

                            sources.add(tr);
                        }
                        t1.setItems(sources);
                    }
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
