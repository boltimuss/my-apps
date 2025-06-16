package com.additolc.nomograph;

import javafx.scene.paint.Color;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NomographCharacteristics {

	private LabelSide labelSide;
	private boolean isDescending;
	private double fontSize;
	private double fontHeightOffset;
	private double tickWidthHeight;
	private double lineWidth;
	private Color color;
}
