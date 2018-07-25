/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.ViewUser;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXProgressBar;
import com.ky.jacon.api.Model.User;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author kys
 */
public class Controller implements Initializable {
    public static User userManagementSelectedTarget; 
    
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
    @FXML
    private TableColumn<User, String> col4;
    @FXML
    private TableColumn<User, String> col5;
    @FXML
    private TableColumn<User, String> col6;
    @FXML
    private JFXComboBox<String> dd1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        dd1.getItems().add("ALL");
        dd1.setValue("ALL");
        new Thread(() -> {
            try {

                List<String> res = Utils.stubs.getRoles();
                
                Platform.runLater(() -> {
                    if (res == null) {
                        Utils.alertError("Failed to get roles.");
                        return;
                    }                    
                    for (String s: res) {
                        if (s.equals("admin")) continue;
                        dd1.getItems().add(s);
                    }

                });
                
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
        
        initialTable();
        
        updateWithRole();
    }    
    
    private void initialTable() {
        t1.setPlaceholder(new Label("No user available!"));
        
        col1.setCellValueFactory(new PropertyValueFactory<>("username"));
        col2.setCellValueFactory(new PropertyValueFactory<>("email"));
        col3.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getRole().getRole_name()));
        // col4
        switchStatusCol();

        // col5
        editCol();
        
        // col6
        deleteCol();
    }
    
    private void editCol() {
        Callback<TableColumn<User, String>, TableCell<User, String>> cellRemove =       //
        (final TableColumn<User, String> param) -> {
          final TableCell<User, String> cell = new TableCell<User, String>()
          {
            
            final JFXButton rmBtn;
            {
              this.rmBtn = new JFXButton("EDIT");
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
                  userManagementSelectedTarget = getTableView().getItems().get( getIndex() );
                    event.consume();
                    StageSettings ss = new StageSettings();
                    ss.setPath("EditUser/view.fxml");
                    ss.setTitle("Edit User");
                    ss.setModal(true);
                    ss.setPreviousStage((Stage) rootPane.getScene().getWindow());
                    Utils.loadWindow(ss);
                    updateWithRole();
                });
                setGraphic( rmBtn );
                setText( null );
              }
            }
          };
          return cell;
        };
        col5.setCellFactory(cellRemove);                
    }
    
    private void switchStatusCol() {
        Callback<TableColumn<User, String>, TableCell<User, String>> cellRemove =       //
        (final TableColumn<User, String> param) -> {
          final TableCell<User, String> cell = new TableCell<User, String>()
          {
            
            final JFXButton rmBtn;
            {
              this.rmBtn = new JFXButton("");
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
                  User u = getTableView().getItems().get( getIndex() );
                  
                  u.setActive(u.getActive() == 1 ? 0 : 1);
                  
                  switchStatus(u);
                });
                rmBtn.setText(
                    (getTableView().getItems().get( getIndex() )
                        .getActive() == 1) ? "DEACTIVE" : "ACTIVE"
                );
                setGraphic( rmBtn );
                setText( null );
              }
            }
          };
          return cell;
        };
        col4.setCellFactory(cellRemove);        
    }
    
    private void deleteCol() {
        Callback<TableColumn<User, String>, TableCell<User, String>> cellRemove =       //
        (final TableColumn<User, String> param) -> {
          final TableCell<User, String> cell = new TableCell<User, String>()
          {
            
            final JFXButton rmBtn;
            {
              this.rmBtn = new JFXButton("DELETE");
              this.rmBtn.setButtonType(JFXButton.ButtonType.RAISED);
              this.rmBtn.getStyleClass().add("primary-button");
            }

            @Override
            public void updateItem( String item, boolean empty )
            {
              super.updateItem( item, empty );
              if ( empty || 
                      getTableView().getItems().get( getIndex() )
                              .getRole().getRole_name().equals("student") )
              {
                setGraphic( null );
                setText( null );
              }
              else
              {
                rmBtn.setOnAction(( ActionEvent event ) -> {
                  User u = getTableView().getItems().get( getIndex() );
                  if (Utils.alertConfirm(
                          "Attendance Management - Remove Attendance",
                          "Are you sure to remove " + u.getUsername() + " ?")) {
                    remove(u);
                  }
                });
                setGraphic( rmBtn );
                setText( null );
              }
            }
          };
          return cell;
        };
        col6.setCellFactory(cellRemove);        
    }
    
    private void switchStatus(User u) {
        Utils.fetching(rootPane, loaderBar, true);
        new Thread(() -> {
            try {
                String res = Utils.stubs.switchStatus(u);
                
                Platform.runLater(() -> {
                    Utils.fetching(rootPane, loaderBar, false);
                    if (res != null) {
                        Utils.alertError(res);
                    } else {
                        Utils.alertSuccess(
                               (u.getActive() == 1 ? "Activate" : "Deactivate") + " "
                                       + u.getUsername() + " successfully!");
                        updateWithRole();
                    }
                });
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }).start();        
    }
    
    private void remove(User u) {
        Utils.fetching(rootPane, loaderBar, true);
        new Thread(() -> {
            try {
                String res = Utils.stubs.deleteUser(u);
                
                Platform.runLater(() -> {
                    Utils.fetching(rootPane, loaderBar, false);
                    if (res != null) {
                        Utils.alertError(res);
                    } else {
                        Utils.alertSuccess(u.getUsername() + " deleted successfully!");
                        updateWithRole();
                    }
                });
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }).start();
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
    private void updateTable(String role) {
        Utils.fetching(rootPane, loaderBar, true);
        new Thread(() -> {
            try {
                List<User> ls = 
                        Utils.stubs.getUsersByRole(role);
                
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
        updateWithRole();
    }

    @FXML
    private void openAddUserAction(ActionEvent event) {
        event.consume();
        StageSettings ss = new StageSettings();
        ss.setPath("AddUser/view.fxml");
        ss.setTitle("Add User");
        ss.setModal(true);
        ss.setPreviousStage((Stage) rootPane.getScene().getWindow());
        Utils.loadWindow(ss); 
        updateWithRole();
    }

    @FXML
    private void ddChange(ActionEvent event) {
        updateWithRole();
    }
    private void updateWithRole() {
        if (dd1.getValue().equals("ALL")) {
            updateTable();
        } else {
            updateTable(dd1.getValue());
        }        
    }
}
