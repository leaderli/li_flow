package com.leaderli.li.flow.image;

import org.eclipse.swt.graphics.Image;

import com.leaderli.li.flow.util.ImageUtil;

public class TabImageProvider {

	private Image image;

	private TabImageProvider(Image image) {
		super();
		this.image = image;
	}

	public Image getImage() {
		return image;
	}

	public static TabImageProvider instance(String image) {
		return new TabImageProvider(ImageUtil.getImage(image));
	}
}
