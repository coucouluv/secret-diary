package com.project.secretdiary.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class MailRequest {

    private String address;
    private String title;
    private String message;

}