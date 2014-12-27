package com.adonax.simplexNoiseVisualizer.models;

import com.adonax.simplexNoiseVisualizer.color.ColorAxis;

public class GUITextureModel extends TextureModel
{
	public final GlobalConfiguration appSettings;

	
	public GUITextureModel(GlobalConfiguration appSettings,
						   OctaveModel[] octaveModels, MixerModel mixerModel,
						   GradientModel gradientModel, ColorAxis colorAxis)
	{
		super(octaveModels,mixerModel, gradientModel, colorAxis);
		this.appSettings = appSettings;

	}
}
