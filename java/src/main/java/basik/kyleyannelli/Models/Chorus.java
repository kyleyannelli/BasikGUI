package basik.kyleyannelli.Models;

public class Chorus extends Pedal {
    /**
     * mix: Ratio of wet to dry.
     *      Mix of 1.0 is 100% wet, 0% dry. Mix of 0.0 is 0% Wet, 100%.
     * depth: The amount to vary the pitch.
     * rate_hz: The rate at which the pitch varies.
     */
    private float mix, depth, rateHz;
    public Chorus(int positionInBoard, boolean isPre, boolean isOn) {
        super("Chorus", positionInBoard, isPre, isOn);
    }

    public Chorus(int positionInBoard, boolean isPre) {
        super("Chorus", positionInBoard, isPre);
    }

    public Chorus(int positionInBoard) {
        super("Chorus", positionInBoard);
    }

    public float getMix() {
        return this.mix;
    }

    public void setMix(float mix) {
        this.mix = keepInRange(mix);
    }

    public float getRateHz() {
        return rateHz;
    }

    public void setRateHz(float rateHz) {
        this.rateHz = rateHz;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }
}
