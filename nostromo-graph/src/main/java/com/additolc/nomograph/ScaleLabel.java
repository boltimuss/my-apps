package com.additolc.nomograph;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ScaleLabel {

	private Color labelColor;
	private String label;
	private boolean drawValue;
	private String stepNum;
	private Point2D stepNumLocation;
	private Point2D scaleLocation;
	private Point2D scaleOffset;
	private double rotation = 0;
}
