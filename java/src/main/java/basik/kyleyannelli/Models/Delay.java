package basik.kyleyannelli.Models;

import basik.kyleyannelli.Helpers.BasikAPI;
import basik.kyleyannelli.fx.Components.BasikDelayComponent;

import java.util.HashMap;

public class Delay extends Pedal {
    private float mix, feedback, delaySeconds;
    public Delay(int positionInBoard, boolean isPre, boolean isOn) {
        super("Delay", positionInBoard, isPre, isOn);
    }

    public Delay(int positionInBoard, boolean isPre) {
        super("Delay", positionInBoard, isPre);
    }

    public Delay(int positionInBoard) {
        super("Delay", positionInBoard);
    }

    @Override
    public Object viewize() {
        BasikDelayComponent delayComponent = new BasikDelayComponent();
        delayComponent.setDelay(this);
        delayComponent.setKnob(delayComponent.getFeedbackKnob(), feedback);
        delayComponent.setKnob(delayComponent.getTimeKnob(), delaySeconds);
        delayComponent.setKnob(delayComponent.getMixKnob(), mix);
        return delayComponent;
    }

    @Override
    public boolean equals(Object p) {
        if(p == this) return true;
        if(p instanceof Delay) return ((Delay) p).getPositionInBoard() == getPositionInBoard();
        return false;
    }

    public static Delay buildFromString(String s) {
        HashMap<String, String> hashedValues = Pedal.hashParamString(s);
        Delay delay = new Delay(Integer.parseInt(hashedValues.get("position")));
        delay.setDelaySeconds(Float.parseFloat(hashedValues.get("delay_seconds")));
        delay.setFeedback(Float.parseFloat(hashedValues.get("feedback")));
        delay.setMix(Float.parseFloat(hashedValues.get("mix")));
        return delay;
    }

    @Override
    public void sendAPIUpdate(int newPosition) {
        String updateString = "mix:" + mix + ",feedback:" + feedback + ",delay_seconds:" + delaySeconds;
        try {
            BasikAPI.patchPedalRequest(getPositionInBoard(), newPosition, updateString);
        } catch(Exception e) {
            System.out.println("FAILED TO UPDATE PEDAL...");
            e.printStackTrace();
            System.out.println("...FAILED TO UPDATE PEDAL");
        }
    }

    @Override
    public void updateParameterFromStringName(String paramName, float paramValue){
        if(paramName.equalsIgnoreCase("mix")) {
            setMix(paramValue);
        } else if(paramName.equalsIgnoreCase("feedback")) {
            setFeedback(paramValue);
        } else if(paramName.equalsIgnoreCase("time")) {
            setDelaySeconds(paramValue);
        } else {
            throw new RuntimeException("Parameter Name: " + paramName + " is not valid for pedal " + this.getName());
        }
    }

    @Override
    public void sendAPIUpdateSingleParameter(String paramName) {
        String updateString = "KEEP:YES,";
        if(paramName.equalsIgnoreCase("mix")) {
            updateString += "mix:" + mix;
        } else if(paramName.equalsIgnoreCase("feedback")) {
            updateString += "feedback:" + feedback;
        } else if(paramName.equalsIgnoreCase("time")) {
            updateString += "delay_seconds:" + delaySeconds;
        }
        try {
            BasikAPI.patchPedalRequest(getPositionInBoard(), getPositionInBoard(), updateString);
        } catch(Exception e) {
            System.out.println("FAILED TO UPDATE PEDAL...");
            e.printStackTrace();
            System.out.println("...FAILED TO UPDATE PEDAL");
        }
    }

    public float getMix() {
        return mix;
    }

    public void setMix(float mix) {
        this.mix = keepInRange(mix);
    }

    public float getFeedback() {
        return feedback;
    }

    public void setFeedback(float feedback) {
        this.feedback = keepInRange(feedback);
    }

    public float getDelaySeconds() {
        return delaySeconds;
    }

    public void setDelaySeconds(float delaySeconds) {
        this.delaySeconds = keepInRange(delaySeconds);
    }
}
