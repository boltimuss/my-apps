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

public class Step1Chart extends Chart {
	
	public Step1Chart(Dimension2D dimensions)
	{
		super(dimensions);
	}
	
	@Override
	public void drawLines()
	{
		getGraphicsContext2D().clearRect(-10, 0, getWidth(), getHeight());
		getGraphicsContext2D().setTransform(1, 0, 0, 1, 0, 0);
		getGraphicsContext2D().translate(4 * mmPerPixel, 0);
		getGraphicsContext2D().scale(2.0, 2.0);
		
		if (getScales().get("altitudeScale").isShowDraggable() && getScales().get("speedLow").isShowDraggable())
		{
			double x1 = getScales().get("altitudeScale").getDraggableX();
			double y1 = getScales().get("altitudeScale").getDraggableY();
			double x2 = getScales().get("speedLow").getDraggableX();
			double y2 = getScales().get("speedLow").getDraggableY();
			
			getGraphicsContext2D().setLineWidth(1);
			getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
		}
		else if (getScales().get("altitudeScale").isShowDraggable() && getScales().get("speedHigh").isShowDraggable())
		{
			double x1 = getScales().get("altitudeScale").getDraggableX();
			double y1 = getScales().get("altitudeScale").getDraggableY();
			double x2 = getScales().get("speedHigh").getDraggableX();
			double y2 = getScales().get("speedHigh").getDraggableY();
			
			getGraphicsContext2D().setLineWidth(1);
			getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
		}
		
		draw(1.0);
	}
	
	private double getKeasLow(double altitudeY, double speedLowY)
	{
		double altX = getScales().get("altitudeScale").getScaleOffset().getX() * mmPerPixel;
		double altY = altitudeY;
		double speedX = getScales().get("speedLow").getScaleOffset().getX() * mmPerPixel;
		double speedY = speedLowY;
		double slope = (speedY - altY) / (speedX - altX);
		double keasLowinterceptX = 47 * mmPerPixel;
		double keasLowinterceptY = altY + (slope * (keasLowinterceptX - altX));
		
		return getScales().get("keasLowScale").getDataPointForSlideValue(keasLowinterceptY);
	}
	
	private double getKeasHigh(double altitudeY, double speedHighY)
	{
		double altX = getScales().get("altitudeScale").getScaleOffset().getX() * mmPerPixel;
		double altY = altitudeY;
		double speedX = getScales().get("speedHigh").getScaleOffset().getX() * mmPerPixel;
		double speedY = speedHighY;
		double slope = (speedY - altY) / (speedX - altX);
		double keasHighinterceptX = 43 * mmPerPixel;
		double keashighinterceptY = altY + (slope * (keasHighinterceptX - altX));
		
		return getScales().get("keasHighScale").getDataPointForSlideValue(keashighinterceptY);
	}
	
	@Override
	public Object execute(Object... parameters)
	{
		double altitude = (double) parameters[0];
		double speedHighValue = (double) parameters[1];
		double speedLowValue = (double) parameters[2];
		boolean useHigh = (boolean) parameters[3];
		
		if (useHigh)
		{
			double value = getKeasHigh(getScales().get("altitudeScale").getPointForSlideValue(altitude).getY(), getScales().get("speedHigh").getPointForSlideValue(speedHighValue).getY());
			String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
			GameState.getInstanceOf().getAircraftState().get(currentAircraftId).setKeas(value);
			return value;
		}
		else 
		{
			double value = getKeasLow(getScales().get("altitudeScale").getPointForSlideValue(altitude).getY(), getScales().get("speedLow").getPointForSlideValue(speedLowValue).getY());
			String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
			GameState.getInstanceOf().getAircraftState().get(currentAircraftId).setKeas(value);
			return value;
		}
	}
	
