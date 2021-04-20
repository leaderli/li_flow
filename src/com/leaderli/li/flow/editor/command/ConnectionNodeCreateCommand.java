package com.leaderli.li.flow.editor.command;

import org.eclipse.gef.commands.Command;

import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;

public class ConnectionNodeCreateCommand extends Command {

	private GotoNode source;
	private FlowNode target;
	private ConnectionNode connection;
	private FlowDiagram flowDiagram;

	@Override
	public boolean canExecute() {
		return source != null && target != null && connection != null && flowDiagram != null;
	}

	@Override
	public void execute() {

		connection.setId(flowDiagram.spanningNextNodeID());
		connection.setParent(flowDiagram);

		connection.setSourceID(source.getId());

		connection.setTargetID(target.getId());

		flowDiagram.addConnectionNode(connection);
	
	}

	@Override
	public void undo() {
		flowDiagram.removeConnectionNode(connection);

	}

	public void setTarget(FlowNode target) {
		this.target = target;
	}

	public void setSource(GotoNode source) {
		this.source = source;
	}

	public void setConnection(ConnectionNode connection) {
		this.connection = connection;
	}

	public void setFlowDiagram(FlowDiagram flowDiagram) {
		this.flowDiagram = flowDiagram;
	}

}