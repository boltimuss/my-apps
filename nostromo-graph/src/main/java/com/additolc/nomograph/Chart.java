package com.additolc.nomograph;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public abstract class Chart extends Canvas {

	protected boolean wasDragged;
	protected final double mmPerPixel = Screen.getPrimary().getDpi()/25.4;
	private HashMap<String, AbstractScale> scales = new HashMap<>();
	private Consumer<MouseEvent> mouseClickedHandler;
	private Consumer<MouseEvent> mouseDraggedHandler;
	
	public Chart(Dimension2D dimensions)
	{
		super(dimensions.getWidth(), dimensions.getHeight());
		addEventHandler(MouseEvent.MOUSE_RELEASED, (MouseEvent event) -> {
			for (AbstractScale s:scales.values()) s.setDragging(false);
		});
			
		addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event) -> {
			
			if (wasDragged) 
			{
				wasDragged = false;
				return;
			}
			
			mouseClickedHandler.accept(event);
			getGraphicsContext2D().clearRect(-10, 0, getWidth(), getHeight());
			getGraphicsContext2D().setTransform(1, 0, 0, 1, 0, 0);
			getGraphicsContext2D().translate(4 * mmPerPixel, 0);
			draw(2.0);
	    });
		
		addEventHandler(MouseEvent.MOUSE_DRAGGED, (MouseEvent event) -> {
			
			wasDragged = true;
			mouseDraggedHandler.accept(event);			
			drawLines();
		});
		
		init();
		getGraphicsContext2D().translate(4 * mmPerPixel, 0);
		draw(2.0);

	}
	
	public Point2D calculateIntersectionPoint(double m1,  double b1, double m2, double b2) 
	{

	    if (m1 == m2) {
	        return null;
	    }

	    double x = (b2 - b1) / (m1 - m2);
	    double y = (m1 * x) + b1;

	    return new Point2D(x, y);
	}
	
	public void draw(double scale)
	{
		getGraphicsContext2D().scale(scale, scale);
		for (AbstractScale s:scales.values()) s.draw(getGraphicsContext2D());
		for (AbstractScale s:scales.values()) s.drawDraggableNotch(getGraphicsContext2D());
		
//		getGraphicsContext2D().setFill(Color.RED);
//		getGraphicsContext2D().fillRect(142 * mmPerPixel, 8 * mmPerPixel, 12 * mmPerPixel, 150 * mmPerPixel);
	}
	public abstract void drawLines();
	
	public abstract Object execute(Object... parameters);
	
	protected abstract void init();
}
