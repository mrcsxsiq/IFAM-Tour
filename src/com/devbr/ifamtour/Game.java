package com.devbr.ifamtour;

import javax.microedition.khronos.opengles.GL10;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.BoundCamera;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl;
import org.anddev.andengine.engine.camera.hud.controls.AnalogOnScreenControl.IAnalogOnScreenControlListener;
import org.anddev.andengine.engine.camera.hud.controls.BaseOnScreenControl.IOnScreenControlListener;
import org.anddev.andengine.engine.camera.hud.controls.DigitalOnScreenControl;
import org.anddev.andengine.engine.handler.physics.PhysicsHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.background.AutoParallaxBackground;
import org.anddev.andengine.entity.scene.background.ParallaxBackground;
import org.anddev.andengine.entity.scene.background.SpriteBackground;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;
import org.anddev.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

public class Game<BitmapTextureAtlas> extends BaseGameActivity  {
	
    private static int CAMERA_WIDTH = 640;  
    private static int CAMERA_HEIGHT = 480; 
    
    public int control = 0 ;
  
    // ===========================================================  
    // Fields  
    // ===========================================================  
  
	// ===========================================================
	// Fields
	// ===========================================================

	private BoundCamera mCamera;

	private Texture mTexture;
	private TiledTextureRegion mPlayerTextureRegion;
	
	private Texture infoTexture;
	private TextureRegion infoTextureRegion;
	
	private Texture mOnScreenControlTexture;
	private TextureRegion mOnScreenControlBaseTextureRegion;
	private TextureRegion mOnScreenControlKnobTextureRegion;
	
	private Texture mOnScreenControlTexture2;
	private TextureRegion mOnScreenControlBaseTextureRegion2;
	private TextureRegion mOnScreenControlKnobTextureRegion2;
	
	private Texture mAutoParallaxBackgroundTexture;

	private TextureRegion mParallax;

	private static final long[] ANIMATE_DURATION = new long[]{150, 150, 150};
	
	private enum PlayerDirection{
        NONE,
        UP,
        DOWN,
        LEFT,
        RIGHT,
        UPLEFT,
        UPRIGHT,
        DOWNLEFT,
        DOWNRIGHT
    }
	
