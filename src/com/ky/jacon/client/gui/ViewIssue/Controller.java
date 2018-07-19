/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.ViewIssue;

import com.jfoenix.controls.JFXProgressBar;
import com.ky.jacon.api.Model.Issue;
import com.ky.jacon.client.gui.Utils.Utils;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;
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
    private ObservableList<Issue> sources = FXCollections.observableArrayList();
    
    @FXML
    private AnchorPane rootPane;
    @FXML
    private JFXProgressBar loaderBar;
    @FXML
    private TableView<Issue> t1;
    @FXML
    private TableColumn<Issue, String> col1;
    @FXML
    private TableColumn<Issue, String> col2;
    @FXML
    private TableColumn<Issue, String> col3;
    @FXML
    private TableColumn<Issue, String> col4;
    @FXML
    private TableColumn<Issue, String> col5;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initialTable();
        
        updateTable();
    }
    
    private void initialTable() {
        t1.setPlaceholder(new Label("No issue available!"));
        
        col1.setCellValueFactory(new PropertyValueFactory<>("tr_date"));
        col2.setCellValueFactory(new PropertyValueFactory<>("tr_returned_date"));
        col3.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getTr_student().getStudent_no()));
        col4.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getTr_book().getBook_isbn()));
        col5.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getTr_status().getStatus_name()));       
    }
    
    private void updateTable() {
        Utils.fetching(rootPane, loaderBar, true);
        new Thread(() -> {
            try {
                List<Issue> trList = 
                        Utils.stubs.getIssues();
                
                Platform.runLater(() -> {
                    sources.removeAll(sources);
                    if (trList != null) {
                        for (int i = 0; i < trList.size(); i++) {
                            Issue tr = trList.get(i);

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
