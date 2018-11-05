/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.servlet;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Beatrice Formai
 */
public class ExceptionJson extends ErrorJson {

    private Throwable th;

    public ExceptionJson(Throwable th) {
        super(th.getClass().getName() + ": " + th.getMessage());
        this.th = th;
    }

    @JsonProperty
    public List<String> getTrace() {
        List<String> trace = new ArrayList<>();
        for (StackTraceElement element: th.getStackTrace()) {
            trace.add(element.getClassName() + "." + element.getMethodName() + "(" + element.getFileName() + ":" + element.getLineNumber() + ")");
        }
        return trace;
    }

}
