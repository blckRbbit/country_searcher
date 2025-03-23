package com.gmail.leonidov.lex.ds.task.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import com.gmail.leonidov.lex.ds.task.domain.util.Constants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.elasticsearch.annotations.Document;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = Constants.INDICES)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Country {

    @Id
    private String id;
    private String code;
    private String nameRu;
    private String nameEn;

}
