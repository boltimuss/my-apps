package com.additolc.graphs;

import java.util.LinkedList;

import com.additolc.nomograph.Chart;
import com.additolc.nomograph.HorizontalScale;
import com.additolc.nomograph.LabelSide;
import com.additolc.nomograph.NomographCharacteristics;
import com.additolc.nomograph.ScaleLabel;
import com.additolc.nomograph.Section;
import com.additolc.nomograph.ShadedRegion;
import com.additolc.nomograph.SlantScale;
import com.additolc.state.GameState;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Step12Chart extends Chart {

	public Step12Chart(Dimension2D dimensions) {
		super(dimensions);
	}

	
	@Override
	public void drawLines() {
		
		getGraphicsContext2D().clearRect(-10, 0, getWidth(), getHeight());
		getGraphicsContext2D().setTransform(1, 0, 0, 1, 0, 0);
		getGraphicsContext2D().translate(4 * mmPerPixel, 0);
		draw(2.0);
		
		if (getScales().get("wingLoadScale").isShowDraggable() && getScales().get("engineOutputScale").isShowDraggable())
		{
			double x1 = getScales().get("wingLoadScale").getDraggableX();
			double y1 = getScales().get("wingLoadScale").getDraggableY();
			double x2 = getScales().get("engineOutputScale").getDraggableX();
			double y2 = getScales().get("engineOutputScale").getDraggableY();
			
			double slope = -((y2 - y1) / (x2 - x1));
			double xOffset = getScales().get("engineOutputScale").getScaleOffset().getX() * mmPerPixel;
			double b2 = (-slope*(x2-xOffset)); 
			
			Point2D intersectionPt = calculateIntersectionPoint(1.0, 0.0, slope, b2);
			
			double xInt = intersectionPt.getX() + xOffset;
			double yInt = y2 - intersectionPt.getY();
			
			String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
			double value = getScales().get("engineDeltaSpeedScale").getDataPointForSlideValue(yInt);
			getScales().get("engineDeltaSpeedScale").setValue(value);
			GameState.getInstanceOf().getAircraftState().get(currentAircraftId).setEngineDeltaSpeed(value);
			
			getGraphicsContext2D().setLineWidth(1);
			getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
			getScales().get("wingLoadScale").drawDraggableNotch(getGraphicsContext2D());
			getScales().get("engineOutputScale").drawDraggableNotch(getGraphicsContext2D());
			
			getGraphicsContext2D().setFill(Color.RED);
			getGraphicsContext2D().fillOval(xInt - 3, yInt - 3, 6, 6);
			
		}	
		else
		{
			return;
		}
		
	}

	@Override
	public Object execute(Object... parameters) {

		double wingload = (double) parameters[0];
		double engineOutput = (double) parameters[1];
		double x1 = getScales().get("wingLoadScale").getPointForSlideValue(wingload).getX();
		double y1 = getScales().get("wingLoadScale").getDraggableY();
		double x2 = getScales().get("engineOutputScale").getPointForSlideValue(engineOutput).getX();
		double y2 = getScales().get("engineOutputScale").getDraggableY();
		
		double slope = -((y2 - y1) / (x2 - x1));
		double xOffset = getScales().get("engineOutputScale").getScaleOffset().getX() * mmPerPixel;
		double b2 = (-slope*(x2-xOffset)); 
		
		Point2D intersectionPt = calculateIntersectionPoint(1.0, 0.0, slope, b2);
		
		double yInt = y2 - intersectionPt.getY();
		
		String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
		double value = getScales().get("engineDeltaSpeedScale").getDataPointForSlideValue(yInt);
		getScales().get("engineDeltaSpeedScale").setValue(value);
		GameState.getInstanceOf().getAircraftState().get(currentAircraftId).setEngineDeltaSpeed(value);
		
		return value;
	}

	@Override
	protected void init() {
		
		getScales().put("wingLoadScale", initWingLoadScale());
		getScales().put("engineOutputScale", initEngineOutputScale());
		getScales().put("engineDeltaSpeedScale", initEngineDeltaSpeedScale());
		
		setMouseClickedHandler((MouseEvent event)->{

			if (wasDragged) 
			{
				wasDragged = false;
				return;
			}
			
			double x = event.getX() / 2.0;
			double y = event.getY() / 2.0;
			
			if (getScales().get("wingLoadScale").containsClick(x, y))
			{
				getScales().get("wingLoadScale").setShowDraggable(!getScales().get("wingLoadScale").isShowDraggable());
			}
			else if (getScales().get("engineOutputScale").containsClick(x, y))
			{
				getScales().get("engineOutputScale").setShowDraggable(!getScales().get("engineOutputScale").isShowDraggable());
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
			
			if ((!getScales().get("wingLoadScale").isDragging()) && 
				getScales().get("wingLoadScale").isDraggingDot(x, y))
			{
				getScales().get("wingLoadScale").setDragging(true);
			}
			else if ((!getScales().get("engineOutputScale").isDragging()) && 
					getScales().get("engineOutputScale").isDraggingDot(x, y))
			{
				getScales().get("engineOutputScale").setDragging(true);
			}
			
			if (getScales().get("wingLoadScale").isShowDraggable() && getScales().get("wingLoadScale").isDragging()) 
			{
				double offsetX = mmPerPixel * getScales().get("wingLoadScale").getScaleOffset().getX();
				if ((x/2.0) < offsetX + (mmPerPixel*getScales().get("wingLoadScale").getMmStartOffset())) return;
				else if ((x/2.0) > offsetX + (getScales().get("wingLoadScale").getMmWidth()*mmPerPixel)) return;
				getScales().get("wingLoadScale").setDraggableX(x/2.0);
			}
			else if (getScales().get("engineOutputScale").isShowDraggable() && getScales().get("engineOutputScale").isDragging()) 
			{
				double offsetX = mmPerPixel * getScales().get("engineOutputScale").getScaleOffset().getX();
				if ((x/2.0) < offsetX) return;
				else if ((x/2.0) > offsetX + (getScales().get("engineOutputScale").getMmWidth()*mmPerPixel)) return;
				getScales().get("engineOutputScale").setDraggableX(x/2.0);
			}
			else
			{
				return;
			}
			
			drawLines();

		});
	}

	private SlantScale initEngineDeltaSpeedScale()
	{
		LinkedList<Section> sections = new LinkedList<>();
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(4.5).numDivisions(2).startValue(200).endValue(180).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(5).numDivisions(2).startValue(180).endValue(160).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(3).numDivisions(2).startValue(160).endValue(150).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(3.25).numDivisions(2).startValue(150).endValue(140).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(3.5).numDivisions(2).startValue(140).endValue(130).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(3.75).numDivisions(2).startValue(130).endValue(120).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(4.25).numDivisions(2).startValue(120).endValue(110).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(4.5).numDivisions(2).startValue(110).endValue(100).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(5).numDivisions(2).startValue(100).endValue(90).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(5.75).numDivisions(2).startValue(90).endValue(80).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(6.5).numDivisions(2).startValue(80).endValue(70).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(7.5).numDivisions(2).startValue(70).endValue(60).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(8.5).numDivisions(2).startValue(60).endValue(50).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(9.75).numDivisions(2).startValue(50).endValue(40).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(11.5).numDivisions(2).startValue(40).endValue(30).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(14).numDivisions(2).startValue(30).endValue(20).build());
		sections.add(Section.builder().fontAxisOffset(2).mmHeight(17).numDivisions(2).startValue(20).endValue(10).build());
		sections.add(Section.builder().fontAxisOffsetLast(2).drawLast(false).fontAxisOffset(2).mmHeight(21).numDivisions(2).startValue(10).endValue(0).color(Color.BLACK).build());

		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(true).color(Color.BLACK).labelSide(LabelSide.RIGHT).build();
		
		SlantScale engineDeltaSpeed = SlantScale.builder()
				.mmStartOffset(57)
				.mmHeight(194.75)
				.sections(sections)
				.charactistics(characteristics)
				.scaleOffset(new Point2D(148, 10))
				.draggableOffset(new Point2D(0,0))
				.build();
		
		engineDeltaSpeed.setLabel(ScaleLabel.builder()
				.label("Engine Delta Speed")
				.drawValue(true)
				.rotation(-135)
				.labelColor(Color.CORNFLOWERBLUE)
				.stepNum("12")
				.scaleLocation(new Point2D(-120, 180))
				.stepNumLocation(new Point2D(-138, 186))
				.build());
		
		engineDeltaSpeed.init();
	
		return engineDeltaSpeed;
	}
	
	private HorizontalScale initEngineOutputScale()
	{
		LinkedList<Section> sections = new LinkedList<>();
	
		sections.add(Section.builder().fontAxisOffsetLast(8).drawLast(false).fontAxisOffset(8).mmWidth(138).numDivisions(51).startValue(0).endValue(50).color(Color.BLACK).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(false).color(Color.BLACK).labelSide(LabelSide.LEFT).build();
		
		HorizontalScale engineOutputScale = HorizontalScale.builder()
				.mmStartOffset(0)
				.mmWidth(138)
				.sections(sections)
				.charactistics(characteristics)
				.scaleOffset(new Point2D(10, 148))
				.clickZone(new Rectangle2D(8 * mmPerPixel, 148 * mmPerPixel, 148 * mmPerPixel, 14 * mmPerPixel))
				.draggableOffset(new Point2D(0, 135))
				.build();
		
		engineOutputScale.setLabel(ScaleLabel.builder()
				.label("Engine Output")
				.labelColor(Color.CORNFLOWERBLUE)
				.stepNum("12")
				.scaleLocation(new Point2D(180, -10))
				.stepNumLocation(new Point2D(266, -22))
				.build());
		
		engineOutputScale.init();
	
		return engineOutputScale;
	}
	
	private HorizontalScale initWingLoadScale()
	{
		LinkedList<Section> sections = new LinkedList<>();
	
		sections.add(Section.builder().fontAxisOffset(8).mmWidth(5.75).numDivisions(2).startValue(208.5).endValue(200).build());
		sections.add(Section.builder().fontAxisOffsetLast(8).drawLast(true).fontAxisOffset(8).mmWidth(132.25).numDivisions(21).startValue(200).endValue(0).color(Color.BLACK).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(true).color(Color.BLACK).labelSide(LabelSide.LEFT).build();
		
		HorizontalScale wingLoadScale = HorizontalScale.builder()
				.mmStartOffset(0)
				.mmWidth(138)
				.sections(sections)
				.charactistics(characteristics)
				.scaleOffset(new Point2D(10, 10))
				.clickZone(new Rectangle2D(8 * mmPerPixel, 8 * mmPerPixel, 148 * mmPerPixel, 14 * mmPerPixel))
				.draggableOffset(new Point2D(0,0))
				.build();
		
		wingLoadScale.setLabel(ScaleLabel.builder()
				.label("Wing-Load")
				.labelColor(Color.CORNFLOWERBLUE)
				.stepNum("12")
				.scaleLocation(new Point2D(380, -10))
				.stepNumLocation(new Point2D(360, -22))
				.build());
		
		wingLoadScale.init();
	
		return wingLoadScale;
	}
}
