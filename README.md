# Assignment#2 Spark

I used Spark 2.2.0 following HW2 instruction document.  

## How to compile and test

Launching HDFS and YARN using `start-dfs.sh` and `start-yarn.sh` as you did in [HW1](https://github.com/yuoa-hw/CSED426_HadoopPractice), you can follow below to run my code.  

```bash
$ make test
```

This will build `KMeans.jar` and automatically run jar in your Spark system and compare the result with standard result (provided by TA).  

For more information, you can type `make help` for full command list.
