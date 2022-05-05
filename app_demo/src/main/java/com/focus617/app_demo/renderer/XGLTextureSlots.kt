package com.focus617.app_demo.renderer

import com.focus617.core.engine.renderer.Texture
import com.focus617.core.engine.renderer.TextureSlots

object XGLTextureSlots : TextureSlots() {

    override val MaxTextureSlots: Int
        get() = 16   //Opengl es can support up to 16 samplers

    override val TextureSlots: Array<Texture?> = arrayOfNulls(MaxTextureSlots)

}