	private VerticalScale initAltitudeScale()
	{
		LinkedList<Section> sections = new LinkedList<>();
		
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(10).numDivisions(4).startValue(320).endValue(290).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(3.25).numDivisions(2).startValue(290).endValue(280).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(9.5).numDivisions(4).startValue(280).endValue(250).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(3.5).numDivisions(2).startValue(250).endValue(240).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(10).numDivisions(4).startValue(240).endValue(210).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(3.5).numDivisions(2).startValue(210).endValue(200).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(9).numDivisions(4).startValue(200).endValue(170).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(2.75).numDivisions(2).startValue(170).endValue(160).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(10).numDivisions(5).startValue(160).endValue(120).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(2.5).numDivisions(2).startValue(120).endValue(110).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(2.5).numDivisions(2).startValue(110).endValue(100).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(2.25).numDivisions(2).startValue(100).endValue(90).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(4.5).numDivisions(3).startValue(90).endValue(70).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(2.25).numDivisions(2).startValue(70).endValue(60).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(4.5).numDivisions(3).startValue(60).endValue(40).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(2.0).numDivisions(2).startValue(40).endValue(30).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(2).numDivisions(2).startValue(30).endValue(20).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(2).numDivisions(2).startValue(20).endValue(10).drawLast(false).build());
		sections.add(Section.builder().fontAxisOffset(7).fontAxisOffsetLast(6).mmHeight(2).numDivisions(2).startValue(10).endValue(0).drawLast(true).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(true).color(Color.BLACK).labelSide(LabelSide.LEFT).build();
		
		VerticalScale altitudeScale = VerticalScale.builder()
				.mmStartOffset(7)
				.mmHeight(95)
				.sections(sections) 
				.charactistics(characteristics)
				.scaleOffset(new Point2D(10, 10))
				.draggableOffset(new Point2D(0,0))
				.clickZone(new Rectangle2D(1 * mmPerPixel, 10 * mmPerPixel, 60, 212 * mmPerPixel))
				.build();
		
		altitudeScale.setLabel(ScaleLabel.builder()
				.drawValue(false)
				.label("Altitude")
				.labelColor(Color.BROWN)
				.stepNum(" 1")
				.scaleLocation(new Point2D(6, -8))
				.stepNumLocation(new Point2D(2, -25))
				.build());
		
		altitudeScale.init();
		
		return altitudeScale;
	}
	
	private VerticalScale initKeasHighScale()
	{
		LinkedList<Section> sections = new LinkedList<>();
		
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(10).numDivisions(2).startValue(60).endValue(80).build());
		sections.add(Section.builder().fontAxisOffset(7).mmHeight(13.5).numDivisions(2).startValue(80).endValue(120).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(9.5).numDivisions(2).startValue(120).endValue(160).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(7.25).numDivisions(2).startValue(160).endValue(200).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(6).numDivisions(2).startValue(200).endValue(240).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(5.25).numDivisions(2).startValue(240).endValue(280).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(4.5).numDivisions(2).startValue(280).endValue(320).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(4).numDivisions(2).startValue(320).endValue(360).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(3.5).numDivisions(2).startValue(360).endValue(400).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(3.25).numDivisions(2).startValue(400).endValue(440).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(3).numDivisions(2).startValue(440).endValue(480).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(2.5).numDivisions(2).startValue(480).endValue(520).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(2.5).numDivisions(2).startValue(520).endValue(560).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(2.25).numDivisions(2).startValue(560).endValue(600).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(4.25).numDivisions(2).startValue(600).endValue(680).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(3.75).numDivisions(2).startValue(680).endValue(760).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(4.25).numDivisions(2).startValue(760).endValue(840).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(3.5).numDivisions(2).startValue(840).endValue(920).color(Color.ORANGE).build());
		sections.add(Section.builder().fontAxisOffsetLast(9).drawLast(true).fontAxisOffset(8).mmHeight(2.75).numDivisions(2).startValue(920).endValue(1000).color(Color.ORANGE).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(false).color(Color.PURPLE).labelSide(LabelSide.LEFT).build();
		
		LinkedList<ShadedRegion> shadedRegions = new LinkedList<>();
		shadedRegions.add(ShadedRegion.builder().color(Color.RED).width(1.0).startValue(800).endValue(1000).build());
		
		VerticalScale keasHighScale = VerticalScale.builder()
				.mmStartOffset(0)
				.mmHeight(95.5)
				.sections(sections)
				.charactistics(characteristics)
				.draggableOffset(new Point2D(0,0))
				.scaleOffset(new Point2D(43, 10))
				.shadedRegions(shadedRegions)
				.build();
		
		keasHighScale.setLabel(ScaleLabel.builder()
				.drawValue(true)
				.label("KEAS")
				.labelColor(Color.BROWN)
				.stepNum("1")
				.scaleLocation(new Point2D(13, -6))
				.stepNumLocation(new Point2D(0, -25))
				.build());
		
		keasHighScale.init();
		
		return keasHighScale;
	}
	
