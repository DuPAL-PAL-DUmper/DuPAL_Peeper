package info.hkzlab.dupal.peeper.controllers;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import info.hkzlab.dupal.peeper.App;
import info.hkzlab.dupal.peeper.controllers.components.DPEvent;
import info.hkzlab.dupal.peeper.controllers.components.PinStatus;
import info.hkzlab.dupal.peeper.controllers.components.DPEvent.DPEventType;
import info.hkzlab.dupal.peeper.devices.PALSpecs;
import info.hkzlab.dupal.peeper.exceptions.PeepholeException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class PeeperInterfaceController {
    @FXML private Label palLabel;
    @FXML private ImageView palModelPicture;
    @FXML private ListView<DPEvent> eventHistoryList;
    @FXML private GridPane pinGrid;

    @FXML private Button writeClrButton;
    @FXML private Button writeClkButton;
    @FXML private Button writeSetButton;
    @FXML private Button readButton;

    private static final int MAX_HISTORY_BUFFER = 500;

    private static final Color colorRPinHI = Color.rgb(0, 200, 0, 0.2);
    private static final Color colorRPinLO = Color.rgb(200, 0, 0, 0.2);
    private static final Color colorWPinHI = Color.rgb(0, 190, 0, 0.7);
    private static final Color colorWPinLO = Color.rgb(200, 0, 0, 0.7);
    private static final Color colorWPinINACTIVE = Color.rgb(180, 180, 180, 0.5);

    private static final Border changedBorder = new Border(new BorderStroke(Color.rgb(200, 0, 0, 0.8), BorderStrokeStyle.DOTTED, new CornerRadii(5.0), BorderStroke.THIN));
    private static final Background wPinHIBackground = new Background(new BackgroundFill(colorWPinHI, new CornerRadii(5.0), new Insets(3.0)));
    private static final Background wPinLOBackground = new Background(new BackgroundFill(colorWPinLO, new CornerRadii(5.0), new Insets(3.0)));
    private static final Background rPinHIBackground = new Background(new BackgroundFill(colorRPinHI, new CornerRadii(5.0), new Insets(5.0)));
    private static final Background rPinLOBackground = new Background(new BackgroundFill(colorRPinLO, new CornerRadii(5.0), new Insets(5.0)));
    
    private static final Font pinLabelFont = new Font("System Bold", 12);

    private PinStatus[] wrPins;
    private PinStatus[] rdPins;
    
    private ArrayList<Label> writeLabels;
    private ArrayList<Label> readLabels;

    private Map<String, PinStatus> pinStatusMap;

    public void initialize() {
        PALSpecs pSpecs = App.phole.getSpecs();
        pinStatusMap = new HashMap<>();
        wrPins = buildWritePinList(pSpecs);
        rdPins = buildReadPinList(pSpecs);
        pinGrid.getColumnConstraints().clear();

        writeLabels = new ArrayList<>();
        readLabels = new ArrayList<>();

        // Initialize grid constraints
        for (int idx = 0; idx < 3; idx++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setPercentWidth(33.33);
            pinGrid.getColumnConstraints().add(column);
        }

        int pinsPerColumn = (pSpecs.getLabels().length / 2);
        double rowHeight = 100.0 / pinsPerColumn;
        pinGrid.getRowConstraints().clear();
        for (int idx = 0; idx < pinsPerColumn; idx++) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(rowHeight);
            pinGrid.getRowConstraints().add(row);
        }

        // Build the grid
        buildPinGrid(pSpecs, pinGrid, wrPins, rdPins, writeLabels, readLabels);

        // Disable the clock button if CLK pin is not present
        if (pSpecs.getMask_CLK() == 0)
            writeClkButton.setDisable(true);

        // Load the proper picture
        loadDevicePicture(pSpecs, palModelPicture);
        palLabel.setText(pSpecs.getDeviceName());

        // Initialize the PINs
        writePinState(wrPins, false);
        readPinState(rdPins);
        updateLabels(writeLabels, readLabels, pinStatusMap);

        // Set actions on buttons
        writeClrButton.setOnAction(event -> {
            for(PinStatus p : wrPins) p.clearChange();
            updateLabels(writeLabels, readLabels, pinStatusMap);
        });

        readButton.setOnAction(event -> {
            readPinState(rdPins);
            updateLabels(writeLabels, readLabels, pinStatusMap);
        });

        writeSetButton.setOnAction(event -> {
            writePinState(wrPins, false);
            readPinState(rdPins);
            updateLabels(writeLabels, readLabels, pinStatusMap);

            DPEvent dpEvent = new DPEvent(DPEventType.SET, wrPins, rdPins);
            eventHistoryList.getItems().add(dpEvent);
            if (eventHistoryList.getItems().size() > MAX_HISTORY_BUFFER) eventHistoryList.getItems().remove(0);

            eventHistoryList.scrollTo(dpEvent);
        });

        writeClkButton.setOnAction(event -> {
            writePinState(wrPins, true);
            readPinState(rdPins);
            updateLabels(writeLabels, readLabels, pinStatusMap);

            DPEvent dpEvent = new DPEvent(DPEventType.CLOCK, wrPins, rdPins);
            eventHistoryList.getItems().add(dpEvent);
            if (eventHistoryList.getItems().size() > MAX_HISTORY_BUFFER) eventHistoryList.getItems().remove(0);
            
            eventHistoryList.scrollTo(dpEvent);
        });
    }

    private void updateLabels(ArrayList<Label> wLabels, ArrayList<Label> rLabels, Map<String, PinStatus> pMap) {
        for(Label l : wLabels) {
            String id = l.getId();
            PinStatus p = pMap.get(id);

            l.setBackground(p.getState() ? wPinHIBackground : wPinLOBackground);
            l.setBorder(p.wasModified() ? changedBorder : null);
        }

        for(Label l : rLabels) {
            String id = l.getId();
            PinStatus p = pMap.get(id);

            l.setBackground(p.getState() ? rPinHIBackground : rPinLOBackground);
        }
    }

    private void loadDevicePicture(PALSpecs pSpecs, ImageView imgV) {
        String imgName = pSpecs.getDeviceName()+".png";
        URL imgRes = App.class.getResource("/graphics/"+imgName);

        if(imgRes == null) imgRes = App.class.getResource("/graphics/unknown.png"); // Fall back to a generic image

        imgV.setImage(new Image(imgRes.toString()));
        imgV.setPreserveRatio(true);
    }

    private void writePinState(PinStatus[] pins, boolean clock) {
        boolean[] pstate = new boolean[pins.length];
        for (int idx = 0; idx < pins.length; idx++) {
            pstate[idx] = pins[idx].getState();
            pins[idx].commitChange();
        }

        try {
            if(clock) App.phole.clock(pstate);
            else App.phole.write(pstate);
        } catch (PeepholeException e) {
            e.printStackTrace();
            Platform.exit();
        }
    }

    private void readPinState(PinStatus[] pins) {
        boolean[] pstate = null;
        
        try {
            pstate = App.phole.read();
        } catch (PeepholeException e) {
            e.printStackTrace();
            Platform.exit();
        }

        for(int idx = 0; idx < pstate.length; idx++) {
            rdPins[idx].setState(pstate[idx]);
        }
    }

    private void buildPinGrid(PALSpecs pSpecs, GridPane grid, PinStatus[] wp, PinStatus[] rp, ArrayList<Label> wLabels, ArrayList<Label> rLabels) {
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
            pinLabel.setBackground(wPinLOBackground);
            pinLabel.setFont(pinLabelFont);

            String id = "W"+p.pinNumber;
            
            pinLabel.setId(id);
            pinStatusMap.put(id, p);

            grid.add(pinLabel, column, row);
            wLabels.add(pinLabel);

            pinLabel.setOnMouseClicked(event -> {
                Label label = (Label)event.getSource();
                PinStatus pin = pinStatusMap.get(label.getId());

                pin.setState(!pin.getState());
                label.setBackground(pin.getState() ? wPinHIBackground : wPinLOBackground);

                label.setBorder(pin.wasModified() ? changedBorder : null);
            });
        }

         for(PinStatus p : rp) {
            int column = 2;
            int row = (pinsPerColumn - 1) - ((p.pinNumber - 1) % pinsPerColumn);

            Label pinLabel = new Label(p.pinName);
            pinLabel.setTextAlignment(TextAlignment.CENTER);

            pinLabel.setAlignment(Pos.CENTER);
            pinLabel.setMaxHeight(Double.MAX_VALUE);
            pinLabel.setMaxWidth(Double.MAX_VALUE);
            pinLabel.setBackground(rPinLOBackground);

            String id = "R"+p.pinNumber;
            pinLabel.setId(id);
            pinStatusMap.put(id, p);
            
            grid.add(pinLabel, column, row);
            rLabels.add(pinLabel);
        }

        Label vccLabel = new Label("VCC");
        Label gndLabel = new Label("GND");

        vccLabel.setAlignment(Pos.CENTER);
        vccLabel.setMaxHeight(Double.MAX_VALUE);
        vccLabel.setMaxWidth(Double.MAX_VALUE);
        vccLabel.setBackground(new Background(new BackgroundFill(colorWPinINACTIVE, new CornerRadii(5.0), new Insets(3.0))));
        grid.add(vccLabel, 1, 0);

        gndLabel.setAlignment(Pos.CENTER);
        gndLabel.setMaxHeight(Double.MAX_VALUE);
        gndLabel.setMaxWidth(Double.MAX_VALUE);
        gndLabel.setBackground(new Background(new BackgroundFill(colorWPinINACTIVE, new CornerRadii(5.0), new Insets(3.0))));
        grid.add(gndLabel, 0, pinsPerColumn-1);
    }

    static private PinStatus[] buildWritePinList(PALSpecs pSpecs) {
        int[] wPin = pSpecs.getWritePinNumbers();
        String[] labels = pSpecs.getLabels();

        PinStatus[] array = new PinStatus[wPin.length];

        for(int idx = 0; idx < array.length; idx++) {
            array[idx] = new PinStatus(wPin[idx], idx, labels[wPin[idx]-1]);
        }

        return array;
    }

    static private PinStatus[] buildReadPinList(PALSpecs pSpecs) {
        int[] rPin = pSpecs.getReadPinNumbers();
        String[] labels = pSpecs.getLabels();

        PinStatus[] array = new PinStatus[rPin.length];

        for(int idx = 0; idx < array.length; idx++) {
            array[idx] = new PinStatus(rPin[idx], idx, labels[rPin[idx]-1]);
        }

        return array;
    }
}
