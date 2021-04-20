package com.leaderli.li.flow.editor.model;

import java.util.ArrayList;
import java.util.List;

public class ConnectionNode extends Node<FlowDiagram> {

	private int sourceID;
	private int targetID;
	private int sourceFlowNodeID;
	private int targetFlowNodeID;

	private List<Location> bendpoints;

	public int getSourceID() {
		return sourceID;
	}

	public void setSourceID(int sourceID) {
		this.sourceID = sourceID;
		Node<GotoNode> source = getParent().getRegisterNode(this.sourceID);
		sourceFlowNodeID = source.getParent().getId();

	}

	public int getSourceFlowNodeID() {
		return sourceFlowNodeID;
	}


	public int getTargetID() {
		return targetID;
	}

	public void setTargetID(int targetID) {
		this.targetID = targetID;
		targetFlowNodeID = this.targetID;
	}

	public int getTargetFlowNodeID() {
		return targetFlowNodeID;
	}




	public List<Location> getBendpoints() {
		if (bendpoints == null) {
			bendpoints = new ArrayList<>();
		}
		return bendpoints;
	}

	public void setBendpoints(List<Location> bendPoints) {
		bendpoints = bendPoints;
	}

	public void addBendpoint(int index, Location location) {
		getBendpoints().add(index, location);
	}

	public void removeBendpoint(int index) {
		getBendpoints().remove(index);
	}

	public void updateBendpoint(int index, Location location) {
		getBendpoints().set(index, location);
	}

	public Location getBendpoint(int index) {
		return getBendpoints().get(index);
	}

	@Override
	public void notifyChanged() {
		Node<?> source = getParent().getRegisterNode(getSourceID());
		if (source != null) {
			source.notifyChanged(GOTO_TYPE | CONNECTION_SOURCE_ROLE);
		}
		Node<?> target = getParent().getRegisterNode(getTargetID());
		if (target != null) {
			target.notifyChanged(FLOW_TYPE | CONNECTION_TARGET_ROLE);
		}
	}

}
