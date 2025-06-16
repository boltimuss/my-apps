package com.additolc.graphs;

import java.util.LinkedList;

import com.additolc.nomograph.Chart;
import com.additolc.nomograph.HorizontalScale;
import com.additolc.nomograph.LabelSide;
import com.additolc.nomograph.NomographCharacteristics;
import com.additolc.nomograph.VerticalScale;
import com.additolc.state.GameState;
import com.additolc.nomograph.ScaleLabel;
import com.additolc.nomograph.Section;
import com.additolc.nomograph.ShadedRegion;
import com.additolc.nomograph.SlantScale;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Step3Chart extends Chart {

	public Step3Chart(Dimension2D dimensions)
	{
		super(dimensions);
	}

	@Override
	public void drawLines() {
		
		getGraphicsContext2D().clearRect(-10, 0, getWidth(), getHeight());
		getGraphicsContext2D().setTransform(1, 0, 0, 1, 0, 0);
		getGraphicsContext2D().translate(4 * mmPerPixel, 0);
		draw(2.0);
		
		if (getScales().get("wingLoadScale").isShowDraggable() && getScales().get("keasLowScale").isShowDraggable())
		{
			double x1 = getScales().get("wingLoadScale").getDraggableX();
			double y1 = getScales().get("wingLoadScale").getDraggableY();
			double x2 = getScales().get("keasLowScale").getDraggableX();
			double y2 = getScales().get("keasLowScale").getDraggableY();
			
			double slope = -((y2 - y1) / (x2 - x1));
			double xOffset = getScales().get("keasLowScale").getScaleOffset().getX() * mmPerPixel;
			double b2 = (-slope*(x2-xOffset)); 
			
			Point2D intersectionPt = calculateIntersectionPoint(1.0, 0.0, slope, b2);
			
			double xInt = intersectionPt.getX() + xOffset;
			double yInt = y2 - intersectionPt.getY();
			
			String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
			GameState.getInstanceOf().getAircraftState().get(currentAircraftId).setQPoint(new Point2D(xInt, yInt));
			
			getGraphicsContext2D().setLineWidth(1);
			getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
			getScales().get("wingLoadScale").drawDraggableNotch(getGraphicsContext2D());
			getScales().get("keasLowScale").drawDraggableNotch(getGraphicsContext2D());
			
			getGraphicsContext2D().setFill(Color.RED);
			getGraphicsContext2D().fillOval(xInt - 3, yInt - 3, 6, 6);
			
		}
		else if (getScales().get("wingLoadScale").isShowDraggable() && getScales().get("keasHighScale").isShowDraggable())
		{
			double x1 = getScales().get("wingLoadScale").getDraggableX();
			double y1 = getScales().get("wingLoadScale").getDraggableY();
			double x2 = getScales().get("keasHighScale").getDraggableX();
			double y2 = getScales().get("keasHighScale").getDraggableY();
			
			double slope = -((y2 - y1) / (x2 - x1));
			double xOffset = getScales().get("keasHighScale").getScaleOffset().getX() * mmPerPixel;
			double b2 = (-slope*(x2-xOffset)); 
			
			Point2D intersectionPt = calculateIntersectionPoint(1.0, 0.0, slope, b2);
			
			double xInt = intersectionPt.getX() + xOffset;
			double yInt = y2 - intersectionPt.getY();
			
			String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
			GameState.getInstanceOf().getAircraftState().get(currentAircraftId).setQPoint(new Point2D(xInt, yInt));
			
			getGraphicsContext2D().setLineWidth(1);
			getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
			getScales().get("wingLoadScale").drawDraggableNotch(getGraphicsContext2D());
			getScales().get("keasHighScale").drawDraggableNotch(getGraphicsContext2D());
			
			getGraphicsContext2D().setFill(Color.RED);
			getGraphicsContext2D().fillOval(xInt - 3, yInt - 3, 6, 6);
			
		}
		else
		{
			return;
		}
		
	}

	@Override
	public Point2D execute(Object... parameters) {
		
		double wingload = (double) parameters[0];
		double keasLow = (double) parameters[1];
		double keasHigh = (double) parameters[2];
		boolean useHigh = (boolean) parameters[3];
		
		double x1 = getScales().get("wingLoadScale").getPointForSlideValue(wingload).getX();
		double y1 = getScales().get("wingLoadScale").getDraggableY();
		double x2 = (useHigh) ? getScales().get("keasHighScale").getPointForSlideValue(keasHigh).getX() : getScales().get("keasLowScale").getPointForSlideValue(keasLow).getX();
		double y2 = (useHigh) ? getScales().get("keasHighScale").getDraggableY() : getScales().get("keasLowScale").getDraggableY();
		
		double slope = -((y2 - y1) / (x2 - x1));
		double xOffset = getScales().get("keasLowScale").getScaleOffset().getX() * mmPerPixel;
		double b2 = (-slope*(x2-xOffset)); 
		
		Point2D intersectionPt = calculateIntersectionPoint(1.0, 0.0, slope, b2);
		
		double xInt = intersectionPt.getX() + xOffset;
		double yInt = y2 - intersectionPt.getY(); 
	    
		String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
		GameState.getInstanceOf().getAircraftState().get(currentAircraftId).setQPoint(new Point2D(xInt, yInt));
		
		return new Point2D(xInt, yInt);
	}

	@Override
	protected void init() {

		getScales().put("wingLoadScale", initWingLoadScale());
		getScales().put("qScale", initQScale());
		getScales().put("keasLowScale", initKeasLowScale());
		getScales().put("keasHighScale", initKeasHighScale());
		
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
			else if (getScales().get("keasLowScale").containsClick(x, y))
			{
				getScales().get("keasLowScale").setShowDraggable(!getScales().get("keasLowScale").isShowDraggable());
				getScales().get("keasHighScale").setShowDraggable(!getScales().get("keasLowScale").isShowDraggable());
			}
			else if (getScales().get("keasHighScale").containsClick(x, y))
			{
				getScales().get("keasHighScale").setShowDraggable(!getScales().get("keasHighScale").isShowDraggable());
				getScales().get("keasLowScale").setShowDraggable(!getScales().get("keasHighScale").isShowDraggable());
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
				getScales().get("keasLowScale").setDragging(false);
				getScales().get("keasHighScale").setDragging(false);
			}
			else if ((!getScales().get("keasLowScale").isDragging()) && 
					getScales().get("keasLowScale").isDraggingDot(x, y))
			{
				getScales().get("wingLoadScale").setDragging(false);
				getScales().get("keasLowScale").setDragging(true);
				getScales().get("keasHighScale").setDragging(false);
			}
			else if ((!getScales().get("keasHighScale").isDragging()) && 
					getScales().get("keasHighScale").isDraggingDot(x, y))
			{
				getScales().get("wingLoadScale").setDragging(false);
				getScales().get("keasLowScale").setDragging(false);
				getScales().get("keasHighScale").setDragging(true);
			}
			
			if (getScales().get("wingLoadScale").isShowDraggable() && getScales().get("wingLoadScale").isDragging()) 
			{
				double offsetX = mmPerPixel * getScales().get("wingLoadScale").getScaleOffset().getX();
				if ((x/2.0) < offsetX + (mmPerPixel*getScales().get("wingLoadScale").getMmStartOffset())) return;
				else if ((x/2.0) > offsetX + (getScales().get("wingLoadScale").getMmWidth()*mmPerPixel)) return;
				getScales().get("wingLoadScale").setDraggableX(x/2.0);
			}
			else if (getScales().get("keasLowScale").isShowDraggable() && getScales().get("keasLowScale").isDragging()) 
			{
				double offsetX = mmPerPixel * getScales().get("keasLowScale").getScaleOffset().getX();
				if ((x/2.0) < offsetX) return;
				else if ((x/2.0) > offsetX + (getScales().get("keasLowScale").getMmWidth()*mmPerPixel)) return;
				getScales().get("keasLowScale").setDraggableX(x/2.0);
			}
			else if (getScales().get("keasHighScale").isShowDraggable() && getScales().get("keasHighScale").isDragging()) 
			{
				double offsetX = mmPerPixel * getScales().get("keasHighScale").getScaleOffset().getX();
				if ((x/2.0) < offsetX) return;
				else if ((x/2.0) > offsetX + (getScales().get("keasHighScale").getMmWidth()*mmPerPixel)) return;
				getScales().get("keasHighScale").setDraggableX(x/2.0);
			}
			else
			{
				return;
			}
			
			drawLines();

		});
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
				.labelColor(Color.SANDYBROWN)
				.stepNum(" 3")
				.scaleLocation(new Point2D(380, -10))
				.stepNumLocation(new Point2D(360, -22))
				.build());
		
		wingLoadScale.init();
	
		return wingLoadScale;
	}
	
	private SlantScale initQScale()
	{
		LinkedList<Section> sections = new LinkedList<>();
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(true).color(Color.BLACK).labelSide(LabelSide.LEFT).build();
		
		LinkedList<ShadedRegion> shadedRegions = new LinkedList<>();
		shadedRegions.add(ShadedRegion.builder().color(Color.YELLOW).width(1.0).yMMStart(137).useYValue(true).yMMEnd(166.5).build());
		shadedRegions.add(ShadedRegion.builder().color(Color.ORANGE).width(1.0).yMMStart(166.5).useYValue(true).yMMEnd(179).build());
		shadedRegions.add(ShadedRegion.builder().color(Color.RED).width(1.0).yMMStart(179).useYValue(true).yMMEnd(194.75).build());
		
		SlantScale qScale = SlantScale.builder()
				.mmStartOffset(0)
				.mmHeight(194.75)
				.sections(sections)
				.charactistics(characteristics)
				.scaleOffset(new Point2D(148, 10))
				.draggableOffset(new Point2D(0,0))
				.shadedRegions(shadedRegions)
				.build();
		
		qScale.setLabel(ScaleLabel.builder()
				.label("Q-Mark")
				.rotation(-90)
				.labelColor(Color.SANDYBROWN)
				.stepNum(" 3")
				.scaleLocation(new Point2D(-196, 230))
				.stepNumLocation(new Point2D(-216, 218))
				.build());
		
		qScale.init();
	
		return qScale;
	}
	
	private HorizontalScale initKeasLowScale()
	{
		LinkedList<Section> sections = new LinkedList<>();
		
		sections.add(Section.builder().fontAxisOffset(8).mmWidth(2.5).numDivisions(2).startValue(0).endValue(80).build());
		sections.add(Section.builder().fontAxisOffset(8).mmWidth(3).numDivisions(2).startValue(80).endValue(120).build());
		sections.add(Section.builder().fontAxisOffset(8).mmWidth(4.25).numDivisions(2).startValue(120).endValue(160).build());
		sections.add(Section.builder().fontAxisOffset(8).mmWidth(5.5).numDivisions(2).startValue(160).endValue(200).build());
		sections.add(Section.builder().fontAxisOffset(8).mmWidth(6.5).numDivisions(2).startValue(200).endValue(240).build());
		sections.add(Section.builder().fontAxisOffset(8).mmWidth(7.75).numDivisions(2).startValue(240).endValue(280).build());
		sections.add(Section.builder().fontAxisOffset(8).mmWidth(9).numDivisions(2).startValue(280).endValue(320).build());
		sections.add(Section.builder().fontAxisOffset(8).mmWidth(10).numDivisions(2).startValue(320).endValue(360).build());
		sections.add(Section.builder().fontAxisOffset(8).mmWidth(11.5).numDivisions(2).startValue(360).endValue(400).build());
		sections.add(Section.builder().fontAxisOffset(8).mmWidth(12.75).numDivisions(2).startValue(400).endValue(440).build());
		sections.add(Section.builder().fontAxisOffset(8).mmWidth(13.5).numDivisions(2).startValue(440).endValue(480).build());
		sections.add(Section.builder().fontAxisOffset(8).mmWidth(15).numDivisions(2).startValue(480).endValue(520).build());
		sections.add(Section.builder().fontAxisOffset(8).mmWidth(16.25).numDivisions(2).startValue(520).endValue(560).build());
		sections.add(Section.builder().fontAxisOffsetLast(8).drawLast(true).fontAxisOffset(8).mmWidth(17.25).numDivisions(2).startValue(560).endValue(600).color(Color.BLACK).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(false).color(Color.BLACK).labelSide(LabelSide.LEFT).build();
		
		HorizontalScale lowSpeedScale = HorizontalScale.builder()
				.mmStartOffset(0)
				.mmWidth(138)
				.sections(sections)
				.charactistics(characteristics)
				.scaleOffset(new Point2D(10, 148))
				.clickZone(new Rectangle2D(8 * mmPerPixel, 148 * mmPerPixel, 148 * mmPerPixel, 14 * mmPerPixel))
				.draggableOffset(new Point2D(0, 135))
				.build();
		
		lowSpeedScale.setLabel(ScaleLabel.builder()
				.label("KEAS")
				.rotation(0)
				.labelColor(Color.SANDYBROWN)
				.stepNum(" 3")
				.scaleLocation(new Point2D(410, 38))
				.stepNumLocation(new Point2D(390, 26))
				.build());
		
		lowSpeedScale.init();
	
		return lowSpeedScale;
	}
	
	private HorizontalScale initKeasHighScale()
	{
		LinkedList<Section> sections = new LinkedList<>();
		
		sections.add(Section.builder().fontAxisOffset(2).mmWidth(2.5).numDivisions(2).startValue(0).endValue(160).build());
		sections.add(Section.builder().fontAxisOffset(2).mmWidth(3).numDivisions(2).startValue(160).endValue(240).build());
		sections.add(Section.builder().fontAxisOffset(2).mmWidth(4.25).numDivisions(2).startValue(240).endValue(320).build());
		sections.add(Section.builder().fontAxisOffset(2).mmWidth(5.5).numDivisions(2).startValue(320).endValue(400).build());
		sections.add(Section.builder().fontAxisOffset(2).mmWidth(6.5).numDivisions(2).startValue(400).endValue(480).build());
		sections.add(Section.builder().fontAxisOffset(2).mmWidth(7.75).numDivisions(2).startValue(480).endValue(560).build());
		sections.add(Section.builder().fontAxisOffset(2).mmWidth(9).numDivisions(2).startValue(560).endValue(640).build());
		sections.add(Section.builder().fontAxisOffset(2).mmWidth(10).numDivisions(2).startValue(640).endValue(720).build());
		sections.add(Section.builder().fontAxisOffset(2).mmWidth(11.5).numDivisions(2).startValue(720).endValue(800).build());
		sections.add(Section.builder().fontAxisOffset(2).mmWidth(12.75).numDivisions(2).startValue(800).endValue(880).build());
		sections.add(Section.builder().fontAxisOffset(2).mmWidth(13.5).numDivisions(2).startValue(880).endValue(960).build());
		sections.add(Section.builder().fontAxisOffset(2).mmWidth(15).numDivisions(2).startValue(960).endValue(1040).build());
		sections.add(Section.builder().fontAxisOffset(2).mmWidth(16.25).numDivisions(2).startValue(1040).endValue(1120).build());
		sections.add(Section.builder().fontAxisOffsetLast(2).drawLast(true).fontAxisOffset(2).mmWidth(17.25).numDivisions(2).startValue(1120).endValue(1200).color(Color.BLACK).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(false).color(Color.BLACK).labelSide(LabelSide.RIGHT).build();
		
		HorizontalScale highSpeedScale = HorizontalScale.builder()
				.mmStartOffset(0)
				.mmWidth(138)
				.sections(sections)
				.charactistics(characteristics)
				.scaleOffset(new Point2D(10, 148))
				.clickZone(new Rectangle2D(8 * mmPerPixel, 133.75 * mmPerPixel, 148 * mmPerPixel, 14 * mmPerPixel))
				.draggableOffset(new Point2D(0, 135))
				.build();
		
		highSpeedScale.setLabel(ScaleLabel.builder()
				.label("KEAS")
				.rotation(0)
				.labelColor(Color.SANDYBROWN)
				.stepNum(" 3")
				.scaleLocation(new Point2D(410, 38))
				.stepNumLocation(new Point2D(390, 26))
				.build());
		
		highSpeedScale.init();
	
		return highSpeedScale;
	}
}
