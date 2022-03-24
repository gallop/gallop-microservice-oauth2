package com.gallop.microservice.user.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * author gallop
 * date 2022-03-10 16:44
 * Description:
 * Modified By:
 */
@Data
@ToString
@NoArgsConstructor
public class AuthToken {
    String access_token;//访问token就是短令牌，用户身份令牌
    String refresh_token;//刷新token
    String jwt_token;//jwt令牌
}
