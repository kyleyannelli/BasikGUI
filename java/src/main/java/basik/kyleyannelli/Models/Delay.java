package basik.kyleyannelli.Models;

import basik.kyleyannelli.fx.Components.BasikChorusComponent;
import basik.kyleyannelli.fx.Components.BasikDelayComponent;

import java.util.HashMap;

public class Delay extends Pedal {
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
        return new Delay(Integer.parseInt(hashedValues.get("position")));
    }


}