	private VerticalScale initKeasLowScale()
	{
		LinkedList<Section> sections = new LinkedList<>();
		
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(20.5).numDivisions(2).startValue(20).endValue(40).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(12).numDivisions(2).startValue(40).endValue(60).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(8.5).numDivisions(2).startValue(60).endValue(80).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(6.5).numDivisions(2).startValue(80).endValue(100).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(5.5).numDivisions(2).startValue(100).endValue(120).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(4.5).numDivisions(2).startValue(120).endValue(140).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(4).numDivisions(2).startValue(140).endValue(160).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(3.5).numDivisions(2).startValue(160).endValue(180).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(3.5).numDivisions(2).startValue(180).endValue(200).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(3).numDivisions(2).startValue(200).endValue(220).build());
		sections.add(Section.builder().drawLast(true).fontAxisOffsetLast(3).fontAxisOffset(3).mmHeight(3.5).numDivisions(2).startValue(220).endValue(240).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(false).color(Color.BLACK).labelSide(LabelSide.RIGHT).build();
		
		VerticalScale keasLowScale = VerticalScale.builder()
				.mmStartOffset(0)
				.mmHeight(75)
				.sections(sections)
				.charactistics(characteristics)
				.scaleOffset(new Point2D(47, 30.5))
				.draggableOffset(new Point2D(0,0))
				.build();
		
		keasLowScale.setLabel(ScaleLabel.builder()
				.drawValue(true)
				.label("KEAS")
				.labelColor(Color.BROWN)
				.stepNum("1")
				.scaleLocation(new Point2D(13, -6))
				.scaleOffset(new Point2D(14, 0))
				.stepNumLocation(new Point2D(0, -25))
				.build());
		
		keasLowScale.init();
		
		return keasLowScale;
	}
	
	private VerticalScale initSpeedHigh()
	{
		LinkedList<Section> sections = new LinkedList<>();
		
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(10).numDivisions(2).startValue(240).endValue(280).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(9).numDivisions(2).startValue(280).endValue(320).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(7.75).numDivisions(2).startValue(320).endValue(360).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(7).numDivisions(2).startValue(360).endValue(400).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(6.5).numDivisions(2).startValue(400).endValue(440).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(5.75).numDivisions(2).startValue(440).endValue(480).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(5.5).numDivisions(2).startValue(480).endValue(520).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(5).numDivisions(2).startValue(520).endValue(560).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(4.5).numDivisions(2).startValue(560).endValue(600).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(4.5).numDivisions(2).startValue(600).endValue(640).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(4).numDivisions(2).startValue(640).endValue(680).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(3.75).numDivisions(2).startValue(680).endValue(720).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(3.75).numDivisions(2).startValue(720).endValue(760).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(3.5).numDivisions(2).startValue(760).endValue(800).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(3.35).numDivisions(2).startValue(800).endValue(840).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(3).numDivisions(2).startValue(840).endValue(880).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(3).numDivisions(2).startValue(880).endValue(920).build());
		sections.add(Section.builder().fontAxisOffset(8).mmHeight(2.75).numDivisions(2).startValue(920).endValue(960).build());
		sections.add(Section.builder().drawLast(true).fontAxisOffsetLast(8).fontAxisOffset(8).mmHeight(2.5).numDivisions(2).startValue(960).endValue(1000).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(false).color(Color.PURPLE).labelSide(LabelSide.LEFT).build();
		
		VerticalScale speedHigh = VerticalScale.builder()
				.mmStartOffset(0)
				.mmHeight(95)
				.sections(sections)
				.charactistics(characteristics)
				.scaleOffset(new Point2D(76, 10))
				.draggableOffset(new Point2D(0,0))
				.clickZone(new Rectangle2D(138 * mmPerPixel, 10 * mmPerPixel, 50, 212 * mmPerPixel))
				.build();
		
		speedHigh.init();
		
		return speedHigh;
	}
	
