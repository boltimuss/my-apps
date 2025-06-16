package com.additolc.graphs;

import java.util.LinkedList;

import com.additolc.nomograph.Chart;
import com.additolc.nomograph.LabelSide;
import com.additolc.nomograph.NomographCharacteristics;
import com.additolc.nomograph.ScaleLabel;
import com.additolc.nomograph.Section;
import com.additolc.nomograph.ShadedRegion;
import com.additolc.nomograph.SlantScale;
import com.additolc.nomograph.VerticalScale;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Step13Chart  extends Chart {

	public Step13Chart(Dimension2D dimensions) {
		super(dimensions);
	}

	@Override
	public void drawLines() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object execute(Object... parameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void init() {
		
		getScales().put("qScale", initQScale());
		getScales().put("formDrag", initFormDrag());
		getScales().put("formDeltaSpeedLow", initFormDeltaSpeedLow());
		getScales().put("formDeltaSpeedHigh", initFormDeltaSpeedHigh());
		
//		setMouseClickedHandler((MouseEvent event)->{
//
//			if (wasDragged) 
//			{
//				wasDragged = false;
//				return;
//			}
//			
//			double x = event.getX() / 2.0;
//			double y = event.getY() / 2.0;
//			
//			if (getScales().get("maxLiftScale").containsClick(x, y))
//			{
//				getScales().get("maxLiftScale").setShowDraggable(!getScales().get("maxLiftScale").isShowDraggable());
//			}
//			else 
//			{
//				return;
//			}
//			
//			getGraphicsContext2D().clearRect(-10, 0, getWidth(), getHeight());
//			getGraphicsContext2D().setTransform(1, 0, 0, 1, 0, 0);
//			getGraphicsContext2D().translate(4 * mmPerPixel, 0);
//			draw(2.0);
//			drawLines();
//		});
			
//		setMouseDraggedHandler((MouseEvent event)->{
//			
//			wasDragged = true;
//			double sceneX = event.getX();
//			double sceneY = event.getY();
//			
//			if ((!getScales().get("maxLiftScale").isDragging()) && 
//				getScales().get("maxLiftScale").isDraggingDo t(sceneX, sceneY))
//			{
//				getScales().get("maxLiftScale").setDragging(true);
//			}
//			
//			if (getScales().get("maxLiftScale").isShowDraggable() && getScales().get("maxLiftScale").isDragging()) 
//			{
//				double offsetY = mmPerPixel * getScales().get("maxLiftScale").getScaleOffset().getY();
//				if ((sceneY/2.0) < offsetY + (mmPerPixel*getScales().get("maxLiftScale").getMmStartOffset())) return;
//				else if ((sceneY/2.0) > offsetY + (getScales().get("maxLiftScale").getMmHeight()*mmPerPixel)) return;
//				getScales().get("maxLiftScale").setDraggableY(sceneY/2.0);
//			}
//			else
//			{
//				return;
//			}
//			
//			drawLines();
//
//		});
	}

	private SlantScale initQScale()
	{
		LinkedList<Section> sections = new LinkedList<>();
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(true).color(Color.BLACK).labelSide(LabelSide.LEFT).build();
		
		LinkedList<ShadedRegion> shadedRegions = new LinkedList<>();
		shadedRegions.add(ShadedRegion.builder().color(Color.YELLOW).width(1.0).yMMStart(137).yMMEnd(166.5).build());
		shadedRegions.add(ShadedRegion.builder().color(Color.ORANGE).width(1.0).yMMStart(166.5).yMMEnd(179).build());
		shadedRegions.add(ShadedRegion.builder().color(Color.RED).width(1.0).yMMStart(179).yMMEnd(193).build());
		
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
				.labelColor(Color.MEDIUMPURPLE)
				.stepNum("13")
				.scaleLocation(new Point2D(-196, 230))
				.stepNumLocation(new Point2D(-216, 218))
				.build());
		
		qScale.init();
	
		return qScale;
	}
	
	private VerticalScale initFormDeltaSpeedLow()
	{
		LinkedList<Section> sections = new LinkedList<>();
		sections.add(Section.builder().fontAxisOffset(2).fontAxisOffsetLast(2).mmHeight(137.5).numDivisions(10).startValue(45).endValue(0).drawLast(true).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(true).color(Color.MEDIUMPURPLE).labelSide(LabelSide.RIGHT).build();
		
		LinkedList<ShadedRegion> shadedRegions = new LinkedList<>();
		
		VerticalScale aeroLoadLimitScale = VerticalScale.builder()
				.mmStartOffset(0)
				.mmHeight(137.5)
				.sections(sections)
				.charactistics(characteristics)
				.scaleOffset(new Point2D(10, 10))
				.draggableOffset(new Point2D(0,0))
				.shadedRegions(shadedRegions)
				.build();
		
		aeroLoadLimitScale.setLabel(ScaleLabel.builder()
				.label("Form DeltaSpeed")
				.rotation(-90)
				.labelColor(Color.MEDIUMPURPLE)
				.stepNum("13")
				.scaleLocation(new Point2D(42, 172))
				.stepNumLocation(new Point2D(42, 145))
				.build());
		
		aeroLoadLimitScale.init();
		
		return aeroLoadLimitScale;
	}
	
	private VerticalScale initFormDeltaSpeedHigh()
	{
		LinkedList<Section> sections = new LinkedList<>();
		sections.add(Section.builder().fontAxisOffset(7).fontAxisOffsetLast(7).mmHeight(137.5).numDivisions(19).startValue(180).endValue(0).drawLast(true).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(true).color(Color.PURPLE).labelSide(LabelSide.RIGHT).build();
		
		LinkedList<ShadedRegion> shadedRegions = new LinkedList<>();
		
		VerticalScale aeroLoadLimitScale = VerticalScale.builder()
				.mmStartOffset(0)
				.mmHeight(137.5)
				.sections(sections)
				.charactistics(characteristics)
				.scaleOffset(new Point2D(10, 10))
				.draggableOffset(new Point2D(0,0))
				.shadedRegions(shadedRegions)
				.build();
		
		aeroLoadLimitScale.setLabel(ScaleLabel.builder()
				.label("  High Speed \r Form DeltaSpeed")
				.rotation(180)
				.labelColor(Color.PURPLE)
				.stepNum("13")
				.scaleLocation(new Point2D(55, 110))
				.stepNumLocation(new Point2D(42, 145))
				.build());
		
		aeroLoadLimitScale.init();
		
		return aeroLoadLimitScale;
	}
	
	private VerticalScale initFormDrag()
	{
		LinkedList<Section> sections = new LinkedList<>();
		sections.add(Section.builder().fontAxisOffset(2).fontAxisOffsetLast(2).mmHeight(137.5).numDivisions(26).startValue(0).endValue(25).drawLast(true).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(false).color(Color.MEDIUMPURPLE).labelSide(LabelSide.RIGHT).build();
		
		LinkedList<ShadedRegion> shadedRegions = new LinkedList<>();
		
		VerticalScale maxLiftScale = VerticalScale.builder()
				.mmStartOffset(0)
				.mmHeight(137.5)
				.sections(sections)
				.charactistics(characteristics)
				.scaleOffset(new Point2D(148, 10))
				.clickZone(new Rectangle2D(142 * mmPerPixel, 8 * mmPerPixel, 12 * mmPerPixel, 150 * mmPerPixel))
				.draggableOffset(new Point2D(0,0))
				.shadedRegions(shadedRegions)
				.build();
		
		maxLiftScale.setLabel(ScaleLabel.builder()
				.label("Form Drag")
				.rotation(-90)
				.labelColor(Color.MEDIUMPURPLE)
				.stepNum("13")
				.scaleLocation(new Point2D(-70, 230))
				.stepNumLocation(new Point2D(-47, 204))
				.build());
		
		maxLiftScale.init();
	
		return maxLiftScale;
	}
}
