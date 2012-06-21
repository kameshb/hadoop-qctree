package com.imaginea.qctree.hadoop;

import java.net.URI;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.NLineInputFormat;
import org.apache.hadoop.mapreduce.lib.output.NullOutputFormat;
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
    if (args.length != 1) {
      System.err.println("Specify Input Path.");
      System.exit(-1);
    }
    Job qcJob = Job.getInstance(getConf(), "Quotient Cube");
    qcJob.addCacheFile(new URI("/cache/files/table.json"));
    qcJob.addFileToClassPath(new Path("/cache/jars/gson-2.2.1.jar"));
    qcJob.setJarByClass(QCDriver.class);
    qcJob.setMapperClass(QCMapper.class);
    qcJob.setNumReduceTasks(0);
    qcJob.setInputFormatClass(NLineInputFormat.class);
    NLineInputFormat.setInputPaths(qcJob, args[0]);
    NLineInputFormat.setNumLinesPerSplit(qcJob, 1000);
    qcJob.setOutputFormatClass(NullOutputFormat.class);
    return qcJob.waitForCompletion(true) ? 0 : -1;
  }

  public static void main(String[] args) throws Exception {
    ToolRunner.run(new Configuration(), new QCDriver(), args);
  }

}
