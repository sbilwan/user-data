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

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * The service that fetches all the users listed to be living in the city.
 */
@Service
public class FetchAllUsersInCityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FetchAllUsersInCityService.class);

    private final RestTemplate restTemplate;

    private final String cityUsersEndpoint;

    @Autowired
    FetchAllUsersInCityService(final RestTemplate restTemplate, @Value("${bpdts-test-app.city-users.endpoint}") final String cityUsersEndpoint) {
        this.restTemplate = restTemplate;
        this.cityUsersEndpoint = cityUsersEndpoint;
    }

    /**
     * The method will hit the endpoint https://bpdts-test-app.herokuapp.com/city/{cityName}/users
     * to get all the users with city listed as cityname.
     * @param cityName name of the city that a user is listed in. ( London )
     * @return the List of all the users that are listed to live in the cityname
     * @throws BusinessException
     */
    public List<User> getAllUsersInCity(final String cityName) throws BusinessException {
        LOGGER.debug(" Going to fetch all users in City  {}", cityName);
        Optional<ResponseEntity<List<User>>> users = Optional.ofNullable(restTemplate.exchange(cityUsersEndpoint, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<User>>() {
        }, Collections.singletonMap("cityname", cityName)));
        return users.map(this::extractUsers).orElseThrow(() -> new RuntimeException("Empty Results from app"));
    }

    private List<User> extractUsers(final ResponseEntity<List<User>> responseEntity) {
        if (responseEntity.hasBody()) {
            responseEntity.getBody().sort(Comparator.comparingLong(User::getId));
            return responseEntity.getBody();
        }else {
            throw new RuntimeException("Empty Results from api ");
        }
    }

}
