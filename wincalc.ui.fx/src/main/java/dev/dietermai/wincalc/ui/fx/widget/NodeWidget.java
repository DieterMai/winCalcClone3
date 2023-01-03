package dev.dietermai.wincalc.ui.fx.widget;

import javafx.scene.Node;

public abstract class NodeWidget<T extends Node> {
	
	private T node;
	
	public NodeWidget<T> initialize() {
		return this;
	}
	
	protected void setNode(T node) {
		this.node = node;
	}
	
	public T getNode() {
		return node;
	}
	
	
	public static <T extends Node> NodeWidget<T> initialize(NodeWidget<T> widget){
		widget.internalCreateNodes();
		widget.internalInitializeNodes();
		widget.internalRegister();
		widget.internalConnect();
		return widget;
	}
	
	protected void internalCreateNodes() {
		createNodes();
	}
	
	protected void internalInitializeNodes() {
		initializeNodes();
	}
	
	protected void internalRegister() {
		register();
	}
	
	protected void internalConnect() {
		connect();
	}
	
	protected abstract void createNodes();
	protected abstract void initializeNodes();
	protected abstract void register();
	protected abstract void connect();
}
