package com.leaderli.li.flow.editor.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.leaderli.li.flow.editor.command.GotoNodeDeleteCommand;
import com.leaderli.li.flow.editor.model.GotoNode;

public class GotoNodeComponentEditPolicy extends ComponentEditPolicy {

	@Override
	protected Command createDeleteCommand(GroupRequest request) {
		GotoNode gotoNode = (GotoNode) getHost().getModel();

		//
		if (gotoNode.getParent().getGotoNodes().size() == 1) {
			return null;
		}

		GotoNodeDeleteCommand delete = new GotoNodeDeleteCommand();
		delete.setModel(gotoNode);
		delete.setParent(gotoNode.getParent());
//		CancellableCommand cancellableCommand = new CancellableCommand(delete, "test delete message", "delete title",
//				MessageDialog.QUESTION);
		return delete;
	}


}