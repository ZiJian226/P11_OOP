package io.github.testgame.lwjgl3.scene.sceneHelper;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

public class Button {
    private Rectangle bounds;
    private Color buttonColor;
    private Color textColor;
    private BitmapFont font;
    private String text;
    private GlyphLayout glyphLayout;
    private Runnable onClick;

    public Button(float x, float y, float width, float height, Color buttonColor, Color textColor, BitmapFont font, String text) {
        this.bounds = new Rectangle(x, y, width, height);
        this.buttonColor = buttonColor;
        this.textColor = textColor;
        this.font = font;
        this.text = text;
        this.glyphLayout = new GlyphLayout(); // Initialize GlyphLayout for text measurement
    }

    public void render(ShapeRenderer shapeRenderer, SpriteBatch batch) {
        // Draw the button (rectangle)
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(buttonColor);
        shapeRenderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
        shapeRenderer.end();

        // Calculate text dimensions
        glyphLayout.setText(font, text);

        // Draw the text inside the button (centered)
        batch.begin();
        font.setColor(textColor);
        float textX = bounds.x + (bounds.width - glyphLayout.width) / 2;
        float textY = bounds.y + (bounds.height + glyphLayout.height) / 2;
        font.draw(batch, text, textX, textY);
        batch.end();
    }

    public boolean isClicked(int screenX, int screenY) {
        return bounds.contains(screenX, screenY);
    }

    public void setButtonColor(Color color) {
        this.buttonColor = color;
    }

    public void setTextColor(Color color) {
        this.textColor = color;
    }

    public void setOnClick(Runnable onClick) {
        this.onClick = onClick;
    }

    public void onClick() {
        if (onClick != null) {
            onClick.run();
        }
    }

    public void dispose() {
        font.dispose();
    }
}
