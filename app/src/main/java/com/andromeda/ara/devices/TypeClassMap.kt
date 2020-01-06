package com.andromeda.ara.devices

class TypeClassMap {
    fun main(type:String): Class<Any> {
        val map = mapOf(
            DeviceConst.LIGHT to LightStatusModel::class.java,
            DeviceConst.TEMP to TempModel::class.java
        )
        return map[type] as Class<Any>? ?: error("")
    }
}