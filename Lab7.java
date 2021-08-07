package org.myorg;

import java.io.IOException;
import java.util.StringTokenizer;
import java.lang.Math;
import java.lang.reflect.Array;
import java.lang.Integer;
import java.lang.Object;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.io.ArrayWritable;

/*
*  Write a mapreduce program that partitions the text file into 5 classes as follows:
*  class 1 contains transactions s.t. the item count is between 1-10,
*  class2 for 11-20, and so on till class5 having 41-50 item count;
*  and computes the total amount obtained for each class.
*  
*  input:  
*  1: 100 200 400 500
*  2: 10 50 5 25 89 20 35 91 78 82 150 125
*  3: 100 300
*  
*  Here tx 1 and tx 3 belong to class 1 as the item count for each is between 1 - 10,
*  while tx 2 belongs to class 2. The total amount from the sales for class 1 is the 
*  sum of the costs of all the items from tx 1 and tx 3. 
*  
*  e.g. output 
*  class1, 1600  
*  class2, 760
*/

public class Lab7 {

    // Class Mapper<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
    public static class JobMapper extends Mapper<Object, Text, Text, LongWritable> {

        @Override
        public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
            /*
             * This function will take each line of the csv file and emit <class, sum>
             */
            String[] prices = value.toString().split(","); // split by delimiter
            long sum_of_prices_per_line = 0;
            for (String price : prices) {
                sum_of_prices_per_line += Integer.parseInt(price);
            } // sum of the prices..
            int prices_len = 0;
            prices_len += Math.ceil(prices.length / 10.0f); // convert no of items into class number
            LongWritable output_val = new LongWritable(sum_of_prices_per_line);
            Text output_key = new Text(String.format("Class%d", prices_len));
            // classify by length
            context.write(output_key, output_val);
        }
    }

    // Class Reducer<KEYIN,VALUEIN,KEYOUT,VALUEOUT>
    public static class JobReducer extends Reducer<Text, LongWritable, Text, LongWritable> {
        private LongWritable result = new LongWritable();

        /*
         * `result` variable for storing the sum class
         */
        public void reduce(Text key, Iterable<LongWritable> values, Context context)
                throws IOException, InterruptedException {
            long sum = 0;
            for (LongWritable val : values) {
                sum += val.get();
            }
            result.set(sum);
            context.write(key, result);
        }
    }

    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Lab7");
        job.setJarByClass(Lab7.class);
        job.setMapperClass(JobMapper.class);
        // job.setCombinerClass(JobReducer.class);
        job.setReducerClass(JobReducer.class);

        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(LongWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(LongWritable.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }
}
