package basik.kyleyannelli.Models;

public class Delay extends Pedal {
    public Delay(int positionInBoard, boolean isPre, boolean isOn) {
        super("Delay", positionInBoard, isPre, isOn);
    }

    public Delay(int positionInBoard, boolean isPre) {
        super("Delay", positionInBoard, isPre);
    }

    public Delay(int positionInBoard) {
        super("Delay", positionInBoard);
    }
}
