import tanks.Direction
import tanks.Drawing
import tanks.Game
import tanks.ItemDrop
import tanks.bullet.DefaultItems
import tanks.item.Item
import tanks.item.ItemBullet
import tanks.network.event.EventItemDrop
import tanks.network.event.EventObstacleDestroy
import tanks.obstacle.Obstacle
import tanks.tankson.MetadataProperty
import kotlin.io.encoding.Base64
import kotlin.math.max
import kotlin.text.Charsets.UTF_8

val DEFAULT_ITEM_STACK: Item.ItemStack<*> = ItemBullet.ItemStackBullet(null, DefaultItems.basic_bullet, 5)

class ObstacleItemDrop(name: String, x: Double, y: Double) : Obstacle(name, x, y) {
    @field:MetadataProperty(
        id = "item",
        name = "Item",
        selector = "item",
        keybind = "game.zoom",
        image = "../item.png"
    )
    @JvmField
    var item: Item.ItemStack<*> = DEFAULT_ITEM_STACK
    var sizeBag = 50.0
    var sizeItem = 50.0

    init {
        primaryMetadataID = "item"
//        secondaryMetadataID = "team"
        posZ = -5.0
        sizeBag = size * 1.5
        sizeItem = size * 0.75
        tankCollision = false
        bulletCollision = false
        replaceTiles = false
        type = ObstacleType.extra
        description = "im an item drop, tanks that drive over me will pick me up!!!"
        batchDraw = false
        setUpdate(true)
    }

    override fun draw() {
        Drawing.drawing.setColor(255.0, 255.0, 255.0)
        if (Game.enable3d) {
            if (posZ < 0) {
                val s = size * 0.75
                for (i in 0..8) {
                    posZ = max(
                        posZ,
                        Game.sampleGroundHeight(posX + Direction.X[i] * s, posY + Direction.Y[i] * s)
                    )
                }
                posZ = max(posZ, Game.sampleGroundHeight(posX, posY))
            }

            Drawing.drawing.drawImage("item.png", posX, posY, posZ, sizeBag, sizeBag)
            Drawing.drawing.drawImage(item.item.icon, posX, calcItemY(), posZ + 9, sizeItem, sizeItem)
        } else {
            Drawing.drawing.drawImage("item.png", posX, posY, sizeBag, sizeBag)
            Drawing.drawing.drawImage(item.item.icon, posX, calcItemY(), sizeItem, sizeItem)
        }
    }

    private fun calcItemY(py: Double = posY): Double {
        return py + sizeBag * 0.15
    }

    override fun update() {
        Game.removeObstacles.add(this)
        val id = ItemDrop(posX, posY, item)
        Game.movables.add(id)
        Game.eventsOut.add(EventItemDrop(id))
        Game.eventsOut.add(EventObstacleDestroy(posX, posY, name))
        HAS_ITEM_DROPS = true
    }

    override fun drawForInterface(x: Double, y: Double) {
        Drawing.drawing.setColor(255.0, 255.0, 255.0)
        Drawing.drawing.drawInterfaceImage("item.png", x, y, sizeBag, sizeBag)
        Drawing.drawing.drawInterfaceImage(item.item.icon, x, calcItemY(y), sizeItem, sizeItem)
    }

    override fun draw3dOutline(r: Double, g: Double, b: Double, a: Double) {
        Drawing.drawing.setColor(r, g, b, a)
        Drawing.drawing.drawImage("item.png", posX, posY, sizeBag, sizeBag)
        Drawing.drawing.drawImage(item.item.icon, posX, calcItemY(), sizeItem, sizeItem)
    }

    override fun setMetadata(meta: String) {
        item = Item.ItemStack.fromString(null, Base64.decode(meta).toString(UTF_8))
    }

    override fun getMetadata(): String {
        return Base64.encode(item.toString().toByteArray())
    }

    override fun getTileHeight(): Double {
        return 0.0
    }
}
