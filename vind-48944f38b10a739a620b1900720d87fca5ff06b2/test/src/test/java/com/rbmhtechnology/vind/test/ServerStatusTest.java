package com.rbmhtechnology.vind.test;

import com.rbmhtechnology.vind.api.result.StatusResult;
import org.junit.Rule;
import org.junit.Test;

import static com.rbmhtechnology.vind.test.Backend.Solr;
import static org.junit.Assert.assertEquals;

public class ServerStatusTest {

    @Rule
    public TestBackend backend = new TestBackend();

    @Test
    @RunWithBackend(Solr)
    public void testPing() {
        StatusResult statusResult = backend.getSearchServer().getBackendStatus();
        assertEquals( StatusResult.Status.UP, statusResult.getStatus());
        assertEquals(0, statusResult.getDetails().get("status"));
    }
}
