package basik.kyleyannelli.models;

public class Reverb extends Pedal {
    public Reverb(String name, int positionInBoard, boolean isPre, boolean isOn) {
        super(name, positionInBoard, isPre, isOn);
    }

    public Reverb(String name, int positionInBoard, boolean isPre) {
        super(name, positionInBoard, isPre);
    }

    public Reverb(String name, int positionInBoard) {
        super(name, positionInBoard);
    }
}
