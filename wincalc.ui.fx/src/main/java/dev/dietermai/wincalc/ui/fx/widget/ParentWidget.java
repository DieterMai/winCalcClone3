package dev.dietermai.wincalc.ui.fx.widget;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Parent;

public abstract class ParentWidget<T extends Parent> extends NodeWidget<T>{
	private List<NodeWidget<?>> children = new ArrayList<>();
	
	protected ParentWidget(){
		
	}
	
	protected void addChild(NodeWidget<?> child) {
		children.add(child);
	}
	
	protected List<NodeWidget<?>> getChildren(){
		return children;
	}
	
	
	protected void internalCreateNodes() {
		createChildeNodes();
		createNodes();
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
		for(NodeWidget<?> child : children) {
			child.internalCreateNodes();
		}
	}
	
	private void initializeChildNodes() {
		for(NodeWidget<?> child : children) {
			child.internalInitializeNodes();
		}
	}
	
	private void registerChildWidgets() {
		for(NodeWidget<?> child : children) {
			child.internalRegister();
		}
	}
	
	private void connectChildWidgets() {
		for(NodeWidget<?> child : children) {
			child.internalConnect();
		}
	}
}
