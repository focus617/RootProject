package com.focus617.core.engine.scene_graph

open class GameObject {
    private val children: MutableList<GameObject> = mutableListOf()
    private val components: MutableList<IfGameComponent> = mutableListOf()
    private val transform: Transform = Transform()

    fun addChild(child: GameObject){
        children.add(child)
    }

    fun addComponent(component: IfGameComponent){
        components.add(component)
    }

    open fun input(){
        components.forEach{
            it.input(transform)
        }
        children.forEach{
            it.input()
        }
    }

    open fun update(){
        components.forEach{
            it.update(transform)
        }
        children.forEach{
            it.update()
        }
    }

    open fun render(){
        components.forEach{
            it.render(transform)
        }
        children.forEach{
            it.render()
        }
    }

    fun getTransform() = transform

}