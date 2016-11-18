package com.sparkml.uber

import org.apache.spark._
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
import org.apache.spark.sql._

import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.clustering.KMeans



    val sqlContext = new SQLContext(sc)
    import sqlContext.implicits._
    import sqlContext._

    val schema = StructType(Array(
      StructField("dt", TimestampType, true),
      StructField("lat", DoubleType, true),
      StructField("lon", DoubleType, true),
      StructField("base", StringType, true)
    ))
       // Spark 2.0
//    val df = spark.read.option("header","false").schema(schema).csv("data/uber.csv")
     // Spark 1.6 using --packages com.databricks:spark-csv_2.10:1.5.0 
    val df = sqlContext.read.format("com.databricks.spark.csv").option("header", "false").schema(schema).load("data/uber.csv")
    df.cache
    df.show
    df.schema

    val featureCols = Array("lat", "lon")
    val assembler = new VectorAssembler().setInputCols(featureCols).setOutputCol("features")
    val df2 = assembler.transform(df)
    val Array(trainingData, testData) = df2.randomSplit(Array(0.7, 0.3), 5043)

    val kmeans = new KMeans().setK(10).setFeaturesCol("features").setMaxIter(3)
    val model = kmeans.fit(trainingData)
    println("Final Centers: ")
    model.clusterCenters.foreach(println)

    val categories = model.transform(testData)
    
    categories.show
    categories.registerTempTable("uber")
    
    categories.select(month($"dt").alias("month"),dayofmonth($"dt").alias("day"),hour($"dt").alias("hour"), $"prediction").groupBy("month","day","hour","prediction").agg(count("prediction").alias("count")).orderBy("day", "hour","prediction").show 

    categories.select(hour($"dt").alias("hour"), $"prediction").groupBy("hour", "prediction").agg(count("prediction")
        .alias("count")).orderBy(desc("count")).show

    categories.groupBy("prediction").count().show()
    
    // 
    
    sqlContext.sql(" select prediction, count(prediction) as count from uber group by prediction").show
    
    sqlContext.sql("SELECT hour(uber.dt) as hr,count(prediction) as ct FROM uber group By hour(uber.dt)").show

    // to save the categories dataframe as json data
    //  categories.select("dt", "base", "prediction").write.format("json").save("uberclusterstest")
    //  to save the model 
    //  model.write.overwrite().save("/user/user01/data/savemodel")
    //  to re-load the model
    //  val sameModel = KMeansModel.load("/user/user01/data/savemodel")
  }
}

