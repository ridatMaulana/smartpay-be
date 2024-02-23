package com.smartzie.payment.component;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response<D> {
    private Integer code = 0;
    private Object message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private D data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object validation;
    public void setResponseCode(ResponseCode responseCode){
        this.code = responseCode.getCode();
        this.message = responseCode.getMessage();
    }
}
