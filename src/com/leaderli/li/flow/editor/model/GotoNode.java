package com.leaderli.li.flow.editor.model;

public class GotoNode extends Node<FlowNode>{


	private int linkedConnectionNode;
	
	private String name;
	
	public int getLinkedConnectionNode() {
		return linkedConnectionNode;
	}

	public void setLinkedConnectionNode(int linkedConnectionNode) {
		this.linkedConnectionNode = linkedConnectionNode;
		this.notifyChanged(CONNECTION_SOURCE_ROLE);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.notifyChanged(NAME_ROLE);
	}
	
	
}
