package com.leaderli.li.flow.editor.part;

import java.util.List;

import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;
import com.leaderli.li.flow.editor.policy.SubflowRefFlowNodeOpenEditPolicy;
import com.leaderli.li.flow.generate.GenerateFromTemplate.Builder;
import com.leaderli.li.flow.listener.DefaultTypeNotifyListener;
import com.leaderli.li.flow.generate.GenerateMethod;
import com.leaderli.li.flow.ui.ComboBoxPropertySourceLabelProvider;
import com.leaderli.li.flow.ui.ModelPropertyDescriptor;
import com.leaderli.li.flow.util.FlowNodeUtil;

public class SubflowRefFlowNodeEditPart extends FlowNodeEditPart {



	@Override
	protected List<GotoNode> getModelChildren() {
		List<GotoNode> gotos = getModel().getGotoNodes();

		if (gotos.isEmpty()) {
			FlowNodeUtil.updateGotoNodeAfterSetFlowName(getProject(), SubflowRefFlowNodeEditPart.this.getModel());
		}
		return gotos;
	}

	@Override
	protected void createEditPolicies() {
		super.createEditPolicies();
		installEditPolicy(FlowNodeEditPart.FLOWNODE_OPEN_ROLE, new SubflowRefFlowNodeOpenEditPolicy());
	}

	@Override
	protected void initAfterSetModel() {
		super.initAfterSetModel();
		this.addNotifyListener(new RenameSubflowNameFlowNodeAdapter());
	}

	@Override
	protected void generateNewSourceCodeBody(Builder build) {
		build.addPlaceHolder("getSubflowName", GenerateMethod.getSubflowName(getModel()));
		build.addPlaceHolder("getExitPoints", GenerateMethod.getExitPoints(getModel()));

	}

	@Override
	protected void addPropertyDescriptor() {

		super.addPropertyDescriptor();

		String[] subFlowNames = getSubFlowNames();

		ComboBoxPropertySourceLabelProvider provider = new ComboBoxPropertySourceLabelProvider(subFlowNames);

		PropertyDescriptor combo = new ComboBoxPropertyDescriptor("", "Flow", provider.getValues());
		combo.setLabelProvider(provider);
		combo.setAlwaysIncompatible(true);
		combo.setDescription("the name of  other subflow");

		ModelPropertyDescriptor<FlowNode, Integer> wrapper = new ModelPropertyDescriptor<FlowNode, Integer>(combo, getModel()) {

			@Override
			public void setProperty(Integer index) {
				model.setFlowName(provider.propIntToString(index));
			}

			@Override
			public Integer getProperty() {
				return provider.propStringToInt(model.getFlowName());
			}
		};

		this.addPropertyDescriptor(wrapper);

	}

	private class RenameSubflowNameFlowNodeAdapter implements DefaultTypeNotifyListener {

		@Override
		public void notifyChanged() {
			FlowNodeUtil.updateGotoNodeAfterSetFlowName(getProject(), getModel());
		}

		@Override
		public int typeAndRole() {
			return FLOW_TYPE | FLOW_NAME_ROLE;
		}
	}
}
