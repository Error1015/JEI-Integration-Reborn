package org.error1015.jirb

import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.neoforged.neoforge.common.ModConfigSpec
import org.error1015.jirb.config.ConfigState
import kotlin.reflect.KProperty

inline val minecraft: Minecraft inline get() = Minecraft.getInstance()

infix operator fun Component.plus(another: Component): MutableComponent = this.copy().append(another)

internal inline val String.asTranslatable: MutableComponent
    inline get() = Component.translatable("tooltip.$ID.$this")

fun MutableComponent.setColor(formatting: ChatFormatting): MutableComponent = this.withStyle(Style.EMPTY.withColor(formatting))

fun MutableComponent.setDarkGray(): MutableComponent = setColor(ChatFormatting.DARK_GRAY)

val String.toLiteral: MutableComponent
    inline get() = Component.literal(this)

operator fun ModConfigSpec.ConfigValue<ConfigState>.getValue(
    any: Any?,
    property: KProperty<*>
): ConfigState = get()