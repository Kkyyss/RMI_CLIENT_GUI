/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.Home;

import com.jfoenix.controls.JFXButton;
import com.ky.jacon.api.Model.Food;
import com.ky.jacon.api.Model.Transaction;
import com.ky.jacon.client.gui.Utils.StageSettings;
import com.ky.jacon.client.gui.Utils.Utils;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private TableView<Transaction> trTable;
    @FXML
    private TableView<Food> menuTable;
    @FXML
    private TableColumn<Food, String> menuName;
    @FXML
    private TableColumn<Food, String> menuStyle;
    @FXML
    private TableColumn<Food, Double> menuPrice;
    @FXML
    private TableColumn<Food, String> menuAdd;
    

    private ObservableList<Food> foods = FXCollections.observableArrayList();
    private ObservableList<Food> orders = FXCollections.observableArrayList();
    private ObservableList<Transaction> transactions = FXCollections.observableArrayList();
    private Double total = 0.0;
    
    @FXML
    private Label totalLabel;
    @FXML
    private TableView<Food> orderTable;
    @FXML
    private TableColumn<Food, String> orderName;
    @FXML
    private TableColumn<Food, String> orderPrice;
    @FXML
    private TableColumn<Food, String> orderRemove;
    @FXML
    private TableColumn<Transaction, Integer> trId;
    @FXML
    private TableColumn<Transaction, String> trDate;
    @FXML
    private TableColumn<Transaction, String> trTotal;
    @FXML
    private TableColumn<Transaction, String> trView;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        initialMenu();
        initialOrder();
        initialTrans();
        updateAllTables();
    }    
    
    private void initialMenu() {
        menuTable.setPlaceholder(new Label("No food available!"));
        
        menuName.setCellValueFactory(new PropertyValueFactory<>("food_name"));
        menuStyle.setCellValueFactory(new PropertyValueFactory<>("food_styles"));
        menuPrice.setCellValueFactory(new PropertyValueFactory<>("food_price"));
        
        Callback<TableColumn<Food, String>, TableCell<Food, String>> cellAddFood =       //
        (final TableColumn<Food, String> param) -> {
            final TableCell<Food, String> cell = new TableCell<Food, String>()
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
                    Utils.alertSuccess("Added!");
                    Food selectedFood = getTableView().getItems().get( getIndex() );
                    orders.add(selectedFood);
                    total += selectedFood.getFood_price();
                    updateTotal();
                  });
                  setGraphic( mdlBtn );
                  // setText( null );
                }
              }
            };
            return cell;
          };
          menuAdd.setCellFactory(cellAddFood);
    }
    private void initialOrder() {
        orderTable.setPlaceholder(new Label("No order available!"));
        
        orderName.setCellValueFactory(new PropertyValueFactory<>("food_name"));
        orderPrice.setCellValueFactory(new PropertyValueFactory<>("food_price"));
        
        Callback<TableColumn<Food, String>, TableCell<Food, String>> cellAddOrder =       //
        (final TableColumn<Food, String> param) -> {
            final TableCell<Food, String> cell = new TableCell<Food, String>()
            {
              final JFXButton mdlBtn;
              {
                this.mdlBtn = new JFXButton("REMOVE");
                this.mdlBtn.setButtonType(JFXButton.ButtonType.FLAT);
                this.mdlBtn.getStyleClass().add("order-remove-button");
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
                    Utils.alertSuccess("Removed!");
                    Food selectedFood = getTableView().getItems().get( getIndex() );
                    orders.remove(getIndex());
                    total -= selectedFood.getFood_price();
                    updateTotal();
                  });
                  setGraphic( mdlBtn );
                  // setText( null );
                }
              }
            };
            return cell;
          };
          orderRemove.setCellFactory(cellAddOrder);
    }
    private void initialTrans() {
        trTable.setPlaceholder(new Label("No transaction available!"));
        
        trId.setCellValueFactory(new PropertyValueFactory<>("tr_id"));
        trDate.setCellValueFactory(new PropertyValueFactory<>("tr_date"));
        trTotal.setCellValueFactory(new PropertyValueFactory<>("tr_total"));
        
        Callback<TableColumn<Transaction, String>, TableCell<Transaction, String>> cellViewTrans =       //
        (final TableColumn<Transaction, String> param) -> {
            final TableCell<Transaction, String> cell = new TableCell<Transaction, String>()
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
                    Transaction selectedObj = getTableView().getItems().get( getIndex() );
                  });
                  setGraphic( mdlBtn );
                  // setText( null );
                }
              }
            };
            return cell;
          };
          trView.setCellFactory(cellViewTrans);        
    }
    
    private void updateAllTables() {
        updateMenu();
        updateOrder();
        updateTrans();
    }

    // Update Table
    private void updateMenu() {
        try {
            List<Food> foodList = Utils.stubs.getFoods();
            
            foods.removeAll(foods);
            
            for (int i = 0; i < foodList.size(); i++) {
                Food food = foodList.get(i);
                
                foods.add(food);
            }
            menuTable.setItems(foods);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void updateOrder() {
        orderTable.setItems(orders);
        updateTotal();
    }
    private void updateTrans() {
        try {
            List<Transaction> trList = 
                    Utils.stubs.getTransactionsByUserId(Utils.userSess.getUser_id());
            
            transactions.removeAll(transactions);
            
            for (int i = 0; i < trList.size(); i++) {
                Transaction tr = trList.get(i);
                
                transactions.add(tr);
            }
            trTable.setItems(transactions);
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    private void updateTotal() {
        totalLabel.setText(String.format("RM %.2f", total));
    }
    
    private void resetOrder() {
        total = 0.0;
        updateTotal();
        orders.removeAll(orders);
    }
    
    @FXML
    private void logoutAction(ActionEvent event) {
        StageSettings ss = new StageSettings();
        ss.setTitle("Login");
        ss.setPath("LoginRegister/view.fxml");
        Utils.redirect(rootPane, ss);
    }

    @FXML
    private void orderAction(ActionEvent event) {
        if (orders.size() <= 0) {
            Utils.alertSuccess("No order bruh!");
            return;
        }
        Transaction tr = new Transaction();

        String foodTxt = "";
        for (int i = 0; i < orders.size(); i++) {
            Food food = orders.get(i);
            
            foodTxt += food.getFood_name();
            
            if (i + 1 != orders.size()) {
                foodTxt += "@";
            }
        }
        
        
        tr.setTr_total(total + "");
        tr.setUser_id(Utils.userSess.getUser_id());
        tr.setTr_food(foodTxt);
        // Utils.alertSuccess(tr.getOrder_date() + "\n" + tr.getTotal() + "\n" + tr.getUser_id() + "\n" + tr.getFood());
        try {
            if (Utils.stubs.addTransaction(tr) != null) {
                Utils.alertSuccess("Transaction created successfully!");
                resetOrder();
                updateTrans();
            }
        } catch (RemoteException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
