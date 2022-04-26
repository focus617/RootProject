package com.focus617.core.platform.event.base

enum class EventType(val typeId: Int) {
    All(0), None(1),    // Special type for test only

    AppTick(20), AppLaunched(21),
    AppUpdate(22), AppRender(23),

    WindowClose(100), WindowResize(101),
    WindowFocus(102), WindowLostFocus(103),
    WindowMoved(104),

    KeyPressed(300), KeyReleased(301),

    MouseButtonPressed(400), MouseButtonReleased(401),
    MouseMoved(402), MouseScrolled(403),

    TouchPress(500), TouchDrag(501),
    PinchStart(520), Pinch(521), PinchEnd(522)
}
