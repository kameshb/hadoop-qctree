hadoop-qctree
=============
QC-tree(short for quotient cube tree) is a compact data structure for representing and implementing quotient cube. 

QC-tree
<ul>
<li> retain all the essential information in a quotient lattice, yet be concise </li>
<li> enable efficient answering of various kinds of queries including point, range,and iceberg queries </li>
<li> afford efficient maintenance against updates </li>
</ul>

hadoop-qctree provides QC-tree construction using Hadoop.

<h3>How to Build Binary</h3>
To build the binary of the hadoop-qctree, run the following command
bq. mvn package
The above command produces a tar ball in the target directory.

<h3>How to Run</h3>
<h4>Local Mode</h4>
Untar the tar ball and move to hadoop-qctree-* directory.

To build a qc-tree for the given input, execute the following command
bq. bin/qctree.sh <input> <output>


