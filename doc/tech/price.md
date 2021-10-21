


-- postgress
	
注意: money （货币）现在已经废弃，用 numeric 或 decimal 以及和 to_char 函数一起使用就可以取代它。

名字	存储空间	描述	范围
money	4 字节	货币金额	-21474836.48 到 +21474836.47

PostgreSQL支持货币（money）类型，在内存中占用8 位空间，表示范围-92233720368547758.08 to +92233720368547758.07


数据类型映射
https://www.enterprisedb.com/docs/jdbc_connector/latest/11_reference_jdbc_data_types/

https://www.postgresql.org/docs/14/datatype.html

https://www.postgresql.org/docs/14/datatype-money.html


leader1：浮点数计算有精确度问题，应该价格放大100用int保存，显示再缩小100。
leader2：我们空降了个leader，跟楼主相反，不许我们用int，非要用浮点数保留6位去解决精度问题。

leader1问题，可读性太差，容易生产BUG
leader1问题，decimal数据库存的字符串，计算有性能问题
解决方法，有没有一种int且较好的可读性，那就是money字段，无敌了


