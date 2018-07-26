/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.ViewBooked;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.ky.jacon.api.Model.Book;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

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
    @FXML
    private TableColumn<Issue, String> col6;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initialTable();
        
        updateTable();
        returnCol();
    }    

    @FXML
    private void reloadAction(ActionEvent event) {
        updateTable();
    }
    
    private void returnCol() {
        Callback<TableColumn<Issue, String>, TableCell<Issue, String>> cellCol =       //
        (final TableColumn<Issue, String> param) -> {
          final TableCell<Issue, String> cell = new TableCell<Issue, String>()
          {
            final JFXButton rmBtn;
            {
              this.rmBtn = new JFXButton("RETURN");
              this.rmBtn.setButtonType(JFXButton.ButtonType.RAISED);
              this.rmBtn.getStyleClass().add("primary-button");
            }

            @Override
            public void updateItem( String item, boolean empty )
            {
              super.updateItem( item, empty );
              if ( empty )
              {
                setGraphic( null );
                setText( null );
              }
              else
              {
                rmBtn.setOnAction(( ActionEvent event ) -> {
                    Issue issue = getTableView().getItems().get( getIndex() );
                  if (Utils.alertConfirm(
                          "Book Management - Return Book",
                          "Are you sure to return " + issue.getTr_book().getBook_isbn() + " ?")) {
                    returnBook(issue.getTr_book());
                  }
                });
                setGraphic( rmBtn );
                setText( null );
              }
            }
          };
          return cell;
        };
        col6.setCellFactory(cellCol);        
    }
    
    private void returnBook(Book book) {
        Utils.fetching(rootPane, loaderBar, true);
        
        new Thread(() -> {
            try {
                Issue tr = new Issue();

                tr.setTr_student(Utils.studSess);
                tr.setTr_book(book);
                
                String rs = Utils.stubs.returnBook(tr);
                
                Platform.runLater(() -> {
                    if (rs == null) {
                        Utils.alertSuccess("Book returned successfully!");
                    } else {
                        Utils.alertError(rs);
                    }
                    updateTable();
                    Utils.fetching(rootPane, loaderBar, false);
                });
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }).start();
    }
    
    private void initialTable() {
        t1.setPlaceholder(new Label("No book borrowed!"));
        
        col1.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getTr_book().getBook_name()));
        col2.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getTr_book().getBook_author()));
        col3.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getTr_book().getBook_subject()));
        col4.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getTr_book().getBook_publisher()));
        col5.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getTr_book().getBook_isbn()));
    }
        
    private void updateTable() {
        Utils.fetching(rootPane, loaderBar, true);
        new Thread(() -> {
            try {
                List<Issue> trList = 
                        Utils.stubs.getIssuesByUserId(Utils.studSess.getUser_id());
                
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
}
