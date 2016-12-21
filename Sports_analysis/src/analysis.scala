/**
  * Created by malin on 29-10-2016.
  */

import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.SparkContext
import org.apache.spark.sql.SQLContext

object Sports_analysis {

  def main(args: Array[String]) {
    val sc = new SparkContext("local[2]","PbSpark")
    val sqlContext = new SQLContext(sc)
    val tweets = sqlContext.jsonFile("C:\\Users\\malin\\Downloads\\Sports_tweets.json")
    tweets.registerTempTable("testtweets")

    val textFile = sc.textFile("C:\\Users\\malin\\Downloads\\Sports_tweets.json")

    def time[A](f: => A) = {
      val s = System.nanoTime
      val ret = f
      println("Execution time : " + (System.nanoTime - s) / 1e9 + " sec")
      ret
    }

    time{
      // Data frame for Retweet count

      val re_tweet_query = sqlContext.sql("select user.name as name, retweeted_status.retweet_count as cnt from testtweets where user.name is not NULL order by cnt desc limit 20")
      re_tweet_query.show(false)
      re_tweet_query.save("retweeted_queries","json")
    }
    time{
      // Data frame for hash tags count

      val anlysis = sqlContext.jsonFile("C:\\Users\\malin\\Downloads\\analysis.txt")
      anlysis.registerTempTable("hashtags")
      anlysis.withColumnRenamed("test","_corrupt_record")
      val df = anlysis.toDF().withColumnRenamed("_corrupt_record", "tag")
      df.registerTempTable("hash1")
      val t = sqlContext.sql("select hash1.tag as records,count(testtweets.text) as count from testtweets join hash1 on testtweets.text like concat ('%',hash1.tag,'%') group by hash1.tag order by count desc limit 10")
      t.show(false)
    }

    time {
      // RDD for different sports analysis

            val cricket= (textFile.filter(line => line.contains("#cricket")).count())
            val Soccer= (textFile.filter(line => line.contains("#soccer")).count())
            val AmericanFootball= (textFile.filter(line => line.contains("#AmericanFootball")).count())
            val Basketball= (textFile.filter(line => line.contains("#Basketball")).count())
            val Olympics= (textFile.filter(line => line.contains("#Olympics")).count())
            val Rugby= (textFile.filter(line => line.contains("#Rugby")).count())
            val Golf= (textFile.filter(line => line.contains("#Golf")).count())
            val Baseball= (textFile.filter(line => line.contains("#Baseball")).count())
            val Tennis= (textFile.filter(line => line.contains("#Tennis")).count())
            val IceHockey= (textFile.filter(line => line.contains("#IceHockey")).count())
            val Volleyball= (textFile.filter(line => line.contains("#Volleyball")).count())

            println(("Number of comments on cricket are : %s \n Number of comments on soccer are : %s \n " +
              "Number of comments on AmericanFootball are : %s \n Number of comments on Basketball are : %s \n " +
              "Number of comments on Olympics are : %s \n Number of comments on Rugby are : %s \n Number of comments on Golf are : %s \n " +
              "Number of comments on Baseball are : %s \n Number of comments on Tennis are : %s \nNumber of comments on IceHockey are : %s \n " +
              "Number of comments on Volleyball are : %s").format(cricket,Soccer,AmericanFootball,Basketball,Olympics,Rugby,Golf,Baseball,Tennis,IceHockey,Volleyball))
    }

    time {
      // RDD for different sources used by users

          val android= (textFile.filter(line => line.contains("Twitter for Android")).count())
          val iphone= (textFile.filter(line => line.contains("Twitter for iPhone")).count())
          val ifttt= (textFile.filter(line => line.contains("IFTTT")).count())
          val roundteam= (textFile.filter(line => line.contains("RoundTeam")).count())
          val twitterfeed= (textFile.filter(line => line.contains("twitterfeed")).count())
          val webClient= (textFile.filter(line => line.contains("Twitter Web Client")).count())
          val ipad= (textFile.filter(line => line.contains("Twitter for iPad")).count())
          val instagram= (textFile.filter(line => line.contains("Instagram")).count())
          val tweetdeck= (textFile.filter(line => line.contains("TweetDeck")).count())
          val hootsuite= (textFile.filter(line => line.contains("Hootsuite")).count())
          val facebook= (textFile.filter(line => line.contains("Facebook")).count())
          val paper= (textFile.filter(line => line.contains("Paper.li")).count())
          val vine= (textFile.filter(line => line.contains("Vine - Make a Scene")).count())
          val sports= (textFile.filter(line => line.contains("Sports Teller")).count())

          println(("Number of Android users : %s\n Number of iPhone users : %s \n " +
           "Number of IFTTT users : %s \n Number of RoundTeam users : %s \n " +
            "Number of twitterfeed users : %s \n Number of Web users : %s\n Number of iPad users : %s \n " +
            "Number of Instagram users : %s \n Number of TweetDeck users : %s \n Number of Hootsuite users : %s \n " +
            "Number of Facebook users : %s \n Number of Paper.li users : %s \n Number of Vine - Make a Scene users : %s \n " +
            "Number of Sports Teller users : %s \n").format(android,iphone,ifttt,roundteam,twitterfeed,webClient,ipad,instagram,tweetdeck,hootsuite,facebook,paper,vine,sports))
    }
    sc.stop()

  }
}
