package basik.kyleyannelli.models;

public class NoiseGate extends Pedal {
    public NoiseGate(int positionInBoard, boolean isPre, boolean isOn) {
        super("NoiseGate", positionInBoard, isPre, isOn);
    }

    public NoiseGate(int positionInBoard, boolean isPre) {
        super("NoiseGate", positionInBoard, isPre);
    }

    public NoiseGate(int positionInBoard) {
        super("NoiseGate", positionInBoard);
    }
}
