package com.leaderli.li.flow.editor.command;

import org.eclipse.gef.requests.BendpointRequest;

public class ConnectionNodeBendpointDeleteCommand extends ConnectionNodeBendpointCommand {

	public ConnectionNodeBendpointDeleteCommand(BendpointRequest request) {
		super(request);
	}

	@Override
	public void execute() {
		model.removeBendpoint(index);

	}

	@Override
	public void undo() {
		model.addBendpoint(index, location);
	}

}
