package com.example;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class GreetingAppTest {
    @Test
    public void testMessageContainsEnv() {
        String msg = GreetingApp.getMessage("PROD");
        assertTrue(msg.contains("PROD"));
    }
}
