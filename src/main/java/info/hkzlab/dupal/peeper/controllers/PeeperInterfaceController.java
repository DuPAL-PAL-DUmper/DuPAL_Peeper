package info.hkzlab.dupal.peeper.controllers;

import info.hkzlab.dupal.peeper.App;
import info.hkzlab.dupal.peeper.controllers.components.PinStatus;
import info.hkzlab.dupal.peeper.devices.PALSpecs;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public class PeeperInterfaceController {
    @FXML private ImageView palModelPicture;
    @FXML private ListView<String> eventHistoryList;
    @FXML private GridPane pinGrid;

    @FXML private Button writeClrButton;
    @FXML private Button writeClkButton;
    @FXML private Button writeSetButton;
    @FXML private Button readButton;

    private PinStatus[] wrPins;
    private PinStatus[] rdPins;

    private static final Color colorRPinHI = Color.rgb(0, 200, 0, 0.4);
    private static final Color colorRPinLO = Color.rgb(200, 0, 0, 0.4);

    private static final Color colorWPinHI = Color.rgb(0, 200, 0, 0.7);
    private static final Color colorWPinLO = Color.rgb(200, 0, 0, 0.7);

    public void initialize() {
        PALSpecs pSpecs = App.phole.getSpecs();

        wrPins = buildWritePinList(pSpecs);
        rdPins = buildReadPinList(pSpecs);

        pinGrid.getColumnConstraints().clear();

        for(int idx = 0; idx < 3; idx++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(33.33);
            pinGrid.getColumnConstraints().add(column);
        }

        int pinsPerColumn = (pSpecs.getLabels().length / 2);
        double rowHeight = 100.0 / pinsPerColumn;
        pinGrid.getRowConstraints().clear();
        for(int idx = 0; idx < pinsPerColumn; idx++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(rowHeight);
            pinGrid.getRowConstraints().add(row);
        }

        buildPinGrid(pSpecs, pinGrid, wrPins, rdPins);
    } 

    private void buildPinGrid(PALSpecs pSpecs, GridPane grid, PinStatus[] wp, PinStatus[] rp) {
        int pinsPerColumn = pSpecs.getLabels().length / 2;

        for(PinStatus p : wp) {
            int column = (p.pinNumber - 1) / pinsPerColumn;
            int row;
            
            if(column == 1) {
                row = (pinsPerColumn - 1) - ((p.pinNumber - 1) % pinsPerColumn);
            } else row = (p.pinNumber - 1) % pinsPerColumn;


            Label pinLabel = new Label(p.pinName);
            pinLabel.setTextAlignment(TextAlignment.CENTER);
            pinLabel.setAlignment(Pos.CENTER);
            pinLabel.setMaxHeight(Double.MAX_VALUE);
            pinLabel.setMaxWidth(Double.MAX_VALUE);
            pinLabel.setBackground(new Background(new BackgroundFill(colorWPinLO, new CornerRadii(5.0), new Insets(3.0))));

            grid.add(pinLabel, column, row);
        }

         for(PinStatus p : rp) {
            int column = 2;
            int row = (pinsPerColumn - 1) - ((p.pinNumber - 1) % pinsPerColumn);

            Label pinLabel = new Label(p.pinName);
            pinLabel.setTextAlignment(TextAlignment.CENTER);

            pinLabel.setAlignment(Pos.CENTER);
            pinLabel.setMaxHeight(Double.MAX_VALUE);
            pinLabel.setMaxWidth(Double.MAX_VALUE);
            pinLabel.setBackground(new Background(new BackgroundFill(colorRPinLO, new CornerRadii(5.0), new Insets(3.0))));
            
            grid.add(pinLabel, column, row);
        }

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
