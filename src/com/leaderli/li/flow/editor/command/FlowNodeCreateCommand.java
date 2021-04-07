package com.leaderli.li.flow.editor.command;

import com.leaderli.li.flow.constant.PluginConstant;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;
import com.leaderli.li.flow.editor.model.Location;

public class FlowNodeCreateCommand extends ModelCommand<FlowNode, FlowDiagram> {
	private Location location;

	private FlowDiagram parent;

	@Override
	public void setParent(FlowDiagram parent) {
		this.parent = parent;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public void undo() {
		parent.removeFlowNode(model);
	}

	private void setUpFlowNode() {
		model.setPoint(location);
		model.setId(parent.spanningNextNodeID());
		initGotoNodesByType();
	}

	@Override
	public void execute() {

		// 防止redo时，重复执行的问题
		if (model.getId() == PluginConstant.NULL_NODE) {
			setUpFlowNode();
		}
		parent.addFlowNode(model);
	}

	private void initGotoNodesByType() {

		if (PluginConstant.TYPE_SERVLET.equals(model.getType())
				|| PluginConstant.TYPE_SUBFLOW_ENTRY.equals(model.getType())) {
			GotoNode gotoNode = new GotoNode();
			gotoNode.setId(parent.spanningNextNodeID());
			gotoNode.setName("default");
			model.addGotoNode(gotoNode);
		}
	}

}
