package com.leaderli.li.flow.editor;

import org.eclipse.core.runtime.Assert;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Handle;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.ui.parts.TreeViewer;

public class GraphicalTreeViewer extends TreeViewer implements GraphicalViewer {

	private boolean setRootEditPart = true;

	@Override
	public void setRootEditPart(RootEditPart editpart) {
		Assert.isLegal(!this.setRootEditPart, "RootEditPart should not be set ");
		super.setRootEditPart(editpart);
	}
	@Override
	public Handle findHandleAt(Point p) {
		Handle handle = null;
		final EditPart editPart = this.findObjectAt(p);
		if (editPart != null) {
			handle = new Handle() {

				@Override
				public DragTracker getDragTracker() {
					System.out.println("drag Tracker" + editPart.getDragTracker(null));

					return editPart.getDragTracker(null);
				}

				@Override
				public Point getAccessibleLocation() {
					return null;
				}
			};
		}
		return handle;
	}

}
