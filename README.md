本项目计划做一个微服务项目可用的基础项目框框，初步设想使用的技术如下： 
1、nacos 作为服务注册发现与配置中心； 
2、使用spring cloud gateway作为网关； 
3、使用spring oauth2作为服务认证中心和资源认证服务器； 
4、使用dubbo 作为服务间的rpc调用； 
5、使用OpenFeign 作为服务间的rest 调用； 
6、使用sentinel作为流控防护组件； 

2022-03-24: 
项目框架还没完成，目前的进度： 
1、完成dubbo 注册到nacoa的基本配置，包括包依赖和配置文件的配置信息； 
2、完成spring oauth2 的密码模式的认证（但由于微服务的各模块基本是自有模块，不涉及第三方， 
3、完成dubbo配合nacos使用，并在微服务模块间调用； 
这样导致前端提交用户登入，gallop-auth模块后端还要再次组装用户信息，用restTemplate 进行oauth
2密码模式的登入操作，登入有异常也要再次包装一次返回给前端，这种方式似乎不太合理。)

关于用户登入认证的处理方式： 
计划重新做个模块，整合spring security 和spring oauth2，先使用security 进行登入操作，
登入成功后，在AuthenticationSuccessHandler 里面，结合oauth2的密码模式的token生成方式，生成jwt 返回；
这样其他微服务模块也可以直接使用oauth2 做为资源服务器使用；

目前还没完成的模块： 
1、gateway 模块还没开发配置； 
2、OpenFeign 还没开发配置； 
3、sentinel 结合OpenFeign 做限流操作还没开发配置； 