/*
 * Copyright (c) 2016-2018. Uniquid Inc. or its affiliates. All Rights Reserved.
 *
 * License is in the "LICENSE" file accompanying this file.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package com.uniquid.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.stream.Collectors;

public class HttpUtils {

    private static Logger LOGGER = LoggerFactory.getLogger(HttpUtils.class);

    private static final String USER_AGENT = "UNIQUID-UTILS-0.1";

    private static final int CONNECTION_TIMEOUT = 10 *1000;
    private static final int READ_TIMEOUT = 15 *1000;

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

    public static <T> T downloadFile(URL url, @Nullable String path, ResponseDecoder<T> responseDecoder) throws Exception {

        try {

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // optional default is GET
            connection.setRequestMethod("GET");

            // add request header
            connection.setRequestProperty("User-Agent", USER_AGENT);

            connection.setConnectTimeout(CONNECTION_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);

            String fileName = "";
            String disposition = connection.getHeaderField("Content-Disposition");
            if (disposition != null) {
                // extracts file name from header field
                int index = disposition.indexOf("filename=");
                if (index > 0) {
                    fileName = disposition.substring(index + 10,
                            disposition.length() - 1);
                }
            } else {
                throw new Exception("Exception");
            }
            String saveFilePath = path != null ? path + File.separator + fileName : fileName;
            // opens an output stream to save into file
            FileOutputStream outputStream = new FileOutputStream(saveFilePath);

            InputStream inputStream = connection.getInputStream();

            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            outputStream.close();
            inputStream.close();

            if (connection.getResponseCode() == responseDecoder.getExpectedResponseCode()) {
                return responseDecoder.manageResponse(saveFilePath);
            } else {
                return responseDecoder.manageUnexpectedResponseCode(connection.getResponseCode(),
                        connection.getResponseMessage(),
                        outputStream.toString());
            }

        } catch (SocketTimeoutException e) {
            LOGGER.error("Unexpected Exception", e);
            throw new Exception("Unexpected Exception", e);
        }
    }

}
