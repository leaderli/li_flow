package com.leaderli.li.flow.editor.action;

import java.io.File;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleType;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.refactoring.IJavaRefactorings;
import org.eclipse.jdt.core.refactoring.descriptors.RenameJavaElementDescriptor;
import org.eclipse.jdt.internal.core.dom.NaiveASTFlattener;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringContribution;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;


public class TestAst2 {
	public static void main(String[] args) throws Exception {

		File file = new File("/Users/li/java/eclipse_workspace/com.leaderli.li.flow/src/com/leaderli/li/flow/editor/action/TestAst2.java");
		URI url = file.toURI();
		String java = String.join("\n", Files.readAllLines(Paths.get(url)));

		@SuppressWarnings("deprecation")
		ASTParser astParser = ASTParser.newParser(AST.JLS8);
		astParser.setSource(java.toCharArray());
		astParser.setKind(ASTParser.K_COMPILATION_UNIT);

		CompilationUnit unit = (CompilationUnit) (astParser.createAST(null));

		unit.accept(new ASTVisitor() {
			@Override
			public boolean visit(TypeDeclaration node) {
				System.out.println("-->: " + node.getName().getFullyQualifiedName());
				if (node.getName().getFullyQualifiedName().equals("Demo")) {

					node.setName(node.getAST().newSimpleName("Fuck"));
				}
				return false;
			}
		});
		RefactoringContribution contribution = RefactoringCore.getRefactoringContribution(IJavaRefactorings.RENAME_COMPILATION_UNIT);
		RenameJavaElementDescriptor descriptor = (RenameJavaElementDescriptor) contribution.createDescriptor();
//		descriptor.setProject(cu.getResource().getProject().getName());
		descriptor.setNewName("NewClass"); // new name for a Class
//		descriptor.setJavaElement(cu);

		RefactoringStatus status = new RefactoringStatus();
		try {
			Refactoring refactoring = descriptor.createRefactoring(status);

			IProgressMonitor monitor = new NullProgressMonitor();
			refactoring.checkInitialConditions(monitor);
			refactoring.checkFinalConditions(monitor);
			Change change = refactoring.createChange(monitor);
			change.perform(monitor);

		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NaiveASTFlattener print = new NaiveASTFlattener();
		unit.accept(print);
		System.out.println(print.getResult());
//		DemoVisitor demoVisitor = new DemoVisitor();
//		unit.accept(demoVisitor);

	}

}



class DemoVisitor extends ASTVisitor {
	@SuppressWarnings("deprecation")
	private static ASTNode createAstNodeWithMethodBody() {
		String body = "\n" +
				"		List<Demo> demos = new ArrayList<>();\n" +
				"		demos.add(new Demo());\n" +
				"		demos.add(new Demo());\n" +
				"		return demos;";
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_STATEMENTS);
		parser.setSource(body.toCharArray());
		ASTNode result = parser.createAST(null);
		return result;
	}
	@Override
	public boolean visit(FieldDeclaration node) {
		for (Object obj : node.fragments()) {
			VariableDeclarationFragment v = (VariableDeclarationFragment) obj;
			System.out.println("Field:\t" + v.getName());

		}

		return true;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		int modifiers = node.getModifiers();
		System.out.println(Modifier.isPublic(modifiers));
		System.out.println(Modifier.isStatic(modifiers));
		System.out.println(Modifier.isFinal(modifiers));
		System.out.println(node.getName().getFullyQualifiedName().equals("name"));
		Type type = node.getReturnType2();
		if (type instanceof PrimitiveType) {

		} else if (type instanceof SimpleType) {

		} else if (type instanceof ParameterizedType) {

		}
		System.out.println(node.getReturnType2().getClass());
		ASTNode body = ASTNode.copySubtree(node.getAST(), createAstNodeWithMethodBody());
		node.setBody((Block) body);
		return true;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		return "Demo".equals(node.getName().getFullyQualifiedName());
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean visit(ImportDeclaration node) {
		if (node.getName().getFullyQualifiedName().equals(Paths.class.getName())) {
			
			ImportDeclaration importDeclaration = node.getAST().newImportDeclaration();
			Name name = node.getAST().newName(Demo.class.getName());
			importDeclaration.setName(name);
			ASTNode.copySubtree(node.getAST(), importDeclaration);
			CompilationUnit cu = (CompilationUnit) node.getRoot();
			cu.imports().add(importDeclaration);
			ASTNode.copySubtree(node.getRoot().getAST(), importDeclaration);
			System.out.println(node.getRoot());

		}
		return super.visit(node);
	}
}

class Demo {

	public final static List<Demo> name() {
		return null;
	}
}