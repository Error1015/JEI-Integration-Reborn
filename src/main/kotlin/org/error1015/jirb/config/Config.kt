package org.error1015.jirb.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.error1015.jirb.getValue

object Config {
    val spec: ModConfigSpec
    private val builder = ModConfigSpec.Builder()

    val modConfig: JEIIntegrationConfig

    object JEIIntegrationConfig {
        val burnTimeTooltipMode by builder.defineEnum("burnTimeTooltip", ConfigState.ENABLE)
        val durabilityTooltipMode by builder.defineEnum("durabilityTooltipMode", ConfigState.ENABLE)
        val enchantabilityTooltipMode by builder.defineEnum("enchantabilityTooltipMode", ConfigState.DISABLE)
        val foodTooltipMode by builder.defineEnum("foodTooltipMode", ConfigState.SHIFT)
        val registryNameMode by builder.defineEnum("registryNameMode", ConfigState.SHIFT_AND_DEBUG)
        val maxStackSizeTooltipMode by builder.defineEnum("maxStackSizeTooltipMode", ConfigState.SHIFT_AND_DEBUG)
        val tagsTooltipMode by builder.defineEnum("tagsTooltipMode", ConfigState.SHIFT)
        val translationKeyTooltipMode by builder.defineEnum("translationKeyTooltipMode", ConfigState.SHIFT_AND_DEBUG)
        val dataComponentsMode by builder.defineEnum("dataComponentsMode", ConfigState.SHIFT_AND_DEBUG)
    }

    init {
        builder.apply {
            push("Tooltip_Options")
            modConfig = JEIIntegrationConfig
            pop()
            spec = build()
        }
    }
}