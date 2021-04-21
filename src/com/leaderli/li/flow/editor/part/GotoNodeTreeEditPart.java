package com.leaderli.li.flow.editor.part;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.Image;

import com.leaderli.li.flow.constant.PluginConstant;
import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.GotoNode;
import com.leaderli.li.flow.editor.policy.GotoNodeComponentEditPolicy;
import com.leaderli.li.flow.util.ImageUtil;

public class GotoNodeTreeEditPart extends GenericsEditPart<GotoNode> {
	protected Image getImage() {
		return ImageUtil.getImage("goto.gif");
	}

	protected String getText() {
		GotoNode gotoNode = getModel();

		FlowDiagram dialgram = gotoNode.getParent().getParent();
		ConnectionNode connectionNode = gotoNode.getLinkedConnectionNode();
		String next = "not set ";
		if (connectionNode != null) {
			next = connectionNode.getTargetFlowNodeID().getName();
		}
		return "Goto <name=\"" + gotoNode.getName() + "\", next=\"" + next + "\">";
	}

	@Override
	protected IFigure createFigure() {
		Figure gotoFigure = new Figure();
		gotoFigure.setLayoutManager(new FlowLayout(true));
		gotoFigure.add(new Label(ImageUtil.getImage(PluginConstant.ICON_GOTONODE)));
		gotoFigure.add(new Label(getText()));

		return gotoFigure;
	}
	@Override
	protected void refreshVisuals() {
		// 获取当前的图形以及model,并根据model中的坐标和大小，在画布上绘制
		Figure figure = (Figure) getFigure();
		AbstractGraphicalEditPart parent = (AbstractGraphicalEditPart) getParent();
		int index = parent.getChildren().indexOf(this);
		parent.setLayoutConstraint(this, figure, new Rectangle(20, 30 * index + 10, 400, -1));
		super.refreshVisuals();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new GotoNodeComponentEditPolicy());

	}


}
