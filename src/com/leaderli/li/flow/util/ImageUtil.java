package com.leaderli.li.flow.util;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;

import com.leaderli.li.flow.LiPlugin;

public class ImageUtil {
	public static final ImageRegistry IMAGE_REGISTRY = LiPlugin.getDefault().getImageRegistry();

	public static Image getImage(String name) {

		getImageDescriptor(name);
		return IMAGE_REGISTRY.get(name);
	}

	public static ImageDescriptor getImageDescriptor(String name) {

		ImageDescriptor imageDescriptor = IMAGE_REGISTRY.getDescriptor(name);
		Image image = IMAGE_REGISTRY.get(name);
		if (imageDescriptor == null) {
			try {
				imageDescriptor = ImageDescriptor
						.createFromURL(new URL(LiPlugin.getDefault().getBundle().getEntry("/"), "icon/" + name));
				IMAGE_REGISTRY.put(name, imageDescriptor);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}
		return imageDescriptor;
	}
}
