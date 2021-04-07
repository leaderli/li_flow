package com.leaderli.li.flow.listener;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;

import com.leaderli.li.flow.LiPlugin;

public class ResourceChangeListenerFactory {

	static List<IResourceChangeListener> POST_CHANGE_LISTENERS = new ArrayList<>();

	static {
		POST_CHANGE_LISTENERS.add(LiPlugin.getDefault().getCallflowController());
	}

	public static void addResourceChangeListener() {
		POST_CHANGE_LISTENERS.forEach(listener -> {
			ResourcesPlugin.getWorkspace().addResourceChangeListener(listener, IResourceChangeEvent.POST_CHANGE);
		});

	}

	public static void removeResourceChangeListener() {
		POST_CHANGE_LISTENERS.forEach(listener -> {
			ResourcesPlugin.getWorkspace().removeResourceChangeListener(listener);
		});

	}


}
