package org.error1015.jirb

import com.mojang.blaze3d.platform.InputConstants
import net.minecraft.ChatFormatting
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import org.error1015.jirb.config.Config
import org.error1015.jirb.config.ConfigState
import org.lwjgl.glfw.GLFW
import java.text.DecimalFormat

@EventBusSubscriber(value = [Dist.CLIENT])
object TooltipHandler {
    val config inline get() = Config.modConfig

    private fun isShiftKeyDown() = InputConstants.isKeyDown(window, GLFW.GLFW_KEY_LEFT_SHIFT) || InputConstants.isKeyDown(window, GLFW.GLFW_KEY_RIGHT_SHIFT)

    private fun isDebugMode() = minecraft.options.advancedItemTooltips

    @SubscribeEvent
    fun onTooltipEvent(event: ItemTooltipEvent) {
        val formatter = DecimalFormat("#.##").apply {
            isGroupingUsed = true
            groupingSize = 3
        }

        val stack = event.itemStack ?: return
        val item = stack.item ?: return
        if (event.itemStack.isEmpty) return

        // Tooltip - Burn Time
        try {
            stack.getBurnTime(RecipeType.SMELTING)
        } catch (_: Exception) {
            logger.warn("Get item burn time failed")
            0
        }.let { burnTime ->
            if (burnTime > 0) {
                val burnTooltip = ("burnTime".asTranslatable + " ${formatter.format(burnTime)} ".asLiteral + "burnTime.suffix".asTranslatable).setDarkGray()
                event.registerTooltip(burnTooltip, config.burnTimeTooltipMode)
            }
        }

        // Tooltip - Durability
        val maxDamage = stack.maxDamage
        val currentDamage: Int = maxDamage - stack.damageValue
        if (maxDamage > 0) {
            val durabilityTooltip = ("durability".asTranslatable + " ${formatter.format(currentDamage)}/$maxDamage".asLiteral).setDarkGray()
            event.registerTooltip(durabilityTooltip, config.durabilityTooltipMode)
        }

        // Tooltip - Enchantability
        val enchantability = stack.enchantmentValue
        if (enchantability > 0) {
            val enchantabilityTooltip = ("enchantability".asTranslatable + " $enchantability".asLiteral).setDarkGray()
            event.registerTooltip(enchantabilityTooltip, config.enchantabilityTooltipMode)
        }

        // Tooltip - Hunger / Saturation
        stack.getFoodProperties(minecraft.player)?.apply {
            val satValue = nutrition * saturation * 2
            val foodTooltip = ("hunger".asTranslatable + " $nutrition ".asLiteral + "saturation".asTranslatable + " ${formatter.format(satValue)}".asLiteral).setDarkGray()
            event.registerTooltip(foodTooltip, config.foodTooltipMode)
        }

        // Tooltip - DataComponent
        stack.components.apply {
            if (this.size() > 0) {
                val components = stack.components ?: return
                val dataComponentsTooltip = ("data_components".asTranslatable + " $components".asLiteral).setColor(ChatFormatting.GREEN)
                event.registerTooltip(dataComponentsTooltip, config.dataComponentsMode)
            }
        }


        // Tooltip - Registry Name
        ("registryName".asTranslatable + " ${BuiltInRegistries.ITEM.getKey(item)}".asLiteral).setDarkGray().let { registryName ->
            event.registerTooltip(registryName, config.registryNameMode)
        }

        // Tooltip - Max Stack Size
        stack.maxStackSize.apply {
            if (this > 0) {
                val maxStackSizeTooltip = ("maxStackSize".asTranslatable + " $this".asLiteral).setDarkGray()
                event.registerTooltip(maxStackSizeTooltip, config.maxStackSizeTooltipMode)
            }
        }

        // Tooltip - Tags
        if (stack.tags.count() > 0) {
            val tagsTooltip = "tags".asTranslatable.setDarkGray()
            val tags = mutableSetOf<Component>().apply {
                for (tag: ResourceLocation in stack.tags.map { it.location }.toList()) {
                    val component = " $tag".asLiteral.setDarkGray()
                    add(component)
                }
            }
            event.registerTooltip(tagsTooltip, config.tagsTooltipMode)
            event.registerTooltips(tags, config.tagsTooltipMode)
        }

        // Tooltip - Translation Key
        ("translationKey".asTranslatable + " ${stack.descriptionId}".asLiteral).setDarkGray().let { translationKeyTooltip ->
            event.registerTooltip(translationKeyTooltip, config.translationKeyTooltipMode)
        }
    }

    private fun ItemTooltipEvent.registerTooltip(
        tooltip: Component,
        state: ConfigState
    ) {
        val isEnabled = when (state) {
            ConfigState.DISABLE -> false
            ConfigState.ENABLE -> true
            ConfigState.SHIFT -> isShiftKeyDown()
            ConfigState.DEBUG -> isDebugMode()
            ConfigState.SHIFT_AND_DEBUG -> isShiftKeyDown() && isDebugMode()
        }
        if (isEnabled) {
            toolTip.add(tooltip)
        }
    }

    private fun ItemTooltipEvent.registerTooltips(
        tooltips: Collection<Component>,
        state: ConfigState
    ) {
        for (component in tooltips) {
            registerTooltip(component, state)
        }
    }
}