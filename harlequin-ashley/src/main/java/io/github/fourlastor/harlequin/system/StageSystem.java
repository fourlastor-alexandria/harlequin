package io.github.fourlastor.harlequin.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.tommyettinger.ds.ObjectList;
import io.github.fourlastor.harlequin.component.ActorComponent;
import java.util.List;
import java.util.function.IntFunction;

/**
 * An Ashley {@link EntitySystem} which supports layer through an arbitrary enum.
 * Actors are automatically added/removed from the correct layer when an entity gains/loses its {@link ActorComponent} component.
 */
public class StageSystem extends EntitySystem {

    private static final Family FAMILY = Family.all(ActorComponent.class).get();
    private static final ComponentMapper<ActorComponent> COMPONENT_MAPPER =
            ComponentMapper.getFor(ActorComponent.class);
    private final ComponentMapper<ActorComponent> actors = COMPONENT_MAPPER;
    private final Stage stage;
    private final List<Group> layerGroups;
    private final ActorsListener actorsListener = new ActorsListener();

    /**
     * @param stage  the stage to control with this system.
     * @param layers the enum used to determine the layer class.
     */
    public StageSystem(Stage stage, Class<? extends Enum<?>> layers) {
        this(stage, layers, ignored -> new Group());
    }

    /**
     * @param stage  the stage to control with this system.
     * @param layers the enum used to determine the layer class.
     * @param layerFactory factory gor layer actors
     */
    public StageSystem(Stage stage, Class<? extends Enum<?>> layers, IntFunction<? extends Group> layerFactory) {
        this.stage = stage;
        int layersCount = layers.getEnumConstants().length;
        this.layerGroups = new ObjectList<>(layersCount);
        for (int i = 0; i < layersCount; i++) {
            this.layerGroups.add(layerFactory.apply(i));
        }
    }

    @Override
    public void update(float deltaTime) {
        stage.getViewport().apply();
        stage.act(deltaTime);
        stage.draw();
    }

    @Override
    public void addedToEngine(Engine engine) {
        engine.addEntityListener(FAMILY, actorsListener);
        for (Group layer : layerGroups) {
            stage.addActor(layer);
        }
    }

    @Override
    public void removedFromEngine(Engine engine) {
        engine.removeEntityListener(actorsListener);
        for (Group layer : layerGroups) {
            layer.remove();
        }
    }

    private class ActorsListener implements EntityListener {

        @Override
        public void entityAdded(Entity entity) {
            if (actors.has(entity)) {
                ActorComponent actorComponent = actors.get(entity);
                Actor actor = actorComponent.actor;
                Enum<?> layer = actorComponent.layer;
                layerGroups.get(layer.ordinal()).addActor(actor);
            }
        }

        @Override
        public void entityRemoved(Entity entity) {
            if (actors.has(entity)) {
                actors.get(entity).actor.remove();
            }
        }
    }
}
