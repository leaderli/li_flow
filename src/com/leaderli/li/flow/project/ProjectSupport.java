package com.leaderli.li.flow.project;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaCore;

import com.leaderli.li.flow.util.ResourcesUtil;

public class ProjectSupport {

	public static IProject createProject(String projectName, URI location) {

		Assert.isTrue(StringUtils.isNotEmpty(projectName));
		IProject project;
		try {
			project = createBaseProject(projectName, location);

			addNature(project);
			addBuildSpec(project);
			String[] folders = { "flow", "src/com/leaderli/li/flow/base", "resource" };
			addToPrjectStructure(project, folders);
			
			addClassPath(project);
			addDependency(project);
			addCodeTemplate(project);
			addBaseCode(project);

		} catch (Exception e) {
			e.printStackTrace();
			project = null;
		}
		return project;
	}

	private static void addBaseCode(IProject project) throws Exception {
		addBaseCode(project, "BaseReturn");
		addBaseCode(project, "BaseServlet");
		addBaseCode(project, "BaseSubFlow");
		addBaseCode(project, "Session");

	}

	private static void addBaseCode(IProject project, String name) throws Exception {
		String fromTo = "src/com/leaderli/li/flow/base/" + name + ".java";
		ResourcesUtil.copyFileFromPluginToProject(project, fromTo, fromTo);

	}

	private static void addCodeTemplate(IProject project) throws Exception {
		
		addCodeTemplate(project,"servlet");
		addCodeTemplate(project,"subflowentry");
		addCodeTemplate(project,"subflowref");
		addCodeTemplate(project,"subflowreturn");
	}
	private static void addCodeTemplate(IProject project,String name) throws Exception {
		String fromTo = "resource/"+name+".template";
		ResourcesUtil.copyFileFromPluginToProject(project, fromTo, fromTo);
	}

	private static void addDependency(IProject project) throws Exception {
		String from = "resource/runner-1.0.jar";
		String to = "lib/runner-1.0.jar";
		ResourcesUtil.copyFileFromPluginToProject(project, from, to);
	}

	private static void addClassPath(IProject project) throws Exception {
		String from = "resource/new_project_classpath.xml";
		String to = ".classpath";
		ResourcesUtil.copyFileFromPluginToProject(project, from, to);
	}

	private static void addToPrjectStructure(IProject project, String[] paths) throws CoreException {
		for (String path : paths) {
			IFolder folder = project.getFolder(path);
			ResourcesUtil.createFolder(folder);
		}

	}

	/**
	 * 
	 * 用来生成 .project文件中的natures属性
	 */
	private static void addNature(IProject project) throws CoreException {
		if (!project.hasNature(JavaCore.NATURE_ID)) {
			IProjectDescription description = project.getDescription();
			String[] prevNatures = description.getNatureIds();
			String[] newNatures = new String[prevNatures.length + 1];
			System.arraycopy(prevNatures, 0, newNatures, 0, prevNatures.length);
			newNatures[prevNatures.length] = JavaCore.NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);
		}

	}

	/**
	 * 
	 * 用来生成 .project文件中的buildSpec属性
	 */
	private static void addBuildSpec(IProject project) throws CoreException {

		IProjectDescription description = project.getDescription();
		ICommand[] buildSpecs = description.getBuildSpec();
		List<ICommand> list = Arrays.asList(buildSpecs);

		if (list.stream().anyMatch(command -> JavaCore.BUILDER_ID.equals(command.getBuilderName()))) {
			return;
		}
		ICommand command = description.newCommand();
		command.setBuilderName(JavaCore.BUILDER_ID);
		project.setDescription(description, new NullProgressMonitor());

	}

	private static IProject createBaseProject(String projectName, URI location) throws CoreException {
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		if (!project.exists()) {

			URI projectLocation = location;
			IProjectDescription desc = project.getWorkspace().newProjectDescription(project.getName());

			if (location != null && ResourcesPlugin.getWorkspace().getRoot().getLocationURI().equals(location)) {
				projectLocation = null;
			}
			desc.setLocationURI(projectLocation);
			project.create(desc, null);

			if (!project.isOpen()) {
				project.open(null);
			}

		}
		return project;
	}
}
