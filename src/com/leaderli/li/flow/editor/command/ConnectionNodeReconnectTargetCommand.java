package com.leaderli.li.flow.editor.command;

import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;

public class ConnectionNodeReconnectTargetCommand extends ModelCommand<ConnectionNode,FlowDiagram> {

	private FlowNode oldTarget, newTarget;



	@Override
	public void execute() {

		if (oldTarget == null) {
			oldTarget = getModel().getTarget();
		}
		getModel().setTarget(newTarget);
		
		getModel().notifyChanged();

	}

	@Override
	public void undo() {
		getModel().setTarget(oldTarget);
		getModel().notifyChanged();

	}

	public void setNewTarget(FlowNode newTarget) {
		this.newTarget = newTarget;
	}
}
