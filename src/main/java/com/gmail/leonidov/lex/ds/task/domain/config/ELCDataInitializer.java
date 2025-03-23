package com.gmail.leonidov.lex.ds.task.domain.config;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import org.springframework.transaction.annotation.Transactional;
import com.gmail.leonidov.lex.ds.task.domain.service.CountryIndexingService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ELCDataInitializer implements CommandLineRunner {

    private final CountryIndexingService indexingService;

    @Override
    @Transactional(readOnly = true)
    public void run(String... args) {
        indexingService.reindex();
    }

}
