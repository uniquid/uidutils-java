/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.utils;

import java.security.SecureRandom;

public class StringUtils {

    private static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public static String getRandomName(int len) {

        SecureRandom random = new SecureRandom();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(random.nextInt(AB.length())));
        }

        return sb.toString();

    }

}