    private PlayerDirection playerDirection = PlayerDirection.NONE;

	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Engine onLoadEngine() {
		final DisplayMetrics displayMetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
    //    CAMERA_WIDTH = 2* displayMetrics.widthPixels;
    //    CAMERA_HEIGHT= 2* displayMetrics.heightPixels;
        this.mCamera = new BoundCamera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera));
        
        
	}

	@Override
	public void onLoadResources() {
		TextureRegionFactory.setAssetBasePath("gfx/");

		this.mTexture = new Texture(128, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mPlayerTextureRegion = TextureRegionFactory.createTiledFromAsset(this.mTexture, this, "person.png", 0, 0, 3, 8);

		this.mOnScreenControlTexture = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mOnScreenControlBaseTextureRegion = TextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_base.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion = TextureRegionFactory.createFromAsset(this.mOnScreenControlTexture, this, "onscreen_control_knob.png", 128, 0);

		this.mOnScreenControlTexture2 = new Texture(256, 128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mOnScreenControlBaseTextureRegion2 = TextureRegionFactory.createFromAsset(this.mOnScreenControlTexture2, this, "onscreen_control_base2.png", 0, 0);
		this.mOnScreenControlKnobTextureRegion2 = TextureRegionFactory.createFromAsset(this.mOnScreenControlTexture2, this, "onscreen_control_knob.png", 128, 0);

		
		this.infoTexture = new Texture(64, 64, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.infoTextureRegion = TextureRegionFactory.createFromAsset(this.infoTexture, this, "info.png", 0, 0);
		
		this.mAutoParallaxBackgroundTexture = new Texture(2048, 2048, TextureOptions.DEFAULT);
		this.mParallax= TextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this, "fstflr.png", 0, 0);

		this.mEngine.getTextureManager().loadTextures(this.mTexture, this.mAutoParallaxBackgroundTexture);
		this.mEngine.getTextureManager().loadTextures(this.mTexture, this.mOnScreenControlTexture);
		this.mEngine.getTextureManager().loadTextures(this.mTexture, this.mOnScreenControlTexture2);
	}

	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene(1);
		
		final Sprite background = new Sprite(0, 0,  this.mParallax);
		background.setScale(4);
		
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 0);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, background));
		scene.setBackground(autoParallaxBackground);
		
		/*final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(0.0f, new Sprite(0, CAMERA_HEIGHT - this.mParallaxLayerBack.getHeight(), this.mParallaxLayerBack)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-5.0f, new Sprite(0, 80, this.mParallaxLayerMid)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-10.0f, new Sprite(0, CAMERA_HEIGHT - this.mParallaxLayerFront.getHeight(), this.mParallaxLayerFront)));
		scene.setBackground(autoParallaxBackground);
*/
		final int playerX = (CAMERA_WIDTH - this.mPlayerTextureRegion.getTileWidth()) / 2;
		final int playerY = (CAMERA_HEIGHT - this.mPlayerTextureRegion.getTileHeight()) / 2;	
		
		final AnimatedSprite player = new AnimatedSprite(playerX, playerY, this.mPlayerTextureRegion);
		player.setScaleCenterY(this.mPlayerTextureRegion.getTileHeight());
		player.setScale(3);
		
		final Sprite infoButton = new Sprite(0, 0, this.infoTextureRegion);
		infoButton.setScale(3);
	
		final PhysicsHandler physicsHandler = new PhysicsHandler(player);
		player.registerUpdateHandler(physicsHandler);
		background.registerUpdateHandler(physicsHandler);
		

	//	scene.getLastChild().attachChild(player);
		
	/*	if ((player.getX() > 150 + 32 ) && (player.getX() < CAMERA_WIDTH - 150 - 32)){
			Log.i("", "player.getX() " + Float.toString((player.getX())));	
			physicsHandler.setVelocityX(0);	
		}
	*/
	//	this.mCamera.setChaseEntity(player);
	//	scene.setChildScene(player);
		
		scene.attachChild(player);

		final AnalogOnScreenControl analogOnScreenControl = new AnalogOnScreenControl(CAMERA_WIDTH - this.mOnScreenControlBaseTextureRegion.getWidth() - 65, CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion.getHeight() - 30 , this.mCamera, this.mOnScreenControlBaseTextureRegion, this.mOnScreenControlKnobTextureRegion, 0.1f, 200, new IAnalogOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {

			//	Log.i("", "pValueY: " + Float.toString(pValueY) + "pValueX: " + Float.toString(pValueX) );
			
				physicsHandler.setVelocity(pValueX * 150, pValueY * 150);		
				
				if ((pValueY < -0.25)&&(pValueX < -0.25)){
		                    // UPLEFT
					if (playerDirection != PlayerDirection.UPLEFT){
                        player.animate(ANIMATE_DURATION, 15, 17, true);
                        playerDirection = PlayerDirection.UPLEFT;                      
                    }
                    
                } else if ((pValueY < -0.25)&&(pValueX > 0.25)){
                    // UPRIGHT
					if (playerDirection != PlayerDirection.UPRIGHT){
                        player.animate(ANIMATE_DURATION, 21, 23, true);
                        playerDirection = PlayerDirection.UPRIGHT;
                    }
                    
                } else if ((pValueY > 0.25)&&(pValueX < -0.25)){
                    // DOWNLEFT
					if (playerDirection != PlayerDirection.DOWNLEFT){
                        player.animate(ANIMATE_DURATION, 12, 14, true);
                        playerDirection = PlayerDirection.DOWNLEFT;
                    }
                    
                } else if ((pValueY > 0.25)&&(pValueX > 0.25)){
                    // DOWNRIGHT
					if (playerDirection != PlayerDirection.DOWNRIGHT){
                        player.animate(ANIMATE_DURATION, 18, 20, true);
                        playerDirection = PlayerDirection.DOWNRIGHT;
                    }
                    
                }else if (pValueY < -0.25){
                    // UP
					if (playerDirection != PlayerDirection.UP){
                        player.animate(ANIMATE_DURATION, 9, 11, true);
                        playerDirection = PlayerDirection.UP;
                    }
                }else if (pValueY > 0.25){
                    // DOWN
                    if (playerDirection != PlayerDirection.DOWN){
                        player.animate(ANIMATE_DURATION, 0, 2, true);
                        playerDirection = PlayerDirection.DOWN;
                    }
                }else if (pValueX < -0.25){
                    // LEFT
                    if (playerDirection != PlayerDirection.LEFT){
                        player.animate(ANIMATE_DURATION, 3, 5, true);
                        playerDirection = PlayerDirection.LEFT;
                    }
                }else if (pValueX > 0.25){
                    // RIGHT
                    if (playerDirection != PlayerDirection.RIGHT){
                        player.animate(ANIMATE_DURATION, 6, 8, true);
                        playerDirection = PlayerDirection.RIGHT;
                    }
                }
                else {
                	// NONE
                    if (player.isAnimationRunning()){
                        player.stopAnimation();
                        playerDirection = PlayerDirection.NONE;
                    }
                }

			}

			@Override
			public void onControlClick(final AnalogOnScreenControl pAnalogOnScreenControl) {
				//
			}
		});
		analogOnScreenControl.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		analogOnScreenControl.getControlBase().setAlpha(0.5f);
		analogOnScreenControl.getControlBase().setScaleCenter(0, 128);
		analogOnScreenControl.getControlBase().setScale(1.25f);
		analogOnScreenControl.getControlKnob().setScale(1.25f);
		analogOnScreenControl.refreshControlKnobPosition();

		scene.setChildScene(analogOnScreenControl);
		
		/*		
		
		final DigitalOnScreenControl digitalOnScreenControl = new DigitalOnScreenControl(0 + 30, - 30 + CAMERA_HEIGHT - this.mOnScreenControlBaseTextureRegion2.getHeight(), this.mCamera, this.mOnScreenControlBaseTextureRegion2, this.mOnScreenControlKnobTextureRegion2, 0.1f, new IOnScreenControlListener() {
			@Override
			public void onControlChange(final BaseOnScreenControl pBaseOnScreenControl, final float pValueX, final float pValueY) {
				Log.i("", "pValueY: " + Float.toString(pValueY) + "pValueX: " + Float.toString(pValueX) );
				
				if (pValueX == -1){
					control = 1;
	            }else if (pValueX == 1){
	            	control = 2;
	            }
			}
		});
		digitalOnScreenControl.getControlBase().setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		digitalOnScreenControl.getControlBase().setAlpha(0.5f);
		digitalOnScreenControl.getControlBase().setScaleCenter(0, 128);
		digitalOnScreenControl.getControlBase().setScale(1.25f);
		digitalOnScreenControl.getControlKnob().setScale(1.25f);
		digitalOnScreenControl.refreshControlKnobPosition();

	//	scene.setChildScene(digitalOnScreenControl);
		analogOnScreenControl.setChildScene(digitalOnScreenControl);
		switch (control) {
		case 1:
			Toast.makeText(getApplicationContext(), "esquerda", Toast.LENGTH_SHORT).show();
		//	control = 0;
			break;
		case 2:
			Toast.makeText(getApplicationContext(), "direita", Toast.LENGTH_SHORT).show();
		//	control = 0;
			break;

		default:
			break;
		}
		
*/
		return scene;
	}

	@Override
	public void onLoadComplete() {
		
	}

	// ===========================================================
	// Methods
	// ===========================================================

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}