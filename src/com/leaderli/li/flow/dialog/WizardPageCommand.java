package com.leaderli.li.flow.dialog;

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
import com.leaderli.li.flow.editor.command.ModelCommand;
import com.leaderli.li.flow.editor.model.Node;
import com.leaderli.li.flow.editor.model.NodeNotify;

public class WizardPageCommand<T extends Node<? extends R>, R extends NodeNotify> extends CommandProxy<T, R> {

	private String textValue;
	private SetNameWizardPage nameWizardPage;
	public WizardPageCommand(ModelCommand<T, R> command, String pageName, String message, String defValue) {
		super(command);
		this.textValue = defValue;
		this.nameWizardPage = new SetNameWizardPage(pageName, message);
	}

	/**
	 * 提供一些与model相关的回调函数，因为在抽象类中无法确定泛型的具体类型，无法对model相关属性赋值进行抽象
	 */
	protected void whenPageComplete() {

	}

	protected boolean isPageComplete() {
		return this.nameWizardPage.isPageComplete();
	}

	protected void setPageComplete(boolean complete) {
		this.nameWizardPage.setPageComplete(complete);
		if (complete) {
			whenPageComplete();
		}
	}

	protected void setMessage(String msg, int type) {
		this.nameWizardPage.setMessage(msg, type);
	}

	protected String getMessage() {
		return this.nameWizardPage.getMessage();

	}

	protected String getPageText() {
		return this.textValue;
	}


	protected boolean performFinish() {
		if (isPageComplete()) {
			if (canExecute()) {
				WizardPageCommand.super.execute();
				return true;
			}
		}
		return false;
	}

	protected void checkComplete() {
		setPageComplete(false);
		if (getPageText().length() > 0) {
			setPageComplete(true);
		} else {
			setMessage("name is empty", IMessageProvider.ERROR);
		}
	}

	public void createControl(Composite composite) {
		composite.setLayout(new GridLayout(2, false));
		new Label(composite, SWT.NORMAL).setText("Name:");
		Text text = new Text(composite, SWT.SINGLE | SWT.BORDER);
		text.setText(textValue);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		text.addModifyListener(e -> {
			textValue = text.getText();
			checkComplete();
		});
		checkComplete();
	}

	public void openNewWizard() {
		new SetNameWizard().open();

	}

	/**
	 * 在执行具体命令前，由向导程序来修改具体参数值（例如name），按照规范修改后点击finish再执行具体的命令，否则不执行命令。
	 * undo命令将传递给具体执行的命令。
	 */
	@Override
	public void execute() {
		openNewWizard();
	}

	private class SetNameWizard extends Wizard {

		@Override
		public boolean performFinish() {
			return WizardPageCommand.this.performFinish();
		}

		@Override
		public void addPages() {
			addPage(nameWizardPage);
		}

		public void open() {
			Shell shell = LiPlugin.getStandardDisplay().getActiveShell();
			WizardDialog wizardDialog = new WizardDialog(shell, this);
			wizardDialog.open();
		}
	}

	private class SetNameWizardPage extends WizardPage {

		protected SetNameWizardPage(String pageName, String message) {
			super(pageName);
		}

		/**
		 * 将实际校验方法由外部类去实现，方便继承类重新编写判断细节
		 */
		private void checkComplete() {
			WizardPageCommand.this.checkComplete();
		}

		@Override
		public void createControl(Composite parent) {
			Composite composite = new Composite(parent, SWT.NORMAL);
			setControl(composite);
			WizardPageCommand.this.createControl(composite);
		}

	}

}
