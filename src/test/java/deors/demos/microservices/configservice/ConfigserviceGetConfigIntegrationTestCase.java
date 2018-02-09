package deors.demos.microservices.configservice;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Test;

public class ConfigserviceGetConfigIntegrationTestCase {

    @Test
    public void testGetEnv() {

        String testTargetUrl = getConfigurationProperty("TEST_TARGET_URL", "test.target.url", "http://localhost:8888");

        Client client = ClientBuilder.newClient();
        Response response = client.target(testTargetUrl + "/env")
                .request()
                .accept(MediaType.APPLICATION_JSON).buildGet().invoke();

        Map output = response.readEntity(Map.class);

        assertEquals(200, response.getStatus());
        assertTrue(output.get("applicationConfig: [classpath:/application.properties]") instanceof Map);
        assertTrue(((Map) output.get("applicationConfig: [classpath:/application.properties]")).containsKey("server.port"));
    }

    @Test
    public void testGetConfigServiceConf() {

        String testTargetUrl = getConfigurationProperty("TEST_TARGET_URL", "test.target.url", "http://localhost:8888");

        Client client = ClientBuilder.newClient();
        Response response = client.target(testTargetUrl + "/configservice/default")
                .request()
                .accept(MediaType.APPLICATION_JSON).buildGet().invoke();

        Map output = response.readEntity(Map.class);

        assertEquals(200, response.getStatus());
        assertTrue(output.get("propertySources") instanceof List);
        assertTrue(((List) output.get("propertySources")).size() == 1);
        assertTrue(((List) output.get("propertySources")).get(0) instanceof Map);
        assertTrue(((Map) ((List) output.get("propertySources")).get(0)).containsKey("name"));
        assertEquals("https://github.com/deors/deors-demos-microservices-configstore.git/application.properties",
            ((Map) ((List) output.get("propertySources")).get(0)).get("name"));
    }

    private static String getConfigurationProperty(String envKey, String sysKey, String defValue) {

        String retValue = defValue;
        String envValue = System.getenv(envKey);
        String sysValue = System.getProperty(sysKey);
        // system property prevails over environment variable
        if (sysValue != null) {
            retValue = sysValue;
        } else if (envValue != null) {
            retValue = envValue;
        }
        return retValue;
    }
}
