package com.example.system.controller;

import com.example.system.model.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class UserControllerTest {
    private static final String URL_PORT = "http://localhost:8080/";
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        restTemplate = new RestTemplate();
    }

    @Test
    @Timeout(value = 4000, unit = TimeUnit.MILLISECONDS)
    void testGetAllUsers() {
        ResponseEntity<List<User>> userResponse = restTemplate.exchange(URL_PORT + "users", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                });
        assertListOfUsers(userResponse);
    }


    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetUserById() {
        User user = restTemplate.getForObject(URL_PORT + "user/1", User.class);

        SoftAssertions.assertSoftly(softly -> {
                    softly.assertThat(user).isNotNull();
                    softly.assertThat(user).isInstanceOfAny(User.class);
                    softly.assertThat(user.getName()).isNotNull().isInstanceOfAny(String.class);
                }
        );
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetNotExistingUserById() {
        HttpClientErrorException exception = assertThrows(HttpClientErrorException.class, () -> restTemplate.getForObject(URL_PORT + "user/111111", User.class));

        SoftAssertions.assertSoftly(softly -> {
                    softly.assertThat(exception.getRawStatusCode()).isEqualTo(404);
                    softly.assertThat(exception.getMessage().contains("does not exist")).isTrue();
                }
        );
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetUsersSortedByAmountOfOrders() {
        ResponseEntity<Map<Object, BigInteger>> userResponse = restTemplate.exchange(
                URL_PORT + "usersSortedByAmountOfOrders", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                });

        Map<Object, BigInteger> body = userResponse.getBody();

        BigInteger firstElement = body.values().stream().findFirst().get();
        BigInteger maxElement = body.values().stream().max(BigInteger::compareTo).get();

        assertThat(maxElement).isEqualTo(firstElement);
    }

    @Test
    @Timeout(value = 3000, unit = TimeUnit.MILLISECONDS)
    void testGetAllUsersWithoutOrders() {

        ResponseEntity<List<User>> userResponse = restTemplate.exchange(URL_PORT + "usersWithoutOrders", HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {
                });
        assertListOfUsers(userResponse);
    }

    private static void assertListOfUsers(final ResponseEntity<List<User>> userResponse) {
        List<User> users = userResponse.getBody();

        SoftAssertions.assertSoftly(softly -> {
                    softly.assertThat(userResponse.getStatusCode().is2xxSuccessful()).isTrue();
                    softly.assertThat(users).isNotNull();
                    softly.assertThat(users).isNotEmpty();
                    softly.assertThat(users.get(0)).isExactlyInstanceOf(User.class);
                    softly.assertThat(users.get(0).getName()).isNotNull().isExactlyInstanceOf(String.class);
                }
        );
    }
}
