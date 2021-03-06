/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.sqoop.config;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.util.GenericOptionsParser;

import com.cloudera.sqoop.mapreduce.db.DBConfiguration;

/**
 * This class provides static helper methods that allow access and manipulation
 * of job configuration. It is convenient to keep such access in one place in
 * order to allow easy modifications when some of these aspects change from
 * version to version of Hadoop.
 */
public final class ConfigurationHelper {

  /**
   * Set the (hinted) number of map tasks for a job.
   */
  public static void setJobNumMaps(Job job, int numMapTasks) {
    job.getConfiguration().setInt(
        ConfigurationConstants.PROP_MAPRED_MAP_TASKS, numMapTasks);
  }

  /**
   * Get the (hinted) number of map tasks for a job.
   */
  public static int getJobNumMaps(JobContext job) {
    return job.getConfiguration().getInt(
        ConfigurationConstants.PROP_MAPRED_MAP_TASKS, 1);
  }

  /**
   * @return the number of mapper output records from a job using its counters.
   */
  public static long getNumMapOutputRecords(Job job)
      throws IOException, InterruptedException {
    return job.getCounters().findCounter(
        ConfigurationConstants.COUNTER_GROUP_MAPRED_TASK_COUNTERS,
        ConfigurationConstants.COUNTER_MAP_OUTPUT_RECORDS).getValue();
  }

  /**
   * @return the number of mapper input records from a job using its counters.
   */
  public static long getNumMapInputRecords(Job job)
      throws IOException, InterruptedException {
    return job.getCounters().findCounter(
          ConfigurationConstants.COUNTER_GROUP_MAPRED_TASK_COUNTERS,
          ConfigurationConstants.COUNTER_MAP_INPUT_RECORDS).getValue();
  }

  /**
   * Get the (hinted) number of map tasks for a job.
   */
  public static int getConfNumMaps(Configuration conf) {
    return conf.getInt(ConfigurationConstants.PROP_MAPRED_MAP_TASKS, 1);
  }

  /**
   * Set the mapper speculative execution property for a job.
   */
  public static void setJobMapSpeculativeExecution(Job job, boolean isEnabled) {
    job.getConfiguration().setBoolean(
        ConfigurationConstants.PROP_MAPRED_MAP_TASKS_SPECULATIVE_EXEC,
        isEnabled);
  }

  /**
   * Set the reducer speculative execution property for a job.
   */
  public static void setJobReduceSpeculativeExecution(
      Job job, boolean isEnabled) {
    job.getConfiguration().setBoolean(
        ConfigurationConstants.PROP_MAPRED_REDUCE_TASKS_SPECULATIVE_EXEC,
        isEnabled);
  }

  /**
   * Sets the Jobtracker address to use for a job.
   */
  public static void setJobtrackerAddr(Configuration conf, String addr) {
    conf.set(ConfigurationConstants.PROP_MAPRED_JOB_TRACKER_ADDRESS, addr);
  }

  /**
   * @return the Configuration property identifying a DBWritable to use.
   */
  public static String getDbInputClassProperty() {
    return DBConfiguration.INPUT_CLASS_PROPERTY;
  }

  /**
   * @return the Configuration property identifying the DB username.
   */
  public static String getDbUsernameProperty() {
    return DBConfiguration.USERNAME_PROPERTY;
  }

  /**
   * @return the Configuration property identifying the DB password.
   */
  public static String getDbPasswordProperty() {
    return DBConfiguration.PASSWORD_PROPERTY;
  }

  /**
   * @return the Configuration property identifying the DB connect string.
   */
  public static String getDbUrlProperty() {
    return DBConfiguration.URL_PROPERTY;
  }

  /**
   * @return the Configuration property identifying the DB input table.
   */
  public static String getDbInputTableNameProperty() {
    return DBConfiguration.INPUT_TABLE_NAME_PROPERTY;
  }

  /**
   * @return the Configuration property specifying WHERE conditions for the
   * db table.
   */
  public static String getDbInputConditionsProperty() {
    return DBConfiguration.INPUT_CONDITIONS_PROPERTY;
  }

  /**
   * Parse arguments in 'args' via the GenericOptionsParser and
   * embed the results in the supplied configuration.
   * @param conf the configuration to populate with generic options.
   * @param args the arguments to process.
   * @return the unused args to be passed to the application itself.
   */
  public static String [] parseGenericOptions(
      Configuration conf, String [] args) throws IOException {
    // This needs to be shimmed because in Apache Hadoop this can throw
    // an IOException, but it does not do so in CDH. We just mandate in
    // this method that an IOException is possible.
    GenericOptionsParser genericParser = new GenericOptionsParser(
        conf, args);
    return genericParser.getRemainingArgs();
  }


  private ConfigurationHelper() {
    // Disable explicit object creation
  }
}
