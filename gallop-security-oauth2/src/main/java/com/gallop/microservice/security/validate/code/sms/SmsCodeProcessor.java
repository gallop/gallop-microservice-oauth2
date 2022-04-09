package com.gallop.microservice.security.validate.code.sms;

import com.gallop.microservice.security.validate.code.SmsCodeRepository;
import com.gallop.microservice.security.validate.code.ValidateCode;
import com.gallop.microservice.security.validate.code.ValidateCodeException;
import com.gallop.microservice.security.validate.code.ValidateCodeProcessor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.Map;

/**
 * author gallop
 * date 2022-04-06 14:34
 * Description:
 * Modified By:
 */
@Component
public class SmsCodeProcessor implements ValidateCodeProcessor {

    @Autowired
    private SmsCodeGenerator smsCodeGenerator;

    @Autowired
    private SmsCodeRepository smsCodeRepository;

    @Autowired
    private SmsCodeSender smsCodeSender;

    @Override
    public void create(ServletWebRequest request) throws Exception {
        ValidateCode validateCode = smsCodeGenerator.generate(request);
        smsCodeRepository.save(request,validateCode);
        String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), "mobile");
        smsCodeSender.send(mobile, validateCode.getCode());
    }

    @Override
    public void validate(ServletWebRequest request) {
        ValidateCode codeInSession = smsCodeRepository.get(request);
        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),"smsCode");
        } catch (ServletRequestBindingException e) {
            throw new ValidateCodeException("获取验证码的值失败");
        }

        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeException("短信验证码的值不能为空");
        }

        if (codeInSession == null) {
            throw new ValidateCodeException("短信验证码不存在");
        }

        if (codeInSession.isExpried()) {
            smsCodeRepository.remove(request);
            throw new ValidateCodeException("短信验证码已过期");
        }

        if (!StringUtils.equals(codeInSession.getCode(), codeInRequest)) {
            throw new ValidateCodeException("短信验证码不匹配");
        }

        smsCodeRepository.remove(request);
    }
}
