package com.additolc.state;

import javafx.geometry.Point2D;
import lombok.Data;

@Data
public class AircraftState {

	private double keas;
	private double mach;
	private Point2D qPoint = new Point2D(0,0);
	private double engineDeltaSpeed;
}
