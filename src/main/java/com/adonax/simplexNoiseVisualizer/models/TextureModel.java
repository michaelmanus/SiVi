package com.adonax.simplexNoiseVisualizer.models;

import com.adonax.simplexNoiseVisualizer.color.ColorAxis;

public class TextureModel {
    public final OctaveModel[] octaveModels;
    public final MixerModel mixerModel;
    public final GradientModel gradientModel;


    //TODO:  should be peg set and HSB, array of these, and selector
    public final ColorAxis colorAxis;


    public TextureModel(OctaveModel[] octaveModels, MixerModel mixerModel, GradientModel gradientModel, ColorAxis colorAxis) {
        this.octaveModels = octaveModels;
        this.mixerModel = mixerModel;
        this.gradientModel = gradientModel;
        this.colorAxis = colorAxis;
    }

}
