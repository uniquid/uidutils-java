/*
 * Copyright (c) 2016-2019. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class UserLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLogger.class);
    private static final Marker LOG_MARKER = MarkerFactory.getMarker("USER");

    public static void info(String format, Object... arguments) {
        LOGGER.info(LOG_MARKER, format, arguments);
    }

    public static void warn(String format, Object... arguments) {
        LOGGER.warn(LOG_MARKER, format, arguments);
    }

    public static void error(String format, Object... arguments) {
        LOGGER.error(LOG_MARKER, format, arguments);
    }

    public static void fatal(String format, Object... arguments) {
        LOGGER.error(LOG_MARKER, "Internal Server Error: " + format + " Please contact Uniquid support to resolve the problem.", arguments);
    }
}
