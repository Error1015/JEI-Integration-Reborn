package org.error1015.jirb

import net.neoforged.fml.common.Mod
import net.neoforged.fml.config.ModConfig
import org.error1015.jirb.config.Config
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT

const val ID = "jei_integration_reborn"
val logger: Logger = LoggerFactory.getLogger(ID)

@Mod(ID)
object JEIIntegrationReborn {
    init {
        val modContainer = LOADING_CONTEXT.activeContainer
        modContainer?.registerConfig(ModConfig.Type.CLIENT, Config.spec)
    }
}