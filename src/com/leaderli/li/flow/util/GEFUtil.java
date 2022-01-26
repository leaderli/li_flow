package com.leaderli.li.flow.util;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.swt.widgets.Display;

import com.leaderli.li.flow.LiPlugin;

public class GEFUtil {

	public static final String FLOW_EXTENSION = ".li";
	public static final String FLOW_FLODER = "flow";

	/**
	 * 
	 * 鼠标的坐标
	 */
	public static Point getCursorPointAtDiagram(EditPartViewer viewer) {
		FigureCanvas figureCanvas = (FigureCanvas) viewer.getControl();
		Display display = LiPlugin.getStandardDisplay();
		org.eclipse.swt.graphics.Point locationAtDisplay = viewer.getControl().toControl(display.getCursorLocation());
		Point locationOfView = figureCanvas.getViewport().getViewLocation();
		Point locationAtDiagram = new Point(locationAtDisplay.x + locationOfView.x,
				locationAtDisplay.y + locationOfView.y);
		return locationAtDiagram;
	}

	public static void createFolderRecursion(IContainer container) {
		if (!container.exists() && container instanceof IFolder) {
			createFolderRecursion(container.getParent());
			try {
				((IFolder) container).create(false, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

	}

	public static IFolder getFlowFolder(IProject project) {
		IFolder flowFolder = project.getFolder(GEFUtil.FLOW_FLODER);
		createFolderRecursion(flowFolder);
		return flowFolder;
	}

	public static IFile getFlowFile(IProject project, String flow) {
		return getFlowFolder(project).getFile(flow + FLOW_EXTENSION);
	}
}
