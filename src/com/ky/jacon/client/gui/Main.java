/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui;

import com.ky.jacon.api.services.GlobalService;
import com.ky.jacon.client.gui.Utils.StageSettings;
import com.ky.jacon.client.gui.Utils.Utils;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

/**
 *
 * @author kys
 */
public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Getting the registry 
        Registry registry = LocateRegistry.getRegistry("localhost", 3344); 
    
        // Looking up the registry for the remote object 
        Utils.stubs = (GlobalService) registry.lookup("JACON");
        
        StageSettings ss = new StageSettings();
        ss.setTitle("Login/Register");
        ss.setPath("LoginRegister/view.fxml");
        Utils.redirect(ss);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
