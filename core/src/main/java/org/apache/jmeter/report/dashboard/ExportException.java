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

package org.apache.jmeter.report.dashboard;

/**
 * The class ExportException provides an exception when data export fails.
 *
 * @since 3.0
 */
public class ExportException extends Exception {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 89868452883024813L;

  /**
   * Instantiates a new export exception.
   */
  public ExportException() { super(); }

  /**
   * Instantiates a new export exception.
   *
   * @param message
   *            the message
   */
  public ExportException(String message) { super(message); }

  /**
   * Instantiates a new export exception.
   *
   * @param message
   *            the message
   * @param cause
   *            the inner cause
   */
  public ExportException(String message, Throwable cause) {
    super(message, cause);
  }
}
