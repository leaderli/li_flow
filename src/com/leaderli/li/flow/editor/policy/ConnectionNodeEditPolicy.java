package com.leaderli.li.flow.editor.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ConnectionEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.leaderli.li.flow.editor.command.ConnectionNodeDeleteCommand;
import com.leaderli.li.flow.editor.model.ConnectionNode;

public class ConnectionNodeEditPolicy extends ConnectionEditPolicy {

	@Override
	protected Command getDeleteCommand(GroupRequest request) {
		ConnectionNodeDeleteCommand delete= new ConnectionNodeDeleteCommand();
		delete.setModel((ConnectionNode) getHost().getModel());
		return delete;
		
	}

}