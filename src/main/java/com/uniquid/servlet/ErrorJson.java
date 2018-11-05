/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.servlet;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.Writer;

/**
 * @author Beatrice Formai
 */
public class ErrorJson {

    private static final ObjectMapper mapper = new ObjectMapper();

    private String error;

    public ErrorJson(String error) {
        this.error = error;
    }

    public void to(Writer dest) throws IOException {
        mapper.writeValue(dest, this);
    }

    @JsonProperty
    public String getError() {
        return error;
    }

}
