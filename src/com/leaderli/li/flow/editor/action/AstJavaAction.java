package com.leaderli.li.flow.editor.action;

import java.util.UUID;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Name;

import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.util.ResourcesUtil;

public class AstJavaAction extends SelectionContextMenuAction<FlowNode> {

	public AstJavaAction() {
		setId("ast");
		setText("ast");

	}

	private static ASTNode createAstNodeWithMethodBody() {
		String body = "String uuid = " + UUID.randomUUID() + ";\nint a = 1;\n int b = 2;\n return (a + b);";
		System.out.println(body);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_STATEMENTS);
		parser.setSource(body.toCharArray());
		ASTNode result = parser.createAST(null);

		return result;
	}

	@Override
	public void run() {

		String name = "NewClass";
		IFile java = (IFile) editPart.getAdapter(IFile.class);
		System.out.println(java.hashCode() + " " + java.getName());
		ResourcesUtil.renameJavaFile(java, name);
		System.out.println(java.hashCode() + " " + java.getName());
		// end
		// 3 AST
//			CompilationUnit astRoot = (CompilationUnit) parser.createAST(null);
//			AST ast = astRoot.getAST();
//			ASTRewrite rewriter = ASTRewrite.create(ast);
//			astRoot.imports().add(newImport(ast));
//			rewriter.set(astRoot, ImportDeclaration.NAME_PROPERTY, newImport(ast), null);

		// 4 manipulating the AST
//			TypeDeclaration type = (TypeDeclaration) astRoot.types().get(0);
//			MethodDeclaration method = type.getMethods()[0];
//
//			ASTNode astNodeWithMethodBody = createAstNodeWithMethodBody();
//			MethodDeclaration newMethod = ast.newMethodDeclaration();
//			newMethod.setName(ast.newSimpleName("print"));
//
//			ASTNode convertedAstNodeWithMethodBody = ASTNode.copySubtree(ast, astNodeWithMethodBody);
//			Block block = (Block) convertedAstNodeWithMethodBody;
//			newMethod.setBody(block);

//			rewriter.remove(method, null);
//			rewriter.replace(method, newMethod, null);

		// 5 writing changes back
//			unit.applyTextEdit(rewriter.rewriteAST(), new NullProgressMonitor());

		// 6 commit to workspacejava source
//			unit.commitWorkingCopy(false, new NullProgressMonitor());

		ResourcesUtil.openJava("demo.Hello", editPart.getProject());
	}

	private Object newImport(AST ast) {
		ImportDeclaration newImportDeclaration = ast.newImportDeclaration();
		Name name = ast.newName(Demo.class.getName());
		newImportDeclaration.setName(name);
		return newImportDeclaration;
	}

}
