package com.gallop.microservice.security.validate.code.sms;

/**
 * author gallop
 * date 2022-04-06 14:44
 * Description:
 * Modified By:
 */
public interface SmsCodeSender {
    /**
     * @param mobile
     * @param code
     */
    void send(String mobile, String code);
}
