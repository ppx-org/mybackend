

# JWT Bearer 授权
首先jwt-bearer认证请求也要携带grant_type参数来表明使用的授权模式：
grant_type=urn:ietf:params:oauth:grant-type:jwt-bearer

The content of the header should look like the following:
Authorization: Bearer <token>




