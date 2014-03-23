package org.mockito.configuration;

import org.mockito.configuration.DefaultMockitoConfiguration;

/**
 * 
 * @author Alexandre
 * 
 * Created this configuration to avoid problems when mocking javax.mail.Store class
 *
 */
public class MockitoConfiguration extends DefaultMockitoConfiguration {

	@Override
	public boolean enableClassCache() {
		return false;
	}
}
