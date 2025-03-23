package com.gmail.leonidov.lex.ds.task.domain.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Entity;
import javax.persistence.Column;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.gmail.leonidov.lex.ds.task.domain.util.Constants;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = Constants.INDICES)
public class CountryPgData {

    @Id
    @Column(
            name = "id",
            unique = true,
            nullable = false,
            updatable = false
    )
    private Integer id;

    @Column(name = "name_ru", nullable = false)
    private String nameRu;

    @Column(name = "name_en", nullable = false)
    private String nameEn;

    @Column(name = "phone_code", nullable = false)
    private Integer phoneCode;

}
