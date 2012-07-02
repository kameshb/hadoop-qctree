package com.imaginea.qctree.hadoop;

import java.io.IOException;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import com.imaginea.qctree.measures.Aggregates;

public class QueryReducer extends
    Reducer<NullWritable, Aggregates, NullWritable, Aggregates> {

  private NullWritable KEY = NullWritable.get();

  @Override
  protected void reduce(NullWritable key, Iterable<Aggregates> values,
      Context context) throws IOException, InterruptedException {

    Aggregates result = new Aggregates();

    for (Aggregates value : values) {
      result.accumalate(value);
    }
    context.write(KEY, result);
  };

}
