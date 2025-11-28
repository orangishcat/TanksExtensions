import basewindow.InputCodes;
import main.Tanks;
import tanks.Drawing;
import tanks.Game;
import tanks.Panel;
import tanks.extension.Extension;
import tanks.gui.InputSelector;
import tanks.gui.input.InputBinding;
import tanks.gui.input.InputBindingGroup;
import tanks.gui.screen.ScreenControlsCamera;

public class PerspectiveMod extends Extension
{
    public static final InputBindingGroup perspective = new InputBindingGroup("game.perspective", new InputBinding(InputBinding.InputType.keyboard, InputCodes.KEY_F5));
    public static InputSelector selector = null;
    public int viewID = 0;

    public PerspectiveMod()
    {
        super("perspective_mod");
    }

    public static void main(String[] args)
    {
        Tanks.launchWithExtensions(new String[0], new tanks.extension.Extension[]{new PerspectiveMod()}, null);
    }

    @Override
    public void update()
    {
        if (Game.screen instanceof ScreenControlsCamera)
        {
            if (selector == null)
                selector = new InputSelector(Drawing.drawing.interfaceSizeX * 2 / 3, Drawing.drawing.interfaceSizeY / 2 + 220, 700, 40, "Toggle perspective", perspective);

            selector.update();
        }

        if (perspective.isValid())
        {
            perspective.invalidate();

            viewID = (viewID + (Game.game.window.pressedKeys.contains(InputCodes.KEY_LEFT_SHIFT) ? 3 : 1)) % 4;
            switch (viewID)
            {
                case 0:
                    Game.angledView = false;
                    Game.followingCam = false;
                    Game.firstPerson = false;
                    break;
                case 1:
                    Game.angledView = true;
                    Game.followingCam = false;
                    break;
                case 2:
                    Game.angledView = false;
                    Game.followingCam = true;
                    Game.firstPerson = false;
                    break;
                case 3:
                    Game.followingCam = true;
                    Game.firstPerson = true;
                    break;
            }

            if (Game.followingCam || Game.firstPerson)
            {
                Drawing.drawing.movingCamera = true;
                Panel.panel.zoomTimer = 1;
                Panel.autoZoom = false;
            }
        }
    }

    @Override
    public void draw()
    {
        if (Game.screen instanceof ScreenControlsCamera)
            selector.draw();
    }
}
