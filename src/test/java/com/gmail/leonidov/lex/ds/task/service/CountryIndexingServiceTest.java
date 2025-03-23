package com.gmail.leonidov.lex.ds.task.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.leonidov.lex.ds.task.domain.model.CountryPgData;
import com.gmail.leonidov.lex.ds.task.domain.repository.CountryPgDataRepository;
import com.gmail.leonidov.lex.ds.task.domain.repository.CountryRepository;
import com.gmail.leonidov.lex.ds.task.domain.service.CountryIndexingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


@SpringBootTest
@ActiveProfiles("test")
public class CountryIndexingServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @Mock
    private CountryPgDataRepository pgDataRepository;

    @InjectMocks
    private CountryIndexingService countryIndexingService;

    public CountryIndexingServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void tearDown() {
        pgDataRepository.deleteAll();
        countryRepository.deleteAll();
    }

    @Test
    public void testReindex_Success() {
        String result = countryIndexingService.reindex();
        assertEquals("Переиндексация успешно завершена", result);
    }

}
