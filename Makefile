#!/usr/bin/env make

default: build

help:
	@printf "\033[1;37mAvailable commands\n\033[0;37m\n"
	@printf "build   Build KMeans application\n"
	@printf "clean   Clear built application\n"
	@printf "test    Test KMeans application\n"
	@printf "help    This help message\n"
	@printf "\033[0m\n"

handin:
	@zip handin.zip KMeans/build.sbt KMeans/src/main/scala/Kmeans.scala
	@printf "\033[1;37mhandin.zip\033[0m created.\n"

clean:
	@-rm -f handin.zip KMeans.jar
	@cd KMeans; sbt clean
	@printf "\033[1;37mAll created files are cleaned.\033[0m\n"

test: KMeans.jar
	@printf "\033[1;37mInitializing HDFS structure...\033[0;37m\n"
	@hdfs dfs -rm -r -f /user/input /user/output
	@hdfs dfs -mkdir -p /user /user/input
	@hdfs dfs -put -f KMeans/kmeans_input.txt /user/input
	@printf "\033[1;37m\nRunning KMeans (mode 0)...\033[0;37m\n"
	@spark-submit --class Kmeans KMeans.jar hdfs://localhost:9000//user/input/kmeans_input.txt hdfs://localhost:9000//user/output 0 3
	@printf "\033[1;37m\nOutput from mode 0 will be saved to <m0_0.txt> and <m0_1.txt>.\n"
	@printf "These two files will be joined and saved as <m0.txt>.\033[0;37m\n"
	@hdfs dfs -get -f /user/output/part-00000 m0_0.txt
	@hdfs dfs -get -f /user/output/part-00001 m0_1.txt
	@cat m0_0.txt > m0.txt
	@cat m0_1.txt >> m0.txt
	@printf "\033[1;37m\nPreparing HDFS structure to relaunch KMeans...\033[0;37m\n"
	@hdfs dfs -rm -r -f /user/output
	@printf "\033[1;37m\nRunning KMeans (mode 1)...\033[0;37m\n"
	@spark-submit --class Kmeans KMeans.jar hdfs://localhost:9000//user/input/kmeans_input.txt hdfs://localhost:9000//user/output 1
	@printf "\033[1;37m\nOutput from mode 1 will be saved to <m1_0.txt> and <m1_1.txt>.\n"
	@printf "These two files will be joined and saved as <m1.txt>.\033[0;37m\n"
	@hdfs dfs -get -f /user/output/part-00000 m1_0.txt
	@hdfs dfs -get -f /user/output/part-00001 m1_1.txt
	@cat m1_0.txt > m1.txt
	@cat m1_1.txt >> m1.txt
	@printf "\033[1;37m\nDifferences of mode 0 is not available (because of random result)\033[0;37m\n"
	@printf "\033[1;37m\nDifferences of mode 1\033[0;37m\n"
	@-diff KMeans/output_example.txt m1.txt
	@printf "\033[0m\n"

build: KMeans.jar

KMeans.jar: KMeans/src/main/scala/Kmeans.scala
	@printf "\033[1;37mBuilding KMeans...\033[0;37m\n"
	@cd KMeans; sbt package
	@cp KMeans/target/scala-*/*.jar KMeans.jar
	@printf "\033[0m\n"
