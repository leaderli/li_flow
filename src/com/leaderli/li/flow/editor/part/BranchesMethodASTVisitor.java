package com.leaderli.li.flow.editor.part;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.generate.GenerateFromTemplate;
import com.leaderli.li.flow.generate.GenerateMethod;
import com.leaderli.li.flow.util.ResourcesUtil;

public class BranchesMethodASTVisitor extends ASTVisitor {

	private final FlowNode flowNode;

	BranchesMethodASTVisitor(FlowNode flowNode) {
		this.flowNode = flowNode;
	}

	@Override
	public boolean visit(MethodDeclaration node) {

		if (GenerateFromTemplate.getBranchesMethod(node)) {
			String bodyContent = GenerateMethod.getBranches(this.flowNode);
			ASTNode body = ASTNode.copySubtree(node.getAST(), ResourcesUtil.createAstNodeWithMethodBody(bodyContent));
			node.setBody((Block) body);
		}
		return false;
	}
}