package com.birdsofprey.nomograph;

import java.util.LinkedList;

import javafx.scene.canvas.GraphicsContext;

public class Nomograph {

	private boolean boxed;
	private LinkedList<Axis> axes = new LinkedList<Axis>();
	
	public boolean isBoxed() {
		return boxed;
	}
	
	public void setBoxed(boolean boxed) {
		this.boxed = boxed;
	}
	
	public void addAxis(Axis axis) {
		axes.add(axis);
	}
	
	public void draw(GraphicsContext gc)
	{
		for (Axis axis:axes)
		{
			axis.draw(gc);
		}
	}
}
