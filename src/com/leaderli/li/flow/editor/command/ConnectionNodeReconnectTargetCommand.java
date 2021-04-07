package com.leaderli.li.flow.editor.command;

import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;

public class ConnectionNodeReconnectTargetCommand extends ModelCommand<ConnectionNode,FlowDiagram> {

	private FlowNode oldTarget, newTarget;



	@Override
	public void execute() {

		if (oldTarget == null) {
			oldTarget = getModel().getParent().getRegisterNode(getModel().getTargetID());
		}
		getModel().setTargetID(newTarget.getId());
		getModel().setTargetFlowNodeID(newTarget.getId());
		
		getModel().notifyChanged();

	}

	@Override
	public void undo() {
		getModel().setTargetID(oldTarget.getId());
		getModel().setTargetFlowNodeID(oldTarget.getId());
		getModel().notifyChanged();

	}

	public void setNewTarget(FlowNode newTarget) {
		this.newTarget = newTarget;
	}
}
