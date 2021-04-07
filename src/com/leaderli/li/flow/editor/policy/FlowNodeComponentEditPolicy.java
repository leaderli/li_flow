package com.leaderli.li.flow.editor.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.leaderli.li.flow.editor.command.FlowNodeDeleteCommand;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;

public class FlowNodeComponentEditPolicy extends ComponentEditPolicy {

	@Override
	protected Command createDeleteCommand(GroupRequest request) {
		FlowNode flowNode = (FlowNode) getHost().getModel();

		FlowNodeDeleteCommand deleteCommand = new FlowNodeDeleteCommand();
		deleteCommand.setModel(flowNode);
		deleteCommand.setParent((FlowDiagram) getHost().getParent().getModel());
//		CancellableCommand cancellableCommand = new CancellableCommand(delete, "test delete message", "delete title",
//				MessageDialog.QUESTION);
		return deleteCommand;
	}


}