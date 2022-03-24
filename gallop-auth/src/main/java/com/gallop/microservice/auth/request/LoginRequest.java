package com.gallop.microservice.auth.request;

import lombok.Data;

/**
 * author gallop
 * date 2022-03-10 17:10
 * Description:
 * Modified By:
 */
@Data
public class LoginRequest {
    String username;
    String password;
    String verifycode;
}
