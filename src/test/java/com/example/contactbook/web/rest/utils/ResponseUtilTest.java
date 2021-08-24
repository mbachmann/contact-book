package com.example.contactbook.web.rest.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

public class ResponseUtilTest {

    private static final String HEADER_NAME = "X-Test";
    private static final String HEADER_VALUE = "FooBar";

    private Optional<Integer> optionalYes;
    private Optional<Integer> optionalNo;
    private HttpHeaders headers;

    @BeforeEach
    public void setup() {
        optionalYes = Optional.of(42);
        optionalNo = Optional.empty();
        headers = new HttpHeaders();
        headers.add(HEADER_NAME, HEADER_VALUE);
    }

    @Test
    public void testOptionalYesWithoutHeaders() {
        ResponseEntity<Integer> response = ResponseUtil.wrapOrNotFound(optionalYes);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(42);
        assertThat(response.getHeaders()).isEmpty();
    }

    @Test
    public void testOptionalNoWithoutHeaders() {
        assertThatExceptionOfType(ResponseStatusException.class).isThrownBy(() -> ResponseUtil.wrapOrNotFound(optionalNo));
    }

    @Test
    public void testOptionalYesWithHeaders() {
        ResponseEntity<Integer> response = ResponseUtil.wrapOrNotFound(optionalYes, headers);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(42);
        assertThat(response.getHeaders()).hasSize(1);
        assertThat(response.getHeaders().get(HEADER_NAME)).hasSize(1);
        assertThat(response.getHeaders().get(HEADER_NAME).get(0)).isEqualTo(HEADER_VALUE);
    }

    @Test
    public void testOptionalNoWithHeaders() {
        assertThatExceptionOfType(ResponseStatusException.class).isThrownBy(() -> ResponseUtil.wrapOrNotFound(optionalNo, headers));
    }

}
