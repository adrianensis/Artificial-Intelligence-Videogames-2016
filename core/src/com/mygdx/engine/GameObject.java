package com.mygdx.engine;

import java.util.LinkedList;
import java.util.List;

public class GameObject {
	private LinkedList<Component> components;
	private String name;
	
	public GameObject(String n) {
		this.name=n;
		components = new LinkedList<Component>();
	}
	
	public String getName(){
		return name;
	}
	
	public void setName(String n){
		this.name=n;
	}
	
	/**
	 * Actualiza el los componentes del gameobject.
	 */
	public void update(){
		for (Component component : components) {
			component.update();
		}
	}
	
	/**
	 * Inserta un componente en un gameobject.
	 * @param component
	 */
	public GameObject addComponent(Component component){
		component.setGameObject(this);
		this.components.add(component);
		return this;
	}
	
	/**
	 * Busca un componente en el gameobject.
	 * @param componentClass
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(Class<T> componentClass){
		for (Component component : components) {
			if(componentClass.isInstance(component)){
				return (T) component;
			}
		}
		
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Component> List<T> getComponents(Class<T> componentClass){
		List<Component> foundComponents = new LinkedList<Component>();
		for (Component component : components) {
			if(componentClass.isInstance(component)){
				foundComponents.add(component);
			}
		}
		
		return (List<T>) foundComponents;
	}
}
