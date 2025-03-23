package com.gmail.leonidov.lex.ds.task.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.gmail.leonidov.lex.ds.task.domain.model.CountryPgData;

public interface CountryPgDataRepository extends JpaRepository<CountryPgData, Integer> {
}
