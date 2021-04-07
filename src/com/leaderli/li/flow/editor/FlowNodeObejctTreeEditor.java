package com.leaderli.li.flow.editor;

import java.util.EventObject;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;

import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;
import com.leaderli.li.flow.editor.part.FlowNodeItemEditPart;
import com.leaderli.li.flow.editor.part.GotoNodeTreeEditPart;
import com.leaderli.li.flow.image.TabImageProvider;
import com.leaderli.li.flow.util.ModelUtil;

public class FlowNodeObejctTreeEditor extends GraphicalEditorWithFlyoutPalette {

	private FlowNode flowNode;

	public FlowNodeObejctTreeEditor() {
		super();
		setEditDomain(new DefaultEditDomain(this));
	}

	// @Override
	// protected void createGraphicalViewer(Composite parent) {
	// GraphicalTreeViewer viewer = new GraphicalTreeViewer();
	// viewer.createControl(parent);
	// viewer.setEditDomain(this.getEditDomain());
	// this.setGraphicalViewer(viewer);
	// this.configureGraphicalViewer();
	// this.initializeGraphicalViewer();
	// }

	@Override
	protected PaletteRoot getPaletteRoot() {
		FlowNodeObjectTreeEditorPaletteRoot palette = new FlowNodeObjectTreeEditorPaletteRoot();
		palette.setFlowNode(flowNode);
		return palette;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		getCommandStack().markSaveLocation();


	}

	public void setFlowNode(FlowNode flowNode) {
		this.flowNode = flowNode;
	}

	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		this.getGraphicalViewer().setContents(flowNode);
	}

	private EditPartFactory editPartFactory;

	private EditPartFactory getEditPartFactory() {
		if (editPartFactory == null) {
			editPartFactory = new EditPartFactory() {

				@Override
				public EditPart createEditPart(EditPart context, Object model) {
					EditPart editPart = null;
					if (model instanceof GotoNode) {
						editPart = new GotoNodeTreeEditPart();
					} else if (model instanceof FlowNode) {
						editPart = new FlowNodeItemEditPart();
					}
					ModelUtil.referToEachOther(model, editPart);
					return editPart;
				}

			};
		}
		return editPartFactory;
	}
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		viewer.setEditPartFactory(getEditPartFactory());

		viewer.addDropTargetListener(new TemplateTransferDropTargetListener(viewer));
		getEditDomain().getPaletteViewer()
				.addDragSourceListener(new TemplateTransferDragSourceListener(getEditDomain().getPaletteViewer()));
	}

	@Override
	public void doSaveAs() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 当使用编辑器编辑文件时执行相关命令，跟根据isDity判断是否需要将文件的标题上加*，表示该文件变动未保存
	 */
	@Override
	public void commandStackChanged(EventObject event) {
		firePropertyChange(PROP_DIRTY);
		super.commandStackChanged(event);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class type) {
		if (type == TabImageProvider.class) {
			return TabImageProvider.instance("servlet.gif");
		}
		return super.getAdapter(type);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		IWorkbenchPartSite site = this.getSite();
		if (site != null) {
			IWorkbenchWindow window = site.getWorkbenchWindow();
			if (window != null) {
				IWorkbenchPage page = window.getActivePage();
				if (page != null) {
					IEditorPart editorPart = page.getActiveEditor();
					if (editorPart != null) {
						this.updateActions(this.getSelectionActions());
					}
				}
			}
		}
	}
}
