package com.userdata.api.service;

import com.userdata.api.model.consumer.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

/**
 * The service that fetch the users that are listed to be living in the city (London)  and also fetch the users living within a distance ( in miles )
 * of the city ( London ).
 */
@Service
public class FetchAllUsersInAndNearCityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FetchAllUsersInAndNearCityService.class);

    private final FetchAllUsersWithinCityDistanceService fetchAllUsersWithinCityDistanceService;

    private final FetchAllUsersInCityService fetchAllUsersInCityService;

    @Autowired
    FetchAllUsersInAndNearCityService(final FetchAllUsersWithinCityDistanceService fetchAllUsersWithinCityDistanceService,
                                      final FetchAllUsersInCityService fetchAllUsersInCityService) {
        this.fetchAllUsersWithinCityDistanceService = fetchAllUsersWithinCityDistanceService;
        this.fetchAllUsersInCityService = fetchAllUsersInCityService;
    }

    public Set<User> getUsersInCityAndWithinDistance(final String cityName, final Long distance) {
        final Set<User> mergedUsers = new TreeSet<>();
        final long count = Stream.of(allUsersInCity(cityName), allUsersWithinDistanceOfCity(distance))
                .map(CompletableFuture::join)
                .map(mergedUsers::addAll)
                .count();
        LOGGER.info("Number of users after merging is {}" , count);
        return mergedUsers;
    }


    /**
     * The method creates a CompletableFuture with the task of getting getting all the users in a city
     * by hitting endpoint ( https://bpdts-test-app.herokuapp.com/city/London/users ) and getting the response.
     *
     * @param cityName the city name ( in the given data set ( london)
     * @return a CompletableFuture that hits the endpoint.
     */
    private CompletableFuture<List<User>> allUsersInCity(final String cityName) {
        return CompletableFuture.supplyAsync(() -> fetchAllUsersInCityService.getAllUsersInCity(cityName));
    }

    /**
     * The method returns the CompletableFuture with the task of getting all the users in the system
     * by hitting the endpoint ( https://bpdts-test-app.herokuapp.com/users ) and filtering the coordinates that are
     * within the (distance) miles of the city.
     *
     * @param distance the distance in miles that user's coordinates should be  from the city ( London)
     * @return CompletableFuture that hits the endpoint.
     */
    private CompletableFuture<List<User>> allUsersWithinDistanceOfCity(final Long distance) {
        return CompletableFuture.supplyAsync(() -> fetchAllUsersWithinCityDistanceService.getAllUsersWithinCityDistance(distance));
    }

}
