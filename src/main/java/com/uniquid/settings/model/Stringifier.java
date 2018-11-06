/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.settings.model;

import com.uniquid.settings.exception.StringifyException;

public interface Stringifier {

    String stringify(Setting setting) throws StringifyException;

}
