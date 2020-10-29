package info.hkzlab.dupal.peeper.controllers;

import info.hkzlab.dupal.peeper.App;
import info.hkzlab.dupal.peeper.controllers.components.PinStatus;
import info.hkzlab.dupal.peeper.devices.PALSpecs;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    @FXML private ListView<PinStatus> readPinList;
    @FXML private ListView<PinStatus> writePinList;

    public void initialize() {
        PALSpecs pSpecs = App.phole.getSpecs();

        ObservableList<PinStatus> wrList = FXCollections.observableArrayList();
        ObservableList<PinStatus> rdList = FXCollections.observableArrayList();
        wrList.addAll(buildWritePinList(pSpecs));
        rdList.addAll(buildReadPinList(pSpecs));

        writePinList.setItems(wrList);
        readPinList.setItems(rdList);
    } 

    private PinStatus[] buildWritePinList(PALSpecs pSpecs) {
        int[] wPin = pSpecs.getWritePinNumbers();
        String[] labels = pSpecs.getLabels();

        PinStatus[] array = new PinStatus[wPin.length];

        for(int idx = 0; idx < array.length; idx++) {
            array[idx] = new PinStatus(wPin[idx], idx, labels[wPin[idx]-1]);
        }

        return array;
    }

    private PinStatus[] buildReadPinList(PALSpecs pSpecs) {
        int[] rPin = pSpecs.getReadPinNumbers();
        String[] labels = pSpecs.getLabels();

        PinStatus[] array = new PinStatus[rPin.length];

        for(int idx = 0; idx < array.length; idx++) {
            array[idx] = new PinStatus(rPin[idx], idx, labels[rPin[idx]-1]);
        }

        return array;
    }
}
