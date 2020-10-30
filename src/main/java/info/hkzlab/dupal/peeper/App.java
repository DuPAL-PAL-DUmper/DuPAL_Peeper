package info.hkzlab.dupal.peeper;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.*;

import info.hkzlab.dupal.peeper.board.boardio.*;
import info.hkzlab.dupal.peeper.devices.*;
import info.hkzlab.dupal.peeper.peephole.Peephole;
import info.hkzlab.dupal.peeper.peephole.DuPALPeephole;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.stage.Stage;

public class App extends Application {
    public static volatile String[] palTypes = {
            // Simple devices
            PAL10L8Specs.PAL_TYPE, PAL12H6Specs.PAL_TYPE,

            // Asynchronous outputs
            PAL16L8Specs.PAL_TYPE, PAL20L8Specs.PAL_TYPE,

            // Registered devices
            PAL16R4Specs.PAL_TYPE, PAL16R6Specs.PAL_TYPE, PAL16R8Specs.PAL_TYPE, PAL20R4Specs.PAL_TYPE,
            PAL20R6Specs.PAL_TYPE, PAL20R8Specs.PAL_TYPE };

    private final static Logger logger = LoggerFactory.getLogger(App.class);

    private final static String version = App.class.getPackage().getImplementationVersion();
    private final static String AppName = "DuPAL Peeper";

    public static Peephole phole = null;

    public static void main(String[] args) throws Exception {
        logger.info(AppName + " " + version);
        launch(args);
    }

    @Override
    public void init() throws Exception {
        String jsonPath = getParameters().getNamed().get("json");

        if(jsonPath == null) { // We'll work connected to the board
            // Obtain PAL type
            String palType = getParameters().getNamed().get("pal");
            PALSpecs pspecs = null;
            if(palType == null) {
                printUsage();
                System.exit(-1);
            }

            try {
                Class<?> specsClass = Class.forName("info.hkzlab.dupal.peeper.devices.PAL" + palType.toUpperCase() + "Specs");
                pspecs = (PALSpecs) specsClass.getConstructor().newInstance(new Object[] {});
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                logger.error("Invalid PAL type selected.");
                System.exit(-1);
            }

            // Obtain serial port
            String serialPort = getParameters().getNamed().get("serial");
            if(serialPort == null) {
                printUsage();
                System.exit(-1);
            }

            // Create the connection to the board
            DuPALManager dpm = new DuPALManager(serialPort);
            DuPALCmdInterface dpci = new DuPALCmdInterface(dpm, pspecs);

            if (!dpm.enterRemoteMode()) {
                logger.error("Unable to put DuPAL board in REMOTE MODE!");
                System.exit(-1);
            }

            // Build the peephole
            phole = new DuPALPeephole(dpci);
        } else { // Initialize the simulated peephole

        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle(AppName + " " + version);
        primaryStage.setResizable(false);
        primaryStage.setHeight(620);
        primaryStage.setWidth(800);

        SplitPane box = FXMLLoader.load(App.class.getResource("/ui/peeperui.fxml"));
        Scene scene = new Scene(box);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override 
    public void stop() throws Exception {
        logger.info("stopping...");

        if(phole != null) phole.close();
    }

    private void printUsage() {
            StringBuffer supportedPALs = new StringBuffer();

            for (String palT : palTypes) {
                supportedPALs.append("\t" + palT + "\n");
            }

            logger.error("peeper --serial=<serial_port> --pal=<pal_type>\n"
                    + "peeper --json=/path/to/dump.json"
                    + "Where <pal_type> can be:\n" + supportedPALs.toString() + "\n");        
    }
}
