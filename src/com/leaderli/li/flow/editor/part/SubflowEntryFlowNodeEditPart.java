package com.leaderli.li.flow.editor.part;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.generate.GenerateFromTemplate.Builder;
import com.leaderli.li.flow.generate.GenerateMethod;
import com.leaderli.li.flow.ui.ModelPropertyDescriptor;

public class SubflowEntryFlowNodeEditPart extends FlowNodeEditPart {



	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		// Entry屏蔽gotoNode的编辑操作
		this.installEditPolicy(EditPolicy.COMPONENT_ROLE, null);

	}


	@Override
	protected void generateNewSourceCodeBody(Builder build) {
		build.addPlaceHolder("getBranches", GenerateMethod.getBranches(this.getModel()));
	}


	// 重新父类的performRequest，屏蔽direct-edit
	@Override
	public void performRequest(Request req) {

		super.executeCommand(this.getCommand(req));
	}

	@Override
	protected void addPropertyDescriptor() {
		this.addPropertyDescriptor(new SubflowEntryFlowNodeNameTextPropertyDescriptor());
	}

	private class SubflowEntryFlowNodeNameTextPropertyDescriptor extends ModelPropertyDescriptor<FlowNode, String> {

		public SubflowEntryFlowNodeNameTextPropertyDescriptor() {
			super(new TextPropertyDescriptor("", "Name"), SubflowEntryFlowNodeEditPart.this.getModel());
		}

		@Override
		public String getProperty() {
			return this.model.getName();
		}

	}


}
