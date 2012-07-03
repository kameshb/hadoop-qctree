<h1>hadoop-qctree</h1>

QC-tree(short for quotient cube tree) is a compact data structure for representing and implementing quotient cube. 

QC-tree
<ul>
<li> retain all the essential information in a quotient lattice, yet be concise </li>
<li> enable efficient answering of various kinds of queries including point, range,and iceberg queries </li>
<li> afford efficient maintenance against updates </li>
</ul>

hadoop-qctree provides QC-tree construction using Hadoop.

<h3>How to Build Binary</h3>
<p>To build the binary of the hadoop-qctree, run the following command</p>
<i><b>mvn clean package</b></i>
<p>The above command produces a tar ball in the target directory.</p>

<h3>How to Run</h3>
<h4>Local Mode</h4>
<p>Untar the tar ball and move to hadoop-qctree-* directory. Need to update the table.json in the conf directory as per input structure</p>

<p>To build a qc-tree for the given input, execute the following command</p>
<i><b>bin/qctree.sh input-file output-dir</b></i>
<p>Once QC-tree construction is over, to run any query execute the following command</p>
<i><b>bin/query.sh output-of-previous-job output-dir query-tobe-run</b></i>
<p>As of now, query should be given as a single string, dimensions separated by comma(,)</p>
<p>Example query: "S2,*,s"</p>



