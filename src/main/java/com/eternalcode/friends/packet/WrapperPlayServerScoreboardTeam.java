package com.eternalcode.friends.packet;

/**
 * PacketWrapper - ProtocolLib wrappers for Minecraft packets
 * Copyright (C) dmulloy2 <http://dmulloy2.net>
 * Copyright (C) Kristian S. Strangeland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.utility.MinecraftReflection;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import org.bukkit.ChatColor;

public class WrapperPlayServerScoreboardTeam extends AbstractPacket {

    private final static ChatColor FRIEND_COLOR = ChatColor.BLUE;
    public static final PacketType TYPE =
            PacketType.Play.Server.SCOREBOARD_TEAM;

    public WrapperPlayServerScoreboardTeam() {
        super(new PacketContainer(TYPE), TYPE);
        handle.getModifier().writeDefaults();
    }

    public WrapperPlayServerScoreboardTeam(PacketContainer packet) {
        super(packet, TYPE);
    }

    public String getName() {
        return handle.getStrings().read(0);
    }

    public WrapperPlayServerScoreboardTeam setName(String value) {
        handle.getStrings().write(0, value);
        return this;
    }

    public Optional<WrappedChatComponent> getDisplayName() {
        return handle.getOptionalStructures().read(0).map((structure) -> structure.getChatComponents().read(0));
    }

    public WrapperPlayServerScoreboardTeam setDisplayName(WrappedChatComponent value) {
        handle.getOptionalStructures().read(0).map((structure) -> structure.getChatComponents().write(0, value));
        return this;
    }

    public Optional<WrappedChatComponent> getPrefix() {
        return handle.getOptionalStructures().read(0).map((structure) -> structure.getChatComponents().read(1));
    }

    public WrapperPlayServerScoreboardTeam setPrefix(WrappedChatComponent value) {
        handle.getOptionalStructures().read(0).map((structure) -> structure.getChatComponents().write(1, value));
        return this;
    }

    public Optional<WrappedChatComponent> getSuffix() {
        return handle.getOptionalStructures().read(0).map((structure) -> structure.getChatComponents().read(2));
    }

    public WrapperPlayServerScoreboardTeam setSuffix(WrappedChatComponent value) {
        handle.getOptionalStructures().read(0).map((structure) -> structure.getChatComponents().write(2, value));
        return this;
    }

    public Optional<String> getNameTagVisibility() {
        return handle.getOptionalStructures().read(0).map((structure) -> structure.getStrings().read(0));
    }

    public WrapperPlayServerScoreboardTeam setNameTagVisibility(String value) {
        handle.getOptionalStructures().read(0).map((structure) -> structure.getStrings().write(0, value));
        return this;
    }

    public Optional<ChatColor> getColor() {
        return handle.getOptionalStructures().read(0).map((structure) -> structure.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat")).read(0));
    }

    public WrapperPlayServerScoreboardTeam setColor(ChatColor value) {
        handle.getOptionalStructures().read(0).map((structure) -> structure.getEnumModifier(ChatColor.class, MinecraftReflection.getMinecraftClass("EnumChatFormat")).write(0, value));
        return this;
    }

    public Optional<String> getCollisionRule() {
        return handle.getOptionalStructures().read(0).map((structure) -> structure.getStrings().read(1));
    }

    public WrapperPlayServerScoreboardTeam setCollisionRule(String value) {
        handle.getOptionalStructures().read(0).map((structure) -> structure.getStrings().write(1, value));
        return this;
    }

    @SuppressWarnings("unchecked")
    public List<String> getPlayers() {
        return (List<String>) handle.getSpecificModifier(Collection.class)
                .read(0);
    }

    public WrapperPlayServerScoreboardTeam setPlayers(List<String> value) {
        handle.getSpecificModifier(Collection.class).write(0, value);
        return this;
    }

    public int getMode() {
        return handle.getIntegers().read(0);
    }

    public WrapperPlayServerScoreboardTeam setMode(int value) {
        handle.getIntegers().write(0, value);
        return this;
    }

    public int getPackOptionData() {
        return handle.getIntegers().read(1);
    }

    public WrapperPlayServerScoreboardTeam setPackOptionData(int value) {
        handle.getIntegers().write(1, value);
        return this;
    }
}
