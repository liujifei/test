package sparksql;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class Sparksql {

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder().appName("Java Spark SQL basic example").master("local[*]")
                .config("spark.some.config.option", "some-value").getOrCreate();
        Dataset<Row> df = spark.read().json("examples/src/main/resources/people.json");

        // 显示DataFrame的内容
        df.show();
        // +----+-------+
        // | age| name|
        // +----+-------+
        // |null|Michael|
        // | 30| Andy|
        // | 19| Justin|
        // +----+-------+

        // 以树形格式打印schema
        df.printSchema();
        // root
        // |-- age: long (nullable = true)
        // |-- name: string (nullable = true)

        // 选择“name”列
        df.select("name").show();
        // +-------+
        // | name|
        // +-------+
        // |Michael|
        // | Andy|
        // | Justin|
        // +-------+

        // 选择所有数据, 但对“age”列执行+1操作
//        df.select(col("name"), col("age").plus(1)).show();
        // +-------+---------+
        // | name|(age + 1)|
        // +-------+---------+
        // |Michael| null|
        // | Andy| 31|
        // | Justin| 20|
        // +-------+---------+

        // 选择年龄“age”大于21的people
//        df.filter(col("age").gt(21)).show();
        // +---+----+
        // |age|name|
        // +---+----+
        // | 30|Andy|
        // +---+----+

        // 根据年龄“age”分组并计数
        df.groupBy("age").count().show();
        // +----+-----+
        // | age|count|
        // +----+-----+
        // | 19| 1|
        // |null| 1|
        // | 30| 1|
        // +----+-----+
        // 注册DataFrame为一个SQL的临时视图
        df.createOrReplaceTempView("people");

        Dataset<Row> sqlDF = spark.sql("SELECT * FROM people");
        sqlDF.show();
        // +----+-------+
        // | age| name|
        // +----+-------+
        // |null|Michael|
        // | 30| Andy|
        // | 19| Justin|
        // +----+-------+
        // 注册DataFrame为一个全局的SQL临时视图
//        df.createGlobalTempView("people");

        // 全局临时视图与系统保留数据库global_temp绑定
        spark.sql("SELECT * FROM global_temp.people").show();
        // +----+-------+
        // | age| name|
        // +----+-------+
        // |null|Michael|
        // | 30| Andy|
        // | 19| Justin|
        // +----+-------+

        // 全局临时视图是跨session的
        spark.newSession().sql("SELECT * FROM global_temp.people").show();
        // +----+-------+
        // | age| name|
        // +----+-------+
        // |null|Michael|
        // | 30| Andy|
        // | 19| Justin|
        // +----+-------+

    }
}
