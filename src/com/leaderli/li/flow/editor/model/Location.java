package com.leaderli.li.flow.editor.model;

import org.eclipse.draw2d.geometry.Point;

import com.leaderli.li.flow.util.GsonUtil;

public class Location {
	public Location() {
	}

	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	private int x;
	private int y;

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}


	@Override
	public String toString() {
		return GsonUtil.toJson(this);
	}

	


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
	}

	public static Location transfer(Point point) {
		return new Location(point.x(), point.y());
	}
	public static Point transfer(Location location) {
		return new Point(location.getX(), location.getY());
	}

}
