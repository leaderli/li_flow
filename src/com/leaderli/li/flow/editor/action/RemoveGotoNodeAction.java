package com.leaderli.li.flow.editor.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.leaderli.li.flow.LiPlugin;
import com.leaderli.li.flow.constant.PluginConstant;
import com.leaderli.li.flow.editor.command.GotoNodeDeleteCommand;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;

public class RemoveGotoNodeAction extends SelectionContextMenuAction<FlowNode> {

	public static final String REMOVE_GOTO_NODE = "RemoveGotoNodeAction";

	public RemoveGotoNodeAction() {
		setId(REMOVE_GOTO_NODE);
		setText("Edit GotoNode");

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
		WizardDialog wizardDialog = new WizardDialog(shell, new EditWizard());
		wizardDialog.open();
	}

	private class EditWizard extends Wizard {

		private EditGotoWizardPage page = new EditGotoWizardPage();

		EditWizard() {
			setWindowTitle("Edit GotoNode");

		}

		@Override
		public void addPages() {

			addPage(page);
		}

		@Override
		public boolean performFinish() {
			this.page.commands.forEach(GotoNodeDeleteCommand::dispatch);
			return true;
		}

	}

	private class EditGotoWizardPage extends WizardPage {
		private Text flowName;

		private List<GotoNodeDeleteCommand> commands = new ArrayList<GotoNodeDeleteCommand>();

		protected EditGotoWizardPage() {
			super("edit gotoNode", "you can delete useless gotoNode", null);
		}

		@Override
		public void createControl(Composite parent) {

			Composite composite = new Composite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
			composite.setLayout(new FillLayout(SWT.HORIZONTAL));
			setControl(composite);

			TableViewer viewer = new TableViewer(composite);
			// viewer.getTable().setHeaderVisible(true);
			// viewer.getTable().setLinesVisible(true);

			List<GotoNode> gotoNodes = getModel().getGotoNodes();
			viewer.setContentProvider(new ArrayContentProvider());

			TableColumn column = new TableColumn(viewer.getTable(), SWT.NONE);
			column.setWidth(300);
			column.setText("gotoNode");
			TableViewerColumn firstNameCol = new TableViewerColumn(viewer, column);
			firstNameCol.setLabelProvider(new ColumnLabelProvider() {

				@Override
				public String getText(Object element) {
					GotoNode gotoNode = (GotoNode) element;
					return gotoNode.getName();
				}

			});

			TableColumn column2 = new TableColumn(viewer.getTable(), SWT.NONE);
			column2.setText("Actions");
			column2.setWidth(100);
			TableViewerColumn actionsNameCol = new TableViewerColumn(viewer, column2);
			actionsNameCol.setLabelProvider(new ColumnLabelProvider() {
				// make sure you dispose these buttons when viewer input changes
				Map<Object, Button> buttons = new HashMap<Object, Button>();

				@Override
				public void update(ViewerCell cell) {

					TableItem item = (TableItem) cell.getItem();
					Button button;
					GotoNode cellElement = (GotoNode) cell.getElement();
					if (buttons.containsKey(cellElement)) {
						button = buttons.get(cellElement);
					} else {
						button = new Button((Composite) cell.getViewerRow().getControl(), SWT.NONE);
						button.addListener(SWT.Selection, new Listener() {
							@Override
							public void handleEvent(Event event) {
								if (viewer.getTable().getItemCount() == 1) {
									setMessage("at leaset one gotonode", IMessageProvider.ERROR);
									return;
								}
								button.dispose();
								item.dispose();
								GotoNodeDeleteCommand command = new GotoNodeDeleteCommand();
								command.setModel(cellElement);
								command.setParent(cellElement.getParent());
								commands.add(command);

							}
						});
						button.setText("delete");
						buttons.put(cell.getElement(), button);
					}
					TableEditor editor = new TableEditor(item.getParent());
					editor.grabHorizontal = true;
					editor.grabVertical = true;
					editor.setEditor(button, item, cell.getColumnIndex());
					editor.layout();
				}

			});

			viewer.setInput(getModel().getGotoNodes());

		}

	}

}
