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
public class ExceptionStackJson {

    private List<ExceptionJson> stack;

    public ExceptionStackJson(Throwable th) {
        stack = new ArrayList<>();
        processCause(th);
    }

    private void processCause(Throwable th) {
        Throwable cause = th.getCause();
        if (cause != null) {
            processCause(cause);
        }
        stack.add(new ExceptionJson(th));
    }

    @JsonProperty
    public List<ExceptionJson> getStack() {
        return stack;
    }

}