	private VerticalScale initSpeedLow()
	{
		LinkedList<Section> sections = new LinkedList<>();
		
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(21.5).numDivisions(2).startValue(40).endValue(60).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(15.25).numDivisions(2).startValue(60).endValue(80).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(12).numDivisions(2).startValue(80).endValue(100).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(9.5).numDivisions(2).startValue(100).endValue(120).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(8.25).numDivisions(2).startValue(120).endValue(140).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(7).numDivisions(2).startValue(140).endValue(160).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(6.25).numDivisions(2).startValue(160).endValue(180).build());
		sections.add(Section.builder().fontAxisOffset(3).mmHeight(5.75).numDivisions(2).startValue(180).endValue(200).build());
		sections.add(Section.builder().drawLast(true).fontAxisOffsetLast(3).fontAxisOffset(3).mmHeight(9.65).numDivisions(3).startValue(200).endValue(240).build());
		 	
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(false).color(Color.BLACK).labelSide(LabelSide.RIGHT).build();
		
		VerticalScale speedLow = VerticalScale.builder()
				.mmStartOffset(0)
				.mmHeight(95)
				.sections(sections)
				.charactistics(characteristics)
				.scaleOffset(new Point2D(76, 10))
				.draggableOffset(new Point2D(0,0))
				.clickZone(new Rectangle2D(160 * mmPerPixel, 10 * mmPerPixel, 50, 212 * mmPerPixel))
				.build();
		
		speedLow.setLabel(ScaleLabel.builder()
				.drawValue(false)
				.label("Speed")
				.labelColor(Color.BROWN)
				.stepNum(" 1")
				.scaleLocation(new Point2D(-40, -30))
				.stepNumLocation(new Point2D(-28, -23))
				.build());
		
		speedLow.init();
		
