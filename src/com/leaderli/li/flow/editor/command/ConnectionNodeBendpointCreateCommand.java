package com.leaderli.li.flow.editor.command;

import org.eclipse.gef.requests.BendpointRequest;

public class ConnectionNodeBendpointCreateCommand extends ConnectionNodeBendpointCommand {

	public ConnectionNodeBendpointCreateCommand(BendpointRequest request) {
		super(request);
	}

	@Override
	public void execute() {
		super.execute();
		model.addBendpoint(index, this.location);
	}

	@Override
	public void undo() {
		model.removeBendpoint(index);
	}

}
