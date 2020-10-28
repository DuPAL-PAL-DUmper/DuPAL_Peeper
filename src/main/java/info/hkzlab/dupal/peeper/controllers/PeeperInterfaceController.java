package info.hkzlab.dupal.peeper.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class PeeperInterfaceController {
    @FXML private ImageView palModelPicture;
    @FXML private ListView<String> eventHistoryList;
    @FXML private TextField cmdInputField;
    @FXML private GridPane writeGridPane;
    @FXML private GridPane readGridPane;
    @FXML private Button writeRstButton;
    @FXML private Button writeClkButton;
    @FXML private Button writeSetButton;
    @FXML private Button readResetButton;

    public void initialize() {

    } 
}
