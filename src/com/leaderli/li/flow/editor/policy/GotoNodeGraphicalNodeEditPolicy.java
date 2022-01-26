package com.leaderli.li.flow.editor.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.leaderli.li.flow.editor.command.ConnectionNodeCreateCommand;
import com.leaderli.li.flow.editor.command.ConnectionNodeReconnectSourceCommand;
import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.GotoNode;

public class GotoNodeGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		return null;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		GotoNode gotoNode = (GotoNode) getHost().getModel();
		// 一个出口仅允许一条线
		if (gotoNode.getLinkedConnectionNode() != null) {
			return null;
		}
		ConnectionNodeCreateCommand command = new ConnectionNodeCreateCommand();
		command.setSource(gotoNode);
		command.setConnection((ConnectionNode) request.getNewObject());
		request.setStartCommand(command);
		return command;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		return null;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {

		GotoNode gotoNode = (GotoNode) request.getTarget().getModel();
		// 一个出口仅允许一条线
		if (gotoNode.getLinkedConnectionNode() != null) {
			return null;
		}
		ConnectionNodeReconnectSourceCommand command = new ConnectionNodeReconnectSourceCommand();
		command.setModel((ConnectionNode) request.getConnectionEditPart().getModel());
		command.setNewSource(gotoNode);
		return command;
	}

}