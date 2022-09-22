package com.example.supportportal.domaine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter @Setter
public class HttpResponse {
    private int httpStatusCode; //200 range: successfull// 400 range : error // 500 user error
    private HttpStatus httpStatus;
    private String reason;
    private String message;

}
