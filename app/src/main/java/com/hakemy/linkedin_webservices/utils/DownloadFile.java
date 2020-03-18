package com.hakemy.linkedin_webservices.utils;

import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Helper class for working with a remote server
 */
public  class DownloadFile {

    public static int responseCode = 0;


    /**
     * Returns text from a URL on a web server
     *
     *
     * @param user
     * @param password
     * @param address
     * @return
     * @throws IOException
     */
    public static String downloadUrl(String address, String user, String password) throws IOException {

        byte [] login=(user+":"+password).getBytes();

        StringBuilder stringBuilder =new StringBuilder().append("Basic ").append(Base64.encodeToString(login,Base64.DEFAULT));

        InputStream is = null;

        try {

            URL url = new URL(address); //wrapping url object
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.addRequestProperty("Authorization",stringBuilder.toString());
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

             responseCode = conn.getResponseCode();
            if (responseCode != 200)
            {
               throw new IOException("Got response code " + responseCode);

            }

            is = conn.getInputStream();
            return readStream(is);

        }
        catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Got response code " + responseCode);

        } finally {
            if (is != null) {
                is.close();
            }
        }

    }

    /**
     * Reads an InputStream and converts it to a String.
     *
     * @param stream
     * @return
     * @throws IOException
     */
    private static String readStream(InputStream stream) throws IOException {

        byte[] buffer = new byte[1024];
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        BufferedOutputStream out = null;
        try {
            int length = 0;
            out = new BufferedOutputStream(byteArray);
            while ((length = stream.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }
            out.flush();
            return byteArray.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

}