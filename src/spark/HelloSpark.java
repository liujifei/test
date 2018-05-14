package spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class HelloSpark {

    public static void main(String[] args) {
        SparkConf conf = new SparkConf().setMaster("local").setAppName("HelloSpark");
        try (JavaSparkContext jsc = new JavaSparkContext(conf)) {
            System.out.println(jsc);
        }
    }

}
