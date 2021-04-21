package com.leaderli.li.flow.editor.model;

import java.util.ArrayList;
import java.util.List;

public class FlowNode extends Node<FlowDiagram> {

	private String type;
	private String name;
	private String flowName;
	private String icon;
	private List<GotoNode> gotoNodes = new ArrayList<>();

	private Location point;

	public FlowNode() {
		super();
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {

		String oldVal = this.name;
		this.name = name;
		this.notifyChanged(FLOW_TYPE | NAME_ROLE);
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {

		String oldVal = this.flowName;
		this.flowName = flowName;
		this.notifyChanged(FLOW_TYPE | FLOW_NAME_ROLE);
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public void addGotoNode(GotoNode gotoNode) {
		gotoNodes.add(gotoNode);
		gotoNode.setParent(this);
		this.notifyChanged(FLOW_TYPE | CHILD_ROLE);
	}
	public void removeGotoNode(GotoNode gotoNode) {
		gotoNodes.remove(gotoNode);
		this.notifyChanged(FLOW_TYPE | CHILD_ROLE);
	}

	public List<GotoNode> getGotoNodes() {
		return gotoNodes;
	}

	public void setGotoNodes(List<GotoNode> gotoNodes) {
		this.gotoNodes = gotoNodes;

	}

	public Location getPoint() {
		return point;
	}

	public void setPoint(Location point) {
		this.point = point;
		this.notifyChanged(FLOW_TYPE | POINT_ROLE);
	}




}
