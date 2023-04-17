package io.github.fourlastor.harlequin.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ActorComponent implements Component {

    public final Actor actor;
    public final Enum<?> layer;

    public ActorComponent(Actor actor, Enum<?> layer) {
        this.actor = actor;
        this.layer = layer;
    }
}
