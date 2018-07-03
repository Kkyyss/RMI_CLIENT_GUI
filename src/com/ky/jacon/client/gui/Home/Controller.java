/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.Home;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.ky.jacon.api.Model.Book;
import com.ky.jacon.api.Model.Issue;
import com.ky.jacon.api.Model.Student;
import com.ky.jacon.client.gui.Utils.StageSettings;
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
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
/**
 * FXML Controller class
 *
 * @author kys
 */
public class Controller implements Initializable {

    @FXML
    private StackPane rootPane;
    private TableView<Issue> trTable;

    private ObservableList<Book> books = FXCollections.observableArrayList();
    private ObservableList<Issue> transactions = FXCollections.observableArrayList();
    
    private Student selectedStudentForBooks = new Student();
    
    private TableColumn<Issue, String> trDate;
    private TableColumn<Issue, String> trView;
    private TableColumn<Issue, String> trBook;

    @FXML
    private Tab homeTab;

    private Tab issuedTab;
    private AnchorPane trPane;
    private JFXSpinner trSpin;
    private TableColumn<Book, Integer> bookQuantity;
    private TableColumn<Issue, String> trStatus;
    private TableColumn<Issue, String> trReturnedDate;
    private TableColumn<Issue, String> trReturnBook;
    private JFXTextField searchStdIssue;
    @FXML
    private JFXButton viewBookButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    private void logoutAction(ActionEvent event) {
        StageSettings ss = new StageSettings();
        ss.setTitle("Login");
        ss.setPath("LoginRegister/view.fxml");
        Utils.redirect(rootPane, ss);
    }

    @FXML
    private void changingTabAction(Event event) {
        if (homeTab.isSelected()){
        }
    }

    @FXML
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
        // ss.setModal(true);
        // ss.setPreviousStage((Stage) rootPane.getScene().getWindow());
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

    private void openReturnBookAction(ActionEvent event) {
        event.consume();
        StageSettings ss = new StageSettings();
        ss.setPath("ReturnBook/view.fxml");
        ss.setTitle("Return Book");
        ss.setModal(true);
        ss.setPreviousStage((Stage) rootPane.getScene().getWindow());
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
}
