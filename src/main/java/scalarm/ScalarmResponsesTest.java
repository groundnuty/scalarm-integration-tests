package scalarm;

import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import sun.net.www.http.HttpClient;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.logging.Level;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.apache.http.conn.ssl.SSLSocketFactory;


public class ScalarmResponsesTest {

    String storageManagerIp = "https://149.156.10.32:16450/status" ;
    String informationServiceIp = "https://149.156.10.32:31034/experiment_managers" ;
    String simulationManagerIp = "149.156.10.32" ;
    String experimentManagerIp = "https://149.156.10.32:50047/status" ;


    @Before
    public void initializeIps() {
        storageManagerIp = System.getProperty("storageManagerIp");
        informationServiceIp = System.getProperty("informationServiceIp");
        simulationManagerIp = System.getProperty("simulationManagerIp");
        experimentManagerIp = System.getProperty("experimentManagerIp");
    }

    public DefaultHttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }


    @Test
    public void storageManagerTest() throws IOException {

        DefaultHttpClient client = getNewHttpClient();

        String storageManagerUrl = storageManagerIp;
        HttpGet request = new HttpGet(storageManagerUrl);
        HttpResponse response = client.execute(request);
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        ScalarmIntegration.logger.log(Level.INFO,responseString);

        assertEquals(200, response.getStatusLine().getStatusCode());

    }

    @Test
    public void informationServiceTest() throws IOException {

        DefaultHttpClient client = getNewHttpClient();

        String storageManagerUrl = informationServiceIp;
        HttpGet request = new HttpGet(storageManagerUrl);
        HttpResponse response = client.execute(request);
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        ScalarmIntegration.logger.log(Level.INFO,responseString);

        assertEquals(200, response.getStatusLine().getStatusCode());


    }

    @Test
    public void experimentManagerTest() throws IOException {

        DefaultHttpClient client = getNewHttpClient();

        String storageManagerUrl = experimentManagerIp;
        HttpGet request = new HttpGet(storageManagerUrl);
        HttpResponse response = client.execute(request);
        String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");
        ScalarmIntegration.logger.log(Level.INFO,responseString);

        assertEquals(200, response.getStatusLine().getStatusCode());


    }

    @Test
    public void simulationManagerTest() {

        String remote = simulationManagerIp;
        //String hostname = remote.getHostName();
        int port = 443;//1830;
        try {
            Socket s = new Socket();
            s.connect(new InetSocketAddress(remote, port), 20000);
            ScalarmIntegration.logger.log(Level.INFO," simulationManager is listening on port " + port + " of " + remote);
            s.close();
        } catch (IOException ex) {
            ScalarmIntegration.logger.log(Level.INFO," simulationManager is not listening on port " + port + " of " + remote);
            assertFalse(true);
        }
    }

}

