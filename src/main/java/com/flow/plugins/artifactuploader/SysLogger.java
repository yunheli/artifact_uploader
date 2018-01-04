/*
 * Copyright 2017 flow.ci
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flow.plugins.artifactuploader;

/**
 * @author yh@firim
 */
public class SysLogger implements Logger {

    private String className;

    public SysLogger(Class clazz) {
        this.className = clazz.getName();
    }

    public void trace(String message) {
        systemShow("trace", message);
    }

    public void info(String message) {
        systemShow("info", message);
    }

    public void warn(String message) {
        systemShow("warn", message);
    }

    private void systemShow(String action, String message) {
        System.out.println("[" + action.toUpperCase() + "]" + this.className + " - " + message);
    }
}