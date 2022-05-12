package com.focus617.app_demo.renderer

import android.media.effect.EffectContext
import android.media.effect.EffectFactory
import com.focus617.core.engine.renderer.texture.Texture2D
import com.focus617.core.platform.base.BaseEntity


/**
 * This class is responsible for managing the information about the visual effects
 * inside an OpenGL context.
 */
object XGLRenderEffect : BaseEntity() {

    private var effectContext: EffectContext? = null

    fun initUnderOpenGl() {
        if (effectContext == null) {
            effectContext = EffectContext.createWithCurrentGlContext()
        }
    }

    // outputTexture2D must be a blank Texture2D object, which will contain the result of effect
    private fun grayScaleEffect(inputTexture2D: Texture2D, outputTexture2D: Texture2D) {
        effectContext?.apply {
            val effect = factory.createEffect(EffectFactory.EFFECT_GRAYSCALE)
            effect.apply(
                inputTexture2D.mHandle,
                inputTexture2D.mWidth,
                inputTexture2D.mHeight,
                outputTexture2D.mHandle
            )
            effect.release()
        }
    }
}