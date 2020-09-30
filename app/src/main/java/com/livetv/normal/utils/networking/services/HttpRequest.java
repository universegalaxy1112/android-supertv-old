package com.livetv.normal.utils.networking.services;

import android.net.Uri;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpRequest {
    static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };
    private static HttpRequest m_HttpRInstante;
    private BufferedReader reader;
    private HttpURLConnection urlConnection;

    public static HttpRequest getInstance() {
        if (m_HttpRInstante == null) {
            m_HttpRInstante = new HttpRequest();
        }
        return m_HttpRInstante;
    }

    private HttpRequest() {
    }

    public String performRequest(String theUrl) {
        try {
            URL url = new URL(Uri.parse(theUrl).toString());
            if (theUrl.contains("https:")) {
                //trustAllHosts();
                HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                https.setHostnameVerifier(DO_NOT_VERIFY);
                this.urlConnection = https;
            } else {
                this.urlConnection = (HttpURLConnection) url.openConnection();
            }
            this.urlConnection.setRequestMethod("GET");
            this.urlConnection.connect();
            InputStream inputStream = this.urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                return null;
            }
            this.reader = new BufferedReader(new InputStreamReader(inputStream));
            while (true) {
                String line = this.reader.readLine();
                if (line == null) {
                    break;
                }
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                this.urlConnection.disconnect();
                inputStream.close();
                this.reader.close();
                this.urlConnection = null;
                return null;
            }
            this.urlConnection.disconnect();
            inputStream.close();
            this.reader.close();
            this.urlConnection = null;
            return buffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            this.urlConnection = null;
            return null;
        } catch (ProtocolException e2) {
            e2.printStackTrace();
            this.urlConnection = null;
            return null;
        } catch (IOException e3) {
            e3.printStackTrace();
            this.urlConnection = null;
            return null;
        }
    }

    public void trustAllHosts() {
        TrustManager[] trustAllCerts = {new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