		return speedLow;
	}


	@Override
	protected void init() {
		
		getScales().put("altitudeScale", initAltitudeScale());
		getScales().put("keasHighScale", initKeasHighScale());
		getScales().put("keasLowScale", initKeasLowScale());
		getScales().put("speedHigh", initSpeedHigh());
		getScales().put("speedLow", initSpeedLow());
		
		setMouseClickedHandler((MouseEvent event)->{
			
			if (wasDragged) 
			{
				wasDragged = false;
				return;
			}
			
			double x = event.getX();
			double y = event.getY();
			
			if (getScales().get("altitudeScale").containsClick(x, y))
			{
				getScales().get("altitudeScale").setShowDraggable(!getScales().get("altitudeScale").isShowDraggable());
			}
			else if (getScales().get("speedLow").containsClick(x, y))
			{
				getScales().get("speedHigh").setShowDraggable(false);
				getScales().get("speedLow").setShowDraggable(!getScales().get("speedLow").isShowDraggable());
			}
			else if (getScales().get("speedHigh").containsClick(x, y))
			{
				getScales().get("speedLow").setShowDraggable(false);
				getScales().get("speedHigh").setShowDraggable(!getScales().get("speedHigh").isShowDraggable());
			}
			else 
			{
				return;
			}
			
			getGraphicsContext2D().clearRect(-10, 0, getWidth(), getHeight());
			getGraphicsContext2D().setTransform(1, 0, 0, 1, 0, 0);
			getGraphicsContext2D().translate(4 * mmPerPixel, 0);
			draw(2.0);
			drawLines();
		});
			
		setMouseDraggedHandler((MouseEvent event)->{
			
			wasDragged = true;
			double x = event.getX();
			double y = event.getY();
			
			if ((!getScales().get("altitudeScale").isDragging()) && 
				getScales().get("altitudeScale").isDraggingDot(x, y))
			{
				getScales().get("altitudeScale").setDragging(true);
				getScales().get("speedHigh").setDragging(false);
				getScales().get("speedLow").setDragging(false);
			}
			else if ((!getScales().get("speedLow").isDragging()) && getScales().get("speedLow").isDraggingDot(x, y))
			{
				getScales().get("altitudeScale").setDragging(false);
				getScales().get("speedHigh").setDragging(false);
				getScales().get("speedLow").setDragging(true);
			}
			else if ((!getScales().get("speedHigh").isDragging()) && getScales().get("speedHigh").isDraggingDot(x, y))
			{
				getScales().get("altitudeScale").setDragging(false);
				getScales().get("speedHigh").setDragging(true);
				getScales().get("speedLow").setDragging(false);
			}
			
			if (getScales().get("altitudeScale").isShowDraggable() && getScales().get("altitudeScale").isDragging()) 
			{
				double offsetY = mmPerPixel * getScales().get("altitudeScale").getScaleOffset().getY();
				if ((y/2.0) < offsetY + (mmPerPixel*getScales().get("altitudeScale").getMmStartOffset())) return;
				else if ((y/2.0) > offsetY + (getScales().get("altitudeScale").getMmHeight()*mmPerPixel)) return;
				getScales().get("altitudeScale").setDraggableY(y/2.0);
				
				if (getScales().get("speedLow").isShowDraggable())
				{
					getScales().get("keasLowScale").setValue(getKeasLow(getScales().get("altitudeScale").getDraggableY(), getScales().get("speedLow").getDraggableY()));
					getScales().get("keasHighScale").setValue(0.0);
					String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
					GameState.getInstanceOf().getAircraftState().get(currentAircraftId).setKeas(getScales().get("keasLowScale").getValue());
				}
				else if (getScales().get("speedHigh").isShowDraggable())
				{
					getScales().get("keasLowScale").setValue(0.0);
					getScales().get("keasHighScale").setValue(getKeasHigh(getScales().get("altitudeScale").getDraggableY(), getScales().get("speedHigh").getDraggableY()));
					String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
					GameState.getInstanceOf().getAircraftState().get(currentAircraftId).setKeas(getScales().get("keasHighScale").getValue());
				}
			}
			else if (getScales().get("speedLow").isShowDraggable() && getScales().get("speedLow").isDragging()) 
			{
				getScales().get("keasHighScale").setValue(0.0);
				double offsetY = mmPerPixel * getScales().get("speedLow").getScaleOffset().getY();
				if ((y/2.0) < offsetY + (mmPerPixel*getScales().get("speedLow").getMmStartOffset())) return;
				else if ((y/2.0) > offsetY + (getScales().get("speedLow").getMmHeight()*mmPerPixel)) return;
				getScales().get("speedLow").setDraggableY(y/2.0);
				getScales().get("keasLowScale").setValue(getKeasLow(getScales().get("altitudeScale").getDraggableY(), getScales().get("speedLow").getDraggableY()));
				String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
				GameState.getInstanceOf().getAircraftState().get(currentAircraftId).setKeas(getScales().get("keasLowScale").getValue());
			}
			else if (getScales().get("speedHigh").isShowDraggable() && getScales().get("speedHigh").isDragging())
			{
				getScales().get("keasLowScale").setValue(0.0);
				double offsetY = mmPerPixel * getScales().get("speedHigh").getScaleOffset().getY();
				if ((y/2.0) < offsetY + (mmPerPixel*getScales().get("speedHigh").getMmStartOffset())) return;
				else if ((y/2.0) > offsetY + (getScales().get("speedHigh").getMmHeight()*mmPerPixel)) return;
				getScales().get("speedHigh").setDraggableY(y/2.0);
				getScales().get("keasHighScale").setValue(getKeasHigh(getScales().get("altitudeScale").getDraggableY(), getScales().get("speedHigh").getDraggableY()));
				String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
				GameState.getInstanceOf().getAircraftState().get(currentAircraftId).setKeas(getScales().get("keasHighScale").getValue());
			}
			else
			{
				return;
			}
			
			drawLines();

		});
	}
}
