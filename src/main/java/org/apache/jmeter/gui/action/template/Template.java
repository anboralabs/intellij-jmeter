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

package org.apache.jmeter.gui.action.template;

import java.io.File;
import java.util.Map;
import java.util.Objects;

/**
 * Template Bean
 *
 * @since 2.10
 */
public class Template {
    private boolean isTestPlan;
    private String name;
    private String fileName;
    private String description;
    private transient File parent; // for relative links
    private Map<String, String> parameters;

    /** @return the name */
    public String getName() {
        return name;
    }

    /** @param name the name to set */
    public void setName(String name) {
        this.name = name;
    }

    /** @return the relativeFileName */
    public String getFileName() {
        return fileName;
    }

    /** @param relativeFileName the relativeFileName to set */
    public void setFileName(String relativeFileName) {
        fileName = relativeFileName;
    }

    /** @return the description */
    public String getDescription() {
        return description;
    }

    /** @param description the description to set */
    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isTestPlan() {
        return isTestPlan;
    }

    public void setTestPlan(boolean isTestPlan) {
        this.isTestPlan = isTestPlan;
    }

    public File getParent() {
        return parent;
    }

    public void setParent(File parent) {
        this.parent = parent;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Template other = (Template) o;
        return isTestPlan == other.isTestPlan &&
                Objects.equals(name, other.name) &&
                Objects.equals(fileName, other.fileName) &&
                Objects.equals(description, other.description) &&
                Objects.equals(parent, other.parent) &&
                Objects.equals(parameters, other.parameters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isTestPlan, name, fileName, description, parent, parameters);
    }

    @Override
    public String toString() {
        return "Template [isTestPlan=" + isTestPlan +
                ", name=" + name +
                ", fileName=" + fileName +
                ", description=" + description +
                ", parameters=" + parameters + "]";
    }
}
