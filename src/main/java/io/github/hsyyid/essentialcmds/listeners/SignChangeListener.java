/*
 * This file is part of EssentialCmds, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2015 - 2015 HassanS6000
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.hsyyid.essentialcmds.listeners;

import io.github.hsyyid.essentialcmds.utils.Utils;

import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.tileentity.SignData;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.tileentity.ChangeSignEvent;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

public class SignChangeListener
{
	@Listener
	public void onSignChange(ChangeSignEvent event, @First Player player)
	{
		SignData signData = event.getText();

		if (signData.getValue(Keys.SIGN_LINES).isPresent())
		{
			String line0 = signData.getValue(Keys.SIGN_LINES).get().get(0).toPlain();
			String line1 = signData.getValue(Keys.SIGN_LINES).get().get(1).toPlain();
			String line2 = signData.getValue(Keys.SIGN_LINES).get().get(2).toPlain();
			String line3 = signData.getValue(Keys.SIGN_LINES).get().get(3).toPlain();

			if (line0.equals("[Warp]"))
			{
				if (Utils.getWarps().contains(line1))
				{
					signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(0, Text.of(TextColors.DARK_BLUE, "[Warp]")));
				}
				else
				{
					signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(0, Text.of(TextColors.DARK_RED, "[Warp]")));
				}
			}
			else if (player != null && player.hasPermission("essentialcmds.color.sign.use"))
			{
				signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(0, TextSerializers.formattingCode('&').deserialize(line0)));
				signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(1, TextSerializers.formattingCode('&').deserialize(line1)));
				signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(2, TextSerializers.formattingCode('&').deserialize(line2)));
				signData = signData.set(signData.getValue(Keys.SIGN_LINES).get().set(3, TextSerializers.formattingCode('&').deserialize(line3)));
			}
		}
	}
}
