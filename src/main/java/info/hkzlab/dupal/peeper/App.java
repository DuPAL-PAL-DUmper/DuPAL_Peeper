package info.hkzlab.dupal.peeper;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.*;

import info.hkzlab.dupal.peeper.board.boardio.*;
import info.hkzlab.dupal.peeper.devices.*;

public class App {
    public static volatile String[] palTypes = { 
                                                // Simple devices
                                                PAL10L8Specs.PAL_TYPE,
                                                PAL12H6Specs.PAL_TYPE,

                                                // Asynchronous outputs
                                                PAL16L8Specs.PAL_TYPE,
                                                PAL20L8Specs.PAL_TYPE,

                                                // Registered devices
                                                PAL16R4Specs.PAL_TYPE,
                                                PAL16R6Specs.PAL_TYPE,
                                                PAL16R8Specs.PAL_TYPE,
                                                PAL20R4Specs.PAL_TYPE,
                                                PAL20R6Specs.PAL_TYPE,
                                                PAL20R8Specs.PAL_TYPE 
                                            };

    private final static Logger logger = LoggerFactory.getLogger(App.class);

    private final static String version = App.class.getPackage().getImplementationVersion();

    private static String serialDevice = null;
    private static PALSpecs pspecs = null;

    public static void main(String[] args) throws Exception {
        logger.info("DuPAL Peeper " + version);

        if (args.length < 2) {
            StringBuffer supportedPALs = new StringBuffer();

            for(String palT : palTypes) {
                supportedPALs.append("\t"+palT+"\n");
            }

            logger.error("Wrong number of arguments passed.\n"
                    + "dupal_analyzer <serial_port> <pal_type>\n"
                    + "Where <pal_type> can be:\n" + supportedPALs.toString() + "\n");

            return;
        }

        parseArgs(args);

        DuPALManager dpm = new DuPALManager(serialDevice);
        DuPALCmdInterface dpci = new DuPALCmdInterface(dpm, pspecs);

        if (!dpm.enterRemoteMode()) {
            logger.error("Unable to put DuPAL board in REMOTE MODE!");
            System.exit(-1);
        } 

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                dpci.reset();
            }
        });

    }

    private static void parseArgs(String[] args) {
        serialDevice = args[0];

        try {
            Class<?> specsClass = Class.forName("info.hkzlab.dupal.peeper.devices.PAL" + args[1].toUpperCase() + "Specs");
            pspecs = (PALSpecs) specsClass.getConstructor().newInstance(new Object[]{});
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            logger.error("Invalid PAL type selected.");
            System.exit(-1);
        }
    }
}
