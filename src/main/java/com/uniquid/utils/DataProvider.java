/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.utils;

public interface DataProvider<T> extends ResponseDecoder<T> {

    String getContentType();

    String getCharset();

    byte[] getPayload();

}
