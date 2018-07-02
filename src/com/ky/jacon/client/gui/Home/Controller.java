/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.Home;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;
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
import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;
/**
 * FXML Controller class
 *
 * @author kys
 */
public class Controller implements Initializable {

    @FXML
    private StackPane rootPane;
    @FXML
    private TableView<Issue> trTable;

    private ObservableList<Book> books = FXCollections.observableArrayList();
    private ObservableList<Issue> transactions = FXCollections.observableArrayList();
    
    private Student selectedStudentForBooks = new Student();
    
    @FXML
    private TableColumn<Issue, String> trDate;
    @FXML
    private TableColumn<Issue, String> trView;
    @FXML
    private TableColumn<Issue, String> trBook;
    @FXML
    private TableView<Book> bookTable;
    @FXML
    private TableColumn<Book, String> bookName;
    @FXML
    private TableColumn<Book, String> bookView;

    @FXML
    private JFXTextField addBookAuthor;
    @FXML
    private JFXTextField addBookName;
    @FXML
    private JFXTextField addBookSubject;
    @FXML
    private JFXTextField addBookPub;
    @FXML
    private JFXTextField addBookISBN;
    @FXML
    private TextArea addBookDesc;
    @FXML
    private TableColumn<Book, String> bookAdd;
    @FXML
    private Label abE1;
    @FXML
    private Label abE2;
    @FXML
    private Label abE3;
    @FXML
    private Label abE4;
    @FXML
    private Label abE5;
    @FXML
    private JFXTextField addBookQuantity;
    @FXML
    private AnchorPane addBookPane;
    @FXML
    private JFXSpinner addBookSpin;
    @FXML
    private Tab homeTab;
    @FXML
    private Tab booksTab;
    @FXML
    private Tab issuedTab;
    @FXML
    private AnchorPane booksPane;
    @FXML
    private JFXSpinner booksSpin;
    @FXML
    private AnchorPane trPane;
    @FXML
    private JFXSpinner trSpin;
    @FXML
    private TableColumn<Book, Integer> bookQuantity;
    @FXML
    private TableColumn<Issue, String> trStatus;
    @FXML
    private TableColumn<Issue, String> trReturnedDate;
    @FXML
    private TableColumn<Issue, String> trReturnBook;
    @FXML
    private JFXTextField tpNoForBooks;
    @FXML
    private Label ul1;
    @FXML
    private Label ul2;
    @FXML
    private Label ul3;
    @FXML
    private Label ul4;
    @FXML
    private JFXToggleButton selectStdBooks;
    @FXML
    private JFXTextField searchStdIssue;




    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initialInterface();
        initialBook();
        initialTrans();
        // updateAllTables();
    }    
    
    private void initialInterface() {
        // START SOURCE FROM
        // URL: https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
        // force the field to be numeric only
        addBookQuantity.textProperty().addListener(
                (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (!newValue.matches("\\d*")) {
                addBookQuantity.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        // END SOURCE FROM
    }
    
    private void initialBook() {
        bookTable.setPlaceholder(new Label("No book available!"));
        
        bookName.setCellValueFactory(new PropertyValueFactory<>("book_name"));
        bookQuantity.setCellValueFactory(new PropertyValueFactory<>("book_quantity"));
        
        Callback<TableColumn<Book, String>, TableCell<Book, String>> cellAddIssue =       //
        (final TableColumn<Book, String> param) -> {
            final TableCell<Book, String> cell = new TableCell<Book, String>()
            {
              final JFXButton mdlBtn;
              {
                this.mdlBtn = new JFXButton("ADD");
                this.mdlBtn.setButtonType(JFXButton.ButtonType.FLAT);
                this.mdlBtn.getStyleClass().add("menu-add-button");
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
                  mdlBtn.setOnAction(( ActionEvent event ) -> {
                      if (!selectStdBooks.isSelected()) {
                          Utils.alertError("Please search and select a student before issue book!");
                          return;
                      }
                    Book selectedBook = getTableView().getItems().get( getIndex() );
                    issueBook(selectedBook);
                  });
                  setGraphic( mdlBtn );
                  // setText( null );
                }
              }
            };
            return cell;
          };
          bookAdd.setCellFactory(cellAddIssue);
          
        Callback<TableColumn<Book, String>, TableCell<Book, String>> cellViewBook =       //
        (final TableColumn<Book, String> param) -> {
            final TableCell<Book, String> cell = new TableCell<Book, String>()
            {
              final JFXButton mdlBtn;
              {
                this.mdlBtn = new JFXButton("VIEW");
                this.mdlBtn.setButtonType(JFXButton.ButtonType.FLAT);
                this.mdlBtn.getStyleClass().add("menu-add-button");
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
                  mdlBtn.setOnAction(( ActionEvent event ) -> {
                    Utils.alertSuccess("View!");
                  });
                  setGraphic( mdlBtn );
                  // setText( null );
                }
              }
            };
            return cell;
          };    
        bookView.setCellFactory(cellViewBook);
    }
    private void initialTrans() {
        trTable.setPlaceholder(new Label("No transaction available!"));
        
        trStatus.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getTr_status().getStatus_name()));
        trDate.setCellValueFactory(new PropertyValueFactory<>("tr_date"));
        trReturnedDate.setCellValueFactory(new PropertyValueFactory<>("tr_returned_date"));
        trBook.setCellValueFactory(
                cellData -> new SimpleStringProperty(cellData.getValue().getTr_book().getBook_name()));
        
        Callback<TableColumn<Issue, String>, TableCell<Issue, String>> cellViewTrans =       //
        (final TableColumn<Issue, String> param) -> {
            final TableCell<Issue, String> cell = new TableCell<Issue, String>()
            {
              final JFXButton mdlBtn;
              {
                this.mdlBtn = new JFXButton("VIEW");
                this.mdlBtn.setButtonType(JFXButton.ButtonType.FLAT);
                this.mdlBtn.getStyleClass().add("trans-view-button");
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
                  mdlBtn.setOnAction(( ActionEvent event ) -> {
                    Utils.alertSuccess("View!");
                    Issue selectedObj = getTableView().getItems().get( getIndex() );
                  });
                  setGraphic( mdlBtn );
                  // setText( null );
                }
              }
            };
            return cell;
          };
          trView.setCellFactory(cellViewTrans);
          
        Callback<TableColumn<Issue, String>, TableCell<Issue, String>> cellReturnBook =       //
        (final TableColumn<Issue, String> param) -> {
            final TableCell<Issue, String> cell = new TableCell<Issue, String>()
            {
              final JFXButton mdlBtn;
              {
                this.mdlBtn = new JFXButton("RETURN");
                this.mdlBtn.setButtonType(JFXButton.ButtonType.FLAT);
                this.mdlBtn.getStyleClass().add("trans-view-button");
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
                    Issue selectedObj = getTableView().getItems().get( getIndex() );
                    if (selectedObj.getTr_status().getStatus_name().
                            toLowerCase().
                            equals("returned"))
                        this.mdlBtn.setDisable(true);                  
                    else {
                        mdlBtn.setOnAction(( ActionEvent event ) -> {
                            returnBook(selectedObj);
                        });
                    }
                  setGraphic( mdlBtn );
                  // setText( null );
                }
              }
            };
            return cell;
          };
          trReturnBook.setCellFactory(cellReturnBook);
    }

    // Update Table
    private void updateBook() {
        Utils.fetching(booksPane, booksSpin, true);
        new Thread(() -> {
            try {
                List<Book> bookList = Utils.stubs.getBooks();
                Platform.runLater(() -> {
                    
                    books.removeAll(books);

                    for (int i = 0; i < bookList.size(); i++) {
                        Book book = bookList.get(i);

                        books.add(book);
                    }
                    bookTable.setItems(books);
                    Utils.fetching(booksPane, booksSpin, false);
                });
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
    private void updateTrans() {
        Utils.fetching(trPane, trSpin, true);
        new Thread(() -> {
            try {
                List<Issue> trList = 
                        Utils.stubs.getIssues();
                
                Platform.runLater(() -> {
                    transactions.removeAll(transactions);
                    
                    if (trList != null) {
                        for (int i = 0; i < trList.size(); i++) {
                            Issue tr = trList.get(i);

                            transactions.add(tr);
                        }
                        trTable.setItems(transactions);                        
                    }
                    Utils.fetching(trPane, trSpin, false);
                });

            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }).start();
    }

    
    @FXML
    private void logoutAction(ActionEvent event) {
        StageSettings ss = new StageSettings();
        ss.setTitle("Login");
        ss.setPath("LoginRegister/view.fxml");
        Utils.redirect(rootPane, ss);
    }

    private void issueBook(Book book) {
        Utils.fetching(booksPane, booksSpin, true);
        
        new Thread(() -> {
            Issue tr = new Issue();

            tr.setUser_id(selectedStudentForBooks.getUser_id());
            tr.setTr_book(book);            
            
            try {
                String rs = Utils.stubs.addIssue(tr);
                
                Platform.runLater(() -> {
                    if (rs == null) {
                        Utils.alertSuccess("Issue created successfully!");
                        updateTrans();                       
                    } else {
                        Utils.alertError(rs);
                    }
                    updateBook();
                    Utils.fetching(booksPane, booksSpin, false);                    
                });
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }).start();
    }
    
    private void returnBook(Issue issue) {
        Utils.fetching(trPane, trSpin, true);
        
        new Thread(() -> {
            try {
                String rs = Utils.stubs.returnBook(issue);
                
                Platform.runLater(() -> {
                    if (rs == null) {
                        Utils.alertSuccess("Book returned successfully!");
                        updateTrans();                       
                    } else {
                        Utils.alertError(rs);
                    }
                    updateBook();
                    Utils.fetching(trPane, trSpin, false);                    
                });
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }).start(); 
    }

    @FXML
    private void addBookAction(ActionEvent event) {
        boolean completeFields;
        String name = addBookName.getText().trim();
        String author = addBookAuthor.getText().trim();
        String subject = addBookSubject.getText().trim();
        String pub = addBookPub.getText().trim();
        String isbn = addBookISBN.getText().trim();
        String desc = addBookDesc.getText().trim();
        int quantity = Integer.parseInt(addBookQuantity.getText());

        completeFields = isCompleteField(
                name.isEmpty(),
                abE1,
                "Required Field!"
        );
        completeFields = isCompleteField(
                isbn.isEmpty(),
                abE5,
                "Required Field!"
        );
        if (!completeFields || (
                name.isEmpty() ||
                isbn.isEmpty())
        ) return; 

        Book book = new Book();
        book.setBook_author(author);
        book.setBook_desc(desc);
        book.setBook_isbn(isbn);
        book.setBook_name(name);
        book.setBook_publisher(pub);
        book.setBook_quantity(quantity);
        book.setBook_subject(subject);
        
        
        Utils.fetching(addBookPane, addBookSpin, true);
        
        new Thread(() -> {
            try {
                final Book myBook = Utils.stubs.addBook(book);
                
                Platform.runLater(() -> {
                    if (myBook != null) {
                        clearAddBookField();
                        Utils.alertSuccess(myBook.getBook_name() + " successfully added!");
                    } else {
                        Utils.alertError("ISBN exist!");
                    }
                    Utils.fetching(addBookPane, addBookSpin, false);
                });
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }            
        }).start();
    }
    
    private void clearAddBookField() {
        addBookName.clear();
        addBookAuthor.clear();
        addBookSubject.clear();
        addBookPub.clear();
        addBookISBN.clear();
        addBookDesc.clear();
        addBookQuantity.setText("0");
    }
    
    public boolean isCompleteField(boolean empty, Label errMsgLbl, String errMsg) {
        if (empty) {
          errMsgLbl.setText(errMsg);
          return false;
        }
        errMsgLbl.setText(null);
        return true;
    }

    @FXML
    private void changingTabAction(Event event) {
        if (homeTab.isSelected()){
            if (booksTab.isSelected()) {
                updateBook();
            }
            if (issuedTab.isSelected()) {
                updateTrans();
            }
        }
    }

    @FXML
    private void searchStudent(ActionEvent event) {
        if (tpNoForBooks.getText().isEmpty()) {
            Utils.alertError("TP No. cannot be empty!");
            return;
        }
        
        Utils.fetching(booksPane, booksSpin, true);
        
        new Thread(() -> {
            try {
                selectedStudentForBooks = Utils.stubs.getStudent(
                        tpNoForBooks.getText().toLowerCase().trim());
                
                Platform.runLater(() -> {
                    if (selectedStudentForBooks != null) {
                        ul1.setText("NAME:");
                        ul2.setText(selectedStudentForBooks.getStudent_name());
                        ul3.setText("INTAKE");
                        ul4.setText(selectedStudentForBooks.getStudent_intake());
                        selectStdBooks.setVisible(true);
                    } else {
                        resetSearchStudentBooksField();
                        ul1.setText("No such student!");
                    }
                });
                Utils.fetching(booksPane, booksSpin, false);
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
    
    private void resetSearchStudentBooksField() {
        ul1.setText("");
        ul2.setText("");
        ul3.setText("");
        ul4.setText("");
        selectStdBooks.setSelected(false);
        selectStdBooks.setVisible(false);
    }

    @FXML
    private void searchIssue(ActionEvent event) {
        if (searchStdIssue.getText().isEmpty()) {
            updateTrans();
            return;
        }
        
        Utils.fetching(trPane, trSpin, true);
        new Thread(() -> {
            try {
                Student student = Utils.stubs.getStudent(searchStdIssue.getText().toLowerCase().trim());
                
                if (student != null) {
                    List<Issue> issues = Utils.stubs.getIssuesByUserId(student.getUser_id());

                    Platform.runLater(() -> {
                        transactions.removeAll(transactions);
                        if (issues != null) {
                            for (int i = 0; i < issues.size(); i++) {
                                Issue tr = issues.get(i);

                                transactions.add(tr);
                            }
                            trTable.setItems(transactions);                        
                        }
                    });                    
                } else {
                   Platform.runLater(() -> {
                       transactions.removeAll(transactions);
                   });
                }

                Utils.fetching(trPane, trSpin, false);
            } catch (RemoteException ex) {
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
}
