package basik.kyleyannelli.models;

public class Delay extends Pedal {
    public Delay(String name, int positionInBoard, boolean isPre, boolean isOn) {
        super(name, positionInBoard, isPre, isOn);
    }

    public Delay(String name, int positionInBoard, boolean isPre) {
        super(name, positionInBoard, isPre);
    }

    public Delay(String name, int positionInBoard) {
        super(name, positionInBoard);
    }
}
