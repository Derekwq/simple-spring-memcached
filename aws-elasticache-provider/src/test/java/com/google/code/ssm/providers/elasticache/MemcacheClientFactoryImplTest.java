/* Copyright (c) 2014-2016 Jakub Białek
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the
 * Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.google.code.ssm.providers.elasticache;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.List;

import net.spy.memcached.FailureMode;

import org.junit.Before;
import org.junit.Test;

import com.google.code.ssm.providers.CacheClient;
import com.google.code.ssm.providers.CacheConfiguration;
import com.google.code.ssm.providers.elasticache.MemcacheClientFactoryImpl;
import com.google.code.ssm.providers.elasticache.ElastiCacheConfiguration;

/**
 * 
 * @author Jakub Białek
 * 
 */
public class MemcacheClientFactoryImplTest {

    private MemcacheClientFactoryImpl factory;

    private List<InetSocketAddress> addrs;

    @Before
    public void setUp() {
        factory = new MemcacheClientFactoryImpl();
        addrs = Collections.singletonList(new InetSocketAddress(12345));
    }

    @Test
    public void createWithBaseConf() throws IOException {
        CacheConfiguration conf = new CacheConfiguration();
        conf.setConsistentHashing(true);
        conf.setOperationTimeout(1000);
        conf.setUseBinaryProtocol(false);

        try {
            CacheClient client = factory.create(addrs, conf);

            assertNotNull(client);
            client.shutdown();
        } catch (RuntimeException ex) {
            printException(ex);
            throw ex;
        }
    }

    @Test
    public void createWithSpecificConf() throws IOException {
        ElastiCacheConfiguration conf = new ElastiCacheConfiguration();
        conf.setConsistentHashing(true);
        conf.setOperationTimeout(1000);
        conf.setUseBinaryProtocol(false);
        conf.setFailureMode(FailureMode.Cancel);
        conf.setShouldOptimize(true);
        conf.setMaxReconnectDelay(1000L);
        conf.setTimeoutExceptionThreshold(100);
        conf.setUseNagleAlgorithm(false);

        CacheClient client = factory.create(addrs, conf);

        assertNotNull(client);
        client.shutdown();
    }
    
    private void printException(final Exception ex) {
        ex.printStackTrace();
        Throwable t = ex;
        while (t.getCause() != null) {
            System.err.println("Cause: ");
            t = t.getCause();
            t.printStackTrace();
        }
    }

}
