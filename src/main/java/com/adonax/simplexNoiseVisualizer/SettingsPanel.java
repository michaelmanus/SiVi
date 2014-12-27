package com.adonax.simplexNoiseVisualizer;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.adonax.simplexNoiseVisualizer.gradients.GradientGUI;
import com.adonax.simplexNoiseVisualizer.models.GradientModel;
import com.adonax.simplexNoiseVisualizer.gradients.LinearGradientFunction;
import com.adonax.simplexNoiseVisualizer.gradients.RadialGradientFunction;
import com.adonax.simplexNoiseVisualizer.gradients.SinusoidalGradientFunction;
import com.adonax.simplexNoiseVisualizer.models.MixerModel;
import com.adonax.simplexNoiseVisualizer.models.OctaveModel;
import com.adonax.simplexNoiseVisualizer.models.GUITextureModel;
import com.adonax.simplexNoiseVisualizer.models.GlobalConfiguration;

public class SettingsPanel extends JPanel
{

	private static final long serialVersionUID = 1L;

	Object parent;
	
	SettingsPanel(final TopPanel topPanel)
	{
		GlobalConfiguration settings = topPanel.getAppSettings();
		
		JLabel octavesLbl = new JLabel("Octaves");
		final JTextField octaves = new JTextField(
				String.valueOf(topPanel.getAppSettings().octaves), 5);
		octaves.addActionListener(new ActionListener()
		{			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				/*
				 * Redisplay with new number of octaves, preserving 
				 * existing octaves where possible.
				 */
				int octaveChannels = Integer.valueOf(octaves.getText()); 
				GlobalConfiguration model = topPanel.getAppSettings();
				model = GlobalConfiguration.setAppSetting(model,
						GlobalConfiguration.Fields.OCTAVES,
						octaveChannels);
				topPanel.setAppSettings(model);
				
				MixerModel mm = topPanel.mixerGUI.getMixerModel();
				GradientModel ggm =
						topPanel.mixerGUI.getGradientGUI().getModel();
						
				float[] weights = mm.weights;
				float[] newWeights = new float[model.octaves];
				for (int i = 0; i < model.octaves; i++)
				{
					if (i < weights.length)
					{
						newWeights[i] = weights[i];
					}
					else
					{
						newWeights[i] = 0.25f; //TODO: default weight
					}
				}
				mm = MixerModel.updateMixSetting(mm, 
						MixerModel.Fields.WEIGHTS, newWeights);
				
				topPanel.updateMixerGUI(
						new MixerGUI(topPanel, mm, ggm));	
				
				topPanel.updateOctavesPanel();
				topPanel.updateOctaveDisplays();

			}
		});
		
