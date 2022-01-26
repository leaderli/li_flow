package com.leaderli.li.flow.editor.serialize;

import java.util.ArrayList;
import java.util.List;

public class SerializeFlowNode {

	private int id;
	private String type;
	private String name;
	private int[] point;
	private String flowName;
	private String icon;
	private List<Integer> gotoNodes = new ArrayList<>();

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
		this.name = name;
	}

	public int[] getPoint() {
		return point;
	}

	public void setPoint(int[] point) {
		this.point = point;
	}

	public String getFlowName() {
		return flowName;
	}

	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public List<Integer> getGotoNodes() {
		return gotoNodes;
	}

	public void setGotoNodes(List<Integer> gotoNodes) {
		this.gotoNodes = gotoNodes;
	}


}
