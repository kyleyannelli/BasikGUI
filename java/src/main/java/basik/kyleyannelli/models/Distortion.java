package basik.kyleyannelli.models;

public class Distortion extends Pedal {
    public Distortion(String name, int positionInBoard, boolean isPre, boolean isOn) {
        super(name, positionInBoard, isPre, isOn);
    }

    public Distortion(String name, int positionInBoard, boolean isPre) {
        super(name, positionInBoard, isPre);
    }

    public Distortion(String name, int positionInBoard) {
        super(name, positionInBoard);
    }
}
