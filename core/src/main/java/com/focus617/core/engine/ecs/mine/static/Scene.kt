package com.focus617.core.engine.ecs.mine.static

import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.fleks.EntityCreateCfg
import com.focus617.core.engine.ecs.fleks.World
import com.focus617.core.engine.ecs.mine.component.CameraMatrix
import com.focus617.core.engine.ecs.mine.component.PerspectiveCameraCmp
import com.focus617.core.engine.ecs.mine.component.Relationship
import com.focus617.core.engine.ecs.mine.component.Tag
import com.focus617.core.engine.ecs.mine.system.OrthographicCameraSystem
import com.focus617.core.engine.ecs.mine.system.PerspectiveCameraSystem
import com.focus617.core.engine.ecs.mine.system.PhysicsSystem
import com.focus617.core.engine.ecs.mine.system.Render2DSystem
import com.focus617.core.platform.base.BaseEntity

object Scene : BaseEntity() {

    private val world = World {
        entityCapacity = 600

        system<PerspectiveCameraSystem>()
        system<OrthographicCameraSystem>()

        system<PhysicsSystem>()
        system<Render2DSystem>()
    }

    private var sceneEntity: Entity =
        world.entity {
            add<Tag> { tag = "Scene" }
            add<Relationship>()
        }
    // Scene is always the first entity(id=0)

    val id: Int = sceneEntity.id

    fun world() = world
    fun entity() = sceneEntity

    /** ++++++++ ECS的公共组件 ++++++++++ */
    val camera: Entity

    init {
        LOG.info("Scene(id=${this.id}) initialized.")

        camera = world.entity {
            add<Tag> { tag = "SceneCamera" }
            add<PerspectiveCameraCmp>()
            add<CameraMatrix>()
            add<Relationship>()
        }
        camera.setParent(sceneEntity)
    }

    const val DefaultTag = "Entity"

    /** ++++++++ 构建 ECS ++++++++++ */
    fun createEntity(
        name: String = "",
        parent: Entity = sceneEntity,
        configuration: EntityCreateCfg.(Entity) -> Unit = {}
    ): Entity {
        val entity = world.entity(configuration)
        entity.addOrUpdateComponent<Tag> {
            tag = name.ifEmpty { DefaultTag }
        }
        entity.addOrUpdateComponent<Relationship>()
        entity.setParent(parent)
        return entity
    }


}