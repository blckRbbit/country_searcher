package com.gmail.leonidov.lex.ds.task.service;

import org.mockito.InjectMocks;
import org.apache.http.HttpHost;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.quality.Strictness;
import org.junit.jupiter.api.BeforeEach;
import org.elasticsearch.client.RestClient;
import org.mockito.junit.jupiter.MockitoSettings;
import org.elasticsearch.client.RestHighLevelClient;
import com.gmail.leonidov.lex.ds.task.web.model.CountryResponse;
import com.gmail.leonidov.lex.ds.task.domain.exception.AppException;
import com.gmail.leonidov.lex.ds.task.domain.service.PhoneNumberService;

import static org.junit.jupiter.api.Assertions.*;

@MockitoSettings(strictness = Strictness.LENIENT)
public class PhoneNumberServiceTest {

    @InjectMocks
    private PhoneNumberService phoneNumberService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        phoneNumberService.elkHost = "localhost";
        phoneNumberService.elkPort = 9200;
        phoneNumberService.elkScheme = "http";
        phoneNumberService.elasticsearchClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost("localhost", 9200, "http"))
        );

    }

    @Test
    public void testGetCountryByPhoneNumber_ValidPhoneNumber() {
        String phoneNumber = "+79513216549";
        String kzPhoneNumber = "+77051321654";
        String bahamasPhoneNumber = "+12423222931";
        String usPhoneNumber = "11165384765";

        CountryResponse response = phoneNumberService.getCountryByPhoneNumber(phoneNumber);
        CountryResponse kzResponse = phoneNumberService.getCountryByPhoneNumber(kzPhoneNumber);
        CountryResponse bahamasResponse = phoneNumberService.getCountryByPhoneNumber(bahamasPhoneNumber);
        CountryResponse usResponse = phoneNumberService.getCountryByPhoneNumber(usPhoneNumber);

        assertNotNull(response);
        assertEquals(0, response.getCode());
        assertEquals("Россия", response.getCountryRu());
        assertEquals("Russia", response.getCountryEn());

        assertEquals(0, kzResponse.getCode());
        assertEquals("Казахстан", kzResponse.getCountryRu());
        assertEquals("Kazakhstan", kzResponse.getCountryEn());

        assertEquals(0, bahamasResponse.getCode());
        assertEquals("Багамы", bahamasResponse.getCountryRu());
        assertEquals("Bahamas", bahamasResponse.getCountryEn());

        assertEquals(0, usResponse.getCode());
        assertEquals("Канада, Соединенные Штаты", usResponse.getCountryRu());
        assertEquals("Canada, United States", usResponse.getCountryEn());
    }

    @Test
    public void testGetCountryByPhoneNumber_EmptyPhoneNumber() {
        CountryResponse response = phoneNumberService.getCountryByPhoneNumber("");
        assertEquals(-1, response.getCode());
        assertEquals("Ошибка ввода", response.getCountryRu());
    }

    @Test
    public void testGetCountryByPhoneNumber_InvalidPhoneNumber() {
        assertThrows(AppException.class, () -> phoneNumberService.getCountryByPhoneNumber("invalid"));
    }

    @Test
    public void testGetCountryByPhoneNumber_NoCountriesFound() {
        String phoneNumber = "08234567890";
        CountryResponse response = phoneNumberService.getCountryByPhoneNumber(phoneNumber);
        assertEquals("Неизвестная страна", response.getCountryRu());
        assertEquals(-404, response.getCode());
    }

}