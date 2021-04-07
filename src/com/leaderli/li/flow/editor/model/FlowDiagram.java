package com.leaderli.li.flow.editor.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.leaderli.li.flow.constant.PluginConstant;
import com.leaderli.li.flow.editor.FlowEditor;

public class FlowDiagram extends NodeNotify {

	private String packageName;
	private int nextNodeID;
	private List<FlowNode> flowNodes;
	private List<ConnectionNode> connectionNodes;

	private transient Map<Integer, Node<?>> id2Node = new HashMap<>();
	private transient FlowEditor editor;

	public String getPackageName() {
		return this.packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName.toLowerCase();
	}

	public int getNextNodeID() {
		return this.nextNodeID;
	}

	public void setNextNodeID(int nextNodeID) {
		this.nextNodeID = nextNodeID;
	}

	//TODO 后续优化id持续增长的问题
	public int spanningNextNodeID() {
		return this.nextNodeID++;
	}

	public List<FlowNode> getFlowNodes() {

		return this.flowNodes;
	}

	public void setFlowNodes(List<FlowNode> flowNodes) {
		this.flowNodes = flowNodes;

	}

	public void addFlowNode(FlowNode flowNode) {
		this.flowNodes.add(flowNode);
		flowNode.setParent(this);
		this.registerNode(flowNode);
		flowNode.getGotoNodes().forEach(this::registerNode);
		this.notifyChanged();
	}

	public void removeFlowNode(FlowNode flowNode) {
		this.flowNodes.remove(flowNode);
		this.unRegisterNode(flowNode);
		this.notifyChanged();
	}

	public List<ConnectionNode> getConnectionNodes() {
		return this.connectionNodes;
	}

	public void setConnectionNodes(List<ConnectionNode> connectionNodes) {
		this.connectionNodes = connectionNodes;
	}

	public void addConnectionNode(ConnectionNode connection) {

		this.connectionNodes.add(connection);
		this.registerNode(connection);

		GotoNode source = (GotoNode) this.id2Node.get(connection.getSourceID());
		source.setLinkedConnectionNode(connection.getId());
		connection.notifyChanged();
		this.notifyChanged();

	}

	public void removeConnectionNode(ConnectionNode connection) {

		this.connectionNodes.remove(connection);
		this.unRegisterNode(connection);

		GotoNode source = (GotoNode) this.id2Node.get(connection.getSourceID());
		if (source != null) {
			source.setLinkedConnectionNode(PluginConstant.NO_LINKED_CONNECTION_NODE);
		}
		connection.notifyChanged();
		this.notifyChanged();
	}

	public void registerNode(Node<?> node) {
		this.id2Node.put(node.getId(), node);
	}

	public void unRegisterNode(Node<?> node) {
		this.id2Node.remove(node.getId());
	}

	@SuppressWarnings("unchecked")
	public <T extends Node<?>> T getRegisterNode(int id) {
		if (id == PluginConstant.NULL_NODE) {
			return null;
		}
		return (T) this.id2Node.get(id);
	}

	public void setEditor(FlowEditor editor) {
		this.editor = editor;
	}

	public FlowEditor getEditor() {
		return this.editor;
	}

}
