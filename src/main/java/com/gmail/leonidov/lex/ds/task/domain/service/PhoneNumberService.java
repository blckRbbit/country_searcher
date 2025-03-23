package com.gmail.leonidov.lex.ds.task.domain.service;

import java.util.List;
import java.util.Arrays;
import java.io.IOException;
import java.util.regex.Pattern;
import org.apache.http.HttpHost;
import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import javax.annotation.PostConstruct;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;
import org.elasticsearch.client.RequestOptions;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.springframework.beans.factory.annotation.Value;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gmail.leonidov.lex.ds.task.domain.model.Country;
import com.gmail.leonidov.lex.ds.task.domain.util.Constants;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import com.gmail.leonidov.lex.ds.task.web.model.CountryResponse;
import com.gmail.leonidov.lex.ds.task.domain.exception.AppException;


@Slf4j
@Service
@RequiredArgsConstructor
public class PhoneNumberService {

    public RestHighLevelClient elasticsearchClient;
    private static final Pattern PHONE_NUMBER_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");

    @Value("${app.elasticsearch.host}")
    public String elkHost;

    @Value("${app.elasticsearch.port}")
    public int elkPort;

    @Value("${app.elasticsearch.scheme}")
    public String elkScheme;

    @PostConstruct
    private void init() {
        this.elasticsearchClient = new RestHighLevelClient(
                RestClient.builder(new HttpHost(elkHost, elkPort, elkScheme))
        );
    }

    public CountryResponse getCountryByPhoneNumber(String phoneNumber) {

        if (isEmptyPhoneNumber(phoneNumber)) {
            return createErrorResponse(-1, "Ошибка ввода", "Input Error");
        }

        validate(phoneNumber);
        phoneNumber = sanitizePhoneNumber(phoneNumber);
        log.info("Определяем страну по номеру телефона: {}", phoneNumber);

        String possibleCodes = generatePossibleCodes(phoneNumber);
        List<Country> countries;

        try {
            countries = searchCountriesByCodes(possibleCodes);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            return createErrorResponse(
                    -500, "Внутренняя ошибка сервиса", "Internal service error");
        }

        if (countries == null || countries.isEmpty() || countries.get(0) == null) {
            return createErrorResponse(-404, "Неизвестная страна", "Unknown Country");
        }

        return createCountryResponse(countries);
    }

    private boolean isEmptyPhoneNumber(String phoneNumber) {
        return phoneNumber == null || phoneNumber.isEmpty();
    }

    private String sanitizePhoneNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[^0-9]", "");
    }

    private String generatePossibleCodes(String phoneNumber) {
        StringBuilder codesBuilder = new StringBuilder();
        for (int i = 1; i <= 8; i++) {
            if (phoneNumber.length() >= i) {
                codesBuilder.append(phoneNumber, 0, i).append(",");
            }
        }
        return codesBuilder.length() > 0 ? codesBuilder.substring(0, codesBuilder.length() - 1) : "";
    }

    private List<Country> searchCountriesByCodes(String possibleCodes) throws IOException {
        SearchRequest searchRequest = new SearchRequest(Constants.INDICES);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

        for (String tempCode : possibleCodes.split(",")) {
            boolQuery.should(QueryBuilders.matchQuery("code", tempCode));
        }

        searchSourceBuilder.query(boolQuery);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = elasticsearchClient.search(searchRequest, RequestOptions.DEFAULT);
        SearchHit[] hits = searchResponse.getHits().getHits();

        return Arrays.stream(hits)
                .map(this::mapHitToCountry)
                .collect(Collectors.toList());
    }

    private Country mapHitToCountry(SearchHit hit) {

        try {
            return new ObjectMapper().readValue(hit.getSourceAsString(), Country.class);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new AppException(createErrorResponse(
                    -500, "Внутренняя ошибка сервиса", "Internal service error"));
        }
    }

    private CountryResponse createCountryResponse(List<Country> countries) {

        int maxLength = countries.stream()
                .mapToInt(country -> country.getCode().length())
                .max()
                .orElse(0);

        String countryNamesRu = countries.stream()
                .filter(country -> country.getCode().length() == maxLength)
                .map(Country::getNameRu)
                .collect(Collectors.joining(", "));

        String countryNamesEn = countries.stream()
                .filter(country -> country.getCode().length() == maxLength)
                .map(Country::getNameEn)
                .collect(Collectors.joining(", "));

        return CountryResponse.builder()
                .code(0)
                .countryRu(countryNamesRu)
                .countryEn(countryNamesEn)
                .build();
    }

    private CountryResponse createErrorResponse(int code, String countryRu, String countryEn) {
        return CountryResponse.builder()
                .code(code)
                .countryRu(countryRu)
                .countryEn(countryEn)
                .build();
    }

    private void validate(String phoneNumber) {
        if (!PHONE_NUMBER_PATTERN.matcher(phoneNumber).matches()) {
            throw new AppException(createErrorResponse(
                    -400, "Номер телефона должен содержать от 10 до 15 цифр, и может начинаться с '+'",
                    "The phone number must be between 10 and 15 digits long and may begin with '+'"));
        }
    }

}
