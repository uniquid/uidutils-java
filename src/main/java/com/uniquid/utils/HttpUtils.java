/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class HttpUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    private static final String USER_AGENT = "UNIQUID-UTILS-0.1";

    public static <T> T sendDataWithPost(URL url, DataProvider<T> dataProvider) throws Exception {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setRequestProperty("Content-Type", dataProvider.getContentType());
        connection.setRequestProperty("Charset", dataProvider.getCharset());
        connection.setRequestProperty("Content-Length", String.valueOf(dataProvider.getPayload().length));
        connection.setDoOutput(true);

        connection.getOutputStream().write(dataProvider.getPayload());

        String body;
        InputStream responseStream = connection.getInputStream();
        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(responseStream))) {
            body = buffer.lines().collect(Collectors.joining("\n"));
        }

        if (connection.getResponseCode() == dataProvider.getExpectedResponseCode()) {

            return dataProvider.manageResponse(body);

        } else {

            return dataProvider.manageUnexpectedResponseCode(connection.getResponseCode(),
                                                             connection.getResponseMessage(),
                                                             body);
        }

    }

    public static <T> T retrieveDataViaHttpGet(URL url, ResponseDecoder<T> responseDecoder) throws Exception {

        try {

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // optional default is GET
            connection.setRequestMethod("GET");

            // add request header
            connection.setRequestProperty("User-Agent", USER_AGENT);

            String body;
            InputStream responseStream = connection.getInputStream();
            try (BufferedReader buffer = new BufferedReader(new InputStreamReader(responseStream))) {
                body = buffer.lines().collect(Collectors.joining("\n"));
            }

            if (connection.getResponseCode() == responseDecoder.getExpectedResponseCode()) {

                return responseDecoder.manageResponse(body);

            } else {

                return responseDecoder.manageUnexpectedResponseCode(connection.getResponseCode(),
                        connection.getResponseMessage(),
                        body);

            }

        } catch (Throwable t) {

            LOGGER.error("Unexpected Exception", t);

            throw new Exception("Unexpected Exception", t);

        }

    }

}
