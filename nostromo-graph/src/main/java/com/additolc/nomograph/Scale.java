package com.additolc.nomograph;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public interface Scale {
	
	public abstract boolean containsClick(double x, double y);
	public abstract boolean isDraggingDot(double x, double y);
	public abstract void draw(GraphicsContext gc);
	public abstract void drawDraggableNotch(GraphicsContext gc);
	public abstract double getDataPointForSlideValue(double slideValue);
	public abstract Point2D getPointForSlideValue(double slideValue);
}
