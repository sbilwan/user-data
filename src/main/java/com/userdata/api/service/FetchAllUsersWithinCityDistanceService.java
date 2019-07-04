package com.userdata.api.service;

import com.userdata.api.error.BusinessException;
import com.userdata.api.model.consumer.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FetchAllUsersWithinCityDistanceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FetchAllUsersWithinCityDistanceService.class);

    private final RestTemplate restTemplate;

    private final String usersEndpoint;

    /** Latitude of the city in request. ( London in this task ). Can be configured in application.yml */
    private final double cityLatitude;

    /** Longitude of the city in request. ( London in this task ). Can be configured in application.yml */
    private final double cityLongitude;

    @Autowired
    FetchAllUsersWithinCityDistanceService(final RestTemplate restTemplate,
                                           @Value("${bpdts-test-app.users.endpoint}") final String usersEndpoint,
                                           @Value("${city.latitude}") double cityLatitude,
                                           @Value("${city.longitude}") double cityLongitude) {
        this.restTemplate = restTemplate;
        this.usersEndpoint = usersEndpoint;
        this.cityLatitude = cityLatitude;
        this.cityLongitude = cityLongitude;
    }

    /**
     * The method will first find all the users by using endpoint ( https://bpdts-test-app.herokuapp.com/users  )
     * and then choose the users whose co-ordinates are within the distance miles of the city by using the
     * haversine formula in {@linkplain GeoUtils}.
     *
     * @param distance the miles from the city.
     * @return the List of filtered user.
     * @throws BusinessException
     */
    public List<User> getAllUsersWithinCityDistance(final Long distance) throws BusinessException {
        LOGGER.debug(" Going to fetch all the users ");
        Optional<ResponseEntity<List<User>>> users = Optional.ofNullable(restTemplate.exchange(usersEndpoint, HttpMethod.GET, null, new ParameterizedTypeReference<List<User>>() {
        }));
        final List<User> allUsers = users.map(this::extractUsers)
                .orElseThrow(() -> new BusinessException("No Response from API"));

        // Filter the users whose distance is either less than or equal to distance miles
        return allUsers.stream()
                .parallel()
                .filter(u -> GeoUtils.haversineDistance(cityLatitude, cityLongitude, u.getLatitude(), u.getLongitude()) <= distance)
                .sorted(Comparator.comparingLong(User::getId))
                .collect(Collectors.toList());

    }

    private List<User> extractUsers(final ResponseEntity<List<User>> responseEntity) {
       if (responseEntity.hasBody()) {
           return responseEntity.getBody();
       }else {
           throw new BusinessException("Empty Results from API");
       }
    }

}
