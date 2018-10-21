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

			val seed = scala.util.Random
			for (i <- 1 to K) centroids(i) = Array(seed.nextDouble * 1000, seed.nextDouble * 1000, seed.nextDouble * 1000)

		} else {

			// user-defined centroids
			// you can use another built-in data type besides Map
			centroids = Map(1 -> Array(5, 1.2, -0.8), 2 -> Array(-3.2, -1.1, 3.0), 3 -> Array(-2.1, 5.1, 1.1))
			K = centroids.size

		}

		// Remove duplicates
		val distinctLines = lines.distinct
		
		/**
		 * Don't change termination condition
		 * sum of moved centroid distances
		 */
		var helloCacheWorld = distinctLines.map(_.split(",").map(_.toDouble)).cache
		var mappinParty: RDD[(Int, Array[Double])] = sc.emptyRDD
		var change: Double = 100
		while (change > 0.001) {
			
			// Map to calculate each's lord
			mappinParty = helloCacheWorld
					.map(assignLord(centroids))
					
			// Re-initialize centroids
			val oldCentroids = centroids
			centroids = Map()
			for (i <- 1 to K) centroids(i) = Array(0, 0, 0, 0)

			// Sum all ones
			mappinParty.map(one => {

				centroids(one._1)(0) = (centroids(one._1)(0) + one._2(0))
				centroids(one._1)(1) = (centroids(one._1)(1) + one._2(1))
				centroids(one._1)(2) = (centroids(one._1)(2) + one._2(2))

				centroids(one._1)(3) += 1

			})

			// Calculate new centroids && calculate change
			change = 0
			for (i <- 1 to K) {

				centroids(i)(0) /= centroids(i)(3)
				centroids(i)(1) /= centroids(i)(3)
				centroids(i)(2) /= centroids(i)(3)

				change += calcEuclidean(oldCentroids(i), centroids(i))

			}

		}

		// Save as text file
		mappinParty.map(one => one._1.toString + "\t" + one._2.mkString(",")).saveAsTextFile(args(1))

	}

	def assignLord(centroids: Map[Int, Array[Double]]) = (you: Array[Double]) => {

		var yourLord = 0
		var smallestDistance = Double.MaxValue

		for ((lord, position) <- centroids) {

			val distance = calcEuclidean(position, you)

			if (distance < smallestDistance) {

				yourLord = lord
				smallestDistance = distance

			}

		}

		(yourLord, you)

	}

	def calcEuclidean(a: Array[Double], b: Array[Double])
			= math.sqrt(
				math.pow(a(0) - b(0), 2) +
				math.pow(a(1) - b(1), 2) +
				math.pow(a(2) - b(2), 2) )

}
