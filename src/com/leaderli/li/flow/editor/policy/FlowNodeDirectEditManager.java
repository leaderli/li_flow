package com.leaderli.li.flow.editor.policy;

import org.eclipse.draw2d.Label;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.viewers.CellEditor;

import com.leaderli.li.flow.editor.part.FlowNodeEditPart;

class FlowNodeDirectEditManager extends DirectEditManager {

	/**
	 * 
	 */
	private final FlowNodeEditPart FlowNodeDirectEditManager;
	private final Label label;

	FlowNodeDirectEditManager(FlowNodeEditPart flowNodeEditPart, GraphicalEditPart source, Class<? extends CellEditor> editorType,
			CellEditorLocator locator, Label label) {
		super(source, editorType, locator);
		FlowNodeDirectEditManager = flowNodeEditPart;
		this.label = label;
	}

	@Override
	protected void initCellEditor() {
		String initialLabelText = label.getText();
		getCellEditor().setValue(initialLabelText);
	}
}