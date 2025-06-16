package com.additolc.nomograph;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class SlantScale extends AbstractScale {

	protected SlantScale(AbstractScaleBuilder<?, ?> b) {
		super(b);
	}

	@Override
	public boolean containsClick(double x, double y)
	{
		boolean result = true;
		result &= (x >= clickZone.getMinX() && x <= clickZone.getMaxX());
		result &= (y >= clickZone.getMinY() && y <= clickZone.getMaxY());
		
		return result;
	}
		
	@Override
	public boolean isDraggingDot(double x, double y)
	{
		boolean result = true;
		double dragX = draggableX;
		double dragY = draggableY;
		
		result &= (((x/2.0) <= dragX + 12) && ((x/2.0) >= dragX - 12));
		result &= (((y/2.0) <= dragY + 12) && ((y/2.0) >= dragY - 12));
		
		return result;
	}
	
	@Override
	public Point2D getPointForSlideValue(double dataPoint)
	{
		for (Section section:sections)
		{
			if ((charactistics.isDescending() && dataPoint <= section.getStartValue() && dataPoint >= section.getEndValue()) ||
				(!charactistics.isDescending() && dataPoint >= section.getStartValue() && dataPoint <= section.getEndValue()))
			{
				double deltaY = Math.abs(section.getStartLocation() - section.getEndLocation());
				double deltaValue = Math.abs(section.getStartValue() - section.getEndValue());
				double percentage = 1 - (Math.abs(dataPoint - section.getEndValue()) / deltaValue);
				Point2D p = new Point2D((scaleOffset.getX() * mmPerPixel), section.getStartLocation() + (deltaY * percentage));
				return p;
			}
		}
		
		return new Point2D(0,0);
	}
	
	@Override
	public double getDataPointForSlideValue(double yInputValue)
	{
		double yValue = yInputValue;
		
		for (Section section:sections)
		{
			if (yValue >= section.getStartLocation() && yValue <= section.getEndLocation())
			{
				double deltaY = Math.abs(section.getStartLocation() - section.getEndLocation());
				double deltaValue = Math.abs(section.getStartValue() - section.getEndValue());
				
				double percentage = Math.abs(yValue - section.getStartLocation()) / deltaY;
				if (charactistics.isDescending())
				{
					return section.getStartValue() - (percentage * deltaValue);
				}
				else
				{
					return (percentage * deltaValue) + section.getStartValue();
				}

			}
		}
		
		return -999;
	}
	
	public void init()
	{
		double mmPerPixel = Screen.getPrimary().getDpi()/25.4;
		double offsetY = (mmPerPixel * scaleOffset.getY());
		
		// draw the section
		double currentPixelLocation = offsetY + (.707 * mmStartOffset*mmPerPixel);
		for (Section section:sections)
		{
			section.setStartLocation(currentPixelLocation);
			section.setEndLocation(currentPixelLocation + (section.getMmHeight() * mmPerPixel * .707));
			currentPixelLocation += (section.getMmHeight() * mmPerPixel * .707);
		}
	}
	
	@Override
	public void draw(GraphicsContext gc)
	{
		double offsetX = mmPerPixel * scaleOffset.getX();
		double offsetY = mmPerPixel * scaleOffset.getY();
		
		gc.save();
		gc.translate(offsetX, offsetY);
		gc.rotate(45);
		gc.translate(-offsetX, -offsetY);
		
		// draw shaded regions first
		if (shadedRegions != null) {
			for (ShadedRegion region:shadedRegions)
			{
				if (!region.isUseYValue())
				{
					double regionXOffset = (offsetX - region.getWidth()) * mmPerPixel;
					double height = Math.abs(getPointForSlideValue(region.getStartValue()).getY() - getPointForSlideValue(region.getEndValue()).getY());
					gc.setFill(region.getColor());
					gc.fillRect(regionXOffset, getPointForSlideValue(region.getStartValue()).getY(), region.getWidth() * mmPerPixel, height);
				}
				else
				{
					gc.setFill(region.getColor());
					gc.fillRect(offsetX - region.getWidth() * mmPerPixel, offsetY + (region.getYMMStart() * mmPerPixel), region.getWidth() * mmPerPixel, (region.getYMMEnd() - region.getYMMStart()) * mmPerPixel);
				}
			}
		}
		
		// draw main axis spine
		gc.setFill(charactistics.getColor());
		gc.setLineWidth(charactistics.getLineWidth());
		gc.strokeLine(offsetX, offsetY, offsetX, offsetY + (mmHeight*mmPerPixel));
		
		// draw the section
		gc.moveTo(offsetX, offsetY + (mmPerPixel*mmStartOffset));
		double startPixel = offsetY + (mmStartOffset*mmPerPixel);
		
		for (Section section:sections)
		{
			double deltaValue = Math.abs(section.getStartValue() - section.getEndValue()) / (section.getNumDivisions() - 1);
			double deltaPixel = (section.getMmHeight() * mmPerPixel) / (section.getNumDivisions() - 1);
			double startValue = section.getStartValue();
			int lastIndex = section.getNumDivisions() - 1;
			
			for (int i = 0; i < section.getNumDivisions(); i++)
			{
				gc.setFont(Font.font(charactistics.getFontSize()));
				gc.setFill((charactistics.getColor() != null) ? section.getColor() : Color.BLACK);
				if (charactistics.getLabelSide().equals(LabelSide.LEFT) && ((i == lastIndex && section.isDrawLast()) || i != lastIndex))
				{
					double offset = (i == lastIndex) ? section.getFontAxisOffsetLast() : section.getFontAxisOffset();
					gc.fillText(""+startValue, offsetX - (offset * mmPerPixel), 
							startPixel + (charactistics.getFontHeightOffset() * mmPerPixel));
					gc.strokeLine(offsetX - (charactistics.getTickWidthHeight() * mmPerPixel), startPixel, offsetX, startPixel);
				}
				else if ((i == lastIndex && section.isDrawLast()) || i != lastIndex)
				{
					double offset = (i == lastIndex) ? section.getFontAxisOffsetLast() : section.getFontAxisOffset();
					gc.fillText(""+startValue, offsetX + (offset * mmPerPixel), 
							startPixel + (charactistics.getFontHeightOffset() * mmPerPixel));
					gc.strokeLine(offsetX + (charactistics.getTickWidthHeight() * mmPerPixel), startPixel, offsetX, startPixel);
				}
				
				startValue += ((charactistics.isDescending()) ? -deltaValue: deltaValue);
				startPixel += deltaPixel;
			}
			
			startPixel -= deltaPixel;
		}
		
		gc.restore();
		
		if (label != null)
		{
			double posOffsetX = (label.getScaleOffset() == null) ? 0 : label.getScaleOffset().getX();
			double posOffsetY = (label.getScaleOffset() == null) ? 0 : label.getScaleOffset().getY();
					
			gc.save();
			gc.translate(offsetX + label.getScaleLocation().getX() + posOffsetX, offsetY + label.getScaleLocation().getY() + posOffsetY);
			gc.rotate(90 + label.getRotation());
			
			gc.setFill(Color.BLACK);
			gc.setFont(Font.font("Sans", FontWeight.BOLD, 12));
			gc.fillText(label.getLabel(), 0, 0);
			
			gc.restore();
			
			if (label.isDrawValue())
			{
				gc.save();
				gc.setFill(label.getLabelColor());
				gc.fillOval(posOffsetX + offsetX + label.getStepNumLocation().getX()-2, posOffsetY + offsetY + label.getStepNumLocation().getY(), 16, 16);
				
				gc.setFill(label.getLabelColor());
				gc.fillOval(posOffsetX + offsetX + label.getStepNumLocation().getX() + 24, posOffsetY + offsetY + label.getStepNumLocation().getY(), 16, 16);
				gc.setFill(Color.WHITE);
				gc.fillOval(posOffsetX + offsetX + label.getStepNumLocation().getX() + 25, posOffsetY + offsetY + label.getStepNumLocation().getY() + 1, 14, 14);
				
				gc.setFill(label.getLabelColor());
				gc.fillRect(posOffsetX + offsetX + label.getStepNumLocation().getX() + 8, posOffsetY + offsetY + label.getStepNumLocation().getY(), 24, 16);
				gc.setFill(Color.WHITE);
				gc.fillRect(posOffsetX + offsetX + label.getStepNumLocation().getX() + 15, posOffsetY + offsetY + label.getStepNumLocation().getY() + 1, 18, 14);
				
				gc.setFill(Color.WHITE);
				gc.setFont(Font.font("Sans", FontWeight.NORMAL, 10));
				gc.fillText(label.getStepNum(), posOffsetX + offsetX + label.getStepNumLocation().getX() + 1, posOffsetY + offsetY + label.getStepNumLocation().getY() + 11);
				gc.translate(offsetX + label.getStepNumLocation().getX(), offsetY + label.getStepNumLocation().getY());
				gc.restore();
				
				gc.setFill(Color.BLACK);
				gc.setFont(Font.font("Sans", FontWeight.BOLD, 8));
				DecimalFormat df = new DecimalFormat("##.##");
				df.setRoundingMode(RoundingMode.UP);
				gc.fillText(df.format(value), posOffsetX + offsetX + label.getStepNumLocation().getX() + 16, posOffsetY + offsetY + label.getStepNumLocation().getY() + 11);

			}
			else 
			{
				gc.save();
				gc.setFill(label.getLabelColor());
				gc.fillOval(offsetX + label.getStepNumLocation().getX(), offsetY + label.getStepNumLocation().getY(), 16, 16);
				gc.setFill(Color.WHITE);
				gc.setFont(Font.font("Sans", FontWeight.NORMAL, 10));
				gc.fillText(label.getStepNum(), offsetX + label.getStepNumLocation().getX() + 1, offsetY + label.getStepNumLocation().getY() + 11);
				gc.translate(offsetX + label.getStepNumLocation().getX(), offsetY + label.getStepNumLocation().getY());
				gc.restore();
			}
		}
	}
	
	@Override
	public void drawDraggableNotch(GraphicsContext gc)
	{
		// show the draggable notch
		double offsetX = mmPerPixel * scaleOffset.getX();
		double offsetY = mmPerPixel * scaleOffset.getY();
		if (draggableX == -99) draggableX = offsetX + (mmPerPixel*mmStartOffset) + (mmPerPixel * draggableOffset.getX());
		if (draggableY == -99) draggableY = offsetY;
		
		if (showDraggable)
		{
			gc.setFill(Color.RED);
			gc.fillOval(draggableX - 3, draggableY - 3, 6, 6);
			
			// draw the value
			int xOffset = (this.charactistics.getLabelSide().equals(LabelSide.RIGHT)) ? 0 : -40;
			
			gc.setFill(Color.BLACK);
			gc.setLineWidth(2);
			gc.strokeRect(draggableX + 5 + xOffset, draggableY - 6, 30, 12);
			gc.setFill(Color.WHITE);
			gc.fillRect(draggableX + 6 + xOffset, draggableY - 5, 28, 10);
			
			DecimalFormat df = new DecimalFormat("##.#");
			df.setRoundingMode(RoundingMode.UP);
			gc.setFill(Color.BLACK);
			gc.setFont(Font.font("Sans", FontWeight.NORMAL, 10));
			
			double value = getDataPointForSlideValue(draggableX);
			if (value > -999)
			{
				gc.fillText(""+df.format(value), draggableX + 8 + xOffset, draggableY + 3);
			}
		}
	}
}
