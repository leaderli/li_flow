package com.leaderli.li.flow.editor.policy;

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

public class ConnectionNodeSelectionEditPolicy  extends SelectionEditPolicy{

	@Override
	protected void hideSelection() {
		
		this.updateConnectionFigure(false);
	}

	private void updateConnectionFigure(boolean selected) {

		int width = selected?2:1;
		PolylineConnection conn = (PolylineConnection) this.getHostFigure();
		conn.setLineWidth(width);
		Display display = this.getHost().getViewer().getControl().getDisplay();
		conn.setForegroundColor(selected?display.getSystemColor(SWT.COLOR_RED):display.getSystemColor(SWT.COLOR_BLACK));
		
	}

	@Override
	protected void showSelection() {
		this.updateConnectionFigure(true);
		
	}
	
}
