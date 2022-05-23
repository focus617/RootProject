package com.focus617.core.ecs.mine.system

import com.focus617.core.ecs.fleks.AnyOf
import com.focus617.core.ecs.fleks.Entity
import com.focus617.core.ecs.fleks.IteratingSystem
import com.focus617.core.ecs.mine.component.Camera
import com.focus617.core.ecs.mine.component.CameraController
import com.focus617.core.ecs.mine.component.CameraProjectionMatrix
import com.focus617.core.ecs.mine.component.CameraViewMatrix

@AnyOf([Camera::class, CameraController::class, CameraProjectionMatrix::class, CameraViewMatrix::class])
class CameraSystem : IteratingSystem() {

    override fun onTickEntity(entity: Entity) {

        TODO("Not yet implemented")
    }
}