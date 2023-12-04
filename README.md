# Harlequin
[![Harlequin on Maven Central](https://img.shields.io/maven-central/v/io.github.fourlastor.gdx/harlequin?label=harlequin)](https://search.maven.org/artifact/io.github.fourlastor.gdx/harlequin)
[![Harlequin-Ashley on Maven Central](https://img.shields.io/maven-central/v/io.github.fourlastor.gdx/harlequin-ashley?label=harlequin-ashley)](https://search.maven.org/artifact/io.github.fourlastor.gdx/harlequin-ashley)

Harlequin is a collection of extensions for [libGDX](https://github.com/libgdx/libgdx)'s scene2d system.

## Setup

Harlequin is hosted at Maven Central, you can get it by adding the following dependencies:

```kts
// Core library
implementation("io.github.fourlastor.gdx:harlequin:$harlequinVersion")
// Ashley extensions
implementation("io.github.fourlastor.gdx:harlequin-ashley:$harlequinVersion")
```

## GWT

Add the following to your GWT xml file:

```xml
<module rename-to="html">
  <!-- Core library -->
  <inherits name="io.github.fourlastor.Harlequin" />
  <!-- Ashley extension -->
  <inherits name="io.github.fourlastor.HarlequinAshley" />
</module>
```

## Common settings

`io.github.fourlastor.harlequin.Harlequin` contains a field named `LIST_CREATOR` which can be set to use a different `List<T>` implementation. For example to use [jdkgdxds](https://github.com/tommyettinger/jdkgdxds):

```java
io.github.fourlastor.harlequin.Harlequin.LIST_CREATOR = new ListCreator() {
    @Override
    public <T> List<T> newList() {
        return new ObjectList<>();
    }
    
    @Override
    public <T> List<T> newList(int size) {
        return new ObjectList<>(size);
    }
};
```

## Animations

Harlequin introduces its own animation classes, a common interface `Animation`, and an enum `Animation.PlayMode` (which directly maps to libGDX's one). This has been done to be able to support different types of animations

### Fixed frames animations

These are similar to the standard libGDX `Animation`s:

```java
new FixedFrameAnimation(frameDuration, arrayOfFrames, playMode);
```

### Keyframe-based animations

These animations support different length per frames, they're usually generated from an editor (see [asset loaders](#asset-loaders) below).

A keyframe-based animation can be instantiated as such:
```java
Animation<T> animation = new KeyFrameAnimation(keyFrames, animationTotalLength, playMode);
```

#### Constructing keyframes

`KeyFrame<T>` is an abstract class, to create one, use `KeyFrame.create(startTime, valueAtTime)`.

### Scene2d `Animation` support

You can use `AnimatedImage` to display/play any `Animation` of drawables.

## Asset Loaders

### DragonBones

Harlequin can load animations from a [DragonBones](https://docs.egret.com/dragonbones/en) export (dragonbones json format) via the asset manager.

:warning: This only support sprite animations for now.

First of all, register the loader:

```java
AssetManager manager = new AssetManager();
assetManager.setLoader(AnimationNode.Group.class, new DragonBonesLoader());
```

Then load your animations as such:

```java
AnimationNode.Group group = assetManager.load(
    PATH_DRAGON_BONES_JSON,
    AnimationNode.Group.class,
    new DragonBonesLoader.Parameters(
        PATH_TO_TEXTURE_ATLAS,
        BASE_PATH_FOR_ANIMATIONS_IN_ATLAS,
        MAP_OF_PLAY_MODES_FOR_ANIMATIONS
    )
);
```

`AnimationNode.Group` is an actor, and you can use it to display your animation.

```java
stage.add(group);
```

To start/change the currently displayed animation:

```java
animationGroup.enter("myAnimationName");
```

### Parameters

Loading an animation with this loader requires you to specify up to 3 parameters.

#### Texture atlas path

This parameter is mandatory. It is the path for the atlas asset containing the animation frames.

#### Base path

This parameter is optional and defaults to `""`. It's the base path for your animation frames within the atlas. Note this uses the texture name from the animation file slots, see [this](https://github.com/fourlastor/libgdx-java-base/blob/9670a60dabedb2b44dc58b62dc186bacbbcd15ef/assets/images/included/animations/dancer/dancer.json#L40-L162) for an example.

#### Play modes

By default, each animation will be played as `PlayMode.LOOP`, you can specify a different play mode by setting a key with the corresponding animation name in this map, missing play modes will default to `PlayMode.LOOP`.

## Parallax images

You can create a `new ParallaxImage(textureOrDrawable, factorX, factorY)` to have an image which moves slower/faster than the `Camera` movement.

Currently `ParallaxImage` fills the entire viewport.

## Ashley extension

### Stage system

You can use `StageSystem` to manage your stage through Ashley.

#### Layers

`StageSystem` supports layers, which are represented by an enum class, you will need to create the enum:

```java
// Layers are drawn top to bottom in enum order
enum Layer {
  BACKGROUND,
  CHARACTER,
  FOREGROUND_EFFECTS
}

StageSystem system = new StageSystem(stage, Layer::class);
```

### Actor component

To add an entity on stage, you need to add `ActorComponent` to it.

```java
Entity entity = new Entity();
AnimatedImage actor = new AnimatedImage(...);
// character in character layer
entity.add(new ActorComponent(actor, Layer.CHARACTER));

Entity background = new Entity();
ParallaxImage backgroundImage = new ParallaxImage(...);
// background image in the background layer
background.add(new ActorComponent(backgroundImage, Layer.BACKGROUND));
```
