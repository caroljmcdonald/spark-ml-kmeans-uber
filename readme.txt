Install and fire up the Sandbox using the instructions here: http://maprdocs.mapr.com/home/SandboxHadoop/c_sandbox_overview.html. 

Use an SSH client such as Putty (Windows) or Terminal (Mac) to login. See below for an example:
use userid: user01 and password: mapr.

For VMWare use:  $ ssh user01@ipaddress 

For Virtualbox use:  $ ssh user01@127.0.0.1 -p 2222 


You can build this project with Maven using IDEs like Eclipse or NetBeans, and then copy the JAR file to your MapR Sandbox, or you can install Maven on your sandbox and build from the Linux command line.

You can use scp to copy your JAR file to the MapR Sandbox:

use userid: user01 and password: mapr.
For VMWare use:  $ scp  nameoffile.jar  user01@ipaddress:/user/user01/. 

For Virtualbox use:  $ scp -P 2222 nameoffile.jar  user01@127.0.0.1:/user/user01/.  

Copy the data file using scp to here:

/user/user01/data/uber.csv


To Run the Spark k-means program: 

spark-submit --class com.sparkml.uber.ClusterUber --master local[2] --packages com.databricks:spark-csv_2.10:1.5.0  spark-kmeans-1.0.jar  

you can also run the code in  ClusterUber.scala in the spark shell 

$spark-shell --master local[2]

There is also a notebook file, in the notebooks directory, which you can import and run in Zeppelin, or view in a Zeppelin viewer
    
 - For Yarn you should change --master parameter to yarn-client - "--master yarn-client"

