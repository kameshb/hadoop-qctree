package com.imaginea.qctree.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

public class QCDriver implements Tool {

  private Configuration conf;

  @Override
  public Configuration getConf() {
    return conf;
  }

  @Override
  public void setConf(Configuration conf) {
    this.conf = conf;
  }

  @Override
  public int run(String[] args) throws Exception {
    Job qcJob = Job.getInstance(getConf(), "Quotient Cube");
    qcJob.setJarByClass(QCDriver.class);
    qcJob.setMapperClass(QCMapper.class);
    qcJob.setNumReduceTasks(0);
    qcJob.setOutputKeyClass(NullWritable.class);
    qcJob.setOutputValueClass(QCTree.class);
    qcJob.setInputFormatClass(NLineInputFormat.class);
    qcJob.setOutputFormatClass(SequenceFileOutputFormat.class);
    NLineInputFormat.setInputPaths(qcJob, args[0]);
    FileOutputFormat.setOutputPath(qcJob, new Path(args[1]));
    NLineInputFormat.setNumLinesPerSplit(qcJob, Integer.valueOf(args[2]));
    return qcJob.waitForCompletion(true) ? 0 : -1;
  }

  public static void main(String[] args) throws Exception {
    ToolRunner.run(new Configuration(), new QCDriver(), args);
  }

}
