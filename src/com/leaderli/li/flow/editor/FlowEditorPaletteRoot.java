package com.leaderli.li.flow.editor;

import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.tools.ConnectionCreationTool;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

import com.leaderli.li.flow.constant.PluginConstant;
import com.leaderli.li.flow.editor.factory.TheFactoryOfCreationFactory;
import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.util.ImageUtil;

public class FlowEditorPaletteRoot {

	public static final int CONNECTION_TOOL_ACTIVATE_KEY_CODE = SWT.SHIFT;
	private PaletteRoot paletteRoot;
//	PaletteGroup group;
	private ConnectionCreationToolEntry connectionTool;
	private SelectionToolEntry selectionTool;


	public ConnectionCreationToolEntry getConnectionTool() {
		return connectionTool;
	}

	public SelectionToolEntry getSelectionTool() {
		return selectionTool;
	}

	public FlowEditorPaletteRoot() {

		paletteRoot = new PaletteRoot();
		selectionTool = new SelectionToolEntry();
		paletteRoot.setDefaultEntry(selectionTool);

		connectionTool = new ConnectionCreationToolEntry("connection", "Create Connection between nodes",
				TheFactoryOfCreationFactory.getGenericsCreationFactoryWithConsumer(ConnectionNode.class, null),
				ImageUtil.getImageDescriptor("connection.gif"), ImageUtil.getImageDescriptor("connection.gif"));

		connectionTool.setToolClass(KeyEnabledConnectionCreationTool.class);
//		addConnection();
		PaletteGroup group = new PaletteGroup("servlet");
		paletteRoot.add(group);

		group.add(selectionTool);
		group.add(connectionTool);
		group.add(newFlowNodeEntry(PluginConstant.ICON_SERVLET, PluginConstant.TYPE_SERVLET));
		group.add(newFlowNodeEntry(PluginConstant.ICON_SUBFLOW_REF, PluginConstant.TYPE_SUBFLOW_REF));
		group.add(newFlowNodeEntry(PluginConstant.ICON_SUBFLOW_RETURN, PluginConstant.TYPE_SUBFLOW_RETURN));

	}
//
//	private void addSelectionTool(PaletteRoot PaletteRoot) {
//
//		if (this.selectionTool == null) {
//
//			this.selectionTool = new SelectionToolEntry();
//		}
//
//		group.add(this.selectionTool);
//		PaletteRoot.setDefaultEntry(this.selectionTool);
//	}
//
//	private void addGroup() {
//		group = new PaletteGroup("servlet");
//		add(group);
//	}

	private CreationToolEntry newFlowNodeEntry(String icon, String type) {
		CreationToolEntry entry = new CombinedTemplateCreationEntry(type, "Create a new " + type,
				TheFactoryOfCreationFactory.getGenericsCreationFactoryWithConsumer(FlowNode.class, flowNode -> {
					flowNode.setName("");
					flowNode.setIcon(icon);
					flowNode.setType(type);
				}), ImageUtil.getImageDescriptor(icon), ImageUtil.getImageDescriptor(icon));
		return entry;
//		group.add(entry);
	}

	public PaletteRoot getPaletteRoot() {
		return paletteRoot;
	}

//	private void addConnection() {
//		if (this.connectionTool == null) {
//
//			this.connectionTool = new ConnectionCreationToolEntry("connection", "Create Connection between nodes",
////					TheFactoryOfCreationFactory.getGenericsCreationFactoryWithConsumer(ConnectionNode.class, null),
//					null,
//					ImageUtil.getImageDescriptor("connection.gif"), ImageUtil.getImageDescriptor("connection.gif"));
//
//			this.connectionTool.setToolClass(KeyEnabledConnectionCreationTool.class);
//		}
//		group.add(this.connectionTool);
//	}


public static class KeyEnabledConnectionCreationTool extends ConnectionCreationTool {

	@Override
	protected boolean handleKeyUp(KeyEvent event) {
		if (event.keyCode == FlowEditorPaletteRoot.CONNECTION_TOOL_ACTIVATE_KEY_CODE && getCurrentViewer().getKeyHandler() != null) {
			getCurrentViewer().getKeyHandler().keyReleased(event);
		}
		return super.handleKeyUp(event);

	}

}

}