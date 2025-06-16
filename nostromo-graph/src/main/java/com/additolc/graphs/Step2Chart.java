package com.additolc.graphs;

import java.util.LinkedList;

import com.additolc.nomograph.Chart;
import com.additolc.nomograph.LabelSide;
import com.additolc.nomograph.NomographCharacteristics;
import com.additolc.nomograph.VerticalScale;
import com.additolc.state.GameState;
import com.additolc.nomograph.ScaleLabel;
import com.additolc.nomograph.Section;
import com.additolc.nomograph.ShadedRegion;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Step2Chart extends Chart {

	public Step2Chart(Dimension2D dimensions)
	{
		super(dimensions);
	}

	@Override
	public void drawLines() {
		
		getGraphicsContext2D().clearRect(-10, 0, getWidth(), getHeight());
		getGraphicsContext2D().setTransform(1, 0, 0, 1, 0, 0);
		getGraphicsContext2D().translate(4 * mmPerPixel, 0);
		getGraphicsContext2D().scale(2.0, 2.0);
		double slope;
		
		if (getScales().get("altitudeScale").isShowDraggable() && getScales().get("keasLowScale").isShowDraggable())
		{
			double x1 = getScales().get("altitudeScale").getDraggableX();
			double y1 = getScales().get("altitudeScale").getDraggableY();
			double x2 = getScales().get("keasLowScale").getDraggableX();
			double y2 = getScales().get("keasLowScale").getDraggableY();
			slope = (y2 - y1) / (x2 - x1);
			double x3 = 76 * mmPerPixel;
			double y3 = (slope * (x3 - x2)) + y2;
			
			getGraphicsContext2D().setLineWidth(1);
			getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
			getGraphicsContext2D().strokeLine(x2, y2, x3, y3);
		}
		else if (getScales().get("altitudeScale").isShowDraggable() && getScales().get("keasHighScale").isShowDraggable())
		{
			double x1 = getScales().get("altitudeScale").getDraggableX();
			double y1 = getScales().get("altitudeScale").getDraggableY();
			double x2 = getScales().get("keasHighScale").getDraggableX();
			double y2 = getScales().get("keasHighScale").getDraggableY();
			slope = (y2 - y1) / (x2 - x1);
			double x3 = 76 * mmPerPixel;
			double y3 = (slope * (x3 - x2)) + y2;
			
			getGraphicsContext2D().setLineWidth(1);
			getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
			getGraphicsContext2D().strokeLine(x2, y2, x3, y3);
		}
		
		draw(1.0);
	}

	private double calcLowKeasYForMachY(boolean useMin, boolean useLowMach)
	{
		double x1 = getScales().get("altitudeScale").getScaleOffset().getX() * mmPerPixel;
		double y1 = getScales().get("altitudeScale").getDraggableY();
		double x2 = (useLowMach) ? getScales().get("keasLowScale").getScaleOffset().getX() * mmPerPixel : getScales().get("keasHighScale").getScaleOffset().getX() * mmPerPixel;
		double x3 = (useLowMach) ? getScales().get("machLow").getScaleOffset().getX() * mmPerPixel : getScales().get("machHigh").getScaleOffset().getX() * mmPerPixel;
		double y3 = (useMin) ? getScales().get("machLow").getScaleOffset().getY() * mmPerPixel : 
			(getScales().get("machLow").getScaleOffset().getY() + getScales().get("machLow").getMmHeight()) * mmPerPixel;
		double slope = (y3 - y1) / (x3 - x1);
		return (slope * (x2 - x1)) + y1;
	}
	
	private double calcAltitudeYForMachY(boolean useMin, boolean useLowMach)
	{
		double x1 = getScales().get("altitudeScale").getScaleOffset().getX() * mmPerPixel;
		double x2 = (useLowMach) ? getScales().get("keasLowScale").getScaleOffset().getX() * mmPerPixel : getScales().get("keasHighScale").getScaleOffset().getX() * mmPerPixel;
		double y2 = (useLowMach) ? getScales().get("keasLowScale").getDraggableY() : getScales().get("keasHighScale").getDraggableY();
		double x3 = (useLowMach) ? getScales().get("machLow").getScaleOffset().getX() * mmPerPixel : getScales().get("machHigh").getScaleOffset().getX() * mmPerPixel;
		double y3 = (useMin) ? getScales().get("machLow").getScaleOffset().getY() * mmPerPixel : 
			(getScales().get("machLow").getScaleOffset().getY() + getScales().get("machLow").getMmHeight()) * mmPerPixel;
		double slope = (y3 - y2) / (x3 - x2);
		return y2 - ((x2 - x1) * slope);
	}
	
	private int clampToscale(double altitudeDragX, double altitudeDragY, double keasLowDragX, double keasLowDragY, double keasHighDragX, double keasHighDragY)
	{
		double slope;
		double bias = .1;
		
		if (getScales().get("altitudeScale").isShowDraggable() && getScales().get("keasLowScale").isShowDraggable())
		{
			double x1 = altitudeDragX;
			double y1 = altitudeDragY;
			double x2 = keasLowDragX;
			double y2 = keasLowDragY;
			slope = (y2 - y1) / (x2 - x1);
			double x3 = 76 * mmPerPixel;
			double y3 = (slope * (x3 - x2)) + y2;
			double offsetY = mmPerPixel * getScales().get("machLow").getScaleOffset().getY();
			
			if (y3 < offsetY + (mmPerPixel*getScales().get("machLow").getMmStartOffset() - bias))
			{
				return -1;
			}
			else if (y3 > offsetY + (getScales().get("machLow").getMmHeight()*mmPerPixel) + bias)
			{
				return 1;
			}
			
		}
		else if (getScales().get("altitudeScale").isShowDraggable() && getScales().get("keasHighScale").isShowDraggable())
		{
			double x1 = altitudeDragX;
			double y1 = altitudeDragY;
			double x2 = keasHighDragX;
			double y2 = keasHighDragY;
			slope = (y2 - y1) / (x2 - x1);
			double x3 = 76 * mmPerPixel;
			double y3 = (slope * (x3 - x2)) + y2;
			
			double offsetY = mmPerPixel * getScales().get("machHigh").getScaleOffset().getY();
			
			if (y3 < offsetY + (mmPerPixel*getScales().get("machHigh").getMmStartOffset()) - bias)
			{
				return -1;
			}
			else if (y3 > offsetY + (getScales().get("machHigh").getMmHeight()*mmPerPixel) + bias)
			{
				return 1;
			}
		}
		
		return 0;
	}
	
	@Override
	public Object execute(Object... parameters) {
		
		double altitude = (double) parameters[0];
		double keasLow = (double) parameters[1];
		double keasHigh = (double) parameters[2];
		boolean useHigh = (boolean) parameters[3];
		
		if (useHigh)
		{
			double value = getHighMach(getScales().get("altitudeScale").getPointForSlideValue(altitude).getY(), getScales().get("keasHighScale").getPointForSlideValue(keasHigh).getY());
			String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
			GameState.getInstanceOf().getAircraftState().get(currentAircraftId).setMach(value);
			return value;
		}
		else 
		{
			double value = getLowMach(getScales().get("altitudeScale").getPointForSlideValue(altitude).getY(), getScales().get("keasLowScale").getPointForSlideValue(keasLow).getY());
			String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
			GameState.getInstanceOf().getAircraftState().get(currentAircraftId).setMach(value);
			return value;
		}
	}

	@Override
	protected void init() {
		
		getScales().put("altitudeScale", initAltitudeScale());
		getScales().put("keasLowScale", initKeasLowScale());
		getScales().put("keasHighScale", initKeasHighScale());
		getScales().put("machLow", initMachLowScale());
		getScales().put("machHigh", initMachHighScale());
		
		setMouseClickedHandler((MouseEvent event)->{
			
			if (wasDragged) 
			{
				wasDragged = false;
				return;
			}
			
			double x = event.getSceneX() / 2.0;
			double y = event.getSceneY() / 2.0;
			
			if (getScales().get("altitudeScale").containsClick(x, y))
			{
				getScales().get("altitudeScale").setShowDraggable(!getScales().get("altitudeScale").isShowDraggable());
			}
			else if (getScales().get("keasLowScale").containsClick(x, y))
			{
				getScales().get("keasHighScale").setShowDraggable(false);
				getScales().get("keasLowScale").setShowDraggable(!getScales().get("keasLowScale").isShowDraggable());
			}
			else if (getScales().get("keasHighScale").containsClick(x, y))
			{
				getScales().get("keasLowScale").setShowDraggable(false);
				getScales().get("keasHighScale").setShowDraggable(!getScales().get("keasHighScale").isShowDraggable());
			}
			else 
			{
				return;
			}
			
			getGraphicsContext2D().clearRect(-10, 0, getWidth(), getHeight());
			getGraphicsContext2D().setTransform(1, 0, 0, 1, 0, 0);
			getGraphicsContext2D().translate(4 * mmPerPixel, 0);
			draw(2.0);
			event.consume();
		});
			
		setMouseDraggedHandler((MouseEvent event)->{
			
			wasDragged = true;
			double sceneX = event.getX();
			double sceneY = event.getY();
		
			if ((!getScales().get("altitudeScale").isDragging()) && 
				getScales().get("altitudeScale").isDraggingDot(sceneX,  sceneY))
			{
				getScales().get("altitudeScale").setDragging(true);
				getScales().get("keasHighScale").setDragging(false);
				getScales().get("keasLowScale").setDragging(false);
			}
			else if ((!getScales().get("keasLowScale").isDragging()) && getScales().get("keasLowScale").isDraggingDot(sceneX, sceneY))
			{
				getScales().get("altitudeScale").setDragging(false);
				getScales().get("keasHighScale").setDragging(false);
				getScales().get("keasLowScale").setDragging(true);
			}
			else if ((!getScales().get("keasHighScale").isDragging()) && getScales().get("keasHighScale").isDraggingDot(sceneX, sceneY))
			{
				getScales().get("altitudeScale").setDragging(false);
				getScales().get("keasHighScale").setDragging(true);
				getScales().get("keasLowScale").setDragging(false);
			}
			
			if (getScales().get("altitudeScale").isShowDraggable() && getScales().get("altitudeScale").isDragging()) 
			{
				double offsetY = mmPerPixel * getScales().get("altitudeScale").getScaleOffset().getY();
				if ((sceneY/2.0) < offsetY + (mmPerPixel*getScales().get("altitudeScale").getMmStartOffset())) return;
				else if ((sceneY/2.0) > offsetY + (getScales().get("altitudeScale").getMmHeight()*mmPerPixel)) return;
				int clampResult = clampToscale(getScales().get("altitudeScale").getDraggableX(), sceneY/2.0, 
						getScales().get("keasLowScale").getDraggableX(), getScales().get("keasLowScale").getDraggableY(), 
						getScales().get("keasHighScale").getDraggableX(), getScales().get("keasHighScale").getDraggableY());
				if (clampResult == 0)
				{
					getScales().get("altitudeScale").setDraggableY(sceneY/2.0);
				}
				else
				{
					getScales().get("altitudeScale").setDraggableY(calcAltitudeYForMachY((clampResult == -1), getScales().get("keasLowScale").isShowDraggable()));
				}
				
				if (getScales().get("keasLowScale").isShowDraggable())
				{
					getScales().get("machLow").setValue(getLowMach(getScales().get("altitudeScale").getDraggableY(), getScales().get("keasLowScale").getDraggableY()));
					String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
					GameState.getInstanceOf().getAircraftState().get(currentAircraftId).setMach(getScales().get("machLow").getValue());
				}
				else if (getScales().get("keasHighScale").isShowDraggable())
				{
					getScales().get("machLow").setValue(getHighMach(getScales().get("altitudeScale").getDraggableY(), getScales().get("keasHighScale").getDraggableY()));
					String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
					GameState.getInstanceOf().getAircraftState().get(currentAircraftId).setMach(getScales().get("machLow").getValue());
				}
			}
			else if (getScales().get("keasLowScale").isShowDraggable() && getScales().get("keasLowScale").isDragging()) 
			{
				double offsetY = mmPerPixel * getScales().get("keasLowScale").getScaleOffset().getY();
				if ((sceneY/2.0) < offsetY + (mmPerPixel*getScales().get("keasLowScale").getMmStartOffset())) return;
				else if ((sceneY/2.0) > offsetY + (getScales().get("keasLowScale").getMmHeight()*mmPerPixel)) return;
				int clampResult = clampToscale(getScales().get("altitudeScale").getDraggableX(), getScales().get("altitudeScale").getDraggableY(), 
						getScales().get("keasLowScale").getDraggableX(), sceneY/2.0, 
						getScales().get("keasHighScale").getDraggableX(), getScales().get("keasHighScale").getDraggableY());
				if (clampResult == 0)
				{
					getScales().get("keasLowScale").setDraggableY(sceneY/2.0);
				}
				else
				{
					getScales().get("keasLowScale").setDraggableY(calcLowKeasYForMachY((clampResult == -1), true));
				}
				getScales().get("machLow").setValue(getLowMach(getScales().get("altitudeScale").getDraggableY(), getScales().get("keasLowScale").getDraggableY()));
				String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
				GameState.getInstanceOf().getAircraftState().get(currentAircraftId).setMach(getScales().get("machLow").getValue());
			}
			else if (getScales().get("keasHighScale").isShowDraggable() && getScales().get("keasHighScale").isDragging())
			{
				double offsetY = mmPerPixel * getScales().get("keasHighScale").getScaleOffset().getY();
				if ((sceneY/2.0) < offsetY + (mmPerPixel*getScales().get("keasHighScale").getMmStartOffset())) return;
				else if ((sceneY/2.0) > offsetY + (getScales().get("keasHighScale").getMmHeight()*mmPerPixel)) return;
				int clampResult = clampToscale(getScales().get("altitudeScale").getDraggableX(), getScales().get("altitudeScale").getDraggableY(), 
						getScales().get("keasLowScale").getDraggableX(), getScales().get("keasLowScale").getDraggableY(), 
						getScales().get("keasHighScale").getDraggableX(), sceneY/2.0);
				if (clampResult == 0)
				{
					getScales().get("keasHighScale").setDraggableY(sceneY/2.0);
				}
				else
				{
					getScales().get("keasHighScale").setDraggableY(calcLowKeasYForMachY((clampResult == -1), false));
				}
				getScales().get("machLow").setValue(getHighMach(getScales().get("altitudeScale").getDraggableY(), getScales().get("keasHighScale").getDraggableY()));
				String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
				GameState.getInstanceOf().getAircraftState().get(currentAircraftId).setMach(getScales().get("machLow").getValue());
			}
			else
			{
				return;
			}
			
			drawLines();
			event.consume();
		});
	}
	
	private double getLowMach(double altitudeY, double speedLowY)
	{
		double altX = getScales().get("altitudeScale").getScaleOffset().getX() * mmPerPixel;
		double altY = altitudeY;
		double speedLowX = getScales().get("keasLowScale").getScaleOffset().getX() * mmPerPixel;
		double slope = (speedLowY - altY) / (speedLowX - altX);
		double machLowX = getScales().get("machLow").getScaleOffset().getX() * mmPerPixel;
		double machLowinterceptY = speedLowY + (slope * (machLowX - speedLowX));
		
		return getScales().get("machLow").getDataPointForSlideValue(machLowinterceptY);
	}
	
	private double getHighMach(double altitudeY, double speedHighY)
	{
		double altX = getScales().get("altitudeScale").getScaleOffset().getX() * mmPerPixel;
		double altY = altitudeY;
		double speedHighX = getScales().get("keasHighScale").getScaleOffset().getX() * mmPerPixel;
		double slope = (speedHighY - altY) / (speedHighX - altX);
		double machHighX = getScales().get("machHigh").getScaleOffset().getX() * mmPerPixel;
		double machHighinterceptY = speedHighY + (slope * (machHighX - speedHighX));
		
		return getScales().get("machHigh").getDataPointForSlideValue(machHighinterceptY);
	}
	
	private VerticalScale initAltitudeScale()
	{
		LinkedList<Section> sections = new LinkedList<>();
		
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(31.5).numDivisions(13).startValue(320).endValue(200).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(20).numDivisions(9).startValue(200).endValue(120).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(2.25).numDivisions(2).startValue(120).endValue(110).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(4.5).numDivisions(3).startValue(110).endValue(90).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(4.5).numDivisions(3).startValue(90).endValue(70).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(4.25).numDivisions(3).startValue(70).endValue(50).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(7).fontAxisOffsetLast(6).mmHeight(10).numDivisions(6).startValue(50).endValue(0).drawLast(true).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(true).color(Color.BLACK).labelSide(LabelSide.LEFT).build();
		
		VerticalScale altitudeScale = VerticalScale.builder()
				.mmStartOffset(10)
				.mmHeight(87)
				.sections(sections) 
				.charactistics(characteristics)
				.scaleOffset(new Point2D(10, 10))
				.clickZone(new Rectangle2D(1 * mmPerPixel, 10 * mmPerPixel, 60, 212 * mmPerPixel))
				.build();
		
		altitudeScale.setLabel(ScaleLabel.builder()
				.drawValue(false)
				.label("Altitude")
				.labelColor(Color.rgb(255, 15, 0))
				.stepNum(" 2")
				.scaleLocation(new Point2D(6, -8))
				.stepNumLocation(new Point2D(2, -25))
				.build());
		
		altitudeScale.init();
		
		return altitudeScale;
	}
	
	private VerticalScale initKeasLowScale()
	{
		LinkedList<Section> sections = new LinkedList<>();
		
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(15.5).numDivisions(2).startValue(80).endValue(120).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(11).numDivisions(2).startValue(120).endValue(160).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(8.5).numDivisions(2).startValue(160).endValue(200).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(7).numDivisions(2).startValue(200).endValue(240).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(5.75).numDivisions(2).startValue(240).endValue(280).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(5).numDivisions(2).startValue(280).endValue(320).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(4.5).numDivisions(2).startValue(320).endValue(360).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(4).numDivisions(2).startValue(360).endValue(400).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(3.5).numDivisions(2).startValue(400).endValue(440).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(3.5).numDivisions(2).startValue(440).endValue(480).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(3).numDivisions(2).startValue(480).endValue(520).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(3).numDivisions(2).startValue(520).endValue(560).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(2.5).numDivisions(2).startValue(560).endValue(600).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(4.75).numDivisions(2).startValue(600).endValue(680).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(4.25).numDivisions(2).startValue(680).endValue(760).build());
		sections.add(Section.builder().fontAxisOffsetLast(7).drawLast(true).fontAxisOffset(7).mmHeight(2.75).numDivisions(2).startValue(760).endValue(800).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(false).color(Color.BLACK).labelSide(LabelSide.LEFT).build();
		
		VerticalScale keasLowScale = VerticalScale.builder()
				.mmStartOffset(0)
				.mmHeight(88.5)
				.sections(sections)
				.charactistics(characteristics)
				.scaleOffset(new Point2D(29, 10))
				.clickZone(new Rectangle2D(22 * mmPerPixel, 8 * mmPerPixel, 21, 212 * mmPerPixel))
				.build();
		
		keasLowScale.setLabel(ScaleLabel.builder()
				.label("KEAS")
				.labelColor(Color.rgb(255, 15, 0))
				.stepNum(" 2")
				.scaleLocation(new Point2D(26, -6))
				.stepNumLocation(new Point2D(0, -25))
				.build());
		
		keasLowScale.init();
		
		return keasLowScale;
	}
	
	private VerticalScale initKeasHighScale()
	{
		LinkedList<Section> sections = new LinkedList<>();
		
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(15.5).numDivisions(3).startValue(160).endValue(240).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(11).numDivisions(3).startValue(240).endValue(320).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(8.5).numDivisions(3).startValue(320).endValue(400).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(7).numDivisions(3).startValue(400).endValue(480).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(5.75).numDivisions(3).startValue(480).endValue(560).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(5).numDivisions(3).startValue(560).endValue(640).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(4.5).numDivisions(3).startValue(640).endValue(720).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(4).numDivisions(3).startValue(720).endValue(800).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(3.5).numDivisions(3).startValue(800).endValue(880).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(3.5).numDivisions(3).startValue(880).endValue(960).build());
		sections.add(Section.builder().fontAxisOffsetLast(2).drawLast(true).fontAxisOffset(2).mmHeight(1.5).numDivisions(2).startValue(960).endValue(1000).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(false).color(Color.PURPLE).labelSide(LabelSide.RIGHT).build();
		
		VerticalScale keasLowScale = VerticalScale.builder()
				.mmStartOffset(0)
				.mmHeight(69.5)
				.sections(sections)
				.charactistics(characteristics)
				.scaleOffset(new Point2D(29, 10))
				.clickZone(new Rectangle2D(30 * mmPerPixel, 8 * mmPerPixel, 21, 212 * mmPerPixel))
				.build();
		
		keasLowScale.init();
		
		return keasLowScale;
	}
	
	private VerticalScale initMachLowScale()
	{
		LinkedList<Section> sections = new LinkedList<>();
		
		sections.add(Section.builder().fontAxisOffset(6).mmHeight(10).numDivisions(2).startValue(.6).endValue(.65).build());
		sections.add(Section.builder().fontAxisOffset(6).mmHeight(9.25).numDivisions(2).startValue(.65).endValue(.7).build());
		sections.add(Section.builder().fontAxisOffset(6).mmHeight(8.75).numDivisions(2).startValue(.7).endValue(.75).build());
		sections.add(Section.builder().fontAxisOffset(6).mmHeight(8.25).numDivisions(2).startValue(.75).endValue(.8).build());
		sections.add(Section.builder().fontAxisOffset(6).mmHeight(7.75).numDivisions(2).startValue(.8).endValue(.85).build());
		sections.add(Section.builder().fontAxisOffset(6).mmHeight(7.25).numDivisions(2).startValue(.85).endValue(.9).build());
		sections.add(Section.builder().fontAxisOffset(6).mmHeight(6.75).numDivisions(2).startValue(.9).endValue(.95).build());
		sections.add(Section.builder().fontAxisOffset(6).mmHeight(6.5).numDivisions(2).startValue(.95).endValue(1.0).build());
		sections.add(Section.builder().fontAxisOffset(6).mmHeight(6.25).numDivisions(2).startValue(1.0).endValue(1.05).build());
		sections.add(Section.builder().fontAxisOffset(6).mmHeight(5.75).numDivisions(2).startValue(1.05).endValue(1.1).build());
		sections.add(Section.builder().fontAxisOffsetLast(6).drawLast(true).fontAxisOffset(6).mmHeight(5.5).numDivisions(2).startValue(1.10).endValue(1.15).build());
		
		LinkedList<ShadedRegion> shadedRegions = new LinkedList<>();
		shadedRegions.add(ShadedRegion.builder().color(Color.GREY).width(1.0).startValue(.60).endValue(.90).build());
		shadedRegions.add(ShadedRegion.builder().color(Color.YELLOW).width(1.0).startValue(.9).endValue(1.00).build());
		shadedRegions.add(ShadedRegion.builder().color(Color.ORANGERED).width(1.0).startValue(1.0).endValue(1.10).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(false).color(Color.BLACK).labelSide(LabelSide.LEFT).build();
		
		VerticalScale machLowScale = VerticalScale.builder()
				.mmStartOffset(0)
				.mmHeight(87)
				.sections(sections)
				.charactistics(characteristics)
				.scaleOffset(new Point2D(76, 10))
				.shadedRegions(shadedRegions)
				.clickZone(new Rectangle2D(69 * mmPerPixel, 1 * mmPerPixel, 21, 212 * mmPerPixel))
				.build();
		
		machLowScale.setLabel(ScaleLabel.builder()
				.label("Mach")
				.labelColor(Color.rgb(255, 15, 0))
				.stepNum(" 2")
				.drawValue(true)
				.scaleLocation(new Point2D(-30, -6))
				.stepNumLocation(new Point2D(-40, -25))
				.build());
		
		machLowScale.init();
		
		return machLowScale;
	}
	
	private VerticalScale initMachHighScale()
	{
		LinkedList<Section> sections = new LinkedList<>();
		
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(10).numDivisions(2).startValue(1.2).endValue(1.3).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(9.25).numDivisions(2).startValue(1.3).endValue(1.4).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(8.75).numDivisions(2).startValue(1.4).endValue(1.5).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(8.25).numDivisions(2).startValue(1.5).endValue(1.6).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(7.75).numDivisions(2).startValue(1.6).endValue(1.7).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(7.25).numDivisions(2).startValue(1.7).endValue(1.8).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(6.75).numDivisions(2).startValue(1.8).endValue(1.9).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(6.5).numDivisions(2).startValue(1.9).endValue(2.0).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(6.25).numDivisions(2).startValue(2.0).endValue(2.1).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(5.75).numDivisions(2).startValue(2.1).endValue(2.2).build());
		sections.add(Section.builder().fontAxisOffsetLast(3).drawLast(true).fontAxisOffset(3).mmHeight(5.5).numDivisions(2).startValue(2.2).endValue(2.3).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(false).color(Color.BLACK).labelSide(LabelSide.RIGHT).build();
		
		VerticalScale machHighScale = VerticalScale.builder()
				.mmStartOffset(0)
				.mmHeight(87)
				.sections(sections)
				.charactistics(characteristics)
				.scaleOffset(new Point2D(76, 10))
				.clickZone(new Rectangle2D(76 * mmPerPixel, 1 * mmPerPixel, 21, 212 * mmPerPixel))
				.build();
		
		machHighScale.init();
		
		return machHighScale;
	}
}
