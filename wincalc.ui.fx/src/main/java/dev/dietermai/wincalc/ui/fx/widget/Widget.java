package dev.dietermai.wincalc.ui.fx.widget;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Node;
import javafx.scene.Parent;

public abstract class Widget<T extends Node>{
	public static <T extends Node> Widget<T> initialize(Widget<T> widget){
		widget.internalCreateNodes();
		widget.internalInitializeNodes();
		widget.internalRegister();
		widget.internalConnect();
		return widget;
	}
	
	private List<Widget<?>> children = new ArrayList<>();
	
	protected Widget(){
		
	}
	
	private T node;
	
	public Widget<T> initialize() {
		return this;
	}
	
	protected void setNode(T node) {
		this.node = node;
	}
	
	public T getNode() {
		return node;
	}
	
	protected void addChild(Widget<?> child) {
		children.add(child);
	}
	
	protected List<Widget<?>> getChildren(){
		return children;
	}
	
	
	protected void internalCreateNodes() {
		createChildeNodes();
		setNode(createNodes());
	}
	

	protected void internalInitializeNodes() {
		initializeChildNodes();
		initializeNodes();
	}
	
	protected void internalRegister() {
		registerChildWidgets();
		register();
	}
	
	protected void internalConnect() {
		connectChildWidgets();
		connect();
	}
	
	private void createChildeNodes() {
		for(Widget<?> child : children) {
			child.internalCreateNodes();
		}
	}
	
	private void initializeChildNodes() {
		for(Widget<?> child : children) {
			child.internalInitializeNodes();
		}
	}
	
	private void registerChildWidgets() {
		for(Widget<?> child : children) {
			child.internalRegister();
		}
	}
	
	private void connectChildWidgets() {
		for(Widget<?> child : children) {
			child.internalConnect();
		}
	}
	
	protected abstract T createNodes();
	protected abstract void initializeNodes();
	protected abstract void register();
	protected abstract void connect();
}
