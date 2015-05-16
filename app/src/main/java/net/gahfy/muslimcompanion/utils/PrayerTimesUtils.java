package net.gahfy.muslimcompanion.utils;

public class PrayerTimesUtils {
    /** Time as the centuries ellapsed since January 1, 2000 at 12:00 GMT. */
    private double time;
    /** The latitude for which the prayer times should be calculated */
    private double latitude;
    /** The longitude for which the prayer times should be calculated */
    private double longitude;

    /**
     * Instantiates a new Prayer Time Utils
     * @param year the year of the date to calculate prayer times
     * @param month the month of the date to calculate prayer times
     * @param day the day of the date to calculate prayer times
     * @param latitude the latitude to calculate prayer times
     * @param longitude the longitude to calculate prayer times
     */
    public PrayerTimesUtils(int year, int month, int day, double latitude, double longitude){
        super();
        initMembers(year, month, day, latitude, longitude);
    }

    /**
     * Initializes the properties of the object
     * @param year the year of the date to calculate prayer times
     * @param month the month of the date to calculate prayer times
     * @param day the day of the date to calculate prayer times
     * @param latitude the latitude to calculate prayer times
     * @param longitude the longitude to calculate prayer times
     */
    public void initMembers(int year, int month, int day, double latitude, double longitude){

        double julianDay = DateUtils.dateToJulian(year, month, day);
        this.latitude    = latitude;
        this.longitude   = longitude;
        time             = (julianDay-2451545)/36525;
    }

    /**
     * Returns the time of Dhuhr (UTC time in decimal hour)
     * @return the time of Dhuhr (UTC time in decimal hour)
     */
    public double getDhuhr(){
        return 12.0 - (longitude/15.0) - (equationOfTime(time)/60.0);
    }

    /**
     * Returns the time of Sunrise (UTC time in decimal hour)
     * @return the time of Sunrise (UTC time in decimal hour)
     */
    public double getSunrise(){
        return getDhuhr() + getTimeBelowHorizonDifference(5.0/6.0);
    }

    /**
     * Returns the time of Maghrib (UTC time in decimal hour)
     * @return the time of Maghrib (UTC time in decimal hour)
     */
    public double getMaghrib(){
        return getDhuhr() + getTimeBelowHorizonDifference(5.0/6.0);
    }


    /**
     * Returns the time of Asr (UTC time in decimal hour)
     * @return the time of Asr (UTC time in decimal hour)
     */
    public double getAsr(double times){
        return getDhuhr() + getTimeShadowSizeDifference(times);
    }

    /**
     * Returns the time difference between dhuhr and the time when the sun is at the given angle
     * @param angle the angle for which to calculate the time
     * @return the time difference between dhuhr and the time when the sun is at the given angle
     */
    public double getTimeBelowHorizonDifference(double angle){
        double topOperand = (-Math.sin(Math.toRadians(angle)))-Math.sin(Math.toRadians(latitude))*Math.sin(Math.toRadians(sunDeclination(time)));
        double bottomOperand = Math.cos(Math.toRadians(latitude))*Math.cos(Math.toRadians(sunDeclination(time)));
        return (1.0/15.0)*Math.toDegrees(Math.acos(topOperand / bottomOperand));
    }

    /**
     * Returns the time difference between dhuhr when the shadow is the given times bigger than the object
     * @param times the number of times the size of the shadow should be the multiple of the size of an object
     * @return the time difference between dhuhr when the shadow is the given times bigger than the object
     */
    public double getTimeShadowSizeDifference(double times) {
        double sunAngleForAsr = -1.0 * Math.toDegrees(MathUtils.acot(times + Math.tan(Math.toRadians(Math.abs(latitude - sunDeclination(time))))));
        return getTimeBelowHorizonDifference(sunAngleForAsr);
    }

    /**
     * Calculate the difference between true solar time and mean. The "equation
     * of time" is a term accounting for changes in the time of solar noon for
     * a given location over the course of a year. Earth's elliptical orbit and
     * Kepler's law of equal areas in equal times are the culprits behind this
     * phenomenon. See the
     * <A HREF="http://www.analemma.com/Pages/framesPage.html">Analemma page</A>.
     * Below is a plot of the equation of time versus the day of the year.
     *
     * <P align="center"><img src="doc-files/EquationOfTime.png"></P>
     *
     * @param  t number of Julian centuries since J2000.
     * @return Equation of time in minutes of time.
     */
    private static double equationOfTime(final double t) {
        double eps = Math.toRadians(obliquityCorrected(t));
        double l0  = Math.toRadians(sunGeometricMeanLongitude(t));
        double m   = Math.toRadians(sunGeometricMeanAnomaly(t));
        double e   = eccentricityEarthOrbit(t);
        double y   = Math.tan(eps/2);
        y *= y;

        double sin2l0 = Math.sin(2 * l0);
        double cos2l0 = Math.cos(2 * l0);
        double sin4l0 = Math.sin(4 * l0);
        double sin1m  = Math.sin(m);
        double sin2m  = Math.sin(2 * m);

        double etime = y*sin2l0 - 2*e*sin1m + 4*e*y*sin1m*cos2l0
                - 0.5*y*y*sin4l0 - 1.25*e*e*sin2m;

        return Math.toDegrees(etime)*4.0;
    }

