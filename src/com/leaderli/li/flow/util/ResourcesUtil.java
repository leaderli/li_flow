package com.leaderli.li.flow.util;

import java.io.File;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.refactoring.IJavaRefactorings;
import org.eclipse.jdt.core.refactoring.descriptors.RenameJavaElementDescriptor;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.Refactoring;
import org.eclipse.ltk.core.refactoring.RefactoringContribution;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.leaderli.li.flow.constant.PluginConstant;

public class ResourcesUtil {

	public static String getSimpleName(IFile f) {
		return ResourcesUtil.getSimpleName(f.getName());
	}

	public static String getSimpleName(String fileName) {
		if (StringUtils.isEmpty(fileName) || !fileName.contains(".")) {
			return fileName;
		}
		return StringUtils.substringBeforeLast(fileName, ".");
	}



	public static String getFlowNodeSourceCodeTemplate(IProject project,String type) {

		try {
			return IOUtils.toString(
					project.getFile(PluginConstant.RESOURCE_DIR + File.separator + type + "." + PluginConstant.TEMPLATE_EXTENSION).getContents());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	public static IFolder getSourceFolderOfSubflow(IProject project, String packageName) {
		packageName = packageName.replace('.', File.separatorChar);
		return project.getFolder(PluginConstant.SOURCE_DIR + File.separatorChar + packageName);

	}
	public static void openJava(String className, IProject project) {

		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

		if (project == null) {
			IEditorInput input = page.getActiveEditor().getEditorInput();
			IResource activeResource = input.getAdapter(IResource.class);
			project = activeResource.getProject();
		}

		int lastSpot = className.lastIndexOf(".");
		String packageName = "";
		String fileName = className;
		if (lastSpot > 0) {
			packageName = className.substring(0, lastSpot).replace('.', File.separatorChar);
			fileName = className.substring(lastSpot + 1);
		}

		IFolder flowPackage = project.getFolder(PluginConstant.SOURCE_DIR + File.separatorChar + packageName);
		IFile java = flowPackage.getFile(fileName + PluginConstant.JAVA_EXTENSION);

		try {
			IDE.openEditor(page, java);
		} catch (PartInitException e) {
			e.printStackTrace();
		}

	}

	public static IFile renameJavaFile(IFile file, String name) {

		Assert.isTrue("java".equals(file.getFileExtension()), " it's not a java file");
		IFolder folder = (IFolder) file.getParent();
		try {
			for (IResource mem : folder.members()) {
				Assert.isTrue(!(name + PluginConstant.JAVA_EXTENSION).equals(mem.getName()), " duplicate named java file with [" + name + ".java]");
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		ICompilationUnit unit = JavaCore.createCompilationUnitFrom(file);
		RefactoringContribution contribution = RefactoringCore.getRefactoringContribution(IJavaRefactorings.RENAME_COMPILATION_UNIT);

		RenameJavaElementDescriptor descriptor = (RenameJavaElementDescriptor) contribution.createDescriptor();
		descriptor.setProject(unit.getResource().getProject().getName());
		descriptor.setNewName(name); // new name for a Class
		descriptor.setJavaElement(unit);
	
		RefactoringStatus status = new RefactoringStatus();
		try {
			Refactoring refactoring = descriptor.createRefactoring(status);
	
			IProgressMonitor monitor = new NullProgressMonitor();
			refactoring.checkInitialConditions(monitor);
			refactoring.checkFinalConditions(monitor);
			Change change = refactoring.createChange(monitor);
			change.perform(monitor);
	
			return folder.getFile(name + PluginConstant.JAVA_EXTENSION);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static CompilationUnit getCompilationUnitBySource(IFile iFile) {
		@SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(JavaCore.createCompilationUnitFrom(iFile));
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		return cu;
	}

	public static CompilationUnit getCompilationUnitBySource(String java) {
		@SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(java.toCharArray());
		CompilationUnit cu = (CompilationUnit) parser.createAST(null);
		return cu;
	}

	public static ASTNode createAstNodeWithMethodBody(String methodBody) {
		@SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_STATEMENTS);
		parser.setSource(methodBody.toCharArray());
		ASTNode result = parser.createAST(null);
		return result;
	}



}
