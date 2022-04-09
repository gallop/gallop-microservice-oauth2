package com.gallop.microservice.security.validate.code;

import com.gallop.microservice.security.validate.code.sms.SmsCodeProcessor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.ServletWebRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * author gallop
 * date 2022-04-06 15:16
 * Description:
 * Modified By:
 */
@RestController
public class SmsCodeController {
    @Resource
    private SmsCodeProcessor smsCodeProcessor;

    @GetMapping("/code/sms")
    public void createCode(HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        smsCodeProcessor.create(new ServletWebRequest(request, response));
    }
}
