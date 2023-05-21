package com.project.secretdiary.dto.request.diary;

import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Getter
public class DiariesRequest {

    private Long id;

    @Min(value = 1, message = "limit은 1 이상 10 이하입니다.")
    @Max(value = 10, message = "limit은 1 이상 10 이하입니다.")
    private int size;

    public DiariesRequest(final Long id, final int size) {
        this.id = id;
        this.size = size;
    }
}
