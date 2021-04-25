package com.leaderli.li.flow.editor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.dnd.TemplateTransferDropTargetListener;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.IWorkbenchWindow;

import com.leaderli.li.flow.editor.action.AstJavaAction;
import com.leaderli.li.flow.editor.action.EditRelateJavaAction;
import com.leaderli.li.flow.editor.action.SelectionContextMenuAction;
import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;
import com.leaderli.li.flow.editor.part.ConnectionNodeEditPart;
import com.leaderli.li.flow.editor.part.EditPartFactoryUtil;
import com.leaderli.li.flow.editor.part.FlowDiagramEditPart;
import com.leaderli.li.flow.editor.part.FlowEditorProvider;
import com.leaderli.li.flow.editor.part.GenericsEditPart;
import com.leaderli.li.flow.editor.part.GotoNodeEditPart;
import com.leaderli.li.flow.editor.serialize.SerializeFlowDiagram;
import com.leaderli.li.flow.editor.serialize.SerializeUtil;
import com.leaderli.li.flow.image.TabImageProvider;
import com.leaderli.li.flow.util.GsonUtil;

/**
 * 
 * 编辑器的入口
 *
 */
public class FlowEditor extends BaseGraphicalEditorWithFlyoutPalette {

	private FlowEditorPaletteRoot palette;

	public FlowEditor() {
	}

	/**
	 * 定义Palette工具栏组件
	 */
	@Override
	protected PaletteRoot getPaletteRoot() {
		if (palette == null) {

			palette = new FlowEditorPaletteRoot();
		}
		return palette.getPaletteRoot();
	}

	@Override
	public CommandStack getCommandStack() {
		return super.getCommandStack();
	}

