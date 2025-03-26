package io.github.testgame.lwjgl3.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.testgame.lwjgl3.abstractEngine.*;
import io.github.testgame.lwjgl3.scene.sceneHelper.SceneFactory;
import io.github.testgame.lwjgl3.scene.sceneHelper.SceneType;
import io.github.testgame.lwjgl3.scene.sceneHelper.Transition;

public class GameInstructionsScene extends Scene {
    private BitmapFont font;
    private SceneManager sceneManager;
    private AudioManager audioManager;
    private IOManager ioManager;
    private Skin skin;
    private Texture playerTexture;
    private Texture germTexture;
    private Texture soapTexture;
    private Texture dirtTexture;
    private Texture maskTexture;
    private Texture sanitizerTexture;
    private Texture speedTexture;
    private Transition sceneTransition;
    private SceneFactory sceneFactory;

    @Override
    public void create() {
        // Load textures
        playerTexture = new Texture("texture/player.png");
        germTexture = new Texture("texture/virus.png");
        soapTexture = new Texture("texture/soap.png");
        dirtTexture = new Texture("texture/mud.png");
        maskTexture = new Texture("texture/powerup.png");
        sanitizerTexture = new Texture("texture/magazine.png");
        speedTexture = new Texture("texture/modifier.png");

        font = new BitmapFont();
        font.getData().setScale(1.5f);

        skin = new Skin();
        createBasicSkin();

        Table table = new Table();
        table.setFillParent(true);

        // Add title
        Label titleLabel = new Label("GAME INSTRUCTIONS", skin, "title");
        table.add(titleLabel).padBottom(35).row();

        // Add instructions with images
        for (String line : instructions) {
            Table lineTable = new Table();

            if (line.endsWith(":")) {
                Label.LabelStyle headerStyle = new Label.LabelStyle(font, Color.YELLOW);
                Label label = new Label(line, headerStyle);
                table.add(label).padTop(20);
            } else if (!line.isEmpty()) {
                // Add image based on content
                Image icon = null;
                if (line.contains("Player:")) {
                    icon = new Image(playerTexture);
                } else if (line.contains("Germs:")) {
                    icon = new Image(germTexture);
                } else if (line.contains("Soap Bar:")) {
                    icon = new Image(soapTexture);
                } else if (line.contains("Dirt:")) {
                    icon = new Image(dirtTexture);
                } else if (line.contains("Mask:")) {
                    icon = new Image(maskTexture);
                } else if (line.contains("Hand Sanitizer:")) {
                    icon = new Image(sanitizerTexture);
                } else if (line.contains("Random Buff:")) {
                    icon = new Image(speedTexture);
                }

                if (icon != null) {
                    icon.setSize(30, 30);
                    lineTable.add(icon).size(30, 30).padRight(10);
                }

                Label label = new Label(line, skin, "default");
                lineTable.add(label).left();
                table.add(lineTable).left();
            } else {
                table.add().height(10);
            }
            table.row();
        }

        stage.addActor(table);
    }

    @Override
    public void dispose() {
        playerTexture.dispose();
        germTexture.dispose();
        soapTexture.dispose();
        dirtTexture.dispose();
        maskTexture.dispose();
        sanitizerTexture.dispose();
        speedTexture.dispose();
        font.dispose();
        skin.dispose();
        stage.dispose();
    }

    private final String[] instructions = {
        "GAME OBJECTIVES:",
        "- Navigate through the map and eliminate 10 germs by landing bubbles on them!",
        "",
        "PLAYER CONTROLS:",
        "- Player: This is you!",
        "- Arrow UP: Move upward",
        "- Arrow DOWN: Move downward",
        "- Arrow LEFT: Move left",
        "- Arrow RIGHT: Move right",
        "- SPACE: Small Dash",
        "- LEFT MOUSE CLICK: Launch 1 bubble",
        "",
        "AGGRESSIVE NPCs:",
        "- Germs: Will chase and damage you on contact",
        "",
        "OBSTACLES:",
        "- Soap Bar: Neutral obstacle that obstructs movement",
        "- Dirt: Game over on contact!",
        "",
        "CONSUMABLES:",
        "- Mask: Restores your health by 1 point",
        "- Hand Sanitizer: Replenishes your bubbles by 5",
        "- Random Buff: Increases movement speed or health by a random percentage",
        "",
        "Press ESCAPE to return to main menu"
    };

    public GameInstructionsScene(SceneManager sceneManager, AudioManager audioManager,
                                 IOManager ioManager, Transition sceneTransition,
                                 SceneFactory sceneFactory) {
        super();
        this.sceneManager = sceneManager;
        this.audioManager = audioManager;
        this.ioManager = ioManager;
        this.sceneTransition = sceneTransition;
        this.sceneFactory = sceneFactory;
        Gdx.input.setInputProcessor(stage);
    }

    private void createBasicSkin() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("white", new Texture(pixmap));
        pixmap.dispose();

        Label.LabelStyle titleStyle = new Label.LabelStyle();
        titleStyle.font = font;
        titleStyle.fontColor = Color.GOLD;
        skin.add("title", titleStyle);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.WHITE;
        skin.add("default", labelStyle);
    }

    @Override
    public void render() {
        // Same blue background as MainMenu
        Gdx.gl.glClearColor(0, 172/255f, 193/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            sceneFactory.disposeScene(SceneType.INSTRUCTIONS);
            sceneTransition.startTransition(SceneType.MAIN_MENU);
        }
    }
    @Override
    public void reset() {
        // Reset UI elements if needed
        if (stage != null) {
            stage.clear();
            create();
        }

        // Set input processor
        Gdx.input.setInputProcessor(stage);

        // Mark scene as active in lifecycle
        setLifeCycle(true);
    }
}
