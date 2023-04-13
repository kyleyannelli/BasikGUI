package basik.kyleyannelli.Models;

import basik.kyleyannelli.fx.Components.BasikReverbComponent;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Reverb extends Pedal {
    /**
     * mix: Ratio of wet to dry.
     *      Mix of 1.0 is 100% wet, 0% dry. Mix of 0.0 is 0% Wet, 100%.
     * roomSize: Size of the "room".
     * damping: This is the tone knob.
     */
    private float mix, roomSize, damping;

    public Reverb(int positionInBoard, boolean isPre, boolean isOn) {
        super("Reverb", positionInBoard, isPre, isOn);
    }

    public Reverb(int positionInBoard, boolean isPre) {
        super("Reverb", positionInBoard, isPre);
    }

    public Reverb(int positionInBoard) {
        super("Reverb", positionInBoard);
    }

    public void setMix(float mix) {
        this.mix = keepInRange(mix);
    }

    public float getMix() {
        return this.mix;
    }

    public float getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(float roomSize) {
        this.roomSize = keepInRange(roomSize);
    }

    public float getDamping() {
        return damping;
    }

    public void setDamping(float damping) {
        this.damping = keepInRange(damping);
    }

    @Override
    public Object viewize() {
        BasikReverbComponent reverbComponent = new BasikReverbComponent();
        reverbComponent.setKnob(reverbComponent.getDampingKnob(), damping);
        reverbComponent.setKnob(reverbComponent.getWidthKnob(), 1.0F);
        reverbComponent.setKnob(reverbComponent.getMixKnob(), mix);
        reverbComponent.setKnob(reverbComponent.getSizeKnob(), roomSize);
        reverbComponent.setReverb(this);
        return reverbComponent;
    }

    @Override
    public boolean equals(Object p) {
        if(p == this) return true;
        if(p instanceof Reverb) {
            return ((Reverb) p).mix == mix &&
                    ((Reverb) p).damping == damping &&
                    ((Reverb) p).roomSize == roomSize &&
                    ((Reverb) p).getPositionInBoard() == getPositionInBoard();
        }
        return false;
    }

    public static Reverb buildFromString(String s) {
        HashMap<String, String> hashedValues = Pedal.hashParamString(s);
        Reverb reverb = new Reverb(Integer.parseInt(hashedValues.get("position")));
        float mix = 1.0F - Float.parseFloat(hashedValues.get("dry_level"));
        reverb.setMix(mix);
        reverb.setDamping(Float.parseFloat(hashedValues.get("damping")));
        reverb.setRoomSize(Float.parseFloat(hashedValues.get("room_size")));
        return reverb;
    }
}
