package net.gahfy.muslimcompanion.models;

public class MuslimLocation {
    /** List of available modes for Muslim location */
    public static enum MODE {
        MODE_PROVIDER,
        MODE_MANUAL
    }

    /** The latitude of the location */
    private double locationLatitude;

    /** The longitude of the location */
    private double locationLongitude;

    /** The microtime when the location has been performed */
    private long locationTime;

    /** The mode (manual or from provider) of the location) */
    private MODE locationMode;

    /**
     * Instantiates a new MuslimLocation
     * @param locationLatitude the latitude of the location to set
     * @param locationLongitude the longitude of the location to set
     * @param locationTime the microtime when the location has been performed to set
     * @param locationMode the mode (manual or from provider) of the location
     */
    public MuslimLocation(double locationLatitude, double locationLongitude, long locationTime, MODE locationMode){
        this.setLocationLatitude(locationLatitude);
        this.setLocationLongitude(locationLongitude);
        this.setLocationTime(locationTime);
        this.setLocationMode(locationMode);
    }

    /**
     * Returns the latitude of the location.
     * @return the latitude of the location
     */
    public double getLocationLatitude() {
        return locationLatitude;
    }

    /**
     * Returns the longitude of the location.
     * @return the longitude of the location
     */
    public double getLocationLongitude() {
        return locationLongitude;
    }

    /**
     * Returns the microtime when the location has been performed.
     * @return the microtime when the location has been performed
     */
    public long getLocationTime() {
        return locationTime;
    }

    /**
     * Returns the mode (manual or from provider) of the location.
     * @return the mode (manual or from provider) of the location
     */
    public MODE getLocationMode() {
        return locationMode;
    }

    /**
     * Sets the latitude of the location.
     * @param locationLatitude the latitude of the location to set
     */
    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    /**
     * Sets the longitude of the location.
     * @param locationLongitude the longitude of the location to set
     */
    public void setLocationLongitude(double locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    /**
     * Sets the microtime when the location has been performed.
     * @param locationTime the microtime when the location has been performed to set
     */
    public void setLocationTime(long locationTime) {
        this.locationTime = locationTime;
    }

    /**
     * Sets the mode (manual or from provider) of the location.
     * @param locationMode the mode (manual or from provider) of the location
     */
    public void setLocationMode(MODE locationMode) {
        this.locationMode = locationMode;
    }
}
