# jwt-springboot-auth-starter
使用手册
1、实现接口UserService,具体用户信息的存储可以在这个接口的实现中定义，可以放在redis，数据库，或则集群中
2、有部分响应码，参考：ApiResponseCode
3、响应体信息参考：ApiResponse<T>
4、通过加入在api上面加入注解ValidateLogin  ，JwtInterceptor拦截器会拦截处理处理对应的会话。比如会话失效、会话不存在等。会在响应头里面返回 JWTAUTH，客户端在后续的请求中，尽量在header里面带上JWTAUTH，服务端这边会在每一次请求中刷新用户的会话时效。保证会话一直有效。
