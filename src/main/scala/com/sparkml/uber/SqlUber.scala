/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sparkml.uber
import org.apache.spark._
import org.apache.spark.rdd.RDD
import org.apache.spark.util.IntParam
import org.apache.spark.sql.SQLContext

import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql._

import org.apache.spark.mllib.stat.Statistics

object SqlUber {

  def main(args: Array[String]) {

    val conf = new SparkConf().setAppName("SparkDFebay")
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)

    import sqlContext.implicits._
    import sqlContext._


    // read saved json data
    val df = sqlContext.read.json("uberclusterstest").cache()

    df.printSchema

    // COMMAND ----------

    df.show

    df.registerTempTable("uber")
    //
    df.select(month($"dt").alias("month"), dayofmonth($"dt").alias("day"), $"prediction").groupBy("month", "day", "prediction").agg(count("prediction").alias("count")).orderBy(desc("count")).show

    // COMMAND ----------

    df.select(month($"dt").alias("month"), dayofmonth($"dt").alias("day"), hour($"dt").alias("hour"), $"prediction").groupBy("month", "day", "hour", "prediction").agg(count("prediction").alias("count")).orderBy(desc("count"), desc("prediction")).show

    df.select(month($"dt").alias("month"), dayofmonth($"dt").alias("day"), hour($"dt").alias("hour"), $"prediction").groupBy("month", "day", "prediction").agg(count("prediction").alias("count")).orderBy(asc("day")).show
    // COMMAND ----------
    df.select(month($"dt").alias("month"), dayofmonth($"dt").alias("day"), hour($"dt").alias("hour"), $"prediction").groupBy("month", "day", "hour", "prediction").agg(count("prediction").alias("count")).orderBy("day", "hour", "prediction").show

    df.select(month($"dt").alias("month"), dayofmonth($"dt").alias("day"), hour($"dt").alias("hour"), $"prediction").groupBy("month", "day", "hour", "prediction").agg(count("prediction").alias("count")).orderBy("day", "hour", "prediction").show

   
  }
}
