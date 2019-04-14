import org.apache.spark.{SparkConf, SparkContext}
import com.typesafe.config.{Config, ConfigFactory}
import org.apache.spark.rdd.RDD


object HelloSpark {
  def main(args: Array[String]) {

    //Create a SparkContext to initialize Spark
    val conf = new SparkConf()
    conf.setMaster("local")
    conf.setAppName("Word Count")
    val sc = new SparkContext(conf)

    val config: Config = ConfigFactory.load()

    val infile = ZeConf().getString("app.infile")
    val outfile = ZeConf().getString("app.outfile")


    // Load the text into a Spark RDD, which is a distributed representation of each line of text
    val textFile: RDD[String] = sc.textFile(infile)
    //val textFile = sc.textFile("hdfs:///tmp/shakespeare.txt")

    //word count
    val counts = textFile.flatMap(line => line.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_ + _)

    counts.foreach(println)
    System.out.println("Total words: " + counts.count());
    counts.saveAsTextFile(outfile)
    //counts.saveAsTextFile("hdfs:///tmp/shakespeareWordCount");

  }

}
