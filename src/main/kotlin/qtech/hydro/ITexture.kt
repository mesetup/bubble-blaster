package qtech.hydro

interface ITexture {
    fun render(gg: GraphicsProcessor)
    fun width(): Int
    fun height(): Int
}