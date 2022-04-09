package com.gallop.microservice.security.properties;

/**
 * author gallop
 * date 2022-04-06 14:24
 * Description:
 * Modified By:
 */
public class ValidateCodeProperties {

    /**
     * 短信验证码配置
     */
    private SmsCodeProperties sms = new SmsCodeProperties();


    public SmsCodeProperties getSms() {
        return sms;
    }

    public void setSms(SmsCodeProperties sms) {
        this.sms = sms;
    }
}
