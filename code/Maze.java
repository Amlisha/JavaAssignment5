package com.example.pacman;

public class Maze {
    private final char[][] grid={
            "###############".toCharArray(),
            "#.............#".toCharArray(),
            "#..###...###..#".toCharArray(),
            "#.............#".toCharArray(),
            "#.###.#.#.###.#".toCharArray(),
            "#.....#.#.....#".toCharArray(),
            "###.#.#.#.#.###".toCharArray(),
            "#...#.....#...#".toCharArray(),
            "#.###.###.###.#".toCharArray(),
            "#.............#".toCharArray(),
            "#.###.#.#.###.#".toCharArray(),
            "#.....#.#.....#".toCharArray(),
            "###.#.#.#.#.###".toCharArray(),
            "#.............#".toCharArray(),
            "###############".toCharArray()
    };
    public int getRows(){
        return grid.length;
    }
    public int getCols(){
        return grid[0].length;
    }
    public boolean isWall(int x,int y){
        if(x<0 || y<0 || y>=grid.length || x>=grid[0].length){
            return true;
        }
        return grid[y][x]=='#';
    }
    public boolean hasDot(int x,int y){
        return grid[y][x]=='.';
    }

    public void collectDot(int x,int y){
        if(hasDot(x,y)){
            grid[y][x]=' ';
        }
    }
    public int countDots(){
        int count=0;
        for (char[] row:grid){
            for (char cell:row) {
                if(cell == '.'){
                    count++;
                }
            }
        }
        return count;
    }
    public char getCell(int x,int y){
        return grid[y][x];
    }
    public boolean isSafeHome(int x, int y) {
        return (x >= 6 && x <= 8 && y >= 2 && y <= 3);
    }
}
