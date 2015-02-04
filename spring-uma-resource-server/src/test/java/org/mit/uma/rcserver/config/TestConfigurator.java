package org.mit.uma.rcserver.config;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

//import static org.junit.Assert.assertThat;
//import static org.junit.Assert.fail;

//import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.ContextConfiguration;

import org.mit.uma.rcserver.config.Configurator;

//@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(
  {
	"file:src/main/webapp/WEB-INF/application-context.xml",
	"file:src/main/webapp/WEB-INF/dispatcher-servlet.xml"
  }
)
@RunWith(SpringJUnit4ClassRunner.class)
public class TestConfigurator {
	
	@Autowired
	private Configurator config;

	@Test
	public void getClientNameTest() {
		assertTrue(config.getThisServerName().length()>0);
	}
}
