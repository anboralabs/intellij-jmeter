/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.jmeter.threads;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.util.JMeterUtils;

/**
 * Holds context for a thread.
 * Generated by JMeterContextService.
 * <br>
 * The class is not thread-safe - it is only intended for use within a single
 * thread.
 */
public class JMeterContext {

  public enum TestLogicalAction {
    CONTINUE,
    START_NEXT_ITERATION_OF_THREAD,
    START_NEXT_ITERATION_OF_CURRENT_LOOP,
    BREAK_CURRENT_LOOP
  }

  private JMeterVariables variables;
  private SampleResult previousResult;
  private Sampler currentSampler;
  private Sampler previousSampler;
  private boolean samplingStarted;
  private StandardJMeterEngine engine;
  private JMeterThread thread;
  private AbstractThreadGroup threadGroup;
  private int threadNum;
  private TestLogicalAction testLogicalAction = TestLogicalAction.CONTINUE;
  private final ConcurrentHashMap<String, Object> samplerContext =
      new ConcurrentHashMap<>(5);
  private boolean recording;

  JMeterContext() { clear0(); }

  /**
   * Internally called by JMeter, never call it directly
   */
  public void clear() { clear0(); }

  private void clear0() {
    variables = null;
    previousResult = null;
    currentSampler = null;
    previousSampler = null;
    samplingStarted = false;
    threadNum = 0;
    thread = null;
    recording = false;
    samplerContext.clear();
  }

  /**
   * Gives access to the JMeter variables for the current thread.
   *
   * @return a pointer to the JMeter variables.
   */
  public JMeterVariables getVariables() {
    // If context variable is null ( Client side ) return client variables
    return (variables != null) ? variables
                               : JMeterContextService.getClientSideVariables();
  }

  /**
   * @return a pointer to the JMeter Properties.
   */
  public Properties getProperties() {
    return JMeterUtils.getJMeterProperties();
  }

  /**
   * Internally called by JMeter, never call it directly
   *
   * @param vars JMeterVariables
   */
  public void setVariables(JMeterVariables vars) { this.variables = vars; }

  public SampleResult getPreviousResult() { return previousResult; }

  /**
   * Internally called by JMeter, never call it directly
   *
   * @param result SampleResult
   */
  public void setPreviousResult(SampleResult result) {
    this.previousResult = result;
  }

  public Sampler getCurrentSampler() { return currentSampler; }

  /**
   * Internally called by JMeter, never call it directly
   *
   * @param sampler Sampler
   */
  public void setCurrentSampler(Sampler sampler) {
    this.previousSampler = currentSampler;
    this.currentSampler = sampler;
  }

  /**
   * Returns the previousSampler.
   *
   * @return Sampler
   */
  public Sampler getPreviousSampler() { return previousSampler; }

  /**
   * @return the threadNum starting from zero (0)
   */
  public int getThreadNum() { return threadNum; }

  /**
   * Internally called by JMeter, never call it directly
   * @param threadNum number of threads
   */
  public void setThreadNum(int threadNum) { this.threadNum = threadNum; }

  public JMeterThread getThread() { return this.thread; }

  /**
   * Internally called by JMeter, never call it directly
   * @param thread to use
   */
  public void setThread(JMeterThread thread) { this.thread = thread; }

  public AbstractThreadGroup getThreadGroup() { return this.threadGroup; }

  /**
   * Internally called by JMeter, never call it directly
   * @param threadgrp ThreadGroup to use
   */
  public void setThreadGroup(AbstractThreadGroup threadgrp) {
    this.threadGroup = threadgrp;
  }

  public StandardJMeterEngine getEngine() { return engine; }

  /**
   * Internally called by JMeter, never call it directly
   * @param engine to use
   */
  public void setEngine(StandardJMeterEngine engine) { this.engine = engine; }

  public boolean isSamplingStarted() { return samplingStarted; }

  /**
   * Internally called by JMeter, never call it directly
   * @param b flag whether sampling has started
   */
  public void setSamplingStarted(boolean b) { samplingStarted = b; }

  /**
   * @param actionOnExecution action to take for next iteration of current loop
   *     in which this component is present
   */
  public void setTestLogicalAction(TestLogicalAction actionOnExecution) {
    this.testLogicalAction = actionOnExecution;
  }

  /**
   * @return TestLogicalAction to start next iteration of current loop in which
   *     this component is present
   */
  public TestLogicalAction getTestLogicalAction() { return testLogicalAction; }

  /**
   * @param restartNextLoop if set to <code>true</code> a restart of the loop
   *     will occur
   * @deprecated use {@link
   *     JMeterContext#setTestLogicalAction(TestLogicalAction)}
   */
  @Deprecated
  public void setStartNextThreadLoop(boolean restartNextLoop) {
    if (restartNextLoop) {
      this.testLogicalAction = TestLogicalAction.START_NEXT_ITERATION_OF_THREAD;
    } else {
      this.testLogicalAction = TestLogicalAction.CONTINUE;
    }
  }

  /**
   * @return {@code true} when current loop iteration of Thread Group will be
   *     interrupted and JMeter will go to next iteration of the Thread Group
   *     loop
   * @deprecated use {@link JMeterContext#getTestLogicalAction()}
   */
  @Deprecated
  public boolean isStartNextThreadLoop() {
    return this.testLogicalAction ==
        TestLogicalAction.START_NEXT_ITERATION_OF_THREAD;
  }

  /**
   * if set to <code>true</code> current loop iteration will be interrupted and
   * JMeter will go to next iteration
   * @param restartNextLoop flag whether to restart
   *
   * @deprecated use {@link JMeterContext#setStartNextThreadLoop(boolean)}
   */
  @Deprecated
  public void setRestartNextLoop(boolean restartNextLoop) {
    setStartNextThreadLoop(restartNextLoop);
  }

  /**
   * @return flag whether restart is set
   * @deprecated use {@link JMeterContext#isStartNextThreadLoop()}
   */
  @Deprecated
  public boolean isRestartNextLoop() {
    return isStartNextThreadLoop();
  }

  /**
   * Clean cached data after sample
   * Internally called by JMeter, never call it directly
   */
  public void cleanAfterSample() {
    if (previousResult != null) {
      previousResult.cleanAfterSample();
    }
    samplerContext.clear();
  }

  /**
   * Sampler context is cleaned up as soon as Post-Processor have ended
   * @return Context to use within PostProcessors to cache data
   */
  public Map<String, Object> getSamplerContext() { return samplerContext; }

  /**
   * Internally called by JMeter, never call it directly
   * @param recording flag whether recording should be done
   */
  public void setRecording(boolean recording) { this.recording = recording; }

  public boolean isRecording() { return recording; }
}
