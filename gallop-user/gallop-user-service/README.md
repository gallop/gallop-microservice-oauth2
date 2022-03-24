spring oauth2 资源服务器配置步骤：
1、/config/ResourceServerConfig 增加配置文件，并添加@EnableResourceServer 注解
2、请求此资源项目接口说，在http header 添加authorization 属性，值为 Bearer eyJhbGci...(值的格式：Bearer+空格+jwt)