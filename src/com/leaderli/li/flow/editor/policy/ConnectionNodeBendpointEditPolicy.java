package com.leaderli.li.flow.editor.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.BendpointEditPolicy;
import org.eclipse.gef.requests.BendpointRequest;

import com.leaderli.li.flow.editor.command.ConnectionNodeBendpointCreateCommand;
import com.leaderli.li.flow.editor.command.ConnectionNodeBendpointDeleteCommand;
import com.leaderli.li.flow.editor.command.ConnectionNodeBendpointMoveCommand;

public class ConnectionNodeBendpointEditPolicy extends BendpointEditPolicy {

	@Override
	protected Command getCreateBendpointCommand(BendpointRequest request) {

		return new ConnectionNodeBendpointCreateCommand(request);
	}

	@Override
	protected Command getDeleteBendpointCommand(BendpointRequest request) {

		return new ConnectionNodeBendpointDeleteCommand(request);

	}

	@Override
	protected Command getMoveBendpointCommand(BendpointRequest request) {
		return new ConnectionNodeBendpointMoveCommand(request);

	}

}
