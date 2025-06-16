package com.additolc.nomograph;

import java.util.LinkedList;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class AbstractScale implements Scale {

	protected LinkedList<Section> sections;
	protected double mmHeight;
	protected double mmWidth;
	protected double mmStartOffset;
	protected NomographCharacteristics charactistics;
	protected Point2D scaleOffset;
	protected LinkedList<ShadedRegion> shadedRegions;
	protected final double mmPerPixel = Screen.getPrimary().getDpi()/25.4;
	protected boolean showDraggable;
	protected ScaleLabel label;
	protected double value;
	protected boolean isDragging;
	protected Rectangle2D clickZone;
	
	@Builder.Default
	protected Point2D draggableOffset = new Point2D(0, 0);
	@Builder.Default
	protected Point2D mouseSceneOffset = new Point2D(0, 0);
	@Builder.Default
	protected double draggableX = -99;
	@Builder.Default
	protected double draggableY = -99;
}
