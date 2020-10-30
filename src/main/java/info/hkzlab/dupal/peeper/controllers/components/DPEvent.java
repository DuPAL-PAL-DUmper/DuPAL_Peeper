package info.hkzlab.dupal.peeper.controllers.components;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DPEvent {
    public enum DPEventType {
        SET,
        CLOCK
    }

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd-MM-yy HH:mm:ss SSS");

    public final long timestamp;
    public final DPEventType type;
    public final int wData, rData;

   public DPEvent(DPEventType type, PinStatus[] write, PinStatus[] read) {
        timestamp = System.currentTimeMillis();
        this.type = type;

        int tempWrite = 0;
        int tempRead = 0;

        for(int idx = 0; idx < write.length; idx++) tempWrite |= (write[idx].getState() ? 1 : 0) << idx;
        for(int idx = 0; idx < read.length; idx++) tempRead |= (read[idx].getState() ? 1 : 0) << idx;

        wData = tempWrite;
        rData = tempRead;
   }

   public String toString() {
       StringBuffer strBuf = new StringBuffer();

       Date now = new Date(timestamp);

       strBuf.append(DATE_FORMAT.format(now) + " --> ");
       strBuf.append(type + " ");
       strBuf.append("W:" + String.format("%06X", wData) + " ");
       strBuf.append("R:" + String.format("%02X", rData) + " ");

       return strBuf.toString();
   }
}
