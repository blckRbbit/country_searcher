package com.gmail.leonidov.lex.ds.task.domain.repository;

import com.gmail.leonidov.lex.ds.task.domain.model.Country;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@EnableElasticsearchRepositories
public interface CountryRepository extends ElasticsearchRepository<Country, String> {
}
