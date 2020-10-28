package info.hkzlab.dupal.peeper.controllers.components;

public class PinStatus {
   public final int pinNumber;
   public final String pinName;

   private boolean status = false;

   public PinStatus(int pinNumber, String pinName) {
        this.pinNumber = pinNumber;
        this.pinName = pinName;
   }

   public void setStatus(boolean status) {
       this.status = status;
   }

   public boolean getStatus() {
       return status;
   }
}
