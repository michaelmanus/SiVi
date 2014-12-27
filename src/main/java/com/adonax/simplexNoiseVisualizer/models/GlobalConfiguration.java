package com.adonax.simplexNoiseVisualizer.models;

public class GlobalConfiguration
{
	public enum Fields {
		OCTAVES,
		FINAL_WIDTH,
		FINAL_HEIGHT,
		COLOR_MAPS
	}
	
	public int octaves;
	public int width;
	public int height;
	public int colorMaps;

	private static GlobalConfiguration self;
	public static GlobalConfiguration inst()
	{
		if (self == null)
		{
			self = new GlobalConfiguration();
		}
		return self;
	}
	
	private GlobalConfiguration()
	{
		this.octaves = 4;
		this.width = 500;
		this.height = 300;
		this.colorMaps = 6;
	}

	public GlobalConfiguration(int octaves, int width,
							   int height, int colorMaps)
	{
		this.octaves = octaves;
		this.width = width;
		this.height = height;
		this.colorMaps = colorMaps;
	}
	
	public static GlobalConfiguration setAppSetting(GlobalConfiguration tpm,
			GlobalConfiguration.Fields field, Object value)
	{
		
		// warning: local shadowing 'octaves'
		int octaves = tpm.octaves;
		int finalWidth = tpm.width;
		int finalHeight = tpm.height;
		int colorMaps = tpm.colorMaps;
		
		switch (field)
		{
		case OCTAVES: octaves = (Integer)value; break;
		case FINAL_WIDTH: finalWidth = (Integer)value; break;
		case FINAL_HEIGHT: finalHeight = (Integer)value; break;
		case COLOR_MAPS: colorMaps = (Integer)value; break;
		}	
		
		return new GlobalConfiguration(octaves, finalWidth,
				finalHeight, colorMaps);
	}
}
