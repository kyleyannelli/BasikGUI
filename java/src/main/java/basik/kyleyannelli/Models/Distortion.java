package basik.kyleyannelli.Models;

import basik.kyleyannelli.fx.Components.BasikDistortionComponent;
import basik.kyleyannelli.fx.Components.BasikReverbComponent;

import java.util.HashMap;

public class Distortion extends Pedal {
    /**
     * gainDb: The gain in decibels.
     */
    private float gainDb;
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

    public void setGainDb(float gainDb) {
        this.gainDb = keepInRange(gainDb);
    }

    @Override
    public Object viewize() {
        BasikDistortionComponent distortionComponent = new BasikDistortionComponent();
        distortionComponent.setKnob(distortionComponent.getDistKnobImage(), (gainDb / 100.0F));
        distortionComponent.setDistortion(this);
        return distortionComponent;
    }

    public static Distortion buildFromString(String s) {
        HashMap<String, String> hashedValues = Pedal.hashParamString(s);
        Distortion distortion = new Distortion(Integer.parseInt(hashedValues.get("position")));
        distortion.setGainDb(Float.parseFloat(hashedValues.get("drive_db")));
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
}
