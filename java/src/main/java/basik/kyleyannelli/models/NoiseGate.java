package basik.kyleyannelli.models;

public class NoiseGate extends Pedal {
    public NoiseGate(String name, int positionInBoard, boolean isPre, boolean isOn) {
        super(name, positionInBoard, isPre, isOn);
    }

    public NoiseGate(String name, int positionInBoard, boolean isPre) {
        super(name, positionInBoard, isPre);
    }

    public NoiseGate(String name, int positionInBoard) {
        super(name, positionInBoard);
    }
}
