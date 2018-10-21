/** 
 * Don't import another package besides below packages
 */
import org.apache.spark.SparkContext
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import scala.collection.mutable.Map

object Kmeans {
  /**
   * This is main function
   * You can create more functions as you want
   */
  def main(args: Array[String]) {
    if (args.length < 3) {
      System.err.println("Usage: KMeans <input_file> <output_file> <mode> <k>")
      System.exit(1)
    }

    /**
     * Don't modify following initialization phase
     */
    val sparkConf = new SparkConf().setAppName("KMeans").set("spark.cores.max", "3")
    val sc = new SparkContext(sparkConf)
    // val lines is base RDD
    val lines = sc.textFile(args(0))
    val mode = args(2).toInt
    /**
     * From here, you can modify codes.
     * you can use given data structure, or another data type and RDD operation
     * you must utilize more than 5 types of RDD operations
     */
    
    var K: Int = 0
    var centroids: Map[Int, Array[Double]] = Map()

    // Set initial centroids
    if (mode == 0) {   
      // randomly sample K data points
      K = args(3).toInt
      // centroids = ...
    }
    else {
      // user-defined centroids
      // you can use another built-in data type besides Map
      centroids = Map(1 -> Array(5, 1.2, -0.8), 2 -> Array(-3.2, -1.1, 3.0), 3 -> Array(-2.1, 5.1, 1.1))
      K = centroids.size
    }
    /**
     * Don't change termination condition
     * sum of moved centroid distances
     */
    var change : Double = 100
    while(change > 0.001) {
      
    }
  }
}
