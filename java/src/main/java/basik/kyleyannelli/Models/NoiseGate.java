package basik.kyleyannelli.Models;

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

    @Override
    public Object viewize() {
        return null;
    }

    public static NoiseGate buildFromString(String s) {
        return null;
    }


}
