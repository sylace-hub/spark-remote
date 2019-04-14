import org.apache.spark.{SparkConf, SparkContext}

object HelloSparkRemote {
  def main(args: Array[String]) {

    //Create a SparkContext to initialize Spark
    val conf = new SparkConf()
      .set("spark.shuffle.service.enabled", "false")
      .set("spark.dynamicAllocation.enabled", "false")
      .set("spark.io.compression.codec", "snappy")
      .set("spark.rdd.compress", "true")
      .set("spark.cores.max", "1")
      //.set("spark.executor.memory", "2g")
      .set("spark.submit.deployMode","client")
      //.set("spark.submit.deployMode","cluster")
      .set("spark.executor.instances","1")
      .set("spark.driver.memory","1g")
      .set("spark.executor.memory","1g")
      //.set("spark.driver.memory","471859200")
      //.set("spark.executor.memory","471859200")
      .set("spark.executor.cores","1")
    //conf.setMaster("local")
    conf.setMaster("yarn-cluster")
    //conf.setMaster("yarn-client")
    conf.setMaster("spark://52.47.131.69:7077")
    //conf.setMaster("yarn-client")
    //.set("spark.hadoop.yarn.resourcemanager.hostname", "35.180.163.217")
    // .set("spark.hadoop.yarn.resourcemanager.address", "35.180.163.217:8050")
    //.set("spark.hadoop.yarn.application.classpath",
    //  "$HADOOP_CONF_DIR,$HADOOP_COMMON_HOME/*,$HADOOP_COMMON_HOME/lib/*,"
    //    + "$HADOOP_HDFS_HOME/*,$HADOOP_HDFS_HOME/lib/*,"
    //    + "$HADOOP_YARN_HOME/*,$HADOOP_YARN_HOME/lib/*,"
    //    + "$HADOOP_MAPRED_HOME/*,$HADOOP_MAPRED_HOME/lib/*");
    conf.setAppName("Word Count");
    val sc = new SparkContext(conf)



    // Load the text into a Spark RDD, which is a distributed representation of each line of text
    val textFile = sc.textFile("src/main/resources/shakespeare.txt")
    //val textFile = sc.textFile("hdfs:///tmp/shakespeare.txt")

    //word count
    val counts = textFile.flatMap(line => line.split(" "))
      .map(word => (word, 1))
      .reduceByKey(_ + _)

    counts.foreach(println)
    System.out.println("Total words: " + counts.count());
    counts.saveAsTextFile("/tmp/shakespeareWordCount")
    //counts.saveAsTextFile("hdfs:///tmp/shakespeareWordCount");


  }

}
