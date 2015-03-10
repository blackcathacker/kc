/*
 * Kuali Coeus, a comprehensive research administration system for higher education.
 * 
 * Copyright 2005-2015 Kuali, Inc.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.kuali.coeus.sys.framework.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.kuali.rice.core.web.listener.KualiInitializeListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;

public class KcServiceLocatorListener extends KualiInitializeListener {
    private static final org.apache.commons.logging.Log LOG = org.apache.commons.logging.LogFactory.getLog(KcServiceLocatorListener.class);

	public void contextDestroyed(ServletContextEvent sce) {}

    public void contextInitialized(ServletContextEvent sce) {
        LOG.debug("Starting KcServiceLocatorListener");
        super.contextInitialized(sce);
        KcServiceLocator.setAppContext(getContext());
    }
    
    /**
     * Translates context parameters from the web.xml into entries in a Properties file.
     */
    protected Properties getContextParameters(ServletContext context) {
        Properties properties = super.getContextParameters(context);
        Properties buildProps = new Properties();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("META-INF/coeus-impl-build.properties")) {
        	buildProps.load(is);
        	for (Map.Entry<Object, Object> prop : buildProps.entrySet()) {
        		context.setInitParameter(prop.getKey().toString(), prop.getValue().toString());
        	}
        	properties.putAll(buildProps);
        } catch (IOException e) {
        	throw new RuntimeException("Unable to read build properties", e);
        }
        

        
        return properties;
    }

}
