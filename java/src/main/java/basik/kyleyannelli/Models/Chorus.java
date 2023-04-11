package basik.kyleyannelli.Models;

import basik.kyleyannelli.fx.Components.BasikChorusComponent;
import basik.kyleyannelli.fx.Components.BasikDistortionComponent;

import java.util.HashMap;

public class Chorus extends Pedal {
    /**
     * mix: Ratio of wet to dry.
     *      Mix of 1.0 is 100% wet, 0% dry. Mix of 0.0 is 0% Wet, 100%.
     * depth: The amount to vary the pitch.
     * rate_hz: The rate at which the pitch varies.
     */
    private float mix, depth, rateHz;
    public Chorus(int positionInBoard, boolean isPre, boolean isOn) {
        super("Chorus", positionInBoard, isPre, isOn);
    }

    public Chorus(int positionInBoard, boolean isPre) {
        super("Chorus", positionInBoard, isPre);
    }

    public Chorus(int positionInBoard) {
        super("Chorus", positionInBoard);
    }

    public float getMix() {
        return this.mix;
    }

    public void setMix(float mix) {
        this.mix = keepInRange(mix);
    }

    public float getRateHz() {
        return rateHz;
    }

    public void setRateHz(float rateHz) {
        this.rateHz = rateHz;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    @Override
    public Object viewize() {
        BasikChorusComponent chorusComponent = new BasikChorusComponent();
        chorusComponent.setKnob(chorusComponent.getDepthKnobImage(), depth);
        chorusComponent.setKnob(chorusComponent.getMixKnobImage(), mix);
        float rateHzNormalized = (rateHz - 100F)/(15000F - 100F);
        chorusComponent.setKnob(chorusComponent.getRateKnobImage(), rateHzNormalized/15000F);
        return chorusComponent;
    }

    public static Chorus buildFromString(String s) {
        HashMap<String, String> hashedValues = Pedal.hashParamString(s);
        Chorus chorus = new Chorus(Integer.parseInt(hashedValues.get("position")));
        chorus.depth = Float.parseFloat(hashedValues.get("depth"));
        chorus.mix = Float.parseFloat(hashedValues.get("mix"));
        chorus.rateHz = Float.parseFloat(hashedValues.get("rate_hz"));
        return chorus;
    }
}
