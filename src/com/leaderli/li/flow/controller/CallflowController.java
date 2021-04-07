package com.leaderli.li.flow.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;

import com.leaderli.li.flow.constant.PluginConstant;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.util.GEFUtil;
import com.leaderli.li.flow.util.GsonUtil;
import com.leaderli.li.flow.util.ResourcesUtil;

public class CallflowController implements IResourceChangeListener {

	public void init(IProject project) {

	}

	private void invalidateResourceCache() {
	}

	public List<IFile> getProjectFlowFiles(IProject project) {

		IFolder folder = GEFUtil.getFlowFolder(project);
		try {
			return Arrays.stream(folder.members()).filter(m -> m.getType() == 1 && PluginConstant.FLOW_EXTENSION.equals(m.getFileExtension())).map(f -> (IFile) f)
					.collect(Collectors.toList());
		} catch (CoreException e) {
			throw new RuntimeException(e);
		}

	}

	public List<String> getFlowSubFlowReturnNodes(IProject project, String flowName) {
		List<String> returns = new ArrayList<String>();
		for (IFile file : getProjectFlowFiles(project)) {
			if (ResourcesUtil.getSimpleName(file).equals(flowName)) {
				try {
					FlowDiagram flowDiagram = GsonUtil.fromJson(file.getContents(), FlowDiagram.class);
					flowDiagram.getFlowNodes().stream()
							.filter(node -> PluginConstant.TYPE_SUBFLOW_RETURN.equals(node.getType()))
							.forEach(node -> {
								returns.add(node.getName());
							});
				} catch (CoreException e) {
					e.printStackTrace();
				}

				break;
			}
		}
		return returns;
	}

	public List<IFile> getFlowNodes(IProject project, String flowName) {
		IFolder folder = ResourcesUtil.getSourceFolderOfSubflow(project, PluginConstant.SUBFLOW_SOURCE_BASE_PACKAGE + flowName);
		if (folder != null) {
			try {
				return Arrays.stream(folder.members()).filter(m -> m.getType() == 1 && PluginConstant.FLOW_EXTENSION.equals(m.getFileExtension())).map(f -> (IFile) f)
						.collect(Collectors.toList());
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		return Collections.emptyList();
	}

	public List<String> getSubFlowNames(IProject project) {
		return getProjectFlowFiles(project).stream().map(f -> ResourcesUtil.getSimpleName(f)).collect(Collectors.toList());
	}

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		// 后续优化
//		Visitor visitor = new Visitor();
//		try {
//			event.getDelta().accept(visitor);
//		} catch (CoreException e) {
//			e.printStackTrace();
//		}
//		if (visitor.isResourceAddedOrRemoved()) {
//			invalidateResourceCache();
//		}
	}

}

class Visitor implements IResourceDeltaVisitor {

	private boolean resourceAddedOrRemoved = false;

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {

		if (delta.getAffectedChildren(IResourceDelta.ADDED | IResourceDelta.REMOVED).length > 0) {
			this.resourceAddedOrRemoved = true;
			return false;
		}
		return delta.getAffectedChildren().length > 0;
	}

	public boolean isResourceAddedOrRemoved() {
		return resourceAddedOrRemoved;
	}

}