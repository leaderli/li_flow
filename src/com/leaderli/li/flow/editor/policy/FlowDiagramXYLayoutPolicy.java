package com.leaderli.li.flow.editor.policy;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.dialogs.IMessageProvider;

import com.leaderli.li.flow.dialog.WizardPageCommand;
import com.leaderli.li.flow.editor.command.FlowNodeChangeConstraintCommand;
import com.leaderli.li.flow.editor.command.FlowNodeCreateCommand;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.Location;
import com.leaderli.li.flow.util.NameUtil;

public class FlowDiagramXYLayoutPolicy extends XYLayoutEditPolicy {

	@Override
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		/**
		 * 当拖拽移动时会创建一个关于改变位置和大小的命令，拖拽结束时执行command的execute方法
		 */
		FlowNodeChangeConstraintCommand command = new FlowNodeChangeConstraintCommand();
		command.setModel((FlowNode) child.getModel());
		Rectangle rect = (Rectangle) constraint;
		Location point = new Location();
		point.setX(rect.x());
		point.setY(rect.y());
		command.setNewPoint(point);
		return command;
	}


	@Override
	protected Command getCreateCommand(CreateRequest request) {

		Command retVal = null;

		if (request.getNewObjectType().equals(FlowNode.class)) {

			WizardPageCommand<FlowNode, FlowDiagram> commandWrapper = createFlowNodeCommand(request);
			retVal = commandWrapper;
		}

		return retVal;
	}

	private WizardPageCommand<FlowNode, FlowDiagram> createFlowNodeCommand(CreateRequest request) {

		FlowNodeCreateCommand command = new FlowNodeCreateCommand();
		FlowNode model = (FlowNode) request.getNewObject();
		FlowDiagram flowDiagram = (FlowDiagram) getHost().getModel();

		command.setLocation(Location.transfer(request.getLocation()));
		command.setModel(model);
		command.setParent(flowDiagram);

		WizardPageCommand<FlowNode, FlowDiagram> commandWrapper = new WizardPageCommand<FlowNode, FlowDiagram>(command,
				"create a new FlowNode", "new FlowNode", model.getName()) {
			@Override
			protected void checkComplete() {
				super.checkComplete();
				if (!isPageComplete()) {
					return;
				}
				setMessage("", IMessageProvider.NONE);
				IStatus validateJavaTypeName = NameUtil.validateJavaTypeName(getPageText());
				if (!validateJavaTypeName.isOK()) {
					this.setMessage(validateJavaTypeName.getMessage(), validateJavaTypeName.getSeverity());
					return;
				}
				if (flowDiagram.getFlowNodes().stream().anyMatch(flow -> getPageText().equals(flow.getName()))) {
					this.setMessage("node " + getPageText() + " already exist", IMessageProvider.ERROR);
					return;

				}
				setPageComplete(true);
			}

			@Override
			protected void whenPageComplete() {
				model.setName(getPageText());
			}
		};

		return commandWrapper;
	}

}