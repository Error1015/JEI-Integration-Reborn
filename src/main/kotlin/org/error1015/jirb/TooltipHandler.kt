package org.error1015.jirb

import net.minecraft.ChatFormatting
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent
import org.error1015.jirb.config.Config
import org.error1015.jirb.config.ConfigState
import org.error1015.jirb.config.isEnabled
import java.text.DecimalFormat

@EventBusSubscriber(value = [Dist.CLIENT])
object TooltipHandler {
    val formatter = DecimalFormat("#.##").apply {
        isGroupingUsed = true
        groupingSize = 3
    }

    @SubscribeEvent
    fun onTooltipEvent(event: ItemTooltipEvent) {
        val stack = event.itemStack ?: return
        val item = stack.item ?: return
        if (event.itemStack.isEmpty) return

        val maxDamage = stack.maxDamage
        val currentDamage: Int = maxDamage - stack.damageValue
        val enchantability = stack.enchantmentValue

        with(Config.modConfig) {
            // Tooltip - Burn Time
            showBurnTime(stack, event)

            // Tooltip - Durability
            showDurability(maxDamage, currentDamage, event)

            // Tooltip - Enchantability
            showEnchantability(enchantability, event)

            // Tooltip - Hunger / Saturation
            showHungerAndSaturation(stack, event)

            // Tooltip - DataComponent
            showDataComponent(stack, event)

            // Tooltip - Registry Name
            showRegistryName(item, event)

            // Tooltip - Max Stack Size
            showMaxStackSize(stack, event)

            // Tooltip - Tags
            showTags(stack, event)

            // Tooltip - Translation Key
            showTranslatioonKey(stack, event)
        }

    }

    private fun Config.JEIIntegrationConfig.showTranslatioonKey(stack: ItemStack, event: ItemTooltipEvent) {
        ("translationKey".asTranslatable + " ${stack.descriptionId}".toLiteral).setDarkGray().let { translationKeyTooltip ->
            event.addTooltip(translationKeyTooltip, translationKeyTooltipMode)
        }
    }

    private fun Config.JEIIntegrationConfig.showTags(stack: ItemStack, event: ItemTooltipEvent) {
        if (stack.tags.count() > 0) {
            val tagsTooltip = "tags".asTranslatable.setDarkGray()
            val tags = mutableSetOf<Component>().apply {
                for (tag: ResourceLocation in stack.tags.map { it.location }.toList()) {
                    val component = " $tag".toLiteral.setDarkGray()
                    this@apply.add(component)
                }
            }
            event.addTooltip(tagsTooltip, tagsTooltipMode)
            event.addTooltips(tags, tagsTooltipMode)
        }
    }

    private fun Config.JEIIntegrationConfig.showMaxStackSize(stack: ItemStack, event: ItemTooltipEvent) {
        stack.maxStackSize.apply {
            if (this > 0) {
                val maxStackSizeTooltip = ("maxStackSize".asTranslatable + " $this".toLiteral).setDarkGray()
                event.addTooltip(maxStackSizeTooltip, maxStackSizeTooltipMode)
            }
        }
    }

    private fun Config.JEIIntegrationConfig.showRegistryName(item: Item, event: ItemTooltipEvent) {
        ("registryName".asTranslatable + " ${BuiltInRegistries.ITEM.getKey(item)}".toLiteral).setDarkGray().let { registryName ->
            event.addTooltip(registryName, registryNameMode)
        }
    }

    private fun Config.JEIIntegrationConfig.showHungerAndSaturation(stack: ItemStack, event: ItemTooltipEvent) {
        stack.getFoodProperties(minecraft.player)?.apply {
            val foodTooltip = ("hunger".asTranslatable + " $nutrition ".toLiteral + "saturation".asTranslatable + " ${formatter.format(saturation)}".toLiteral).setDarkGray()
            event.addTooltip(foodTooltip, foodTooltipMode)
        }
    }

    private fun Config.JEIIntegrationConfig.showEnchantability(enchantability: Int, event: ItemTooltipEvent) {
        if (enchantability > 0) {
            val enchantabilityTooltip = ("enchantability".asTranslatable + " $enchantability".toLiteral).setDarkGray()
            event.addTooltip(enchantabilityTooltip, enchantabilityTooltipMode)
        }
    }

    private fun Config.JEIIntegrationConfig.showDurability(maxDamage: Int, currentDamage: Int, event: ItemTooltipEvent) {
        if (maxDamage > 0) {
            val durabilityTooltip = ("durability".asTranslatable + " ${formatter.format(currentDamage)}/$maxDamage".toLiteral).setDarkGray()
            event.addTooltip(durabilityTooltip, durabilityTooltipMode)
        }
    }

    private fun Config.JEIIntegrationConfig.showBurnTime(stack: ItemStack, event: ItemTooltipEvent) {
        try {
            stack.getBurnTime(RecipeType.SMELTING)
        } catch (_: Exception) {
            logger.warn("Get item burn time failed")
            0
        }.let { burnTime ->
            if (burnTime > 0) {
                val burnTooltip = ("burnTime".asTranslatable + " ${formatter.format(burnTime)} ".toLiteral + "burnTime.suffix".asTranslatable).setDarkGray()
                event.addTooltip(burnTooltip, burnTimeTooltipMode)
            }
        }
    }

    private fun Config.JEIIntegrationConfig.showDataComponent(stack: ItemStack, event: ItemTooltipEvent) {
        stack.components.apply {
            if (this.size() > 0) {
                val components = stack.components ?: return
                val dataComponentsTooltip = ("data_components".asTranslatable + " $components".toLiteral).setColor(ChatFormatting.GREEN)
                event.addTooltip(dataComponentsTooltip, dataComponentsMode)
            }
        }
    }

}


// f3 + h 调试模式状态
val advancedMode = minecraft.options.advancedItemTooltips

private fun ItemTooltipEvent.addTooltip(
    tooltip: Component, state: ConfigState
) {
    if (state.isEnabled(flags.hasShiftDown(), advancedMode)) {
        toolTip.add(tooltip)
    }
}

private fun ItemTooltipEvent.addTooltips(
    tooltips: Collection<Component>, state: ConfigState
) {
    for (component in tooltips) {
        addTooltip(component, state)
    }
}