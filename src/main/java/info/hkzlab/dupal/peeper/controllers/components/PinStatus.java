package info.hkzlab.dupal.peeper.controllers.components;

public class PinStatus {
   public final int pinNumber;
   public final int index;
   public final String pinName;

   private boolean status = false;
   private boolean previousStatus = false;

   public PinStatus(int pinNumber, int index, String pinName) {
        this.pinNumber = pinNumber;
        this.pinName = pinName;
        this.index = index;
   }

   public void setStatus(boolean status) {
       previousStatus = this.status;
       this.status = status;
   }

   public boolean getStatus() {
       return status;
   }

   public boolean wasModified() {
       return status != previousStatus;
   }

   public void commitChange() {
       previousStatus = status;
   }

   public String toString() {
       return pinName;
   }
}
