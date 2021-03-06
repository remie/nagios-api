package net.collabsoft.nagios.cache;

import java.util.concurrent.TimeUnit;
import static junit.framework.TestCase.assertNotNull;
import net.collabsoft.nagios.AppConfig;
import org.jmock.MockObjectTestCase;
import org.junit.Test;
import org.junit.Assert.*;

public class TestCacheManager extends MockObjectTestCase {

    private static final int DEFAULT_REFRESH_INTERVAL=5;
    private static final TimeUnit DEFAULT_REFRESH_INTERVAL_UNIT= TimeUnit.MINUTES;
    private static final String DEFAULT_CACHE_ENTRY_VALUE = "someValue";
    private static final int TEST_REFRESH_INTERVAL=1;
    private static final TimeUnit TEST_REFRESH_INTERVAL_UNIT= TimeUnit.HOURS;
    private static final String TEST_CACHE_ENTRY_VALUE = "This is a (somewhat) random string";
    
    // ----------------------------------------------------------------------------------------------- Constructor

    @Override
    public void setUp() throws Exception {
        
    }
    
    @Override
    public void tearDown() {
        
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(CacheManagerImpl.getInstance());
    }

    @Test
    public void testProperties() {
        CacheManagerImpl cm = (CacheManagerImpl) CacheManagerImpl.getInstance();
        assertEquals(DEFAULT_REFRESH_INTERVAL, cm.getRefreshInterval());
        assertEquals(DEFAULT_REFRESH_INTERVAL_UNIT, cm.getRefreshIntervalUnit());
        
        cm.setRefreshInterval(TEST_REFRESH_INTERVAL);
        assertEquals(TEST_REFRESH_INTERVAL, cm.getRefreshInterval());
        
        cm.setRefreshIntervalUnit(TEST_REFRESH_INTERVAL_UNIT);
        assertEquals(TEST_REFRESH_INTERVAL_UNIT, cm.getRefreshIntervalUnit());
        
        assertNotNull(cm.getCache());
        cm.clear();
    }
    
    @Test
    public void testCache() {
        CacheManagerImpl cm = (CacheManagerImpl) CacheManagerImpl.getInstance();
        assertNotNull(cm.getCache());
        assertNotNull(cm.getCache());
        cm.clear();

        AppConfig.getInstance().setStateless(true);
        assertNotNull(cm.getCache());
        AppConfig.getInstance().setStateless(false);
        cm.clear();
    }
    
    @Test
    public void testCacheEntries() {
        CacheManagerImpl cm = (CacheManagerImpl) CacheManagerImpl.getInstance();
        
        MockCacheEntry cacheEntry = new MockCacheEntry();
        cacheEntry.setSomeValue(TEST_CACHE_ENTRY_VALUE);
        cm.setEntry(MockCacheEntry.CACHEKEY, cacheEntry);
        assertNotNull(cm.getEntry(MockCacheEntry.CACHEKEY));

        cacheEntry = (MockCacheEntry)cm.getEntry(MockCacheEntry.CACHEKEY);
        assertEquals(TEST_CACHE_ENTRY_VALUE, cacheEntry.getSomeValue());
        cm.clear();

        AppConfig.getInstance().setStateless(true);
        try {
            cm.setEntry(MockCacheEntry.CACHEKEY, cacheEntry);
            fail("This should have thrown an UnsupportedOperationException");
        } catch(UnsupportedOperationException ex) {
            // THIS IS SUPPOSED TO HAPPEN!
        }
        AppConfig.getInstance().setStateless(false);
        cm.clear();
        
        cacheEntry = (MockCacheEntry)cm.getEntry(MockCacheEntry.INVALID_CACHEKEY);
        assertNull(cacheEntry);
        cm.clear();
        
        cacheEntry = (MockCacheEntry)cm.getEntry(MockCacheEntry.CACHEKEY);
        assertNotNull(cacheEntry);
        assertEquals(DEFAULT_CACHE_ENTRY_VALUE, cacheEntry.getSomeValue());
        
        cacheEntry = (MockCacheEntry)cm.getEntry(MockCacheEntry.ALT_CACHEKEY);
        assertNotNull(cacheEntry);
        assertEquals(DEFAULT_CACHE_ENTRY_VALUE, cacheEntry.getSomeValue());
        
        cacheEntry = (MockCacheEntry)cm.getEntry(MockCacheEntry.ERROR_CACHEKEY);
        assertNull(cacheEntry);
    }
    
    @Test
    public void testRefresh() {
        CacheManagerImpl cm = (CacheManagerImpl) CacheManagerImpl.getInstance();
        
        AppConfig config = AppConfig.getInstance();
        config.setParserType(AppConfig.ParserType.FILE);
        MockCacheEntry cacheEntry = (MockCacheEntry)cm.getEntry(MockCacheEntry.CACHEKEY);
        assertNotNull(cacheEntry);

        cm.refresh();
        cacheEntry = (MockCacheEntry)cm.getEntry(MockCacheEntry.CACHEKEY);
        assertNotNull(cacheEntry);
        assertEquals(DEFAULT_CACHE_ENTRY_VALUE, cacheEntry.getSomeValue());
    }
    
    // ----------------------------------------------------------------------------------------------- Getters & Setters


    // ----------------------------------------------------------------------------------------------- Public methods


    // ----------------------------------------------------------------------------------------------- Private methods


    // ----------------------------------------------------------------------------------------------- Private Getters & Setters

}
