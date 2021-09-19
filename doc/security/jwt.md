

# JWT
* 过期时间处理
 > 有效期1小时，快到期15分钟内重新生成


### 权限

1. jwt带role:[1,2,3,8]
2. roleId map uri_BitSet
3. uri get uri_id
4. 判断是否有权限
支持 /module/* /module/test/*

5. 缓存两个
5.1 修改个人角色时，jwt需要变更
5.2 修改角色权限时，uri_BitSet等需要变更

