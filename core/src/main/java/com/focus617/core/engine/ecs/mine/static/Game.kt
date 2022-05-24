package com.focus617.core.engine.ecs.mine.static

import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.fleks.World
import com.focus617.core.engine.ecs.mine.component.CameraMatrix
import com.focus617.core.engine.ecs.mine.component.Relationship
import com.focus617.core.engine.ecs.mine.system.PerspectiveCameraSystem
import com.focus617.core.engine.ecs.mine.system.RenderSystem

object Game {
    val world = World {
        entityCapacity = 600

        system<PerspectiveCameraSystem>()
        system<RenderSystem>()
    }

    /** ++++++++ ECS的公共组件 ++++++++++ */
    val scene: Entity
    val camera: Entity

    /** ++++++++ 构建 ECS ++++++++++ */
    init {
        scene = world.entity {
            add<Relationship>()
        }

        camera = world.entity {
            add<CameraMatrix>()
            add<Relationship>()
        }
        camera.setParent(scene)
    }
}