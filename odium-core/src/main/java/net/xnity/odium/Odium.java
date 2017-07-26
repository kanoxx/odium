/*
 * This file is part of Odium, licensed under the MIT License (MIT).
 *
 * Copyright (C) 2017 Lukas Nehrke
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

package net.xnity.odium;

import java.util.Optional;
import javax.annotation.Nullable;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.inventory.ItemStack;

public final class Odium {

  private static final String PATH = "net.xnity.odium.%s.OdiumImpl";
  private static final IOdium NONE = new OdiumImpl();

  @Nullable
  private static IOdium odium;

  static {
    final String server = Bukkit.getServer().getClass().getPackage().getName();
    final String version = server.substring(server.lastIndexOf('.') + 1);
    try {
      odium = (IOdium) Class.forName(String.format(PATH, version)).newInstance();
    } catch (Throwable throwable) {
      odium = NONE;
      throwable.printStackTrace();
    }
  }

  private Odium() {}

  private static Optional<IOdium> getOdium() {
    return Optional.ofNullable(odium);
  }

  public static Optional<CommandMap> getCommandMap() {
    return getOdium().map(o -> o.getCommandMap().orElse(null));
  }

  public static Optional<String> itemToJson(ItemStack itemStack) {
    return getOdium().map(o -> o.itemToJson(itemStack).orElse(null));
  }
}
