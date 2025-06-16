package com.additolc.nomograph;

import javafx.scene.paint.Color;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ShadedRegion {

	private double width;
	private double startValue;
	private double endValue;
	private double yMMStart;
	private double yMMEnd;
	private boolean useYValue;
	private Color color;
}
