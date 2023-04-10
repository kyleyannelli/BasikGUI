package basik.kyleyannelli.Models;

public abstract class Amp {
    private final String name;
    private float gain;
    private int lowCutoff, highCutoff;

    public Amp(String name) {
        this.name = name;
        this.gain = 0.0F;
        this.lowCutoff = -1;
        this.highCutoff = -1;
    }

    public String getName() {
        return this.name;
    }

    public void setGain(float gain) {
        this.gain = keepInRange(gain);
    }

    /**
     * Keeps a float parameter in range of 0.0 through 1.0. Very typical range for parameters.
     * @param parameter The parameter for the pedal. Example, mix, dry_level, feedback, and etc...
     * @return
     */
    public float keepInRange(float parameter) {
        if(parameter < 9.9999F && parameter > 0.0F) {
            return parameter;
        }
        // equal to with float... unlikely
        else if(parameter >= 9.9999F) {
            return 1.0F;
        }
        else {
            return 0.0F;
        }
    }
}
