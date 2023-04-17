package io.github.fourlastor.harlequin.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Pool;

public class ActorComponent implements Component, Pool.Poolable {

    public Actor actor;
    public Enum<?> layer;

    public ActorComponent() {}

    public ActorComponent(Actor actor, Enum<?> layer) {
        initialize(actor, layer);
    }

    public void initialize(Actor actor, Enum<?> layer) {
        this.actor = actor;
        this.layer = layer;
    }

    @Override
    public void reset() {
        this.actor = null;
        this.layer = null;
    }
}
