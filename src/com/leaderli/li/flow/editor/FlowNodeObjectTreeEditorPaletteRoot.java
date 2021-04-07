package com.leaderli.li.flow.editor;


import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;

import com.leaderli.li.flow.constant.PluginConstant;
import com.leaderli.li.flow.editor.factory.TheFactoryOfCreationFactory;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;
import com.leaderli.li.flow.util.ImageUtil;

public class FlowNodeObjectTreeEditorPaletteRoot extends PaletteRoot {

	PaletteGroup group;

	FlowNode flowNode;

	public FlowNodeObjectTreeEditorPaletteRoot() {
		addGroup();
		addGotoNode();

	}

	public void setFlowNode(FlowNode flowNode) {
		this.flowNode = flowNode;
	}


	private void addGroup() {
		group = new PaletteGroup("servlet");
		add(group);
	}

	
	private void addGotoNode() {
		CreationToolEntry entry = new CombinedTemplateCreationEntry("return ", "Create a new return",
				TheFactoryOfCreationFactory.getGenericsCreationFactoryWithConsumer(GotoNode.class, gotoNode -> {
					gotoNode.setName("");
				}), ImageUtil.getImageDescriptor(PluginConstant.ICON_GOTONODE),
				ImageUtil.getImageDescriptor(PluginConstant.ICON_GOTONODE));
		group.add(entry);
	}

	
}
