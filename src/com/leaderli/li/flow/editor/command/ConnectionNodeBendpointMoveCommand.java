package com.leaderli.li.flow.editor.command;

import org.eclipse.gef.requests.BendpointRequest;

import com.leaderli.li.flow.editor.model.Location;

public class ConnectionNodeBendpointMoveCommand extends ConnectionNodeBendpointCommand {

	public ConnectionNodeBendpointMoveCommand(BendpointRequest request) {
		super(request);
	}

	private Location oldLocation;
	@Override
	public void execute() {
		super.execute();
		if(oldLocation == null) {
			oldLocation = model.getBendpoint(index);
		}
		model.updateBendpoint(index,location);
	}

	@Override
	public void undo() {
		model.updateBendpoint(index, oldLocation);
	}

}
