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

package net.xnity.odium.v1_11_R1;

import java.util.Optional;
import net.minecraft.server.v1_11_R1.IChatBaseComponent;
import net.minecraft.server.v1_11_R1.NBTTagCompound;
import net.minecraft.server.v1_11_R1.Packet;
import net.minecraft.server.v1_11_R1.PacketPlayOutChat;
import net.xnity.odium.IOdium;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_11_R1.CraftServer;
import org.bukkit.craftbukkit.v1_11_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_11_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class OdiumImpl implements IOdium {

  @Override
  public Optional<CommandMap> getCommandMap() {
    return Optional.of(((CraftServer) Bukkit.getServer()).getCommandMap());
  }

  @Override
  public Optional<String> itemToJson(ItemStack itemStack) {
    return Optional.ofNullable(CraftItemStack.asNMSCopy(itemStack).save(new NBTTagCompound()).toString());
  }

  private static void send(Player player, Packet packet) throws Exception {
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
  }

  private static byte convert(net.xnity.odium.ChatMessageType type) {
    switch (type) {
      case ACTION_BAR:
        return (byte) 2;
      case SYSTEM:
        return (byte) 1;
      default:
        return (byte) 0;
    }
  }

  @Override
  public boolean sendJsonMessage(Player player, String json, net.xnity.odium.ChatMessageType type) {
    try {
      send(player, new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a(json), convert(type)));
      return true;
    } catch (Throwable throwable) {
      return false;
    }
  }
}
