package com.leaderli.li.flow.editor.action;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.leaderli.li.flow.LiPlugin;
import com.leaderli.li.flow.constant.PluginConstant;
import com.leaderli.li.flow.editor.command.GotoNodeCreateCommand;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;

public class AddGotoNodeAction extends SelectionContextMenuAction<FlowNode> {

	public static final String ADD_GOTO_NODE = "AddGotoNodeAction";

	public AddGotoNodeAction() {
		setId(ADD_GOTO_NODE);
		setText("Add Goto");

	}

	@Override
	public boolean calculateEnabled() {
		if (super.calculateEnabled()) {
			return PluginConstant.TYPE_SERVLET.equals(getModel().getType());
		}
		return false;
	}

	@Override
	public void run() {
		Shell shell = LiPlugin.getStandardDisplay().getActiveShell();
		WizardDialog wizardDialog = new WizardDialog(shell, new TestWizard());
		wizardDialog.open();
	}

	private class TestWizard extends Wizard {

		private NewGotoWizardPage page = new NewGotoWizardPage();

		TestWizard() {
			setWindowTitle("New GotoNode");

		}

		@Override
		public void addPages() {

			addPage(page);
		}

		@Override
		public boolean performFinish() {
			if (page.isPageComplete()) {
				GotoNode gotoNode = new GotoNode();
				gotoNode.setId(getModel().getParent().spanningNextNodeID());
				gotoNode.setName(page.flowName.getText());

				GotoNodeCreateCommand gotoNodeCreateCommand = new GotoNodeCreateCommand();
				gotoNodeCreateCommand.setModel(gotoNode);
				gotoNodeCreateCommand.setParent(getModel());

				gotoNodeCreateCommand.dispatch();

				return true;
			}
			return false;
		}

	}

	private class NewGotoWizardPage extends WizardPage {
		private Text flowName;

		protected NewGotoWizardPage() {
			super("new GotoNode", "Create a new GotonNode", null);
		}

		/**
		 * 校验当前页面是否操作完毕，进行下一步操作
		 */
		private void checkComplete() {
			setPageComplete(false);
			String gotoName = flowName.getText();
			if (gotoName.length() == 0) {
				this.setMessage("A goto name cannot be empty", IMessageProvider.WARNING);
				return;
			}

			if (getModel().getGotoNodes().stream().anyMatch(gotoNode -> gotoName.equals(gotoNode.getName()))) {
				this.setMessage("goto name duplicate", IMessageProvider.ERROR);
				return;

			}
			this.setMessage("Create a new flow named " + gotoName);
			setPageComplete(true);

		}

		@Override
		public void createControl(Composite parent) {
			Composite composite = new Composite(parent, SWT.NORMAL);
			setControl(composite);
			composite.setLayout(new GridLayout(2, false));
			new Label(composite, SWT.NORMAL).setText("Name:");
			flowName = new Text(composite, SWT.SINGLE | SWT.BORDER);
			flowName.setText("default");
			flowName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
			flowName.addModifyListener(e -> {
				checkComplete();
			});
			checkComplete();
		}

	}

}
