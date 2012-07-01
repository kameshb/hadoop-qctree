package com.imaginea.qctree.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import com.imaginea.qctree.measures.Aggregates;

public class QueryReducer extends
    Reducer<NullWritable, Aggregates, Aggregates, NullWritable> {

  @Override
  protected void reduce(NullWritable key,
      java.lang.Iterable<Aggregates> values, Context context)
      throws IOException, InterruptedException {
  };

}
