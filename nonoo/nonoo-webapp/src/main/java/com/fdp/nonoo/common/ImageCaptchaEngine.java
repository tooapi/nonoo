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

public class ImageCaptchaEngine extends ListImageCaptchaEngine {

	private static final int DEFAULT_WIDTH = 80;
	private static final int DEFAULT_HEIGHT = 28;
	private static final int MIN_FONT_SIZE= 12;
	private static final int MAX_FONT_SIZE = 16;
	private static final int MIN_ACCEPTED_WORD_LENGTH = 4;
	private static final int MAX_ACCEPTED_WORD_LENGTH = 4;
	private static final String RANDOM_WORD = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	private static final Font[] RANDOM_FONT = { new Font("nyala", 1, MAX_FONT_SIZE),
			new Font("Arial", 1, MAX_FONT_SIZE), new Font("nyala", 1, MAX_FONT_SIZE),
			new Font("Bell", 1, MAX_FONT_SIZE), new Font("Bell MT", 1, MAX_FONT_SIZE),
			new Font("Credit", 1, MAX_FONT_SIZE), new Font("valley", 1, MAX_FONT_SIZE),
			new Font("Impact", 1, MAX_FONT_SIZE) };
	private static final Color[] RANDOM_COLOR = { new Color(255, 255, 255),
			new Color(255, 220, 220), new Color(220, 255, 255),
			new Color(220, 220, 255), new Color(255, 255, 220),
			new Color(220, 255, 220) };

	protected void buildInitialFactories() {
		RandomFontGenerator fontGenerator = new RandomFontGenerator(MIN_FONT_SIZE, MAX_FONT_SIZE, RANDOM_FONT);
		FunkyBackgroundGenerator backgroundGenerator = new FunkyBackgroundGenerator(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		DecoratedRandomTextPaster randomTextPaster = new DecoratedRandomTextPaster(MIN_ACCEPTED_WORD_LENGTH, MAX_ACCEPTED_WORD_LENGTH,new RandomListColorGenerator(RANDOM_COLOR), new TextDecorator[0]);
		addFactory(new GimpyFactory(new RandomWordGenerator(RANDOM_WORD), new ComposedWordToImage(fontGenerator, backgroundGenerator, randomTextPaster)));
	}
}
