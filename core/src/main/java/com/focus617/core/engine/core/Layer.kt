package com.focus617.core.engine.core

import com.focus617.core.engine.objects.DrawableObject
import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.base.Event
import java.io.Closeable

abstract class Layer(
    protected var mDebugName: String = "Layer"
) : BaseEntity(), Closeable {

    val gameObjectList = mutableListOf<DrawableObject>()

    //init {
        //gameObjectList.add(Triangle())
        //gameObjectList.add(Circle(1.0f))
        //gameObjectList.add(Cube())
        //gameObjectList.add(Cone(1.0f, 1.0f))
        //gameObjectList.add(Cylinder(1.0f, 1.0f))
        //gameObjectList.add(Ball(1.0f))
        //gameObjectList.add(Star(5, 0.38f, 1.0f, 0.5f))
    //}

    abstract fun onAttach()
    abstract fun onDetach()

    // 负责更新 本Layer 负责管理的Game Objects
    abstract fun onUpdate(timeStep: TimeStep)

    // 负责接收事件，并完成对应的转发
    abstract fun onEvent(event: Event): Boolean

    // 需要在EGL环境下进行初始化的资源
    abstract fun initOpenGlResource()
}