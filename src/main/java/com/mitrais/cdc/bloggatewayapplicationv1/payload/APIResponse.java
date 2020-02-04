package com.mitrais.cdc.bloggatewayapplicationv1.payload;

import com.mitrais.cdc.bloggatewayapplicationv1.entity.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class APIResponse {

    private boolean success;
    private String message;
    private Object data;

    public APIResponse(boolean success, String message, Object data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }
}
