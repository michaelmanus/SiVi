package com.adonax.simplexNoiseVisualizer;

import com.adonax.simplexNoiseVisualizer.NoiseData;
import com.adonax.simplexNoiseVisualizer.TextureFunctions;
import com.adonax.simplexNoiseVisualizer.models.GlobalConfiguration;
import com.adonax.simplexNoiseVisualizer.models.TextureModel;

import java.awt.image.BufferedImage;

/**
 * Created by One on 12/27/2014.
 */
public class TextureGenerator {

    final GlobalConfiguration configuration;
    public TextureGenerator(int width, int height)
    {
        configuration = GlobalConfiguration.inst();

        configuration.width = width;
        configuration.height = height;
    }

    public BufferedImage generate(TextureModel model)
    {
        NoiseData noiseData =
                TextureFunctions.makeNoiseDataArray(
                        configuration.width,
                        configuration.height,
                        model.octaveModels,
                        model.mixerModel);

        final BufferedImage image = TextureFunctions.makeImage(
            noiseData, model.mixerModel,
                model.colorAxis.getColorMap());
        return image;
    }

}