    /**
     * Calculate the Geometric Mean Longitude of the Sun.
     * This value is close to 0° at the spring equinox,
     * 90° at the summer solstice, 180° at the automne equinox
     * and 270° at the winter solstice.
     *
     * @param  t number of Julian centuries since J2000.
     * @return Geometric Mean Longitude of the Sun in degrees,
     *         in the range 0° (inclusive) to 360° (exclusive).
     */
    private static double sunGeometricMeanLongitude(final double t) {
        double L0 = 280.46646 + t*(36000.76983 + 0.0003032*t);
        L0 = L0 - 360*Math.floor(L0/360);
        return L0;
    }

    /**
     * Calculate the mean obliquity of the ecliptic.
     *
     * @param  t number of Julian centuries since J2000.
     * @return Mean obliquity in degrees.
     */
    private static double meanObliquityOfEcliptic(final double t) {
        final double seconds = 21.448 - t*(46.8150 + t*(0.00059 - t*(0.001813)));
        return 23.0 + (26.0 + (seconds/60.0))/60.0;
    }

    /**
     * Calculate the corrected obliquity of the ecliptic.
     *
     * @param  t number of Julian centuries since J2000.
     * @return Corrected obliquity in degrees.
     */
    private static double obliquityCorrected(final double t) {
        final double e0 = meanObliquityOfEcliptic(t);
        final double omega = Math.toRadians(125.04 - 1934.136*t);
        return e0 + 0.00256 * Math.cos(omega);
    }

    /**
     * Calculate the Geometric Mean Anomaly of the Sun.
     *
     * @param  t number of Julian centuries since J2000.
     * @return Geometric Mean Anomaly of the Sun in degrees.
     */
    private static double sunGeometricMeanAnomaly(final double t) {
        return 357.52911 + t * (35999.05029 - 0.0001537*t);
    }

    /**
     * Calculate the eccentricity of earth's orbit. This is the ratio
     * {@code (a-b)/a} where <var>a</var> is the semi-major axis
     * length and <var>b</var> is the semi-minor axis length.   Value
     * is 0 for a circular orbit.
     *
     * @param  t number of Julian centuries since J2000.
     * @return The unitless eccentricity.
     */
    private static double eccentricityEarthOrbit(final double t) {
        return 0.016708634 - t*(0.000042037 + 0.0000001267*t);
    }

    /**
     * Calculate the declination of the sun. Declination is analogous to latitude
     * on Earth's surface, and measures an angular displacement north or south
     * from the projection of Earth's equator on the celestial sphere to the
     * location of a celestial body.
     *
     * @param t number of Julian centuries since J2000.
     * @return Sun's declination in degrees.
     */
    private static double sunDeclination(final double t) {
        final double e = Math.toRadians(obliquityCorrected(t));
        final double b = Math.toRadians(sunApparentLongitude(t));
        final double sint = Math.sin(e) * Math.sin(b);
        final double theta = Math.asin(sint);
        return Math.toDegrees(theta);
    }

    /**
     * Calculate the true longitude of the sun. This the geometric mean
     * longitude plus a correction factor ("equation of center" for the
     * sun).
     *
     * @param  t number of Julian centuries since J2000.
     * @return Sun's true longitude in degrees.
     */
    private static double sunTrueLongitude(final double t) {
        return sunGeometricMeanLongitude(t) + sunEquationOfCenter(t);
    }

    /**
     * Calculate the apparent longitude of the sun.
     *
     * @param  t number of Julian centuries since J2000.
     * @return Sun's apparent longitude in degrees.
     */
    private static double sunApparentLongitude(final double t) {
        final double omega = Math.toRadians(125.04 - 1934.136 * t);
        return sunTrueLongitude(t) - 0.00569 - 0.00478 * Math.sin(omega);
    }

    /**
     * Calculate the equation of center for the sun. This value is a correction
     * to add to the geometric mean longitude in order to get the "true" longitude
     * of the sun.
     *
     * @param  t number of Julian centuries since J2000.
     * @return Equation of center in degrees.
     */
    private static double sunEquationOfCenter(final double t) {
        final double m = Math.toRadians(sunGeometricMeanAnomaly(t));
        return Math.sin(1*m) * (1.914602 - t*(0.004817 + 0.000014*t)) +
                Math.sin(2*m) * (0.019993 - t*(0.000101             )) +
                Math.sin(3*m) * (0.000289);
    }
}
