package com.additolc.graphs;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.LinkedList;

import com.additolc.messaging.Subscriber;
import com.additolc.nomograph.Chart;
import com.additolc.nomograph.LabelSide;
import com.additolc.nomograph.NomographCharacteristics;
import com.additolc.nomograph.ScaleLabel;
import com.additolc.nomograph.Section;
import com.additolc.nomograph.ShadedRegion;
import com.additolc.nomograph.SlantScale;
import com.additolc.nomograph.VerticalScale;
import com.additolc.state.GameState;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class Step4Chart extends Chart {

	private double loadLimit;
	
	public Step4Chart(Dimension2D dimensions) {
		super(dimensions);
	}

	@Override
	public void drawLines() {
		
		if (getScales().get("maxLiftScale").isShowDraggable())
		{
			
			getGraphicsContext2D().clearRect(-10, 0, getWidth(), getHeight());
			getGraphicsContext2D().setTransform(1, 0, 0, 1, 0, 0);
			getGraphicsContext2D().translate(4 * mmPerPixel, 0);
			getGraphicsContext2D().scale(2.0, 2.0);
			
			double x1 = getScales().get("maxLiftScale").getDraggableX();
			double y1 = getScales().get("maxLiftScale").getDraggableY();
			String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
			double x2 = GameState.getInstanceOf().getAircraftState().get(currentAircraftId).getQPoint().getX();
			double y2 = GameState.getInstanceOf().getAircraftState().get(currentAircraftId).getQPoint().getY();
			double slope = (y2 - y1) / (x2 - x1);
			double x3 = getScales().get("aeroLoadLimit").getScaleOffset().getX() * mmPerPixel;
			double y3 = (slope * (x3 - x2)) + y2;
			
			getGraphicsContext2D().setLineWidth(1);
			getGraphicsContext2D().strokeLine(x1, y1, x2, y2);
			getGraphicsContext2D().strokeLine(x2, y2, x3, y3);
			draw(1.0);
			getGraphicsContext2D().setFill(Color.RED);
			getGraphicsContext2D().fillOval(x2-3, y2-3, 6, 6);
			
			loadLimit = getScales().get("aeroLoadLimit").getDataPointForSlideValue(y3);
			int xOffset = (getScales().get("aeroLoadLimit").getCharactistics().getLabelSide().equals(LabelSide.LEFT)) ? 0 : -40;
			
			getGraphicsContext2D().setFill(Color.BLACK);
			getGraphicsContext2D().setLineWidth(2);
			getGraphicsContext2D().strokeRect(x3 + 5 + xOffset, y3 - 6, 30, 12);
			getGraphicsContext2D().setFill(Color.WHITE);
			getGraphicsContext2D().fillRect(x3 + 6 + xOffset, y3 - 5, 28, 10);
			
			DecimalFormat df = new DecimalFormat("##.#");
			df.setRoundingMode(RoundingMode.UP);
			getGraphicsContext2D().setFill(Color.BLACK);
			getGraphicsContext2D().setFont(Font.font("Sans", FontWeight.NORMAL, 10));
			
			if (loadLimit > -999)
			{
				getGraphicsContext2D().fillText(""+df.format(loadLimit), x3 + 8 + xOffset, y3 + 3);
			}
			
			getGraphicsContext2D().setFill(Color.BLACK);
			getGraphicsContext2D().fillOval(x3 - 3, y3 - 3, 6, 6);
			
		}
	}

	@Override
	public Object execute(Object... parameters) {
		
		double maxLift = (double)parameters[0];
		
		double x1 = getScales().get("maxLiftScale").getScaleOffset().getX() * mmPerPixel;
		double y1 = getScales().get("maxLiftScale").getPointForSlideValue(maxLift).getY();
		String currentAircraftId = GameState.getInstanceOf().getCurrentAircraft();
		double x2 = GameState.getInstanceOf().getAircraftState().get(currentAircraftId).getQPoint().getX();
		double y2 = GameState.getInstanceOf().getAircraftState().get(currentAircraftId).getQPoint().getY();
		double slope = (y2 - y1) / (x2 - x1);
		double x3 = getScales().get("aeroLoadLimit").getScaleOffset().getX() * mmPerPixel;
		double y3 = (slope * (x3 - x2)) + y2;
		
		return getScales().get("aeroLoadLimit").getDataPointForSlideValue(y3);
	}
	
	@Override
	protected void init() {
		
		getScales().put("qScale", initQScale());
		getScales().put("maxLiftScale", initMaxLiftScale());
		getScales().put("aeroLoadLimit", initAeroLoadLimitScale());
		
		setMouseClickedHandler((MouseEvent event)->{

			if (wasDragged) 
			{
				wasDragged = false;
				return;
			}
			
			double x = event.getX() / 2.0;
			double y = event.getY() / 2.0;
			
			if (getScales().get("maxLiftScale").containsClick(x, y))
			{
				getScales().get("maxLiftScale").setShowDraggable(!getScales().get("maxLiftScale").isShowDraggable());
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
			double sceneX = event.getX();
			double sceneY = event.getY();
			
			if ((!getScales().get("maxLiftScale").isDragging()) && 
				getScales().get("maxLiftScale").isDraggingDot(sceneX, sceneY))
			{
				getScales().get("maxLiftScale").setDragging(true);
			}
			
			if (getScales().get("maxLiftScale").isShowDraggable() && getScales().get("maxLiftScale").isDragging()) 
			{
				double offsetY = mmPerPixel * getScales().get("maxLiftScale").getScaleOffset().getY();
				if ((sceneY/2.0) < offsetY + (mmPerPixel*getScales().get("maxLiftScale").getMmStartOffset())) return;
				else if ((sceneY/2.0) > offsetY + (getScales().get("maxLiftScale").getMmHeight()*mmPerPixel)) return;
				getScales().get("maxLiftScale").setDraggableY(sceneY/2.0);
			}
			else
			{
				return;
			}
			
			drawLines();

		});
	}

	private VerticalScale initAeroLoadLimitScale()
	{
		LinkedList<Section> sections = new LinkedList<>();
		sections.add(Section.builder().fontAxisOffset(6).fontAxisOffsetLast(6).mmHeight(137.5).numDivisions(25).startValue(12).endValue(0).drawLast(true).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(true).color(Color.BLACK).labelSide(LabelSide.LEFT).build();
		
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
				.label("Load Limit")
				.rotation(180)
				.labelColor(Color.YELLOW)
				.stepNum(" 4")
				.scaleLocation(new Point2D(-25, 104))
				.stepNumLocation(new Point2D(-37, 24))
				.build());
		
		aeroLoadLimitScale.init();
		
		return aeroLoadLimitScale;
	}
	
	private VerticalScale initMaxLiftScale()
	{
		LinkedList<Section> sections = new LinkedList<>();
		sections.add(Section.builder().fontAxisOffset(2).fontAxisOffsetLast(2).mmHeight(144.5).numDivisions(22).startValue(0).endValue(21).drawLast(true).build());
		
		NomographCharacteristics characteristics = NomographCharacteristics.builder()
				.fontSize(7).tickWidthHeight(1).fontHeightOffset(.75).lineWidth(1).isDescending(false).color(Color.BLACK).labelSide(LabelSide.RIGHT).build();
		
		LinkedList<ShadedRegion> shadedRegions = new LinkedList<>();
		
		VerticalScale maxLiftScale = VerticalScale.builder()
				.mmStartOffset(0)
				.mmHeight(144.5)
				.sections(sections)
				.charactistics(characteristics)
				.scaleOffset(new Point2D(148, 10))
				.clickZone(new Rectangle2D(142 * mmPerPixel, 8 * mmPerPixel, 12 * mmPerPixel, 150 * mmPerPixel))
				.draggableOffset(new Point2D(0,0))
				.shadedRegions(shadedRegions)
				.build();
		
		maxLiftScale.setLabel(ScaleLabel.builder()
				.label("Maximum Lift")
				.rotation(0)
				.labelColor(Color.YELLOW)
				.stepNum(" 4")
				.scaleLocation(new Point2D(25, 334))
				.stepNumLocation(new Point2D(21, 418))
				.build());
		
		maxLiftScale.init();
	
		return maxLiftScale;
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
				.labelColor(Color.YELLOW)
				.stepNum(" 4")
				.scaleLocation(new Point2D(-196, 230))
				.stepNumLocation(new Point2D(-216, 218))
				.build());
		
		qScale.init();
	
		return qScale;
	}
}
