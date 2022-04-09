package com.gallop.microservice.security.validate.code.impl;

import com.gallop.microservice.security.validate.code.SmsCodeRepository;
import com.gallop.microservice.security.validate.code.ValidateCode;
import com.gallop.microservice.security.validate.code.ValidateCodeException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.concurrent.TimeUnit;

/**
 * author gallop
 * date 2022-04-06 15:27
 * Description:
 * Modified By:
 */
@Component
public class RedisSmsCodeRepository implements SmsCodeRepository {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(ServletWebRequest request, ValidateCode code) {
        redisTemplate.opsForValue().set(buildKey(request), code, 10, TimeUnit.MINUTES);
    }

    @Override
    public ValidateCode get(ServletWebRequest request) {
        Object value = redisTemplate.opsForValue().get(buildKey(request));
        if (value == null) {
            return null;
        }
        return (ValidateCode) value;
    }

    @Override
    public void remove(ServletWebRequest request) {
        redisTemplate.delete(buildKey(request));
    }

    private String buildKey(ServletWebRequest request) {
       /* String deviceId = request.getHeader("deviceId");
        if (StringUtils.isBlank(deviceId)) {
            throw new ValidateCodeException("请在请求头中携带deviceId参数");
        }*/

        String mobile = null;
        try {
            mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), "mobile");
        } catch (ServletRequestBindingException e) {
            e.printStackTrace();
        }
        if (StringUtils.isBlank(mobile)) {
            throw new ValidateCodeException("请携带mobile参数");
        }
        return "code:sms:" + mobile;
    }
}
