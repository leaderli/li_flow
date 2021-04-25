package com.leaderli.li.flow.editor.policy;

import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;

import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.part.FlowNodeEditPart;

public abstract class FlowEditorOpenRoleEditPolicy extends FlowEditorEditPolicy<FlowNode, FlowNodeEditPart> {

	public FlowEditorOpenRoleEditPolicy() {
		super();
	}

	@Override
	public boolean understandsRequest(Request req) {
		if (req.getType().equals(RequestConstants.REQ_OPEN)) {
			return true;
		}
		return super.understandsRequest(req);
	}

	@Override
	public Command getCommand(Request request) {
		if (request.getType().equals(RequestConstants.REQ_OPEN)) {
			return new Command() {
				@Override
				public boolean canUndo() {
					return false;
				}

				@Override
				public void execute() {
					executor().run();
				}

			};
		}
		return super.getCommand(request);
	}


	protected abstract Runnable executor();



}