package com.qtech.bubbles.graphics

interface ITexture {
    fun render(gg: GraphicsProcessor?)
    fun width(): Int
    fun height(): Int
}