package com.gallop.microservice.auth.config;

import com.gallop.microservice.auth.enhancer.JwtTokenEnhancer;
import com.gallop.microservice.auth.service.ClientsService;
import com.gallop.microservice.auth.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;

import javax.annotation.Resource;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

/**
 * author gallop
 * date 2021-06-25 10:50
 * Description:
 * Modified By:
 */
@Configuration
@EnableAuthorizationServer
public class CustomAuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
    @Resource
    private ClientsService clientsService;

    @Value("${auth.clientId}")
    private String clientId;
    @Value("${auth.clientSecret}")
    private String clientSecret;

    //jwt令牌转换器
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired
    MyUserDetailsService userDetailsService;
    @Autowired
    AuthenticationManager authenticationManagerBean;
    @Autowired
    TokenStore tokenStore;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private CustomUserAuthenticationConverter customUserAuthenticationConverter;

    //读取密钥的配置
    @Bean("keyProp")
    public KeyProperties keyProperties(){
        return new KeyProperties();
    }
    @Resource(name = "keyProp")
    private KeyProperties keyProperties;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
        //security.checkTokenAccess("isAuthenticated()");
        //security.tokenKeyAccess("isAuthenticated()");
        security
                // 开启/oauth/token_key验证端口无权限访问
                .tokenKeyAccess("permitAll()")
                // 开启/oauth/check_token验证端口认证权限访问
                .checkTokenAccess("isAuthenticated()"); // isAuthenticated()
        //super.configure(security);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //内存的方式配置client 账号，可以做临时测试用
        /*clients.inMemory()
                .withClient("client_001")
                .secret(passwordEncoder().encode("123456")) //passwordEncoder.encode("123456")
                .authorizedGrantTypes("authorization_code","refresh_token")
                .scopes("all","todo:read","todo:write")
                .redirectUris("http://www.baidu.com")
                .accessTokenValiditySeconds(86400)
                .refreshTokenValiditySeconds(604800);*/

        System.err.println(">>>>>>>>>>>>into the clients....");
        //内存的处理方式
        clients.inMemory()
                .withClient(clientId)
                .secret(passwordEncoder.encode(clientSecret))
                .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                .accessTokenValiditySeconds(3600)
                .scopes("all");

        //数据库的处理方式
        /*clients.withClientDetails(clientId -> {
            System.err.println(">>>>clientId:"+clientId);
            final ClientDetails clientDetails = this.clientsService.getByClientId(clientId);
            if (clientDetails == null) {
                throw new ClientRegistrationException(String.format("%s is not registered.", clientId));
            }
            return clientDetails;
        });*/
    }
    @Bean
    @Autowired
    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JwtTokenStore(jwtAccessTokenConverter);
    }
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();

        converter.setKeyPair(keyPair());
        //配置自定义的CustomUserAuthenticationConverter
        //DefaultAccessTokenConverter accessTokenConverter = (DefaultAccessTokenConverter) converter.getAccessTokenConverter();
        //accessTokenConverter.setUserTokenConverter(customUserAuthenticationConverter);
        return converter;
    }
    @Bean
    public JwtTokenEnhancer jwtTokenEnhancer() {
        return new JwtTokenEnhancer();
    }
    @Bean
    public KeyPair keyPair() {
        KeyPair keyPair = new KeyStoreKeyFactory
                (keyProperties.getKeyStore().getLocation(), keyProperties.getKeyStore().getSecret().toCharArray())
                .getKeyPair(keyProperties.getKeyStore().getAlias(),keyProperties.getKeyStore().getPassword().toCharArray());
        return keyPair;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //配置JWT的内容增强器
        TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
        List<TokenEnhancer> delegates = new ArrayList<>();
        delegates.add(jwtTokenEnhancer());
        delegates.add(jwtAccessTokenConverter);
        enhancerChain.setTokenEnhancers(delegates);
        System.err.println("--------------tokenStore="+tokenStore);
        for (TokenEnhancer tokenEnhancer:delegates){
            System.err.println("tokenEnhancer="+tokenEnhancer);
        }
        endpoints.accessTokenConverter(jwtAccessTokenConverter)
                .authenticationManager(authenticationManagerBean)//认证管理器 使用密码模式需要配置
                .tokenEnhancer(enhancerChain)
                .tokenStore(tokenStore)//令牌存储
                .reuseRefreshTokens(false)  //refresh_token是否重复使用
                .userDetailsService(userDetailsService); //用户信息service

                //.allowedTokenEndpointRequestMethods(HttpMethod.GET,
                //       HttpMethod.POST);
    }


    public static void main(String[] args) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        System.err.println("pwd:"+passwordEncoder.encode("123456"));
    }
}
