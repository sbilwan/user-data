package com.userdata.api.service;

/**
 * Based on the formula and maths mentioned in the  https://rosettacode.org/wiki/Haversine_formula
 */
public final class GeoUtils {

    /** Radius of the average circumference */
    private static final double AVG_RADIUS_CIRCUMFERENCE = 6372.8; // In kilometers

    private static final double KM_TO_MILE_CONVERTER = 0.621371;

    private GeoUtils() {

    }

    /**
     * Haversine formula to calculate the great-circle distance between two geo-coordinates
     *
     * @param sourceLat the latitude of the city ( London )
     * @param sourceLon the Longitude of the oity ( London )
     * @param destinationLat the destination Latitude.
     * @param destinationLon the destination Longitude.
     * @return the distance in miles.
     */
    public static double haversineDistance(final double sourceLat, final double sourceLon,
                                   final double destinationLat, final double destinationLon) {
        double differenceLat = Math.toRadians(destinationLat - sourceLat);
        double differenceLng = Math.toRadians(destinationLon - sourceLon);
        double sourceLatInRadians = Math.toRadians(sourceLat);
        double destinationLatInRadians = Math.toRadians(destinationLat);

        double a = Math.pow(Math.sin(differenceLat / 2),2) + Math.pow(Math.sin(differenceLng / 2),2) * Math.cos(sourceLatInRadians) * Math.cos(destinationLatInRadians);
        double c = 2 * Math.asin(Math.sqrt(a));
        return AVG_RADIUS_CIRCUMFERENCE * c * KM_TO_MILE_CONVERTER;
    } 

}
