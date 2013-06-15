package org.umlg.restlet.test;

import org.eclipse.uml2.uml.Model;
import org.junit.After;
import org.junit.Before;
import org.umlg.framework.ModelLoadedEvent;
import org.umlg.framework.ModelLoader;
import org.restlet.Component;
import org.umlg.runtime.adaptor.TumlGraphManager;

/**
 * Date: 2013/01/04
 * Time: 4:25 PM
 */
public abstract class BaseRestletTest implements ModelLoadedEvent {

    protected Component component;
    private boolean loaded = false;

    @Before
    public void before() throws Exception {
        TumlGraphManager.INSTANCE.deleteGraph();
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
