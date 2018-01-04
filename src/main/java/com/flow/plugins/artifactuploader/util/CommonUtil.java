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

package com.flow.plugins.artifactuploader.util;

import com.github.lalyos.jfiglet.FigletFont;

/**
 * @author yh@firim
 */
public class CommonUtil {

    private final static String LINE_BREAK = System.getProperty("line.separator");

    /**
     * show Jfiglet message
     * @param message
     * @return
     */
    public static String showJfigletMessage(String message) {
        try {
            return LINE_BREAK + FigletFont.convertOneLine(message);
        } catch (Throwable throwable) {
            return "";
        }
    }
}

