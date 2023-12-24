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

package org.apache.jmeter.gui.action;

import com.google.auto.service.AutoService;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.updateSettings.impl.IdeRestartKt;
import java.awt.event.ActionEvent;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import kotlin.Unit;
import org.apache.jmeter.gui.GuiPackage;
import org.apache.jmeter.util.JMeterUtils;

@AutoService(Command.class)
public class ExitIDECommand extends AbstractActionWithNoRunningTest {

  private static final Set<String> commands = new HashSet<>();

  static { commands.add(ActionNames.EXIT); }

  /**
   * Constructor for the ExitCommand object
   */
  public ExitIDECommand() {}

  /**
   * Gets the ActionNames attribute of the ExitCommand object
   *
   * @return The ActionNames value
   */
  @Override
  public Set<String> getActionNames() {
    return commands;
  }

  /**
   * Description of the Method
   *
   * @param e
   *            Description of Parameter
   */
  @Override
  public void doActionAfterCheck(ActionEvent e) {
    ApplicationManager.getApplication().invokeLater(() -> {
      IdeRestartKt.restartOrNotify(
          ProjectManager.getInstance().getDefaultProject(), false, () -> {
            ApplicationManager.getApplication().restart();
            return Unit.INSTANCE;
          });
    });
  }
}
