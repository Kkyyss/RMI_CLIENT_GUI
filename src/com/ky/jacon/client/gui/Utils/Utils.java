/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.Utils;

import com.jfoenix.controls.JFXProgressBar;
import com.jfoenix.controls.JFXSpinner;
import com.ky.jacon.api.Model.Student;
import com.ky.jacon.api.Model.User;
import com.ky.jacon.api.services.GlobalService;
import com.ky.jacon.client.gui.Main;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author kys
 */
public class Utils {
    public static GlobalService stubs;
    public static User userSess;
    public static Student studSess;
    
    public static void redirect(StageSettings ss) {
        loadWindow(ss);
    }
  public static void redirect(AnchorPane rootPane, StageSettings ss) {
    closeWindow(rootPane);
    loadWindow(ss);
  }    
  public static void redirect(StackPane rootPane, StageSettings ss) {
    closeWindow(rootPane);
    loadWindow(ss);
  } 
  
  public static void closeWindow(AnchorPane pane) {
    Stage stage = (Stage) pane.getScene().getWindow();
    stage.close();
  }
    public static void closeWindow(StackPane pane) {
        Stage stage = (Stage) pane.getScene().getWindow();
        stage.close();
  }

  public static void loadWindow(StageSettings ss) {
    try {
      System.out.println(ss.getPath());
      Parent parent = FXMLLoader.load(Main.class.getResource(ss.getPath()));
      Stage stage = new Stage(StageStyle.DECORATED);
      // stage.getIcons().add(new Image(Client.class.getClassLoader().getResourceAsStream("images/modules.png")));
      stage.setTitle(ss.getTitle());
      stage.setScene(new Scene(parent));
      stage.setResizable(ss.isResizable());
      stage.setMaximized(ss.isFullscreen());
      if (ss.isResizable()) {
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        stage.setMinWidth(888);
      }
      
      if (ss.isModal()) {
        stage.initOwner(ss.getPreviousStage());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.showAndWait();
      } else
          stage.show();
    } catch (IOException ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
  
  public static void alertError(String msg) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setResizable(false);
    alert.setHeaderText(null);
    alert.setContentText(msg);
    alert.showAndWait();
  }
  
  public static void alertSuccess(String msg) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setResizable(false);
    alert.setHeaderText(null);
    alert.setContentText(msg);
    
    alert.getDialogPane().setPrefWidth(300);
    alert.setX(0);
    alert.setY(0);
    alert.initModality(Modality.NONE);
    alert.show();
    new Thread(() -> {
        try {
            TimeUnit.SECONDS.sleep(3);
            Platform.runLater(() -> {
                alert.close();
            }); 
        } catch (InterruptedException ex) {
            Logger.getLogger(Utils.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }).start();
  }
  
  public static boolean alertConfirm(String title, String msg) {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setResizable(false);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(msg);
    alert.showAndWait();
    return alert.getResult() == ButtonType.OK;
  }    
  
    public static void fetching(AnchorPane pane, JFXSpinner spin, boolean isFetching) {
        pane.setDisable(isFetching);
        spin.setDisable(!isFetching);
        spin.setVisible(isFetching);
    }
    
    public static void fetching(AnchorPane pane, JFXProgressBar spin, boolean isFetching) {
        pane.setDisable(isFetching);
        spin.setDisable(!isFetching);
        spin.setVisible(isFetching);
    }
    
    public static boolean isCompleteField(boolean empty, Label errMsgLbl, String errMsg) {
        if (empty) {
          errMsgLbl.setText(errMsg);
          return false;
        }
        errMsgLbl.setText(null);
        return true;
    }
}
