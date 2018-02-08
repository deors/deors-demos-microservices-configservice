package deors.demos.microservices.configservice;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ConfigserviceApplicationTestCase {

    @Test
    public void testIsAlive() {
        assertTrue(new ConfigserviceApplication().isAlive());
    }
}
