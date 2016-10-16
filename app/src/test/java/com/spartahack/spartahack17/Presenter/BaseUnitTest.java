package com.spartahack.spartahack17.Presenter;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created by ryancasler on 10/12/16
 * SpartaHack2016-Android
 */
@RunWith(PowerMockRunner.class)
public class BaseUnitTest {

    protected Throwable throwable;

    @Before public void before() throws Exception {
        // Mockito has a very convenient way to inject mocks by using the @Mock annotation. To
        // inject the mocks in the test the initMocks method needs to be called.
        MockitoAnnotations.initMocks(this);

        throwable = new Throwable("Test throwable");
    }
}
