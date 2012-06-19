package com.imaginea.qctree.hadoop;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
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
    qcJob.setInputFormatClass(NLineInputFormat.class);
    NLineInputFormat.setNumLinesPerSplit(qcJob, 1000);
    return qcJob.waitForCompletion(true) ? 0 : -1;
  }

  public static void main(String[] args) throws Exception {
    ToolRunner.run(new Configuration(), new QCDriver(), args);
  }

}
