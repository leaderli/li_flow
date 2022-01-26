package com.leaderli.li.flow.editor.command;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.geometry.Dimension;

import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;

public class FlowNodeDeleteCommand extends ModelCommand<FlowNode,FlowDiagram> {
	private static final Dimension defaultDimension = new Dimension(-1, -1);

	private FlowDiagram parent;

	private List<ConnectionNode> removedConnectionNode = new ArrayList<>();

	@Override
	public void setParent(FlowDiagram parent) {
		this.parent = parent;
	}
	

	@Override
	public void execute() {
		parent.removeFlowNode(model);
		// 删除该node相关的线条
		parent.getConnectionNodes().stream().filter(connection -> connection.getTarget() == model
				|| connection.getSourceFlowNode() == model)
				.forEach(removedConnectionNode::add);
		
		removedConnectionNode.forEach(parent::removeConnectionNode);
	}

	@Override
	public void undo() {
		parent.addFlowNode(model);
		removedConnectionNode.forEach(parent::addConnectionNode);
	}

}
