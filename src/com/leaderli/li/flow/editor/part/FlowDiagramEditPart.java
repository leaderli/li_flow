package com.leaderli.li.flow.editor.part;

import java.util.List;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.GroupBoxBorder;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MarginBorder;
import org.eclipse.gef.EditPolicy;

import com.leaderli.li.flow.adapter.Notify;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.ModelFieldEnum;
import com.leaderli.li.flow.editor.policy.FlowDiagramXYLayoutPolicy;

public class FlowDiagramEditPart extends GenericsEditPart<FlowDiagram> {


	@Override
	protected IFigure createFigure() {
		// 定义一个基本的画布
		FreeformLayer layer = new FreeformLayer();
		layer.setBorder(new GroupBoxBorder("Diagram"));
		layer.setLayoutManager(new FreeformLayout());
		layer.setBorder(new MarginBorder(5));
		layer.setOpaque(false);
		return layer;
	}

	@Override
	protected void createEditPolicies() {
		// 关于布局的处理策略
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new FlowDiagramXYLayoutPolicy());
	}

	@Override
	protected void initAfterSetModel() {
		addNotify(new FlowDiagramAdapter());
	}

	@Override
	public List<FlowNode> getModelChildren() {
		return getModel().getFlowNodes();
	}

	/**
	 * 返回子Figure的父Figure，默认返回createFigure创建的Figure
	 */
	@Override
	public IFigure getContentPane() {

		return super.getContentPane();
	}



	private class FlowDiagramAdapter implements Notify {

		/**
		 * 新增或删除FlowNode、ConnectionNode时，重新绘制页面
		 */
		@Override
		public void notifyChanged(int typeRole, String oldVal, String newVal) {
			refreshChildren();
		}
	}

}