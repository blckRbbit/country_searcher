package com.gmail.leonidov.lex.ds.task.web.model;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryResponse {

    private Integer code;
    private String countryRu;
    private String countryEn;

    public String toString() {
        return String.format("{\"code\" : %d, \"countryRu\" : \"%s\", \"countryEn\" : \"%s\"}", code, countryRu, countryEn);
    }
}
