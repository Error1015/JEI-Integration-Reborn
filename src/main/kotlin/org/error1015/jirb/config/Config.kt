package org.error1015.jirb.config

import net.neoforged.neoforge.common.ModConfigSpec
import org.error1015.jirb.getValue

object Config {
    val spec: ModConfigSpec
    private val builder = ModConfigSpec.Builder()

    val modConfig: JEIIntegrationConfig

    object JEIIntegrationConfig {
        val burnTimeTooltipMode by builder.defineEnum("burnTimeTooltip", ConfigState.DISABLE)
        val durabilityTooltipMode by builder.defineEnum("durabilityTooltipMode", ConfigState.DISABLE)
        val enchantabilityTooltipMode by builder.defineEnum("enchantabilityTooltipMode", ConfigState.DISABLE)
        val foodTooltipMode by builder.defineEnum("foodTooltipMode", ConfigState.DISABLE)
        val registryNameMode by builder.defineEnum("registryNameMode", ConfigState.DISABLE)
        val maxStackSizeTooltipMode by builder.defineEnum("maxStackSizeTooltipMode", ConfigState.DISABLE)
        val tagsTooltipMode by builder.defineEnum("tagsTooltipMode", ConfigState.DISABLE)
        val translationKeyTooltipMode by builder.defineEnum("translationKeyTooltipMode", ConfigState.DISABLE)
        val dataComponentsMode by builder.defineEnum("dataComponentsMode", ConfigState.DISABLE)
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