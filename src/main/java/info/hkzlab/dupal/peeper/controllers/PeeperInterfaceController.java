package info.hkzlab.dupal.peeper.controllers;

import info.hkzlab.dupal.peeper.App;
import info.hkzlab.dupal.peeper.devices.PALSpecs;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;

public class PeeperInterfaceController {
    @FXML private ImageView palModelPicture;
    @FXML private ListView<String> eventHistoryList;
    @FXML private Button writeRstButton;
    @FXML private Button writeClkButton;
    @FXML private Button writeSetButton;
    @FXML private Button readRefreshButton;
    @FXML private ListView<String> readPinList;
    @FXML private ListView<String> writePinList;

    public void initialize() {
        PALSpecs pspecs = App.phole.getSpecs();
    } 
}
