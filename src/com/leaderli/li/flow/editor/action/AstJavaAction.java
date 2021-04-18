package com.leaderli.li.flow.editor.action;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.UUID;

import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Name;

import com.leaderli.li.flow.LiPlugin;
import com.leaderli.li.flow.constant.PluginConstant;
import com.leaderli.li.flow.editor.model.FlowNode;

public class AstJavaAction extends SelectionContextMenuAction<FlowNode> {

	public AstJavaAction() {
		setId("ast");
		setText("ast");

	}

	private static ASTNode createAstNodeWithMethodBody() {
		String body = "String uuid = " + UUID.randomUUID() + ";\nint a = 1;\n int b = 2;\n return (a + b);";
		System.out.println(body);
		@SuppressWarnings("deprecation")
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_STATEMENTS);
		parser.setSource(body.toCharArray());
		ASTNode result = parser.createAST(null);

		return result;
	}

	@Override
	public void run() {

		IFile java = (IFile) editPart.getAdapter(IFile.class);
		IProject project = java.getProject();
		IPath path = project.getFullPath().append("lib/runner-1.0.jar");
		System.out.println(path);
		System.out.println(project.getFile("lib/runner-1.0.jar").exists());
		if(true)
		return;
		IFolder folder= (IFolder) project.getFolder(PluginConstant.LIB_DIR);
		if(!folder.exists()) {
			try {
				folder.create(true, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		String jar = "runner-1.0.jar";
		IFile jarFile = folder.getFile(jar);
		if(!jarFile.exists()) {

			URL url = LiPlugin.getDefault().getBundle().getResource("resource/runner-1.0.jar");
			URI uri;
			try {
			InputStream stream = url.openStream();
				jarFile.create(stream, true, null);
			} catch (Exception e) {
				e.printStackTrace();
			}
				
		}
		
		IFile classpath = project.getFile(".classpath");
		SAXReader reader = new SAXReader();
        try {
			Document document = reader.read(classpath.getContents());
			Node node = document.selectSingleNode("//classpathentry[@kind='lib' and  @path='lib/runner-1.0.jar']");
			if(node!=null) {
				return;
			}
			Branch root = document.getRootElement();
			Element runner_jar= root .addElement("classpathentry")
		            .addAttribute("kind", "lib")
		            .addAttribute("path", "lib/runner-1.0.jar");
	        OutputFormat format = OutputFormat.createPrettyPrint();
	        XMLWriter   writer = new XMLWriter(System.out, format);
	        writer.write(document);
	        writer.close();
			System.out.println(document.asXML());
			InputStream stream = new ByteArrayInputStream(document.asXML().getBytes());
			try {
				classpath.setContents(stream, true, true, null);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		} catch (DocumentException e1) {
			e1.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
        {
			try {
//				ResourcesUtil.copyFileFromPluginToProject("","");
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}

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

	}

	private Object newImport(AST ast) {
		ImportDeclaration newImportDeclaration = ast.newImportDeclaration();
		Name name = ast.newName(Demo.class.getName());
		newImportDeclaration.setName(name);
		return newImportDeclaration;
	}

}
