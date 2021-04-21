package com.leaderli.li.flow.editor.model;

import java.util.ArrayList;
import java.util.List;

public class ConnectionNode extends Node<FlowDiagram> {

	private GotoNode sourceID;
	private FlowNode targetID;
	private FlowNode sourceFlowNodeID;
	private FlowNode targetFlowNodeID;

	private List<Location> bendpoints;

	public GotoNode getSourceID() {
		return sourceID;
	}

	public void setSourceID(GotoNode sourceID) {
		this.sourceID = sourceID;
		sourceFlowNodeID = sourceID.getParent();

	}

	public FlowNode getSourceFlowNodeID() {
		return sourceFlowNodeID;
	}


	public FlowNode getTargetID() {
		return targetID;
	}

	public void setTargetID(FlowNode targetID) {
		this.targetID = targetID;
		targetFlowNodeID = this.targetID;
	}

	public FlowNode getTargetFlowNodeID() {
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
		if (sourceID != null) {
			sourceID.notifyChanged(GOTO_TYPE | CONNECTION_SOURCE_ROLE);
		}
		if (targetID != null) {
			targetID.notifyChanged(FLOW_TYPE | CONNECTION_TARGET_ROLE);
		}
	}

}