		JLabel widthLbl = new JLabel("Width");
		final JTextField widthSetting = new JTextField(5);
		widthSetting.setText(String.valueOf(settings.width));
		widthSetting.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{	
				GradientGUI.closeAllGradientWindows();
				
				int newWidth = 
						Integer.valueOf(widthSetting.getText());
				
				GlobalConfiguration newSettings =
						GlobalConfiguration.setAppSetting(
								topPanel.getAppSettings(),
								GlobalConfiguration.Fields.FINAL_WIDTH,
								newWidth);
				
				rebuildWithNewSizeSettings(topPanel, newSettings);
			}
		});
		
		JLabel heightLbl = new JLabel("Height");
		final JTextField heightSetting = new JTextField(5);
		heightSetting.setText(String.valueOf(settings.height));
		heightSetting.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				GradientGUI.closeAllGradientWindows();
				
				int newHeight = 
						Integer.valueOf(heightSetting.getText());
				
				GlobalConfiguration newSettings =
						GlobalConfiguration.setAppSetting(
								topPanel.getAppSettings(),
								GlobalConfiguration.Fields.FINAL_HEIGHT,
								newHeight);

				rebuildWithNewSizeSettings(topPanel, newSettings);
			}
		});
		
		JLabel colorMapsLbl = new JLabel("Color Maps");
		JTextField colorMapsSetting = new JTextField(5);
		colorMapsSetting.setEnabled(false);
		colorMapsSetting.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// TODO Auto-generated method stub
				/*
				 * Goal: change the number of color maps in the
				 * color map GUI, non-destructively as much as 
				 * possible.
				 */
			}
		});
		
		setLayout(new GridBagLayout());
		GridBagConstraints gbConstraints = new GridBagConstraints();
		gbConstraints.anchor = GridBagConstraints.LINE_START;
		
		gbConstraints.gridx = 0;
		gbConstraints.gridy = 0;
		add(octavesLbl, gbConstraints);
		gbConstraints.gridx = 1;
		gbConstraints.gridy = 0;
		add(octaves, gbConstraints);
		
		gbConstraints.gridx = 0;
		gbConstraints.gridy = 1;
		add(widthLbl, gbConstraints);
		gbConstraints.gridx = 1;
		gbConstraints.gridy = 1;
		add(widthSetting, gbConstraints);
		
		gbConstraints.gridx = 0;
		gbConstraints.gridy = 2;
		add(heightLbl, gbConstraints);
		gbConstraints.gridx = 1;
		gbConstraints.gridy = 2;
		add(heightSetting, gbConstraints);
		
		gbConstraints.gridx = 0;
		gbConstraints.gridy = 3;
		add(colorMapsLbl, gbConstraints);
		gbConstraints.gridx = 1;
		gbConstraints.gridy = 3;
		add(colorMapsSetting, gbConstraints);
		
	}
	
	private GradientModel reviseGradientGUIModel(
			GradientModel ggm,
			GlobalConfiguration oldMSettings, GlobalConfiguration newSettings)
	{
		
		LinearGradientFunction oldLGF = 
				(LinearGradientFunction)ggm.gradients[0];
		
		LinearGradientFunction newLGF = new LinearGradientFunction(
				newSettings.width, newSettings.height,
				oldLGF.xLeft, oldLGF.xRight,
				oldLGF.yTop, oldLGF.yBottom);
		
		RadialGradientFunction oldRGF = 
				(RadialGradientFunction)ggm.gradients[1];
		
		float widthScaling = newSettings.width / (float)oldMSettings.width;
		float heightScaling = newSettings.height / (float)oldMSettings.height;
		
		// or should we leave 'center point' at same abs location?
		RadialGradientFunction newRGF = new RadialGradientFunction(
				(int)(oldRGF.centerPoint.getX() * widthScaling),
				(int)(oldRGF.centerPoint.getY() * heightScaling),
				// Tricky: assumes one changed, and other is 1:1
				(float)oldRGF.radius * widthScaling * heightScaling,
				oldRGF.edgeVal, oldRGF.centerVal);
		
		SinusoidalGradientFunction oldSGF =
				(SinusoidalGradientFunction)ggm.gradients[2];
		
		SinusoidalGradientFunction newSGF = 
				new SinusoidalGradientFunction(
						oldSGF.originX, oldSGF.originY,
						oldSGF.period, oldSGF.theta,
						oldSGF.highVal,	oldSGF.lowVal);
		
		return new GradientModel(
				newLGF, newRGF, newSGF, ggm.selected);
	}
	
	private void rebuildWithNewSizeSettings(TopPanel topPanel, 
			GlobalConfiguration newSettings)
	{
		// General note: color not affected, thus ignored.
		OctaveModel[] octaveModels =
				new OctaveModel[newSettings.octaves];
		for (int i = 0; i < newSettings.octaves; i++)
		{
			octaveModels[i] = 
					topPanel.octaveGUIs[i].getOctaveModel();
		}

		GradientModel gradientModel =
				reviseGradientGUIModel(
					topPanel.mixerGUI.getGradientGUI().getModel(),
					topPanel.getAppSettings(), 
					newSettings);	

		NoiseData gradientData = 
				GradientGUI.createGradientFunctionData(
						newSettings.width,
						newSettings.height,
						gradientModel);
		
		MixerModel newMixerModel = 
				MixerModel.updateMixSetting(
						topPanel.mixerGUI.getMixerModel(), 
						MixerModel.Fields.GRADIENT_DATA, 
						gradientData);
		
		GUITextureModel sivi = new GUITextureModel(
				newSettings, 
				octaveModels, 
				newMixerModel,
				gradientModel,
				null);
		
		topPanel.setAppSettings(sivi.appSettings);
		topPanel.loadOctavesPanel(sivi.octaveModels);
		topPanel.updateMixerGUI(new MixerGUI(
				topPanel, sivi.mixerModel, 
				sivi.gradientModel));
		topPanel.updateFinalDisplay(
				new FinalDisplay(sivi.appSettings));
		
		topPanel.updateOctaveDisplays();
		
	}
	
}
