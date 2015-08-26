package com.adobe.custom.connect.connect_url_shortner.core.impl.listeners;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;
import javax.jcr.observation.ObservationManager;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.sling.jcr.api.SlingRepository;
import org.osgi.service.component.ComponentContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A component to demonstrate how changes in the repository
 * can be listened for. It registers a JCR listener on activation.
 * The component is activated immediately after the bundle is
 * started. On deactivation the listener is unregistered. 
 */
@Component(immediate = true)
public class SimpleRepositoryListener implements EventListener {

    private final Logger LOGGER = LoggerFactory.getLogger(SimpleRepositoryListener.class);

    @Reference
    private SlingRepository repository;

    private Session session;
    private ObservationManager observationManager;

    public void onEvent(EventIterator it) {
        while (it.hasNext()) {
            Event event = it.nextEvent();
            try {
                LOGGER.debug("node event: {}", event.getPath());
            }
            catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
    }
    
    protected void activate(ComponentContext context) throws Exception {
        session = repository.loginAdministrative(null);
        observationManager = session.getWorkspace().getObservationManager();
        /*
         * Event types are bitwise OR'ed. The last parameter prevents changes done by this session to be sent to the
         * listener (which would result in an endless loop in this case)
         */
        observationManager.addEventListener(this, Event.NODE_ADDED | Event.NODE_REMOVED, "/content", true, null,
            null, true);
        LOGGER.debug("added JCR event listener");
    }

    protected void deactivate(ComponentContext componentContext) {
        try {
            if (observationManager != null) {
                observationManager.removeEventListener(this);
                LOGGER.debug("removed JCR event listener");
            }
        }
        catch (RepositoryException re) {
            LOGGER.error("error removeing the JCR event listener", re);
        }
        finally {
            if (session != null) {
                session.logout();
                session = null;
            }
        }
    }
}

