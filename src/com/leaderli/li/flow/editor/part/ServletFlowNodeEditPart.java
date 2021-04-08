package com.leaderli.li.flow.editor.part;

import com.leaderli.li.flow.editor.policy.ServletFlowNodeOpenEditPolicy;
import com.leaderli.li.flow.generate.GenerateFromTemplate.Builder;
import com.leaderli.li.flow.generate.GenerateMethod;

public class ServletFlowNodeEditPart extends FlowNodeEditPart {


	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		this.installEditPolicy(FlowNodeEditPart.FLOWNODE_OPEN_ROLE, new ServletFlowNodeOpenEditPolicy());
	}

	@Override
	protected void generateNewSourceCodeBody(Builder build) {
		build.addPlaceHolder("getBranches", GenerateMethod.getBranches(this.getModel()));
	}

}
