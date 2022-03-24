keytool -genkeypair -alias mytest -keyalg RSA -keypass 123456 -keystore keystore.jks -storepass gallop123
keytool -genkeypair -alias khankey -keyalg RSA -keypass khan12 -keystore khan.keystore -storepass aaabbbccc
查询jks 密钥库的证书信息：
keytool -list -rfc -keystore keystore.jks

获取公钥方式一：通过通道符号把cert格式转成public key格式
keytool -list -rfc --keystore keystore.jks | openssl x509 -inform pem -pubkey

### 获取公钥方式二：jks 密钥库提取公钥的步骤
1、jks 密钥库文件转pfx文件：
keytool -v -importkeystore -srckeystore keystore.jks -srcstoretype jks -srcstorepass gallop123 -destkeystore abc.pfx -deststoretype pkcs12 -deststorepass gallop123 -destkeypass 123456

2、从pfx文件提取密钥对(如果pfx证书已加密，会提示输入密码)
openssl pkcs12 -in abc.pfx -nocerts -nodes -out idsrv4.key

3、从密钥对提取公钥
openssl rsa -in idsrv4.key -pubout -out idsrv4_pub.key

4、从密钥对提取私钥
openssl rsa -in  idsrv4.key -out idsrv4_pri.key
5、因为RSA算法使用的是 pkcs8 模式补足，需要对提取的私钥进一步处理得到最终私钥
openssl pkcs8 -topk8 -inform PEM -in idsrv4_pri.key -outform PEM -nocrypt

https://blog.csdn.net/qq_43692950/article/details/122566821

https://www.cnblogs.com/fengzheng/p/11724625.html