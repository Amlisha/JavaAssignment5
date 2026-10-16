package com.example.pacman;

public abstract class GameCharacter {
    protected int x;
    protected int y;

    public GameCharacter(int x,int y){
        this.x=x;
        this.y=y;
    }
    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }
    public void setPosition(int x,int y){
        this.x=x;
        this.y=y;
    }
    public abstract void move(Maze maze);
}
