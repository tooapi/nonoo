package com.fdp.nonoo.common;

import java.awt.Color;
import java.awt.Font;

import com.octo.captcha.component.image.backgroundgenerator.FunkyBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.ComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

public class CaptchaEngine extends ListImageCaptchaEngine {

	private static final String randomWord = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	private static final Font[] font = { new Font("nyala", 1, 24),
			new Font("Arial", 1, 24), new Font("nyala", 1, 24),
			new Font("Bell", 1, 24), new Font("Bell MT", 1, 24),
			new Font("Credit", 1, 24), new Font("valley", 1, 24),
			new Font("Impact", 1, 24) };
	private static final Color[] randomColor = { new Color(255, 255, 255),
			new Color(255, 220, 220), new Color(220, 255, 255),
			new Color(220, 220, 255), new Color(255, 255, 220),
			new Color(220, 255, 220) };

	protected void buildInitialFactories() {
		RandomFontGenerator fontGenerator = new RandomFontGenerator(24, 32,
				font);
		FunkyBackgroundGenerator backgroundGenerator = new FunkyBackgroundGenerator(
				100, 50);
		DecoratedRandomTextPaster textPaster = new DecoratedRandomTextPaster(4,
				4, new RandomListColorGenerator(randomColor),
				new TextDecorator[0]);
		addFactory(new GimpyFactory(new RandomWordGenerator(randomWord),
				new ComposedWordToImage(fontGenerator, backgroundGenerator,
						textPaster)));
	}
}