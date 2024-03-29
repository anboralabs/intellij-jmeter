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

package org.apache.jmeter.report.processor.graph;

import org.apache.jmeter.control.TransactionController;
import org.apache.jmeter.report.core.Sample;
import org.apache.jmeter.samplers.SampleResult;

/**
 * The class CountValueSelector provides a projection to count samples.
 *
 * @since 3.0
 */
public class CountValueSelector extends AbstractGraphValueSelector {
  private static final Double ONE = 1.0d;
  private static final Double ZERO = 0.0d;

  /**
   *
   * @param ignoreTransactionController boolean ignore {@link SampleResult}
   *     generated by {@link TransactionController}
   */
  public CountValueSelector(boolean ignoreTransactionController) {
    super(ignoreTransactionController);
  }

  /**
   * @see GraphValueSelector#select(String, Sample)
   */
  @Override
  public Double select(String series, Sample sample) {
    if (isIgnoreTransactionController()) {
      if (!sample.isController()) {
        return ONE;
      }
    } else {
      if (!sample.isEmptyController()) {
        return ONE;
      }
    }
    return ZERO;
  }
}
