package org.bistu.kjcx.godshandandthief.actor;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class GameActor {
	
	public String name;
	protected ArrayList<GameActor> children = new ArrayList<GameActor>();
	
	protected float actorX, actorY, shrink;
	String text;
	protected Bitmap actorBitmap;
	protected Paint paint;
	
	public enum ActorStatus {
		Action,		//行动
		NoDisp,		//不显示
		NoAction,	//无行动
		Dead,		//死亡
	}
	
	public ActorStatus status;
	
	protected long level;
	
	
	
	public GameActor() {
		this("no name");
		
	}
	
	public GameActor(String name) {
		this.name = name;
		this.status = ActorStatus.Action;
	}
	
	public void update(long elapsedTime) {
		for(GameActor actorChild : children) {		//增强的for循环
			if(actorChild.status == ActorStatus.Action || actorChild.status == ActorStatus.NoDisp)
				actorChild.update(elapsedTime);
		}
		
	}
	
	public void render() {
		for(GameActor actorChild : children)
				actorChild.render();
	}
	
	public void render(Canvas canvas) {
		for(GameActor actorChild : children)
				actorChild.render(canvas);
	}
	
	public void addChild(GameActor actor) {
		children.add(actor);
		actor.level = level + 1;
	}
	
	public GameActor search(String name) {
		if(this.name == name)
			return this;
		for(GameActor actorChild : children)
			if(actorChild.name == name)
				return actorChild;
		return null;
	}
	
	public void cleanUpDead() {
		if(this.status == ActorStatus.Dead)
			for(GameActor actorChild : children)
				actorChild.status = ActorStatus.Dead;
		for(GameActor actorChild : children)
			actorChild.cleanUpDead();
		children.remove(children);
	}
	
	boolean checkStatus(GameActor actor) {
		return false;
	}
	
}
