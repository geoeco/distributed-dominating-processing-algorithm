# Distributed Dominating Processing Algorithm
This project is the implementation of the algorithm I designed for my Master's
thesis during the academic year 2016-17. The goal of the Distributed Dominating
Processing Algorithm (DDPA for short) is to efficiently process top-k dominating
queries on big unindexed data sets using Apache Spark. For more information on
top-k dominating queries have a look [here](http://www4.comp.polyu.edu.hk/~csmlyiu/journal/vldbj_kdom.pdf).

## Building & Running
The code is meant to be run on Apache Spark. That means that it has to be
compiled to a jar file and that jar file hasto be passed to Spark via
spark-submit.

To build the jar file you need to run `sbt assembly` in the root directory. You
will find the generated jar file inside `target/scala-2.11`.

You can then run the jar as follows (additional options can of course be added
to spark-submit):
```
spark-submit --master yarn distributed-dominating-processing-algorithm-assembly-1.0.jar -k 50 -d 3 -m 8 -i /inputDir -o /outputDir
```

As you can see, some arguments are expected to be provided. Here's the full list:
```
-k, --k <value>                 number of results of the top-k dominating query
-d, --dimensions <value>        number of dimensions
-m, --cellsPerDimension <value> number of grid cells per dimension
--min <value>                   minimum allowed value for a coordinate (inclusive), default value 0.0
--max <value>                   maximum allowed value for a coordinate (exclusive), default value 1.0
-i, --inputPath <value>         path to input file(s)
-o, --outputPath <value>        path to output directory (must exist)
```
Of the above, `min` and `max` are optional, while the rest has to be provided by
the user.

### Input Format
The expected input is one or multiple csv files, where each line represents a
data point and each column a coordinate of that point. This means that the
number of columns should be equal to the value of the `dimensions` property
provided during `spark-submit`. Below you can see an example:

```
0.8763226114304481,0.8456107199266948,0.9485541515776619
0.08278816809806255,0.47544884633743223,0.6773146466859055
0.5541281239382909,0.7934896366853125,0.9967355240592989
0.3022814566984946,0.4100483087713034,0.01758632563596796
0.12313909784889032,0.33682707562837755,0.28890798298911835
0.8250999858279972,0.04719383518784925,0.690184199195101
0.8216319184207431,0.256080072118125,0.530401604831869
```

To specify multiple input files, provide as input the path to the directory
containing them.

### Running Locally
For development purposes it is convenient to be a able to run the code locally, without the need of a Spark cluster.
You can do so with `sbt run`:
```
sbt -Dspark.master=local[*] "run -k 50 -d 3 -m 8 -i /inputDir -o /outputDir"
```
