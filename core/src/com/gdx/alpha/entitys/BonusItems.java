package com.gdx.alpha.entitys;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

public abstract class BonusItems extends Actor {
    protected Vector2 position;
    Bounds bonusItemsBound;

    BonusItems()
    {
        this.position = new Vector2();
        this.bonusItemsBound = new Bounds(0,0,0,0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    public Vector2 getPosition () {return this.position;}

    public Bounds getBonusItemsBound() {return this.bonusItemsBound;}
}
