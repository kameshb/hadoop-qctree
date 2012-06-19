package com.imaginea.qctree.hadoop;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import com.imaginea.qctree.Row;
import com.imaginea.qctree.Table;

public class QCMapper extends Mapper<LongWritable, Text, Text, LongWritable> {

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
      baseTable.addRow(row);
    }

  };

}
