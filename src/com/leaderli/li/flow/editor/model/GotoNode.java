package com.leaderli.li.flow.editor.model;

public class GotoNode extends Node<FlowNode>{


	private int linkedConnectionNode;
	
	private String name;
	
	public int getLinkedConnectionNode() {
		return linkedConnectionNode;
	}

	public void setLinkedConnectionNode(int linkedConnectionNode) {
		this.linkedConnectionNode = linkedConnectionNode;
		this.notifyChanged(GOTO_TYPE | CONNECTION_SOURCE_ROLE);
		getParent().notifyChanged(FLOW_TYPE | CHILD_ROLE);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.notifyChanged(GOTO_TYPE | NAME_ROLE);
	}
	
	
}
