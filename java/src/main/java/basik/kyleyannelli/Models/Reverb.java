package basik.kyleyannelli.Models;

public class Reverb extends Pedal {
    /**
     * mix: Ratio of wet to dry.
     *      Mix of 1.0 is 100% wet, 0% dry. Mix of 0.0 is 0% Wet, 100%.
     * roomSize: Size of the "room".
     * damping: This is the tone knob.
     */
    private float mix, roomSize, damping;

    public Reverb(int positionInBoard, boolean isPre, boolean isOn) {
        super("Reverb", positionInBoard, isPre, isOn);
    }

    public Reverb(int positionInBoard, boolean isPre) {
        super("Reverb", positionInBoard, isPre);
    }

    public Reverb(int positionInBoard) {
        super("Reverb", positionInBoard);
    }

    public void setMix(float mix) {
        this.mix = keepInRange(mix);
    }

    public float getMix() {
        return this.mix;
    }

    public float getRoomSize() {
        return roomSize;
    }

    public void setRoomSize(float roomSize) {
        this.roomSize = keepInRange(roomSize);
    }

    public float getDamping() {
        return damping;
    }

    public void setDamping(float damping) {
        this.damping = keepInRange(damping);
    }
}
