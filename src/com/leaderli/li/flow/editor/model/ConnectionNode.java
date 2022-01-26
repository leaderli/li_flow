package com.leaderli.li.flow.editor.model;

public class ConnectionNode extends Node<FlowDiagram> {

	private GotoNode source;
	private FlowNode target;
	private FlowNode sourceFlowNode;

	public GotoNode getSource() {
		return source;
	}

	public void setSource(GotoNode source) {
		this.source = source;
		sourceFlowNode = source.getParent();
	}

	public FlowNode getSourceFlowNode() {
		return sourceFlowNode;
	}

	public FlowNode getTarget() {
		return target;
	}

	public void setTarget(FlowNode target) {
		if (source != null) {
			source.notifyChanged(GOTO_TYPE | CONNECTION_TARGET_ROLE, target, this.target);
		}
		this.target = target;
	}

	@Override
	public void notifyChanged() {
		if (source != null) {
			source.notifyChanged(GOTO_TYPE | CONNECTION_SOURCE_ROLE);
		}
		if (target != null) {
			target.notifyChanged(FLOW_TYPE | CONNECTION_TARGET_ROLE);
		}
	}

}
