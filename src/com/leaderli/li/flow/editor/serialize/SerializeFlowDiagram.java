package com.leaderli.li.flow.editor.serialize;

import java.util.ArrayList;
import java.util.List;

public class SerializeFlowDiagram {

	private String packageName;
	private int nextNodeID;
	private List<SerializeFlowNode> flowNodes = new ArrayList<>();
	private List<SerializeGotoNode> gotoNodes = new ArrayList<>();
	private List<SerializeConnectionNode> connectionNodes = new ArrayList<>();

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public int getNextNodeID() {
		return nextNodeID;
	}

	public void setNextNodeID(int nextNodeID) {
		this.nextNodeID = nextNodeID;
	}
	public List<SerializeFlowNode> getFlowNodes() {
		return flowNodes;
	}

	public void setFlowNodes(List<SerializeFlowNode> flowNodes) {
		this.flowNodes = flowNodes;
	}

	public List<SerializeGotoNode> getGotoNodes() {
		return gotoNodes;
	}

	public void setGotoNodes(List<SerializeGotoNode> gotoNodes) {
		this.gotoNodes = gotoNodes;
	}

	public List<SerializeConnectionNode> getConnectionNodes() {
		return connectionNodes;
	}

	public void setConnectionNodes(List<SerializeConnectionNode> connectionNodes) {
		this.connectionNodes = connectionNodes;
	}

}
