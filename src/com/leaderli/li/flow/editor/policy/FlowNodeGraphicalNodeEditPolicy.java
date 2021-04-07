package com.leaderli.li.flow.editor.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.leaderli.li.flow.constant.PluginConstant;
import com.leaderli.li.flow.editor.command.ConnectionNodeCreateCommand;
import com.leaderli.li.flow.editor.command.ConnectionNodeReconnectTargetCommand;
import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;

public class FlowNodeGraphicalNodeEditPolicy extends GraphicalNodeEditPolicy {

	@Override
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		ConnectionNodeCreateCommand command = (ConnectionNodeCreateCommand) request.getStartCommand();
		FlowNode flowNode = (FlowNode) getHost().getModel();

		// begin不允许使用target
		if (PluginConstant.TYPE_SUBFLOW_ENTRY.equals(flowNode.getType())) {
			return null;
		}

		command.setTarget((FlowNode) getHost().getModel());
		command.setFlowDiagram((FlowDiagram) getHost().getParent().getModel());

		return command;
	}

	@Override
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		return null;
	}

	@Override
	protected Command getReconnectTargetCommand(ReconnectRequest request) {

		ConnectionNodeReconnectTargetCommand command = new ConnectionNodeReconnectTargetCommand();
		FlowNode flowNode = (FlowNode) request.getTarget().getModel();

		// begin不允许使用target
		if (PluginConstant.TYPE_SUBFLOW_ENTRY.equals(flowNode.getType())) {
			return null;
		}

		command.setNewTarget(flowNode);
		command.setModel((ConnectionNode) request.getConnectionEditPart().getModel());
		return command;
	}

	@Override
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		return null;
	}

}