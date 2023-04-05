package basik.kyleyannelli.models;

public class Chorus extends Pedal {
    public Chorus(String name, int positionInBoard, boolean isPre, boolean isOn) {
        super(name, positionInBoard, isPre, isOn);
    }

    public Chorus(String name, int positionInBoard, boolean isPre) {
        super(name, positionInBoard, isPre);
    }

    public Chorus(String name, int positionInBoard) {
        super(name, positionInBoard);
    }
}
