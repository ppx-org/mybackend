
# Example代码说明
#### Exmple.java
```
1. @Table("test_example")表名
2. @Conflict("example_name")或@Conflict({"res_name", "res_parent_id"}) 
3. extends MyPersistable<Integer>
@Override
public Integer getId() {
	return exampleId;
}
4. LocalDate 表示日期yyyy-MM-dd
5. LocalDateTime 表示日期+时间yyyy-MM-dd HH:mm:ss
```
 
###  ExampleController.java
```
1. @RequestMapping(ModuleConfig.TEST + "example") 路径 /模块名称/Controller名称/method名称


# Spring支持的request参数如下
page，第几页，从0开始，默认为第0页
size，每一页的大小，默认为20
sort，排序相关的信息，例如sort=firstname&sort=lastname,desc表示在按firstname正序排列基础上按lastname倒序排列
https://www.cnblogs.com/loveer/p/11303608.html

# 设置pageable中的pagesize的默认值
pageable.page.size=15 或 @PageableDefault(size=3)Pageable pageable

# sequence
select nextval('test_example_example_id_seq');
alter sequence test_example_example_id_seq restart with 300;
```

### ExampleRepo.java
```
// MyCriteria实例生成的查询sql会替换${c}
````

### ExampleServ.java
```
// 使用where开头: MyCriteria.where("e.example_id")
// 使用and开头: MyCriteria.empty().and("e.example_id")

```









