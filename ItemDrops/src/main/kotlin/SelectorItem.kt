
import tanks.Game
import tanks.GameObject
import tanks.gui.screen.ScreenEditorItem
import tanks.gui.screen.leveleditor.ScreenLevelEditor
import tanks.gui.screen.leveleditor.selector.MetadataSelector
import tanks.item.Item
import tanks.tankson.FieldPointer
import java.lang.reflect.Field

class SelectorItem(f: Field) : MetadataSelector(f) {
    override fun changeMetadata(
        p0: ScreenLevelEditor?,
        p1: GameObject?,
        p2: Int
    ) {

    }

    override fun openEditorOverlay(editor: ScreenLevelEditor) {
        Game.screen = ScreenEditorItem(FieldPointer(editor.mousePlaceable, metadataField, false), Game.screen)
        (Game.screen as ScreenEditorItem).showLoadFromTemplate = true
    }

    override fun getMetadataDisplayString(o: GameObject?): String {
        return (metadataField.get(o) as Item.ItemStack<*>).item.name
    }
}