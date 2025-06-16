package com.additolc.nomograph;

import javafx.scene.paint.Color;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Section {

	private double mmHeight;
	private double mmWidth;
	private int numDivisions;
	private double startValue;
	private double endValue;
	private double fontAxisOffset;
	private double fontAxisOffsetLast;
	private boolean drawLast;
	private double startLocation;
	private double endLocation;
	private Color color;
}
