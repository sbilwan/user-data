package com.userdata.api.controller;

import com.userdata.api.model.consumer.User;
import com.userdata.api.service.FetchAllUsersInAndNearCityService;
import com.userdata.api.service.FetchAllUsersInCityService;
import com.userdata.api.service.FetchAllUsersWithinCityDistanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

/**
 * The controller class that exposes the endpoint for fetching the users from the API.
 *
 */
@RestController
@RequestMapping(path = UsersController.PATH_MAPPING)
public class UsersController {

    public static final String PATH_MAPPING = "api.userdata/v1";

    private static final String API_OPERATIONS_ENDPOINT_BEGIN = "http://localhost:8080/api.userdata/v1/";

    private static final String FETCH_USER_ENDPOINT = "users/city/{cityname}/{distance}";

    private static final String API_OPERATION_JSON = "{\"fetch_users_in_london_or_50_miles_of_london\": \""+ API_OPERATIONS_ENDPOINT_BEGIN + FETCH_USER_ENDPOINT  + "\"}";

    private static final Logger LOGGER = LoggerFactory.getLogger(UsersController.class);

    private final FetchAllUsersInAndNearCityService fetchAllUsersInAndNearCityService;


    @Autowired
    UsersController(final FetchAllUsersInAndNearCityService fetchAllUsersInAndNearCityService) {
        this.fetchAllUsersInAndNearCityService = fetchAllUsersInAndNearCityService;
    }

    @GetMapping(value = "/",  produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<String> instructions() {
        return new ResponseEntity<>(API_OPERATION_JSON, HttpStatus.OK);
    }

    @GetMapping(value = FETCH_USER_ENDPOINT,  produces = APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<Set<User>> getUsersNearCity(@PathVariable("cityname") final String cityName,
                                                     @PathVariable("distance") final Long distance) {
        LOGGER.info(" Request received for fetching users in city {} and around the distance {}", cityName, distance);
        return new ResponseEntity<>(fetchAllUsersInAndNearCityService.getUsersInCityAndWithinDistance(cityName, distance), HttpStatus.OK);
    }




}
