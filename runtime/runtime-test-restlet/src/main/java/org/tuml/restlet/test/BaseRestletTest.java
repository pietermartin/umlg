package org.tuml.restlet.test;

import org.eclipse.uml2.uml.Model;
import org.junit.After;
import org.junit.Before;
import org.tuml.framework.ModelLoadedEvent;
import org.tuml.framework.ModelLoader;
import org.restlet.Component;

import java.awt.*;

/**
 * Date: 2013/01/04
 * Time: 4:25 PM
 */
public abstract class BaseRestletTest implements ModelLoadedEvent {

    protected Component component;
    private boolean loaded = false;

    @Before
    public void before() throws Exception {
        this.component = instantiateComponent();
        this.component.start();
        if (!ModelLoader.INSTANCE.isLoaded()) {
            ModelLoader.INSTANCE.subscribeModelLoaderEvent(this);
            while (!this.loaded) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @After
    public void after() throws Exception {
        this.component.stop();
    }

    protected abstract Component instantiateComponent();

    @Override
    public void loaded(Model model) {
        this.loaded = true;
    }
}
