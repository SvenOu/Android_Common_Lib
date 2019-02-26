package com.sv.common.web.client;

import android.util.Log;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.springframework.util.StringUtils;


/**
 * <p>
 * 创建HttpClient的工厂类。
 * <br/>
 * <br/>
 * 可以设置参数：<br/>
 * {@link #setHttpsPort(int)} HTTPS协议所用端口；<br/>
 * {@link #setHttpPort(int)} HTTP协议所用端口；<br/>
 * {@link #setConnectionTimeout(int)} 连接超时时长，默认10秒；<br/>
 * {@link #setSocketTimeout(int)} 请求超时时长，默认30秒。<br/>
 * <p>
 * </p>
 */
public class HttpClientFactory {
    private static final String TAG = HttpClientFactory.class.getSimpleName();

    private static final int HTTPS_DEFAULT_PORT = 443;

    private static final int HTTP_DEFAULT_PORT = 80;

    private int httpsPort = HTTPS_DEFAULT_PORT;

    private int httpPort = HTTP_DEFAULT_PORT;

    private int connectionTimeout = 10000;

    private int socketTimeout = 30000;

    private CloseableHttpClient closeableHttpClient;

    /**
     * <p>
     * 根据{@code url}创建 {@code CloseableHttpClient}实例 。
     * url必须使用http或者https协议。
     * <p>
     * 连接超时时长，默认10秒；请求超时时长，默认30秒。<br/>
     * 可以自定这两个参数，且需要重新创一个实例才生效。
     * <p>
     * </p>
     */
    public CloseableHttpClient create(String url) throws Exception {
        initByUrl(url);
        HttpClientBuilder builder = HttpClientBuilder.create();
        RequestConfig requestConfig = RequestConfig
                .custom()
                .setConnectTimeout(connectionTimeout)
                .setSocketTimeout(socketTimeout)
                .build();
        SSLSocketFactoryEx sslSocketFactoryEx = new SSLSocketFactoryEx();

        // 用于过滤证书和域名
        SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(
                sslSocketFactoryEx.getSslContext(),
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        builder.setSSLSocketFactory(sslConnectionFactory);

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", sslConnectionFactory)
                .build();

        HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(registry);
        builder.setConnectionManager(ccm);
        builder.setDefaultRequestConfig(requestConfig);
        closeableHttpClient = builder.build();
        return closeableHttpClient;
    }

    /**
     * Shutdown the {@code ConnectionManager}
     */
    public void shutdown() {
        closeableHttpClient.getConnectionManager().shutdown();
    }

    /**
     * <p>
     * 根据url初始化创建HttpClient所需要的协议和端口
     * url使用的协议必须是http或者https。
     * </p>
     *
     * @param url
     * @throws Exception url使用的协议必须是http或者https。
     */
    private void initByUrl(String url) throws Exception {
        if (StringUtils.hasText(url)) {
            java.net.URL jurl = new java.net.URL(url);

            String protocol = jurl.getProtocol();
            if (!"http".equalsIgnoreCase(protocol) && !"https".equalsIgnoreCase(protocol)) {
                throw new IllegalArgumentException("unsupported protocol");
            }

            int port = jurl.getPort();
            Log.d(TAG, "Port of URL: " + port);
            if ("http".equalsIgnoreCase(protocol) && 0 < port) {
                this.httpPort = port;
            } else if ("https".equalsIgnoreCase(protocol) && 0 < port) {
                this.httpsPort = port;
            }
        }
    }

    /**
     * 获取HTTP使用的端口
     *
     * @return the httpPort
     */
    public int getHttpPort() {
        if (0 <= httpPort) {
            return HTTP_DEFAULT_PORT;
        }
        return httpPort;
    }

    /**
     * 设置HTTP使用的端口
     *
     * @param httpPort
     */

    public void setHttpPort(int httpPort) {
        if (0 != httpPort) {
            this.httpPort = httpPort;
        }
    }

    /**
     * 获取HTTPS使用的端口
     *
     * @return the httpsPort
     */
    public int getHttpsPort() {
        if (0 <= httpsPort) {
            return HTTPS_DEFAULT_PORT;
        }
        return httpsPort;
    }

    /**
     * 设置HTTPS使用的端口
     *
     * @param httpsPort the httpsPort to set
     */
    public void setHttpsPort(int httpsPort) {
        if (0 != httpsPort) {
            this.httpsPort = httpsPort;
        }
    }

    /**
     * 获取连接超时时限值
     *
     * @return the connectionTimeout
     */
    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    /**
     * 设置连接超时时限值
     *
     * @param connectionTimeout the connectionTimeout to set
     */
    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    /**
     * 获取请求超时时限值
     *
     * @return the socketTimeout
     */
    public int getSocketTimeout() {
        return socketTimeout;
    }

    /**
     * 设置请求超时时限值
     *
     * @param socketTimeout the socketTimeout to set
     */
    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }
}