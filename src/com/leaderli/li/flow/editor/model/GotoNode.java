package com.leaderli.li.flow.editor.model;

public class GotoNode extends Node<FlowNode>{


	private int linkedConnectionNode;
	
	private String name;
	
	public int getLinkedConnectionNode() {
		return this.linkedConnectionNode;
	}

	public void setLinkedConnectionNode(int linkedConnectionNode) {
		this.linkedConnectionNode = linkedConnectionNode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
