package org.error1015.jirb

import net.minecraft.ChatFormatting
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.neoforged.neoforge.common.ModConfigSpec
import org.error1015.jirb.config.ConfigState
import kotlin.reflect.KProperty

val minecraft: Minecraft get() = Minecraft.getInstance()

val window get() = minecraft.window.window

infix operator fun Component.plus(another: Component): MutableComponent = this.copy().append(another)

val String.asTranslatable: MutableComponent
    get() = Component.translatable("tooltip.$ID.$this")

fun MutableComponent.setColor(formatting: ChatFormatting): MutableComponent =this.withStyle(Style.EMPTY.withColor(formatting))

fun MutableComponent.setDarkGray(): MutableComponent = setColor(ChatFormatting.DARK_GRAY)

val String.asLiteral: MutableComponent
    get() = Component.literal(this)

operator fun ModConfigSpec.ConfigValue<ConfigState>.getValue(
    any: Any?,
    property: KProperty<*>
): ConfigState {
    return this.get()
}