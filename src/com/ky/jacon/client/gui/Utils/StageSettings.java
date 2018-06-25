/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ky.jacon.client.gui.Utils;

import javafx.stage.Stage;

/**
 *
 * @author kys
 */
public class StageSettings {
    private String title, path;
    private boolean resizable = false, modal = false, fullscreen = false;
    private Stage previousStage;

    public String getTitle() {
        return title;
    }

    public boolean isModal() {
        return modal;
    }

    public boolean isFullscreen() {
        return fullscreen;
    }

    public Stage getPreviousStage() {
        return previousStage;
    }

    public String getPath() {
        return path;
    }

    public boolean isResizable() {
        return resizable;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPreviousStage(Stage previousStage) {
        this.previousStage = previousStage;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public void setModal(boolean modal) {
        this.modal = modal;
    }

    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }
}
