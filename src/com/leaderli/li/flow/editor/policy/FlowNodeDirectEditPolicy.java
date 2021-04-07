package com.leaderli.li.flow.editor.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.DirectEditPolicy;
import org.eclipse.gef.requests.DirectEditRequest;

import com.leaderli.li.flow.editor.command.FlowNodeRenameCommand;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.part.FlowNodeEditPart;

class FlowNodeDirectEditPolicy extends DirectEditPolicy {
	/**
	 * 
	 */
	private final FlowNodeEditPart FlowNodeDirectEditPolicy;

	/**
	 * @param flowNodeEditPart
	 */
	FlowNodeDirectEditPolicy(FlowNodeEditPart flowNodeEditPart) {
		FlowNodeDirectEditPolicy = flowNodeEditPart;
	}

	@Override
	protected void showCurrentEditValue(DirectEditRequest request) {
	}

	@Override
	protected Command getDirectEditCommand(DirectEditRequest request) {
		FlowNodeRenameCommand command = new FlowNodeRenameCommand();
		command.setModel((FlowNode) getHost().getModel());
		command.setNewName((String) request.getCellEditor().getValue());
		return command;
	}
}