	/**
	 * 保存文件时的回调函数
	 */
	@Override
	public void doSave(IProgressMonitor monitor) {
		// /**
		// * 可以将未保存状态的*号移除
		// */
		getCommandStack().markSaveLocation();

		IFile iFile = ((IFileEditorInput) getEditorInput()).getFile();
		FlowDiagram flowDiagram = (FlowDiagram) getGraphicalViewer().getContents().getModel();
		SerializeFlowDiagram seriaFlowDiagram = SerializeUtil.transfer(flowDiagram);
		InputStream stream = new ByteArrayInputStream(GsonUtil.toJson(seriaFlowDiagram).getBytes());
		try {
			iFile.setContents(stream, true, true, monitor);
		} catch (CoreException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 当使用编辑器编辑文件时执行相关命令，跟根据isDity判断是否需要将文件的标题上加*，表示该文件变动未保存
	 */
//	@Override
//	public void commandStackChanged(EventObject event) {
//		firePropertyChange(PROP_DIRTY);
//		super.commandStackChanged(event);
//	}



	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	protected void setPartName(IFile inputFile) {
		// 拼接 项目名和文件名
		String title = getProject().getName() + "[" + inputFile.getName() + "]";
		setPartName(title);
	}

	@Override
	protected void initializeGraphicalViewer() {
		super.initializeGraphicalViewer();
		/**
		 * 设定编辑器编辑的文件内容,FlowDiagram为一个model，setContents接收一个model时，
		 * 会根据EditPartFactory找到对应的EditPart来处理
		 */

		IFile iFile = ((IFileEditorInput) getEditorInput()).getFile();
		getGraphicalViewer().setContents(loadInputFile(iFile));
	}

	private FlowDiagram loadInputFile(IFile file) {

		try {
			SerializeFlowDiagram seriaFlowDiagram = GsonUtil.fromJson(file.getContents(), SerializeFlowDiagram.class);
			FlowDiagram flowDiagram = SerializeUtil.transfer(seriaFlowDiagram);
			return flowDiagram;
		} catch (CoreException e) {
			e.printStackTrace();
		}
		return null;
	}




	/**
	 * 扩大访问权限
	 */
	@Override
	public GraphicalViewer getGraphicalViewer() {
		return super.getGraphicalViewer();
	}


	private EditPartFactory editPartFactory;

	@SuppressWarnings("unchecked")
	private EditPartFactory getEditPartFactory() {
		if (editPartFactory == null) {
			editPartFactory = (part, model) -> {
				EditPart editPart = null;

				if (model instanceof ConnectionNode) {
					editPart = new ConnectionNodeEditPart();
				} else if (model instanceof FlowDiagram) {
					editPart = new FlowDiagramEditPart();
				} else if (model instanceof GotoNode) {
					editPart = new GotoNodeEditPart();
				} else if (model instanceof FlowNode) {
					String type = ((FlowNode) model).getType();
					editPart = EditPartFactoryUtil.getFlowNodeEditPartByType(type);
				}

				if (editPart instanceof FlowEditorProvider) {
					((FlowEditorProvider<FlowEditor>) editPart).setEditor(FlowEditor.this);
					editPart.setModel(model);
				}
				return editPart;
			};

		}
		return editPartFactory;
	}

	@Override
	protected void configureGraphicalViewer() {

		super.configureGraphicalViewer();
		BaseGraphicalEditorWithFlyoutPalette editor = this;
		GraphicalViewer graphicalViewer = getGraphicalViewer();
		graphicalViewer.setEditPartFactory(getEditPartFactory());
		graphicalViewer.addDropTargetListener(new TemplateTransferDropTargetListener(graphicalViewer));
		getEditDomain().getPaletteViewer()
				.addDragSourceListener(new TemplateTransferDragSourceListener(getEditDomain().getPaletteViewer()));
		configureKeyboardShortcuts();
		graphicalViewer.setContextMenu(new FLowEditorContextMenuProvider(getActionRegistry(), graphicalViewer));

	}

	private void configureKeyboardShortcuts() {
		getGraphicalViewer().setKeyHandler(new FlowEditorKeyHandler(getGraphicalViewer()));
	}

	@Override
	protected void createActions() {

		createSelectionContextMenuAction(EditRelateJavaAction.class);
		createSelectionContextMenuAction(AstJavaAction.class);
		// ActionRegistry registry = this.getActionRegistry();
		// IAction action = new FlowNodeSelectionAction(this);
		// registry.registerAction(action);
		// this.getSelectionActions().add(action.getId());
		super.createActions();
	}

	private void createSelectionContextMenuAction(Class<? extends SelectionContextMenuAction<?, FlowEditor, ? extends GenericsEditPart<?, FlowEditor>>> actionClass) {
		try {
			SelectionContextMenuAction<?, ?, ? extends GenericsEditPart<?, FlowEditor>> action = actionClass.newInstance();
			action.setWorkbenchPart(this);
			getActionRegistry().registerAction(action);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class type) {
		if (type == TabImageProvider.class) {
			return TabImageProvider.instance("flow.gif");
		} else if (type == IProject.class) {
			return getProject();
		} else if (type == IFile.class) {
			return getFile();
		}

		return super.getAdapter(type);
	}

	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		IWorkbenchPartSite site = getSite();
		if (site != null) {
			IWorkbenchWindow window = site.getWorkbenchWindow();
			if (window != null) {
				IWorkbenchPage page = window.getActivePage();
				if (page != null) {
					IEditorPart editorPart = page.getActiveEditor();
					if (editorPart != null) {
						updateActions(getSelectionActions());
					}
				}
			}
		}
	}

	private final class FlowEditorKeyHandler extends GraphicalViewerKeyHandler implements Listener {
		private ToolEntry activeEntry;
		private boolean enabled;

		private FlowEditorKeyHandler(GraphicalViewer viewer) {
			super(viewer);
			enabled = true;
			viewer.getControl().addListener(31, this);
		}

		@Override
		public boolean keyPressed(KeyEvent event) {
			if (enabled && event.keyCode == FlowEditorPaletteRoot.CONNECTION_TOOL_ACTIVATE_KEY_CODE) {
				PaletteViewer pv = getEditDomain().getPaletteViewer();
				activeEntry = pv.getActiveTool();
				pv.setActiveTool(palette.getConnectionTool());
			}
			return super.keyPressed(event);
		}

		@Override
		public boolean keyReleased(KeyEvent event) {
			if (enabled && event.keyCode == FlowEditorPaletteRoot.CONNECTION_TOOL_ACTIVATE_KEY_CODE) {
				getEditDomain().getPaletteViewer().setActiveTool(activeEntry);
			}
			return super.keyReleased(event);
		}

		@Override
		public void handleEvent(Event e) {
		}

	}

}