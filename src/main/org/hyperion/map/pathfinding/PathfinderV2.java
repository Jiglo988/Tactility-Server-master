package org.hyperion.map.pathfinding;

import org.hyperion.map.WorldMap;

public class PathfinderV2 {

	public static int[][] findRoute(int absX, int absY, int destX, int destY, int heightLevel) {
		int[][] via = new int[104][104];
		int[][] cost = new int[104][104];
		int[] tileQueueX = new int[4000];
		int[] tileQueueY = new int[4000];
		for(int xx = 0; xx < 104; xx++) {
			for(int yy = 0; yy < 104; yy++) {
				cost[xx][yy] = 99999999;
			}
		}
		int regionX = absX >> 3;
		int regionY = absY >> 3;
		int localX = absX - 8 * (regionX - 6);
		int localY = absY - 8 * (regionY - 6);

		int curX = localX;
		int curY = localY;
		via[curX][curY] = 99;
		cost[curX][curY] = 0;
		int head = 0;
		int tail = 0;
		tileQueueX[head] = curX;
		tileQueueY[head++] = curY;
		boolean foundPath = false;
		int pathLength = tileQueueX.length;
		while(tail != head) {
			curX = tileQueueX[tail];
			curY = tileQueueY[tail];
			int x = (regionX - 6) * 8 + curX;
			int y = (regionY - 6) * 8 + curY;
			if(x == destX && y == destY) {
				foundPath = true;
				break;
			}
			tail = (tail + 1) % pathLength;
			int thisCost = cost[curX][curY] + 1;
			if(curY > 0 && via[curX][curY - 1] == 0 && Check(heightLevel, absX, absY, x, y - 1)) {
				tileQueueX[head] = curX;
				tileQueueY[head] = curY - 1;
				head = (head + 1) % pathLength;
				via[curX][curY - 1] = 1;
				cost[curX][curY - 1] = thisCost;
			}
			if(curX > 0 && via[curX - 1][curY] == 0 && Check(heightLevel, absX, absY, x - 1, y)) {
				tileQueueX[head] = curX - 1;
				tileQueueY[head] = curY;
				head = (head + 1) % pathLength;
				via[curX - 1][curY] = 2;
				cost[curX - 1][curY] = thisCost;
			}
			if(curY < 104 - 1 && via[curX][curY + 1] == 0 && Check(heightLevel, absX, absY, x, y + 1)) {
				tileQueueX[head] = curX;
				tileQueueY[head] = curY + 1;
				head = (head + 1) % pathLength;
				via[curX][curY + 1] = 4;
				cost[curX][curY + 1] = thisCost;
			}
			if(curX < 104 - 1 && via[curX + 1][curY] == 0
					&& Check(heightLevel, absX, absY, x + 1, y)) {
				tileQueueX[head] = curX + 1;
				tileQueueY[head] = curY;
				head = (head + 1) % pathLength;
				via[curX + 1][curY] = 8;
				cost[curX + 1][curY] = thisCost;
			}
			if(curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0
					&& Check(heightLevel, absX, absY, x - 1, y - 1)
					&& Check(heightLevel, absX, absY, x - 1, y)
					&& Check(heightLevel, absX, absY, x, y - 1)) {
				tileQueueX[head] = curX - 1;
				tileQueueY[head] = curY - 1;
				head = (head + 1) % pathLength;
				via[curX - 1][curY - 1] = 3;
				cost[curX - 1][curY - 1] = thisCost;
			}
			if(curX > 0 && curY < 104 - 1 && via[curX - 1][curY + 1] == 0
					&& Check(heightLevel, absX, absY, x - 1, y + 1)
					&& Check(heightLevel, absX, absY, x - 1, y)
					&& Check(heightLevel, absX, absY, x, y + 1)) {
				tileQueueX[head] = curX - 1;
				tileQueueY[head] = curY + 1;
				head = (head + 1) % pathLength;
				via[curX - 1][curY + 1] = 6;
				cost[curX - 1][curY + 1] = thisCost;
			}
			if(curX < 104 - 1 && curY > 0 && via[curX + 1][curY - 1] == 0
					&& Check(heightLevel, absX, absY, x + 1, y - 1)
					&& Check(heightLevel, absX, absY, x + 1, y)
					&& Check(heightLevel, absX, absY, x, y - 1)) {
				tileQueueX[head] = curX + 1;
				tileQueueY[head] = curY - 1;
				head = (head + 1) % pathLength;
				via[curX + 1][curY - 1] = 9;
				cost[curX + 1][curY - 1] = thisCost;
			}
			if(curX < 104 - 1 && curY < 104 - 1
					&& via[curX + 1][curY + 1] == 0
					&& Check(heightLevel, absX, absY, x + 1, y + 1)
					&& Check(heightLevel, absX, absY, x + 1, y)
					&& Check(heightLevel, absX, absY, x, y + 1)) {
				tileQueueX[head] = curX + 1;
				tileQueueY[head] = curY + 1;
				head = (head + 1) % pathLength;
				via[curX + 1][curY + 1] = 12;
				cost[curX + 1][curY + 1] = thisCost;
			}
		}

		if(foundPath) {
			tail = 0;
			tileQueueX[tail] = curX;
			tileQueueY[tail++] = curY;
			int l5;
			for(int j5 = l5 = via[curX][curY]; curX != localX
					|| curY != localY; j5 = via[curX][curY]) {
				if(j5 != l5) {
					l5 = j5;
					tileQueueX[tail] = curX;
					tileQueueY[tail++] = curY;
				}
				if((j5 & 2) != 0) {
					curX++;
				} else if((j5 & 8) != 0) {
					curX--;
				}
				if((j5 & 1) != 0) {
					curY++;
				} else if((j5 & 4) != 0) {
					curY--;
				}
			}
	        /*for(int i = 0; i < newWalkCmdX.length; i++) {
				newWalkCmdX[i] = 0;
				newWalkCmdY[i] = 0;
				tmpNWCX[i] = 0;
				tmpNWCY[i] = 0;
			}*/
			int size = tail--;

			//int differenceX = (toX >> 3) - 6;
			//int differenceX = (toX >> 3) - 6;
			int[][] path = new int[size][2];
			/*newWalkCmdSteps = size;
			newWalkCmdX[0] = newWalkCmdY[0] = tmpNWCX[0] = tmpNWCY[0] = 0;
			newWalkCmdX[0] = absX + tileQueueX[tail] - mapRegionX * 8;
			newWalkCmdY[0] = absY + tileQueueY[tail] - mapRegionY * 8;*/
			for(int i = 1; i < size; i++) {
				path[i][0] = absX + tileQueueX[-- tail];
				path[i][1] = absY + tileQueueY[tail];
			}
			return path;
		}
		return null;
	}

	public static boolean Check(int height, int absX, int absY, int toAbsX, int toAbsY) {
		return WorldMap.checkPos(height, absX, absY, toAbsX, toAbsY, 0);
	}

}
