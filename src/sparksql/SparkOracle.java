package sparksql;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

public class SparkOracle {

    public static void main(String[] args) {
        SparkSession spark = SparkSession.builder().appName("Java Spark SQL from oracle").master("local[*]")
                .config("spark.some.config.option", "some-value").getOrCreate();

        Dataset<Row> jdbcDF = spark.read().format("jdbc").option("url", "jdbc:oracle:thin:@172.22.5.66:1526:dcappdb1")
                .option("dbtable", "ODS_TEST1017001").option("user", "dataClearing").option("password", "dataC_learing")
                .option("driver", "oracle.jdbc.driver.OracleDriver").load();
        jdbcDF.select("USERID", "USERNAME").orderBy("USERID").write().parquet("hdfs://172.22.5.249/tmp/test.parquet");
        Dataset<Row> parquetFileDF = spark.read().parquet("hdfs://172.22.5.249/tmp/test.parquet");
        parquetFileDF.createOrReplaceTempView("parquetFile");
        String sql = "SELECT count(1) c FROM ODS_TEST1017001";
        System.out.println("sql:" + sql);
        Dataset<Row> namesDF = spark.sql(sql);
        namesDF.show();
    }
}
