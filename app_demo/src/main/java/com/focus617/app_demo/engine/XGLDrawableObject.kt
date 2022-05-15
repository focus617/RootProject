package com.focus617.app_demo.engine

import com.focus617.core.engine.objects.DrawableObject

abstract class XGLDrawableObject : DrawableObject() {
    abstract fun initOpenGlResource()
}