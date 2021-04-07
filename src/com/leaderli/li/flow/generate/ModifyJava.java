package com.leaderli.li.flow.generate;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.TextEdit;

public class ModifyJava {

	private ICompilationUnit compileUnit;
	private CompilationUnit cu;




	@SuppressWarnings("deprecation")
	public void begin(IFile java) {

		compileUnit = JavaCore.createCompilationUnitFrom(java);
		try {
			compileUnit.open(null);
			compileUnit = compileUnit.getWorkingCopy(null);
			ASTParser parser = ASTParser.newParser(AST.JLS8);
			parser.setSource(compileUnit);
			cu = (CompilationUnit) parser.createAST(null);
			cu.recordModifications();
		} catch (JavaModelException e) {
			throw new RuntimeException(e);
		}
	}

	public void modify(ASTVisitor visitor) {
		cu.accept(visitor);
	}
	public void commit() {
		try {
			IDocument document = new Document(compileUnit.getSource());
			TextEdit textEdit = cu.rewrite(document, null);
			textEdit.apply(document);
			compileUnit.getBuffer().setContents(document.get());
			compileUnit.reconcile(ICompilationUnit.NO_AST, false, null, null);
			compileUnit.commitWorkingCopy(true, null);
			compileUnit.discardWorkingCopy();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
