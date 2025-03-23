package com.gmail.leonidov.lex.ds.task.domain.service;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.gmail.leonidov.lex.ds.task.domain.model.Country;
import org.springframework.transaction.annotation.Transactional;
import com.gmail.leonidov.lex.ds.task.domain.model.CountryPgData;
import com.gmail.leonidov.lex.ds.task.domain.repository.CountryRepository;
import com.gmail.leonidov.lex.ds.task.domain.repository.CountryPgDataRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryIndexingService {

    private final CountryRepository countryRepository;
    private final CountryPgDataRepository pgDataRepository;

    @Transactional(readOnly = true)
    public String reindex() {

        log.info("Переиндексация начата...");
        try {

            List<CountryPgData> pgCountryData = pgDataRepository.findAll();

            log.info("Объектов для индексации: {}", pgCountryData.size());

            countryRepository.saveAll(
                    pgCountryData.stream()
                            .map(
                                    country -> Country.builder()
                                            .id(String.valueOf(country.getId()))
                                            .code(String.valueOf(country.getPhoneCode()))
                                            .nameRu(country.getNameRu())
                                            .nameEn(country.getNameEn())
                                            .build()
                            )
                            .collect(Collectors.toList())
            );

            log.info("Переиндексировано объектов: {}", countryRepository.count());

        } catch (Exception e) {
            log.warn("Ошибка переиндексации данных", e);
            return "Ошибка переиндексации данных";
        }
        return "Переиндексация успешно завершена";
    }

}
