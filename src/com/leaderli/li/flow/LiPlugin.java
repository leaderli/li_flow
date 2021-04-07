package com.leaderli.li.flow;

import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.leaderli.li.flow.controller.CallflowController;
import com.leaderli.li.flow.listener.ResourceChangeListenerFactory;

/**
 * The activator class controls the plug-in life cycle
 */
public class LiPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.leaderli.li.flow"; //$NON-NLS-1$

	// The shared instance
	private static LiPlugin plugin;

	/**
	 * The constructor
	 */
	public LiPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		ResourceChangeListenerFactory.addResourceChangeListener();
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
		ResourceChangeListenerFactory.removeResourceChangeListener();
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static LiPlugin getDefault() {
		return plugin;
	}
	public static Display getStandardDisplay() {
        Display display = Display.getCurrent();
        if (display != null) {
            return display;
        }
        Thread[] allThreads = new Thread[Thread.activeCount()];
        for (Thread findDisplay : allThreads) {
            display = Display.findDisplay(findDisplay);
            if (display != null) {
                break;
            }
        }
        if (display == null) {
            return Display.getDefault();
        }
        return display;
    }

	public CallflowController getCallflowController() {
		return new CallflowController();
	}

}
