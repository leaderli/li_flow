package com.leaderli.li.flow.editor.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.dialogs.IMessageProvider;

import com.leaderli.li.flow.dialog.WizardPageCommand;
import com.leaderli.li.flow.editor.command.GotoNodeCreateCommand;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;

public class FlowNodeItemXYLayoutPolicy extends XYLayoutEditPolicy {




	@Override
	protected Command getCreateCommand(CreateRequest request) {

		Command retVal = null;

		if (request.getNewObjectType().equals(GotoNode.class)) {

			WizardPageCommand<GotoNode, FlowNode> commandWrapper = createGotoNodeCommand(request);
			retVal = commandWrapper;
		}

		return retVal;
	}

	private WizardPageCommand<GotoNode, FlowNode> createGotoNodeCommand(CreateRequest request) {

		GotoNodeCreateCommand command = new GotoNodeCreateCommand();
		GotoNode model = (GotoNode) request.getNewObject();
		FlowNode flowNode = (FlowNode) getHost().getModel();

		command.setModel(model);
		command.setParent(flowNode);

		WizardPageCommand<GotoNode, FlowNode> commandWrapper = new WizardPageCommand<GotoNode, FlowNode>(command,
				"create a new GotoNode", "new GotoNode", model.getName()) {
			@Override
			protected void checkComplete() {
				super.checkComplete();
				if (!isPageComplete()) {
					return;
				}
				setMessage("", IMessageProvider.NONE);
				if (flowNode.getGotoNodes().stream().anyMatch(aGoto -> getPageText().equals(aGoto.getName()))) {
					this.setMessage("node " + getPageText() + " already exist", IMessageProvider.ERROR);
					return;

				}
				setPageComplete(true);
			}

			@Override
			protected void whenPageComplete() {
				model.setName(getPageText());
				model.setId(flowNode.getParent().spanningNextNodeID());
			}
		};

		return commandWrapper;
	}

}