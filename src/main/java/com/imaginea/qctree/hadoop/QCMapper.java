package com.imaginea.qctree.hadoop;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.imaginea.qctree.QCCube;
import com.imaginea.qctree.Row;
import com.imaginea.qctree.Table;

public class QCMapper extends Mapper<LongWritable, Text, NullWritable, QCTree> {

  private static final Log LOG = LogFactory.getLog(QCMapper.class);
  private Table baseTable = Table.getTable();

  @Override
  public void run(Context context) throws java.io.IOException,
      InterruptedException {

    int noOfDim = baseTable.getDimensionHeaders().size();
    Row row;
    while (context.nextKeyValue()) {
      String rowStr = context.getCurrentValue().toString();
      String[] values = rowStr.split("\t");

      String[] dim = new String[noOfDim];
      double[] ms = new double[values.length - noOfDim];

      System.arraycopy(values, 0, dim, 0, noOfDim);
      for (int i = 0; i < ms.length; ++i) {
        ms[i] = Double.parseDouble(values[noOfDim + i]);
      }
      row = new Row(dim, ms);
      if (LOG.isDebugEnabled()) {
        LOG.debug("Adding row : " + row);
      }
      baseTable.addRow(row);
    }
    QCCube cube = QCCube.construct();
    QCTree tree = QCTree.build(cube);

    context.write(NullWritable.get(), tree);
  }
}
