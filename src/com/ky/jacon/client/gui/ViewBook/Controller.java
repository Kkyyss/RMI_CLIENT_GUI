/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.ViewBook;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXProgressBar;
import com.ky.jacon.api.Model.Book;
import com.ky.jacon.client.gui.Utils.StageSettings;
import com.ky.jacon.client.gui.Utils.Utils;
import static com.sun.javaws.CacheUtil.remove;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
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
    @FXML
    private JFXButton addBookBtn;
    

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
        if (Utils.studSess != null) {
            addBookBtn.setVisible(false);
            col5.setVisible(false);
            col6.setVisible(false);
        }
        t1.setPlaceholder(new Label("No book available!"));
        
        col1.setCellValueFactory(new PropertyValueFactory<>("book_name"));
        col2.setCellValueFactory(new PropertyValueFactory<>("book_author"));
        col3.setCellValueFactory(new PropertyValueFactory<>("book_isbn"));
        col4.setCellValueFactory(new PropertyValueFactory<>("book_quantity"));
        // col5
        editCol();
        // col6
        deleteCol();
    }
    
    private void editCol() {
        
    }
    
    private void deleteCol() {
        Callback<TableColumn<Book, String>, TableCell<Book, String>> cellRemove =       //
        (final TableColumn<Book, String> param) -> {
          final TableCell<Book, String> cell = new TableCell<Book, String>()
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
              if ( empty )
              {
                setGraphic( null );
                setText( null );
              }
              else
              {
                rmBtn.setOnAction(( ActionEvent event ) -> {
                  Book b = getTableView().getItems().get( getIndex() );
                  if (Utils.alertConfirm(
                          "Attendance Management - Remove Attendance",
                          "Are you sure to remove " + b.getBook_isbn() + " ?")) {
                    remove(b);
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
    
    private void remove(Book b) {
        Utils.fetching(rootPane, loaderBar, true);
        new Thread(() -> {
            try {
                String res = Utils.stubs.deleteBook(b.getBook_id());
                
                Platform.runLater(() -> {
                    Utils.fetching(rootPane, loaderBar, false);
                    if (res != null) {
                        Utils.alertError(res);
                    } else {
                        Utils.alertSuccess(b.getBook_isbn() + " deleted successfully!");
                        updateTable();
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

    @FXML
    private void openAddBookAction(ActionEvent event) {
        event.consume();
        StageSettings ss = new StageSettings();
        ss.setPath("AddBook/view.fxml");
        ss.setTitle("Add Book");
        ss.setModal(true);
        ss.setPreviousStage((Stage) rootPane.getScene().getWindow());
        
        Utils.loadWindow(ss);
        updateTable();
    }
    
}
