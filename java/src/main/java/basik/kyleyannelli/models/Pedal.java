package basik.kyleyannelli.models;

public abstract class Pedal {
    private String name;
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
    }

    public Pedal(String name, int positionInBoard) {
        this.name = name;
        this.positionInBoard = positionInBoard;
    }
}
