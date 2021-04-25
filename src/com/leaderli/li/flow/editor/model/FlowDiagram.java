package com.leaderli.li.flow.editor.model;

import java.util.ArrayList;
import java.util.List;

public class FlowDiagram extends NodeNotify {

	private String packageName;
	private int nextNodeID;
	private List<FlowNode> flowNodes = new ArrayList<>();
	private List<ConnectionNode> connectionNodes = new ArrayList<>();

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName.toLowerCase();
	}


	public int getNextNodeID() {
		return nextNodeID;
	}

	public void setNextNodeID(int nextNodeID) {
		this.nextNodeID = nextNodeID;
	}



	//TODO 后续优化id持续增长的问题
	public int spanningNextNodeID() {
		return nextNodeID++;
	}



	public List<FlowNode> getFlowNodes() {

		return flowNodes;
	}

	public void setFlowNodes(List<FlowNode> flowNodes) {
		this.flowNodes = flowNodes;

	}

	public List<ConnectionNode> getConnectionNodes() {
		return connectionNodes;
	}

	public void setConnectionNodes(List<ConnectionNode> connectionNodes) {
		this.connectionNodes = connectionNodes;
	}

	public void addFlowNode(FlowNode flowNode) {
		flowNodes.add(flowNode);
		flowNode.setParent(this);
		this.notifyChanged();
	}

	public void removeFlowNode(FlowNode flowNode) {
		flowNodes.remove(flowNode);
		this.notifyChanged();
	}


	public void addConnectionNode(ConnectionNode connection) {

		connectionNodes.add(connection);
		connection.setParent(this);

		GotoNode source = connection.getSource();
		source.setLinkedConnectionNode(connection);
		connection.notifyChanged();

	}

	public void removeConnectionNode(ConnectionNode connection) {

		connectionNodes.remove(connection);

		GotoNode source = connection.getSource();
		if (source != null) {
			source.setLinkedConnectionNode(null);
		}
		connection.notifyChanged();
	}



}
