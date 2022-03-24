package com.gallop.microservice.auth.service;

import com.gallop.microservice.user.domain.AuthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

/**
 * author gallop
 * date 2022-03-10 16:44
 * Description:
 * Modified By:
 */
@Service
public class AuthService {
    @Value("${auth.tokenValiditySeconds}")
    int tokenValiditySeconds;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    RestTemplate restTemplate;

    //用户认证申请令牌，将令牌存储到redis
    public AuthToken login(String username, String password, String clientId, String clientSecret) {

        //请求spring security申请令牌
        AuthToken authToken = this.applyToken(username, password, clientId, clientSecret);
        if(authToken == null){
            //ExceptionCast.cast(AuthCode.AUTH_LOGIN_APPLYTOKEN_FAIL);
        }
        return authToken;

    }

    //申请令牌
    private AuthToken applyToken(String username, String password, String clientId, String clientSecret){
        //从nacos中获取认证服务的一个实例的地址
        List<ServiceInstance> instances = discoveryClient.getInstances("gallop-auth");
        ServiceInstance instance = instances.get(0);

        //此地址就是http://ip:port
        URI uri = instance.getUri();
        //令牌申请的地址 http://localhost:40400/auth/oauth/token
        String authUrl = uri+ "/oauth/token";
        System.err.println("auth-uri="+authUrl);
        //定义header
        LinkedMultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        String httpBasic = getHttpBasic(clientId, clientSecret);
        header.add("Authorization",httpBasic);

        //定义body
        //client_id=client_2&client_secret=123456
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type","password");
        body.add("username",username);
        body.add("password",password);
        //body.add("client_id",clientId);
        //body.add("client_secret",clientSecret);

        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(body, header);
        //String url, HttpMethod method, @Nullable HttpEntity<?> requestEntity, Class<T> responseType, Object... uriVariables

        //设置restTemplate远程调用时候，对400和401不让报错，正确返回数据
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler(){
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if(response.getRawStatusCode()!=400 && response.getRawStatusCode()!=401){
                    super.handleError(response);
                }
            }
        });

        ResponseEntity<Map> exchange = restTemplate.exchange(authUrl, HttpMethod.POST, httpEntity, Map.class);

        //申请令牌信息
        Map bodyMap = exchange.getBody();
        if(bodyMap == null ||
                bodyMap.get("access_token") == null ||
                bodyMap.get("refresh_token") == null ||
                bodyMap.get("jti") == null){

            //解析spring security返回的错误信息
            if(bodyMap!=null && bodyMap.get("error_description")!=null){
                System.err.println("error:"+bodyMap.get("error_description"));
                String error_description = (String) bodyMap.get("error_description");
                if(error_description.indexOf("UserDetailsService returned null")>=0){
                    //ExceptionCast.cast(AuthCode.AUTH_ACCOUNT_NOTEXISTS);
                }else if(error_description.indexOf("坏的凭证")>=0){
                    //ExceptionCast.cast(AuthCode.AUTH_CREDENTIAL_ERROR);
                }
            }


            return null;
        }
        AuthToken authToken = new AuthToken();
        authToken.setAccess_token((String) bodyMap.get("jti"));//用户身份令牌
        authToken.setRefresh_token((String) bodyMap.get("refresh_token"));//刷新令牌
        authToken.setJwt_token((String) bodyMap.get("access_token"));//jwt令牌
        return authToken;
    }
    //获取httpbasic的串
    private String getHttpBasic(String clientId,String clientSecret){
        String string = clientId+":"+clientSecret;
        //将串进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic "+new String(encode);
    }

    public static void main(String[] args) {
        String clientId="client_001";
        String clientSecret = "123456";
        String string = clientId+":"+clientSecret;
        //将串进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        String result = "Basic "+new String(encode);
        System.err.println("result="+result);
    }

}
