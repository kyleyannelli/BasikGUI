package basik.kyleyannelli.Models;

import basik.kyleyannelli.Helpers.BasikAPI;
import basik.kyleyannelli.fx.Components.BasikDistortionComponent;
import java.util.HashMap;

public class Distortion extends Pedal {
    public final float maxDb = 50.0F;
    /**
     * gainDb: The gain in decibels.
     */
    private float gainDb;
    private float gainPercent;
    public Distortion(int positionInBoard, boolean isPre, boolean isOn) {
        super("Distortion", positionInBoard, isPre, isOn);
    }

    public Distortion(int positionInBoard, boolean isPre) {
        super("Distortion", positionInBoard, isPre);
    }

    public Distortion(int positionInBoard) {
        super("Distortion", positionInBoard);
    }

    public float getGainDb() {
        return this.gainDb;
    }

    public  float getGainPercent() { return this.gainPercent; }

    public void setGainDb(float gainDb, boolean adjustGainPercent) {
        if(gainDb > maxDb) this.gainDb = maxDb;
        else if(gainDb < 0.0F) this.gainDb = 0.0F;
        else this.gainDb = gainDb;

        if(adjustGainPercent) setGainPercent(this.gainDb / maxDb, false);
    }

    public void setGainPercent(float gainPercent, boolean adjustGainDb) {
        this.gainPercent = keepInRange(gainPercent);
        if(adjustGainDb) setGainDb(this.gainPercent * maxDb, false);
    }

    @Override
    public Object viewize() {
        BasikDistortionComponent distortionComponent = new BasikDistortionComponent();
        distortionComponent.setKnob(distortionComponent.getDistKnobImage(), gainPercent);
        distortionComponent.setDistortion(this);
        return distortionComponent;
    }

    public static Distortion buildFromString(String s) {
        HashMap<String, String> hashedValues = Pedal.hashParamString(s);
        Distortion distortion = new Distortion(Integer.parseInt(hashedValues.get("position")));
        distortion.setGainDb(Float.parseFloat(hashedValues.get("drive_db")), true);
        return distortion;
    }

    @Override
    public boolean equals(Object p) {
        if(p == this) return true;
        if(p instanceof Distortion) {
            return ((Distortion) p).gainDb == gainDb &&
                    ((Distortion) p).getPositionInBoard() == getPositionInBoard();
        }
        return false;
    }

    @Override
    public void sendAPIUpdate(int newPosition) {
        String updateString = "drive_db:" + gainDb;
        try {
            BasikAPI.patchPedalRequest(getPositionInBoard(), newPosition, updateString);
        } catch(Exception e) {
            System.out.println("FAILED TO UPDATE PEDAL...");
            e.printStackTrace();
            System.out.println("...FAILED TO UPDATE PEDAL");
        }
    }

    @Override
    public void sendAPIUpdateSingleParameter(String paramName) {
        sendAPIUpdate(getPositionInBoard());
    }

    @Override
    public void updateParameterFromStringName(String paramName, float paramValue) {
        if(paramName.equalsIgnoreCase("gain")) {
            setGainPercent(paramValue, true);
        } else {
            throw new RuntimeException("Parameter Name: " + paramName + " is not valid for pedal " + this.getName());
        }
    }
}
