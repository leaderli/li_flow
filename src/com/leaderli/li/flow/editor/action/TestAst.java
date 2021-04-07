package com.leaderli.li.flow.editor.action;

import java.io.File;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.leaderli.li.flow.util.ResourcesUtil;

public class TestAst {
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws Exception {
		File file = new File("/Users/li/java/runtime-EclipseApplication/test_java/resource/servlet.template");

		URI url = file.toURI();
		String java = String.join("\n", Files.readAllLines(Paths.get(url)));
		java = "package flow.subflow.hello;\r\n" + java;
		// Set the stage by creating an empty compilation unit and its AST
		CompilationUnit cu = ResourcesUtil.getCompilationUnitBySource(java);
		System.out.println(cu);
		cu.accept(new ASTVisitor() {
			@Override
			public boolean visit(TypeDeclaration node) {

				node.setName(node.getAST().newSimpleName("DD1"));
				return true;
			}

			@Override
			public boolean visit(MethodDeclaration node) {


				if (getBranchesMethod(node)) {
					String methodBody = "Map<String,String> branches = new HashMap<>();\n" +
							"branches.put(\"default\",\"\");\n" +
							"\n" +
							"return branches;";

					ASTNode body = ASTNode.copySubtree(node.getAST(), ResourcesUtil.createAstNodeWithMethodBody(methodBody));
					node.setBody((Block) body);
				}
				return false;
			}
		});
		

		System.out.println(cu);

//		// Create the AST node with the method body. Note that this AST
//		// node will belong to a diferent AST!
//		ASTNode astNodeWithMethodBody = createAstNodeWithMethodBody();
//
//		// Create the MethodDeclaration
//		MethodDeclaration methodDeclaration = ast.newMethodDeclaration();
//		methodDeclaration.setName(ast.newSimpleName("myMethod"));
//
//		// Convert the AST node with the method body to belong to
//		// the desired AST, and set it as the method body
//		ASTNode convertedAstNodeWithMethodBody = ASTNode.copySubtree(ast, astNodeWithMethodBody);
//		Block block = (Block) convertedAstNodeWithMethodBody;
//		methodDeclaration.setBody(block);
//
//		// (If necessary: Create a class declaration that will contain the
//		// newly generated method)
//		TypeDeclaration typeDeclaration = ast.newTypeDeclaration();
//		typeDeclaration.setName(ast.newSimpleName("Example"));
////		typeDeclaration.bodyDeclarations().add(methodDeclaration);
//		compilationUnit.types().add(typeDeclaration);
	}

	private static boolean getBranchesMethod(MethodDeclaration node) {
		int modifiers = node.getModifiers();
		if (!Modifier.isPublic(modifiers)) {
			return false;
		}
		if (!node.getName().getFullyQualifiedName().equals("getBranches")) {
			return false;
		}
		if (node.getReceiverType() != null) {
			return false;
		}
		return node.getReturnType2().toString().equals("Map<String,String>");
	}


}
