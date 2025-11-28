import main.Tanks
import tanks.Game
import tanks.extension.Extension
import tanks.network.event.EventSetupHotbar

var HAS_ITEM_DROPS = false

class ItemDrops : Extension("item_drops") {
    override fun setUp() {
        Game.registerObstacle(ObstacleItemDrop::class.java, "item_drop")
        Game.registerMetadataSelector("item", SelectorItem::class.java)
        registerImage("item.png")
    }

    override fun update() {
        if (HAS_ITEM_DROPS) {
            HAS_ITEM_DROPS = false
            for (p in Game.players) {
                p.hotbar.itemBar.showItems = true
                Game.eventsOut.add(EventSetupHotbar(p))
            }
        }
    }
}

fun main(args: Array<String>) {
    Tanks.launchWithExtensions(args, arrayOf(ItemDrops()), null)
}