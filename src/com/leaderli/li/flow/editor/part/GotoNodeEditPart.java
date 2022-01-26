package com.leaderli.li.flow.editor.part;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.OrderedLayout;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

import com.leaderli.li.flow.constant.PluginConstant;
import com.leaderli.li.flow.editor.FlowEditor;
import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.GotoNode;
import com.leaderli.li.flow.editor.model.ModelRole;
import com.leaderli.li.flow.editor.policy.GotoNodeGraphicalNodeEditPolicy;
import com.leaderli.li.flow.listener.DefaultTypeNotifyListener;
import com.leaderli.li.flow.util.ExtendedChopboxAnchor;
import com.leaderli.li.flow.util.ImageUtil;

public class GotoNodeEditPart extends GenericsEditPart<GotoNode, FlowEditor> implements NodeEditPart, ModelRole {

	IFigure outputTerminal = null;
	Label terminalLabel = null;

	public IFigure getOutputTerminal() {
		if (outputTerminal == null) {
			outputTerminal = new Label(ImageUtil.getImage("outputright.gif"));
		}
		return outputTerminal;
	}

	public Label getTerminalLabel() {
		if (terminalLabel == null) {
			terminalLabel = new Label(getModel().getName());
		}
		return terminalLabel;
	}

	@Override
	protected IFigure createFigure() {

		IFigure figure = new Figure();
		FlowLayout layout = new FlowLayout(true);
		layout.setMinorSpacing(1);
		layout.setMinorAlignment(OrderedLayout.ALIGN_BOTTOMRIGHT);
		figure.setLayoutManager(layout);

		figure.add(getTerminalLabel());
		figure.add(getOutputTerminal());
		return figure;
	}

	@Override
	protected void refreshVisuals() {
		// 没有连接线的gotoNode用红色表示
		if (getModel().getLinkedConnectionNode() == null) {
			figure.setForegroundColor(new Color(Display.getDefault(), new RGB(255, 0, 0)));
		} else {
			figure.setForegroundColor(new Color(Display.getDefault(), new RGB(0, 0, 0)));

		}
		getTerminalLabel().setText(getModel().getName());
		super.refreshVisuals();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new GotoNodeGraphicalNodeEditPolicy());

	}

	@Override
	protected List<ConnectionNode> getModelSourceConnections() {
		List<ConnectionNode> connectionNodes = ((FlowDiagramEditPart) getParent().getParent()).getModel()
				.getConnectionNodes();
		return connectionNodes.stream().filter(connectionNode -> getModel() == connectionNode.getSource())
				.collect(Collectors.toList());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(ConnectionEditPart connection) {
		return new ExtendedChopboxAnchor(getOutputTerminal(), PluginConstant.CHOPBOX_ANCHOR_RIGHT);

	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new ExtendedChopboxAnchor(getOutputTerminal(), PluginConstant.CHOPBOX_ANCHOR_RIGHT);
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(ConnectionEditPart connection) {
		return null;
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return null;
	}
	@Override
	protected void initAfterSetModel() {
		addNotifyListener(() -> {
			refreshVisuals();
			refreshSourceConnections();
		});
	}

	private class ConnectionSourceGotoNodeAdapter implements DefaultTypeNotifyListener {

		@Override
		public void notifyChanged() {
		}

		@Override
		public int typeAndRole() {
			return GOTO_TYPE | CONNECTION_TARGET_ROLE;
		}



	}

	private class RenameGotoNodeAdapter implements DefaultTypeNotifyListener {

		@Override
		public void notifyChanged() {
			refreshVisuals();
		}

		@Override
		public int typeAndRole() {
			return GOTO_TYPE | NAME_ROLE;
		}

	}

}