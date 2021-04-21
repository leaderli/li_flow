package com.leaderli.li.flow.util;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.dialogs.IMessageProvider;

import com.leaderli.li.flow.LiPlugin;
import com.leaderli.li.flow.dialog.WizardPageCommand;
import com.leaderli.li.flow.editor.FlowEditor;
import com.leaderli.li.flow.editor.command.FlowNodeRenameCommand;
import com.leaderli.li.flow.editor.command.ModelCommand;
import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;
import com.leaderli.li.flow.editor.model.Node;
import com.leaderli.li.flow.editor.model.NodeNotify;

public class ModelUtil {

	public static FlowEditor getFlowEditor(NodeNotify model) {
		while (model instanceof Node) {
			model = ((Node<?>) model).getParent();
		}
		if (model instanceof FlowDiagram) {
			return ((FlowDiagram) model).getEditor();
		}
		return null;
	}

	public static void referToEachOther(Object model, EditPart editPart) {
		if (editPart != null) {
			editPart.setModel(model);
			if (model instanceof Node) {
				((Node<?>) model).setEditPart(editPart);
			}
		}
	}

	public static IStatus validateFlowNodeName(FlowNode flowNode, String name) {

		if (name == null || name.trim().isEmpty()) {
			return new Status(IStatus.ERROR, LiPlugin.PLUGIN_ID, "node name can not be empty");
		}
		if (name.equals(flowNode.getName())) {
			return new Status(IStatus.CANCEL, LiPlugin.PLUGIN_ID, "");
		}
		IStatus status = NameUtil.validateJavaTypeName(name);
		if (status.isOK()) {

			FlowDiagram flowDiagram = flowNode.getParent();
			if (flowDiagram.getFlowNodes().stream()
					.anyMatch(flow -> name.equals(flow.getName()))) {
				return new Status(IStatus.ERROR, LiPlugin.PLUGIN_ID, "node " + name + " already exist");

			}

		}
		return status;

	}

	public static void openRenameFLowNodeWizard(FlowNode flowNode, CommandStack commandStack) {
		ModelUtil.openRenameFLowNodeWizard(flowNode, commandStack, flowNode.getName());
	}

	public static void openRenameFLowNodeWizard(FlowNode flowNode, CommandStack commandStack, String newName) {

		FlowNodeRenameCommand renameCommand = new FlowNodeRenameCommand();
		renameCommand.setModel(flowNode);

		RenameFlowNodeWizardPageCommand renameWizardPageCommand = new RenameFlowNodeWizardPageCommand(renameCommand, "rename FlowNode", "", newName, flowNode,
				commandStack,
				renameCommand);

		renameWizardPageCommand.openNewWizard();

	}

	public static int status2MessageType(IStatus status) {
		switch (status.getSeverity()) {
		case IStatus.ERROR:
			return IMessageProvider.ERROR;
		case IStatus.INFO:
			return IMessageProvider.INFORMATION;
		case IStatus.WARNING:
			return IMessageProvider.WARNING;
		default:
			return IMessageProvider.NONE;

		}
	}

	public static FlowNode getLinkedFlowNode(GotoNode got) {
		if (got == null || got.getParent() == null) {
			return null;
		}
		FlowDiagram flowDiagram = got.getParent().getParent();
		if (flowDiagram == null) {
			return null;
		}
		ConnectionNode connectionNode = got.getLinkedConnectionNode();
		if (connectionNode == null) {
			return null;
		}
		FlowNode target = connectionNode.getTargetFlowNodeID();
		return target;
	}

}

class RenameFlowNodeWizardPageCommand extends WizardPageCommand<FlowNode, FlowDiagram> {
	private final FlowNode flowNode;
	private final CommandStack commandStack;
	private final FlowNodeRenameCommand renameCommand;

	public RenameFlowNodeWizardPageCommand(ModelCommand<FlowNode, FlowDiagram> command, String message, String title, String defValue, FlowNode flowNode, CommandStack commandStack,
			FlowNodeRenameCommand renameCommand) {
		super(command, title, message, defValue);
		this.flowNode = flowNode;
		this.commandStack = commandStack;
		this.renameCommand = renameCommand;
	}

	@Override
	public boolean isPageComplete() {
		return super.isPageComplete();
	}

	@Override
	public String getMessage() {
		return super.getMessage();
	}

	@Override
	public void checkComplete() {

		setPageComplete(false);
		IStatus status = ModelUtil.validateFlowNodeName(flowNode, getPageText());
		int messageType = ModelUtil.status2MessageType(status);
		setMessage(status.getMessage(), messageType);
		if (status.isOK()) {
			setPageComplete(true);
		}

	}

	@Override
	protected void whenPageComplete() {
		renameCommand.setNewName(getPageText());
	}

	@Override
	protected boolean performFinish() {
		if (isPageComplete()) {
			if (canExecute()) {
				commandStack.execute(commandProxy);
				return true;
			}
		}
		return false;
	}
}
