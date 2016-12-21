/**
  * Created by malin on 11-11-2016.
  */

import org.apache.http.client.HttpClient
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.client.methods.HttpGet
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer
import org.apache.commons.io.IOUtils
import org.apache.spark.{SparkConf, SparkContext}

object Api {
  val accessToken = "140638234-vixOttdihfuUCbKLSCSmi5jQp0aVvzOb6QVJx0Eg"
  val accessSecret = "DOpe1iIBsJ5lTmwmhvZevKu4v6dOF8amRHOH7483U5CSs"
  val consumerKey = "YfWLcKJddN8Elx2jtQsj4uBK9"
  val consumerSecret = "GAmNZ7aR8zrbIcbabr6zs9p628wzz6fBNeZKzLYrCHOqCpvKrv"

  def main(args: Array[String]) {

    val consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret)
    consumer.setTokenWithSecret(accessToken, accessSecret)


    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")

    val sc = new SparkContext(sparkConf)

    // Contains SQLContext which is necessary to execute SQL queries
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    // Reads json file and stores in a variable
    val tweet = sqlContext.read.json("C:\\Users\\malin\\Downloads\\Sports_tweets.json")
    tweet.registerTempTable("testtweets")
    val s8 = sqlContext.sql("select user.name as name,user.id from testtweets where text like '%soccer%'")
    s8.show(false)
    val userid = s8.first().getLong(1)
    println("Selected user id is:"+userid)

    val request = new HttpGet("https://api.twitter.com/1.1/users/show.json?user_id="+userid)
    consumer.sign(request)
    val client = new DefaultHttpClient()
    val response = client.execute(request)

    println(response.getStatusLine().getStatusCode());
    println(IOUtils.toString(response.getEntity().getContent()))
  }

}
