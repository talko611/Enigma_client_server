package com.enigma.Utils;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public class UiAdapter {
    private SimpleBooleanProperty isLoaded;
    private SimpleBooleanProperty isConfigure;
    private SimpleStringProperty currentConfig;

    public UiAdapter(){
        this.currentConfig = new SimpleStringProperty();
        this.isConfigure = new SimpleBooleanProperty(false);
        this.isLoaded = new SimpleBooleanProperty(false);
    }


    public SimpleBooleanProperty isLoadedProperty() {
        return isLoaded;
    }

    public SimpleBooleanProperty isConfigureProperty() {
        return isConfigure;
    }

    public SimpleStringProperty currentConfigProperty() {
        return currentConfig;
    }

    public void setIsLoaded(boolean isLoaded) {
        this.isLoaded.set(isLoaded);
    }

    public void setIsConfigure(boolean isConfigure) {
        this.isConfigure.set(isConfigure);
    }

    public void setCurrentConfig(String currentConfig) {
        this.currentConfig.set(currentConfig);
    }
}
