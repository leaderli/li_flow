package com.leaderli.li.flow.editor.serialize;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.leaderli.li.flow.editor.model.ConnectionNode;
import com.leaderli.li.flow.editor.model.FlowDiagram;
import com.leaderli.li.flow.editor.model.FlowNode;
import com.leaderli.li.flow.editor.model.GotoNode;
import com.leaderli.li.flow.editor.model.Location;
import com.leaderli.li.flow.util.GsonUtil;

public class SerializeUtil {

	public static FlowDiagram transfer(SerializeFlowDiagram origin) {

		FlowDiagram target = new FlowDiagram();
		target.setPackageName(origin.getPackageName());
		target.setNextNodeID(origin.getNextNodeID());

		Map<Integer, FlowNode> flowNodes = new HashMap<>();
		Map<Integer, SerializeFlowNode> seriaFlowNodes = new HashMap<>();
		for (SerializeFlowNode seriaFlowNode : origin.getFlowNodes()) {
			FlowNode flowNode = new FlowNode();

			int[] point = seriaFlowNode.getPoint();

			flowNode.setId(seriaFlowNode.getId());
			flowNode.setType(seriaFlowNode.getType());
			flowNode.setName(seriaFlowNode.getName());
			flowNode.setFlowName(seriaFlowNode.getFlowName());
			flowNode.setIcon(seriaFlowNode.getIcon());
			flowNode.setPoint(new Location(point[0], point[1]));

			flowNode.setParent(target);

			flowNodes.put(flowNode.getId(), flowNode);
			seriaFlowNodes.put(flowNode.getId(), seriaFlowNode);

		}
		target.setFlowNodes(flowNodes.values().stream().collect(Collectors.toList()));

		Map<Integer, GotoNode> gotoNodes = new HashMap<>();
		Map<Integer, SerializeGotoNode> seriaGotoNodes = new HashMap<>();

		for (SerializeGotoNode seriaGotoNode : origin.getGotoNodes()) {

			GotoNode gotoNode = new GotoNode();

			gotoNode.setId(seriaGotoNode.getId());
			gotoNode.setName(seriaGotoNode.getName());

			gotoNodes.put(gotoNode.getId(), gotoNode);
			seriaGotoNodes.put(gotoNode.getId(), seriaGotoNode);
		}

		Map<Integer, ConnectionNode> connectionNodes = new HashMap<>();
		Map<Integer, SerializeConnectionNode> seriaConnectionNodes = new HashMap<>();

		for (SerializeConnectionNode seriaConnection : origin.getConnectionNodes()) {

			ConnectionNode connectionNode = new ConnectionNode();

			connectionNode.setId(seriaConnection.getId());
//			connectionNode.setSourceID(gotoNodes.get(seriaConnection.getSource()));
			connectionNode.setTarget(flowNodes.get(seriaConnection.getTarget()));

			connectionNode.setParent(target);

			connectionNodes.put(connectionNode.getId(), connectionNode);
			seriaConnectionNodes.put(connectionNode.getId(), seriaConnection);
		}
		target.setConnectionNodes(connectionNodes.values().stream().collect(Collectors.toList()));
		// 补全属性
		for (FlowNode flowNode : target.getFlowNodes()) {

			flowNode.setGotoNodes(seriaFlowNodes.get(flowNode.getId()).getGotoNodes()
					.stream()
					.map(id -> {
						GotoNode go = gotoNodes.get(id);

						int connectionId = seriaGotoNodes.get(id).getConnection();
						ConnectionNode connectionNode = connectionNodes.get(connectionId);

						go.setParent(flowNode);
						go.setLinkedConnectionNode(connectionNode);

						// 此时gotoNode才有parent
						if (connectionNode != null) {
							connectionNode.setSource(go);
						}
						return go;
					})
					.collect(Collectors.toList()));

		}
		return target;

	}

	public static SerializeFlowDiagram transfer(FlowDiagram origin) {

		SerializeFlowDiagram target = new SerializeFlowDiagram();

		target.setPackageName(origin.getPackageName());
		target.setNextNodeID(origin.getNextNodeID());

		target.setGotoNodes(new ArrayList<>());

		List<SerializeFlowNode> flowNodes = origin.getFlowNodes()
				.stream()
				.map(flowNode -> {

					Location point = flowNode.getPoint();
					SerializeFlowNode seriaFlowNode = new SerializeFlowNode();

					seriaFlowNode.setId(flowNode.getId());
					seriaFlowNode.setType(flowNode.getType());
					seriaFlowNode.setName(flowNode.getName());
					seriaFlowNode.setFlowName(flowNode.getFlowName());
					seriaFlowNode.setIcon(flowNode.getIcon());
					seriaFlowNode.setPoint(new int[] { point.getX(), point.getY() });
					seriaFlowNode.setGotoNodes(new ArrayList<>());

					flowNode.getGotoNodes().forEach(gotoNode -> {

						SerializeGotoNode seriaGotoNode = new SerializeGotoNode();

						seriaGotoNode.setId(gotoNode.getId());
						seriaGotoNode.setName(gotoNode.getName());
						int connectionId = 0;
						ConnectionNode connectionNode = gotoNode.getLinkedConnectionNode();
						if (connectionNode != null) {
							connectionId = connectionNode.getId();
						}
						seriaGotoNode.setConnection(connectionId);

						target.getGotoNodes().add(seriaGotoNode);
						seriaFlowNode.getGotoNodes().add(seriaGotoNode.getId());
					});

					return seriaFlowNode;
				})
				.collect(Collectors.toList());
		target.setFlowNodes(flowNodes);



		List<SerializeConnectionNode> connectionNodes = origin.getConnectionNodes()
				.stream()
				.map(connectionNode -> {

					SerializeConnectionNode seriaConnectionNode = new SerializeConnectionNode();

					seriaConnectionNode.setId(connectionNode.getId());
					seriaConnectionNode.setSource(connectionNode.getSource().getId());
					seriaConnectionNode.setTarget(connectionNode.getTarget().getId());

					return seriaConnectionNode;
				})
				.collect(Collectors.toList());

		target.setConnectionNodes(connectionNodes);

		return target;
	}

	public static void main(String[] args) {
		FlowDiagram flowDiagram = new FlowDiagram();
		flowDiagram.setPackageName("flow.subflow.main");
		SerializeFlowDiagram seriaFlowDiagram = transfer(flowDiagram);
		System.out.println(GsonUtil.toJson(seriaFlowDiagram));

	}
}
