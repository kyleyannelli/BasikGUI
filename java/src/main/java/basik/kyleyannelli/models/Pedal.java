package basik.kyleyannelli.models;

public abstract class Pedal {
    private final String name;
    // position of pedal on effects chain (pedalboard)
    private int positionInBoard;
    // is the effect before the amplifier (pre-amp) or after (effects-loop)
    private boolean isPre;
    private boolean isOn;

    public Pedal(String name, int positionInBoard, boolean isPre, boolean isOn) {
        this.name = name;
        this.positionInBoard = positionInBoard;
        this.isPre = isPre;
        this.isOn = isOn;
    }

    public Pedal(String name, int positionInBoard, boolean isPre) {
        this.name = name;
        this.positionInBoard = positionInBoard;
        this.isPre = isPre;
        this.isOn = true;
    }

    public Pedal(String name, int positionInBoard) {
        this.name = name;
        this.positionInBoard = positionInBoard;
        this.isPre = true;
        this.isOn = true;
    }

    public String getName() {
        return name;
    }

    public boolean isOn() {
        return isOn;
    }

    public void setOn(boolean on) {
        isOn = on;
    }

    public int getPositionInBoard() {
        return positionInBoard;
    }

    public void setPositionInBoard(int positionInBoard) {
        this.positionInBoard = positionInBoard;
    }

    public boolean isPre() {
        return isPre;
    }

    public void setPre(boolean pre) {
        isPre = pre;
    }

    /**
     * Keeps a float parameter in range of 0.0 through 1.0. Very typical range for parameters.
     * @param parameter The parameter for the pedal. Example, mix, dry_level, feedback, and etc...
     * @return
     */
    public float keepInRange(float parameter) {
        if(parameter < 1.0001F && parameter > 0.0F) {
            return parameter;
        }
        // equal to with float... unlikely
        else if(parameter >= 1.0001F) {
            return 1.0F;
        }
        else {
            return 0.0F;
        }
    }
}
