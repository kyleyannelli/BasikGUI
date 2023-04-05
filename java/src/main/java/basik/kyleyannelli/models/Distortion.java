package basik.kyleyannelli.models;

public class Distortion extends Pedal {
    /**
     * gainDb: The gain in decibels.
     */
    private float gainDb;
    public Distortion(int positionInBoard, boolean isPre, boolean isOn) {
        super("Distortion", positionInBoard, isPre, isOn);
    }

    public Distortion(int positionInBoard, boolean isPre) {
        super("Distortion", positionInBoard, isPre);
    }

    public Distortion(int positionInBoard) {
        super("Distortion", positionInBoard);
    }

    public float getGainDb() {
        return this.gainDb;
    }

    public void setGainDb(float gainDb) {
        this.gainDb = keepInRange(gainDb);
    }
}
