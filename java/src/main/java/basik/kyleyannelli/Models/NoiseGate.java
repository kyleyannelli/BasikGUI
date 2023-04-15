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

    @Override
    public boolean equals(Object p) {
        return false;
    }

    public static NoiseGate buildFromString(String s) {
        return null;
    }

    @Override
    public void sendAPIUpdate(int newPosition) {

    }

    @Override
    public void sendAPIUpdateSingleParameter(String paramName) {

    }

    @Override
    public void updateParameterFromStringName(String paramName, float paramValue) {

    }
}
