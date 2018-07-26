/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.Home;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import com.ky.jacon.client.gui.Utils.StageSettings;
import com.ky.jacon.client.gui.Utils.Utils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
/**
 * FXML Controller class
 *
 * @author kys
 */
public class Controller implements Initializable {

    @FXML
    private StackPane rootPane;

    @FXML
    private Tab userTab;
    @FXML
    private JFXTabPane tabPane;
    @FXML
    private JFXButton viewBookedBtn;
    @FXML
    private JFXButton viewIssueBtn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initialInterface();
    }   
    
    private void initialInterface() {
        if (!Utils.userSess.getRole().getRole_name().equals("admin")) {
            tabPane.getTabs().remove(userTab);    
        }
        if (Utils.studSess == null) {
            viewBookedBtn.setVisible(false);
        } else {
            viewIssueBtn.setVisible(false);
        }
    }
    
    @FXML
    private void logoutAction(ActionEvent event) {
        StageSettings ss = new StageSettings();
        Utils.studSess = null;
        Utils.userSess = null;
        ss.setTitle("Login");
        ss.setPath("Login/view.fxml");
        Utils.redirect(rootPane, ss);
    }
    
    private void openAddBookAction(ActionEvent event) {
        event.consume();
        StageSettings ss = new StageSettings();
        ss.setPath("AddBook/view.fxml");
        ss.setTitle("Add Book");
        ss.setModal(true);
        ss.setPreviousStage((Stage) rootPane.getScene().getWindow());
        
        Utils.loadWindow(ss);        
    }

    @FXML
    private void openViewBookAction(ActionEvent event) {
        event.consume();
        StageSettings ss = new StageSettings();
        ss.setPath("ViewBook/view.fxml");
        ss.setTitle("View Book");
        ss.setModal(true);
        ss.setPreviousStage((Stage) rootPane.getScene().getWindow());
        Utils.loadWindow(ss);
    }

    @FXML
    private void openViewIssueAction(ActionEvent event) {
        event.consume();
        StageSettings ss = new StageSettings();
        ss.setPath("ViewIssue/view.fxml");
        ss.setTitle("View Issue");
        // ss.setModal(true);
        // ss.setPreviousStage((Stage) rootPane.getScene().getWindow());
        Utils.loadWindow(ss);        
    }

    @FXML
    private void openReturnOrIssueBookAction(ActionEvent event) {
        event.consume();
        StageSettings ss = new StageSettings();
        ss.setPath("IssueOrReturnBook/view.fxml");
        ss.setTitle("Issue/Return Book");
        ss.setModal(true);
        ss.setPreviousStage((Stage) rootPane.getScene().getWindow());
        Utils.loadWindow(ss);
    }

    @FXML
    private void openVIewUserAction(ActionEvent event) {
        event.consume();
        StageSettings ss = new StageSettings();
        ss.setPath("ViewUser/view.fxml");
        ss.setTitle("View User");
        ss.setModal(true);
        ss.setPreviousStage((Stage) rootPane.getScene().getWindow());
        Utils.loadWindow(ss);
    }

    @FXML
    private void openBookedAction(ActionEvent event) {
        event.consume();
        StageSettings ss = new StageSettings();
        ss.setPath("ViewBooked/view.fxml");
        ss.setTitle("View Booked");
        // ss.setModal(true);
        // ss.setPreviousStage((Stage) rootPane.getScene().getWindow());
        Utils.loadWindow(ss);          
    }
}
