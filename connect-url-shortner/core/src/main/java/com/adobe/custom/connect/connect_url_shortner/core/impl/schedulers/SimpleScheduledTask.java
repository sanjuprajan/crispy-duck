package com.adobe.custom.connect.connect_url_shortner.core.impl.schedulers;

import java.util.Dictionary;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.commons.osgi.PropertiesUtil;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simple demo for cron-job like tasks that get executed regularly.
 * It also demonstrates how property values can be set. Users can
 * set the property values in /system/console/configMgr
 */
@Component(immediate = true, metatype = true, label = "A scheduled task")
@Service(value = Runnable.class)
@Property(name = "scheduler.expression", value = "*/30 * * * * ?")
public class SimpleScheduledTask implements Runnable {

    private final Logger LOGGER = LoggerFactory.getLogger(SimpleScheduledTask.class);
    
    @Override
    public void run() {
        LOGGER.debug("SimpleScheduledTask is now running, myParameter='{}'", this.myParameter);
    }
    
    @Property(label = "A parameter", description = "Can be configured in /system/console/configMgr")
    public static final String MY_PARAMETER = "myParameter";
    private String myParameter;
    
    protected void activate(ComponentContext componentContext) {
        configure(componentContext.getProperties());
    }

    protected void configure(Dictionary<?, ?> properties) {
        this.myParameter = PropertiesUtil.toString(properties.get(MY_PARAMETER), null);
        LOGGER.debug("configure: myParameter='{}''", this.myParameter);
    }
}
