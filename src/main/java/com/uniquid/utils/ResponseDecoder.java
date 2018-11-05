/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.utils;

public interface ResponseDecoder<T> {

    int getExpectedResponseCode();

    T manageResponse(String serverResponse) throws Exception;

    T manageUnexpectedResponseCode(int responseCode, String responseMessage) throws Exception;

}
