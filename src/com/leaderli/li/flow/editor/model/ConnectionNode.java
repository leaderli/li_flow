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

	}
	public int getTargetID() {
		return targetID;
	}
	public void setTargetID(int targetID) {
		this.targetID = targetID;
	}
	public int getSourceFlowNodeID() {
		return sourceFlowNodeID;
	}
	public void setSourceFlowNodeID(int sourceFlowNodeID) {
		this.sourceFlowNodeID = sourceFlowNodeID;
	}
	public int getTargetFlowNodeID() {
		return targetFlowNodeID;
	}
	public void setTargetFlowNodeID(int targetFlowNodeID) {
		this.targetFlowNodeID = targetFlowNodeID;
	}
	
	
	
	
	public List<Location> getBendpoints() {
		if(bendpoints == null) {
			bendpoints = new ArrayList<>();
		}
		return bendpoints;
	}
	public void setBendpoints(List<Location> bendPoints) {
		bendpoints = bendPoints;
	}
	
	public void addBendpoint(int index, Location location) {
		getBendpoints().add(index, location);
		 super.notifyChanged();
	}
	
	public void removeBendpoint(int index) {
		getBendpoints().remove(index);
		 super.notifyChanged();
	}
	public void updateBendpoint(int index, Location location) {
		getBendpoints().set(index, location);
		 super.notifyChanged();
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
