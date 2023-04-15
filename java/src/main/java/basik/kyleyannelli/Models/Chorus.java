package basik.kyleyannelli.Models;

import basik.kyleyannelli.Helpers.BasikAPI;
import basik.kyleyannelli.fx.Components.BasikChorusComponent;

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
        float rateHzNormalized = (rateHz)/(100F);
        chorusComponent.setKnob(chorusComponent.getRateKnobImage(), rateHzNormalized/15000F);
        chorusComponent.setChorus(this);
        return chorusComponent;
    }

    public static Chorus buildFromString(String s) {
        HashMap<String, String> hashedValues = Pedal.hashParamString(s);
        Chorus chorus = new Chorus(Integer.parseInt(hashedValues.get("position")));
        chorus.depth = Float.parseFloat(hashedValues.get("depth"));
        chorus.mix = Float.parseFloat(hashedValues.get("mix"));
        chorus.rateHz = (Float.parseFloat(hashedValues.get("rate_hz")))/100F;
        return chorus;
    }

    @Override
    public boolean equals(Object p) {
        if(p == this) return true;
        if(p instanceof Chorus) {
            return ((Chorus) p).mix == mix &&
                    ((Chorus) p).rateHz == rateHz &&
                    ((Chorus) p).depth == depth &&
                    ((Chorus) p).getPositionInBoard() == getPositionInBoard();
        }
        return false;
    }

    @Override
    public void sendAPIUpdate(int newPosition) {
        String updateString = "mix:" + mix + ",depth:" + depth + ",rate_hz:" + rateHz;
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
        String updateString = "KEEP:YES,";
        if(paramName.equalsIgnoreCase("mix")) {
            updateString += "mix:" + mix;
        } else if(paramName.equalsIgnoreCase("depth")) {
            updateString += "depth:" + depth;
        } else if(paramName.equalsIgnoreCase("rate")) {
            updateString += "rate_hz:" + rateHz;
        }
        try {
            BasikAPI.patchPedalRequest(getPositionInBoard(), getPositionInBoard(), updateString);
        } catch(Exception e) {
            System.out.println("FAILED TO UPDATE PEDAL...");
            e.printStackTrace();
            System.out.println("...FAILED TO UPDATE PEDAL");
        }
    }

    @Override
    public void updateParameterFromStringName(String paramName, float paramValue) {
        if(paramName.equalsIgnoreCase("mix")) {
            setMix(paramValue);
        } else if(paramName.equalsIgnoreCase("depth")) {
            setDepth(paramValue);
        } else if(paramName.equalsIgnoreCase("rate")) {
            setRateHz(paramValue);
        } else {
            throw new RuntimeException("Parameter Name: " + paramName + " is not valid for pedal " + this.getName());
        }
    }
}
